package io.github.twinklekhj.ros.tf;

import io.github.twinklekhj.ros.core.RosBridge;
import io.github.twinklekhj.ros.op.*;
import io.github.twinklekhj.ros.type.geometry.TransformStamped;
import io.github.twinklekhj.ros.type.tf.TFArray;
import io.github.twinklekhj.ros.codec.TFMessageCodec;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A TF Client that listens to TFs from tf2_web_republisher
 * - fixedFrame: The fixed frame.
 * - angularThresh: The angular threshold for the TF republisher.
 * - transThresh: The translation threshold for the TF republisher.
 * - rate: The rate for the TF republisher.
 * - updateDelay: The time (in ms) to wait after a new subscription to update the TF republisher's list of TFs.
 */
@ToString
public class TFClient {
    private static final Logger logger = LoggerFactory.getLogger(TFClient.class);

    private static final MessageCodec transformCodec = new TFMessageCodec();
    private static final DeliveryOptions transformDelivery = new DeliveryOptions().setCodecName(transformCodec.name());

    protected final Map<String, TFFrame> frameInfo = new HashMap<>();

    private final RosBridge bridge;

    private String serverName = "/tf2_web_republisher";
    private String republishServiceName = "/republish_tfs";
    private String fixedFrame = "base_link";

    private double angularThresh = 2.0;
    private double transThresh = 0.01;
    private double rate = 10.0;
    private long updateDelay = 50;

    private boolean republishUpdateRequested = false;

    public TFClient(RosBridge bridge) {
        this.bridge = bridge;
    }
    public TFClient(RosBridge bridge, String parent) {
        this.bridge = bridge;
        this.serverName = parent + "/tf_web_republisher";
        this.republishServiceName = parent + "/republish_tfs";
    }

    /**
     * Set the server name
     * @param serverName customized server name.
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * Set the service name
     * @param republishServiceName service name
     */
    public void setRepublishServiceName(String republishServiceName) {
        this.republishServiceName = republishServiceName;
    }

    /**
     * Set the fixed frame.
     * @param fixedFrame frame name
     */
    public void setFixedFrame(String fixedFrame) {
        this.fixedFrame = fixedFrame;
    }

    /**
     * Set the angular threshold for the TF republisher.
     * @param angularThresh the angular threshold
     */
    public void setAngularThresh(double angularThresh) {
        this.angularThresh = angularThresh;
    }

    /**
     * Set the translation threshold for the TF republisher.
     * @param transThresh the translation threshold
     */
    public void setTransThresh(double transThresh) {
        this.transThresh = transThresh;
    }

    /**
     * Set the rate for the TF republisher.
     * @param rate the rate
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * Set the time (in ms) to wait after a new subscription
     * @param updateDelay wait time
     */
    public void setUpdateDelay(long updateDelay) {
        this.updateDelay = updateDelay;
    }

    private String parsingFrameID(String frame) {
        if (frame.startsWith("/")) {
            frame = frame.substring(1);
        }
        return frame;
    }


    public Future<Void> updateGoal(){
        RosService service = RosService.builder(this.republishServiceName, "tf2_web_republisher/RepublishTFs").build();
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

        RosTopic topic = RosTopic.builder(topicName, TFArray.TYPE).build();
        if(this.bridge.isSubscribed(topic)){
            this.bridge.unsubscribe(topic);
        }

        return this.bridge.subscribe(topic, message -> {
            logger.info("message: {}", message);
            JsonObject json = message.body();
            TFArray tf = TFArray.fromJsonObject(json);

            this.processTFArray(tf);
        }).future();
    }

    /**
     * Process the incoming TF message and send them out using the callback functions.
     * @param tf The TF message from the server.
     */
    private void processTFArray(TFArray tf) {
        for (TransformStamped transformStamped: tf.getTransforms()){
            String frameID = parsingFrameID(transformStamped.getChildFrameID());

            if(this.frameInfo.containsKey(frameID)){
                TFFrame frame = this.frameInfo.get(frameID);

                frame.setTransform(transformStamped);
                frame.getHandlers().forEach(cb -> {
                    cb.handle(frame.transform);
                });
            }

        }
    }


    /**
     * Subscribe to the given TF frame.
     *
     * @param frameID The TF frame to subscribe to.
     * @param handler handler
     * @return handler ID
     */
    public String subscribe(String frameID, Handler<TransformStamped> handler) {
        EventBus bus = this.bridge.getBus();
        frameID = parsingFrameID(frameID);

        if (!this.frameInfo.containsKey(frameID)) {
            this.frameInfo.put(frameID, new TFFrame(frameID));

            if(!this.republishUpdateRequested){
                bridge.getVertx().setTimer(this.updateDelay, aLong -> {
                    this.updateGoal();
                });
                this.republishUpdateRequested = true;
            }
        }
        // 이미 transform이 있으면 처리
        else if (this.frameInfo.get(frameID).hasTransform()) {
            TransformStamped transform = frameInfo.get(frameID).getTransform();
            handler.handle(transform);
        }

        // transform handler 추가
        return frameInfo.get(frameID).addHandler(handler);
    }

    /**
     * Unsubscribe from the given TF frame.
     *
     * @param frameID   The TF frame to unsubscribe from.
     * @param handlerID The callback function to remove.
     */
    public void unsubscribe(String frameID, String handlerID) {
        frameID = parsingFrameID(frameID);

        if (frameInfo.containsKey(frameID)) {
            frameInfo.get(frameID).removeHandler(handlerID);

            if(!frameInfo.get(frameID).hasHandler()){
                frameInfo.remove(frameID);
            }
        }
    }
}
