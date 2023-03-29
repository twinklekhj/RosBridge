package io.github.twinklekhj.ros.ws;

import io.github.twinklekhj.ros.op.*;
import io.github.twinklekhj.ros.type.geometry.TransformStamped;
import io.github.twinklekhj.ros.type.tf.TFMessage;
import io.github.twinklekhj.ros.ws.codec.TFMessageCodec;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A TF Client that listens to TFs from tf2_web_republisher
 * - fixedFrame: The fixed frame.
 * - angularThresh: The angular threshold for the TF republisher.
 * - transThresh: The translation threshold for the TF republisher.
 * - rate: The rate for the TF republisher.
 * - updateDelay: The time (in ms) to wait after a new subscription to update the TF republisher's list of TFs.
 */
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class TFClient {
    private static final Logger logger = LoggerFactory.getLogger(TFClient.class);

    private static final MessageCodec transformCodec = new TFMessageCodec();
    private static final DeliveryOptions transformDelivery = new DeliveryOptions().setCodecName(transformCodec.name());
    protected final Map<String, List<String>> frameHandlers = new HashMap<>();
    protected final Map<String, TransformStamped> frameInfo = new HashMap<>();
    @NonNull
    private final RosBridge bridge;
    @Builder.Default
    private String serverName = "/tf2_web_republisher";
    @Builder.Default
    private String repubServiceName = "/republish_tfs";
    @Builder.Default
    private String fixedFrame = "base_link";
    @Builder.Default
    private double angularThresh = 2.0;
    @Builder.Default
    private double transThresh = 0.01;
    @Builder.Default
    private double rate = 10.0;
    private ActionClient actionClient = null;
    private boolean republishUpdateRequested = false;

    private static TFClientBuilder builder() {
        return new TFClientBuilder();
    }

    public static TFClientBuilder builder(RosBridge bridge) {
        return builder().bridge(bridge);
    }

    public static TFClientBuilder builder(RosBridge bridge, String parent) {
        if (!parent.startsWith("/")) {
            parent = String.format("/%s", parent);
        }
        return builder(bridge).serverName(parent + "/tf2_web_republisher").repubServiceName(parent + "/republish_tfs");
    }

    private String parsingFrameID(String frame) {
        if (frame.startsWith("/")) {
            frame = frame.substring(1);
        }
        return frame;
    }

    public ActionClient getActionClient(){
        if(actionClient != null) return actionClient;
        return new ActionClient(bridge, this.serverName, "tf2_web_republisher/TFSubscriptionAction");
    }

    public Future<Void> updateGoal(){
        RosService service = RosService.builder(this.repubServiceName, "tf2_web_republisher/RepublishTFs").build();
        service.setArgs(frameInfo.keySet(), this.fixedFrame, this.angularThresh, this.transThresh, this.rate);

        return bridge.callService(service).future().compose(this::processResponse).compose(rosSubscription -> {
            this.republishUpdateRequested = false;
            return Future.succeededFuture();
        });
    }

    /**
     * Process response
     * @param response 응답
     * @return future
     */
    public Future<RosSubscription> processResponse(RosResponse response){
        Map<String, Object> values = response.getValues();
        String topicName = values.get("topic_name").toString();

        RosTopic topic = RosTopic.builder(topicName, "tf2_web_republisher/TFArray").build();
        if(this.bridge.isSubscribed(topic)){
            this.bridge.unsubscribe(topic);
        }

        return this.bridge.subscribe(topic, message -> {
            logger.info("message: {}", message);
            JsonObject json = message.body();
            TFMessage tf = TFMessage.fromJsonObject(json);

            EventBus bus = this.bridge.getBus();
            for (TransformStamped transform : tf.getTransforms()) {
                String frameID = parsingFrameID(transform.getChildFrameID());

                // handler 가 있으면
                if (this.frameHandlers.containsKey(frameID)) {
                    this.frameHandlers.get(frameID).forEach(handlerID -> {
                        bus.publish(handlerID, transform, transformDelivery);
                    });
                }
            }
        }).future();
    }


    /**
     * Subscribe to the given TF frame.
     *
     * @param frameID The TF frame to subscribe to.
     * @param handler handler
     * @return handler ID
     */
    public String subscribe(String frameID, Handler<Message<TFMessage>> handler) {
        EventBus bus = this.bridge.getBus();
        frameID = parsingFrameID(frameID);

        // transform handler 추가
        String handlerID = String.format("%s_%s", frameID, RosOperation.current());
        bus.consumer(handlerID, handler);

        if (!this.frameHandlers.containsKey(frameID)) {
            this.frameHandlers.put(frameID, new ArrayList<>());
            if(!this.republishUpdateRequested){
                this.updateGoal();
                this.republishUpdateRequested = true;
            }
        }

        // 이미 transform이 있으면 처리
        if (this.frameInfo.containsKey(frameID)) {
            TransformStamped transform = frameInfo.get(frameID);
            this.frameHandlers.get(frameID).forEach(id -> {
                bus.publish(id, transform, transformDelivery);
            });
        }

        // 구독 ID 추가
        frameHandlers.get(frameID).add(handlerID);

        return handlerID;
    }

    /**
     * Unsubscribe from the given TF frame.
     *
     * @param frameID   The TF frame to unsubscribe from.
     * @param handlerID The callback function to remove.
     */
    public void unsubscribe(String frameID, String handlerID) {
        EventBus bus = bridge.getBus();
        frameID = parsingFrameID(frameID);

        if (frameHandlers.containsKey(frameID)) {
            bus.consumer(handlerID).unregister();
            frameHandlers.get(frameID).remove(handlerID);

            if(frameHandlers.get(frameID).isEmpty()){
                frameHandlers.remove(frameID);
            }
        }
    }
}
