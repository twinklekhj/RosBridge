package io.github.twinklekhj.ros.ws;

import io.github.twinklekhj.ros.op.*;
import io.github.twinklekhj.ros.type.MessageType;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketConnectOptions;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * RosBridge 총괄 클래스
 *
 * @author khj
 * @since 2023.03.10
 */
public class RosVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(RosVerticle.class);

    protected final Vertx vertx;
    protected final ConnProps props;

    protected WebSocket socket;
    protected EventBus bus;

    protected Set<String> publishedTopics = new HashSet<>();
    protected Map<String, List<Handler<Message<JsonObject>>>> topicListeners = new HashMap<>();
    protected Map<String, Handler<Message<JsonObject>>> serviceListeners = new HashMap<>();

    protected boolean connected = false;
    protected boolean connectedError = false;

    public RosVerticle(Vertx vertx, ConnProps props) {
        this.vertx = vertx;
        this.props = props;
    }

    public boolean hasConnected() {
        return connected;
    }

    public boolean hasConnectedError() {
        return connectedError;
    }

    @Override
    public void start() {
        RosVerticle that = this;

        Handler<Buffer> onMessage = buffer -> {
            String msg = buffer.toString();
            if (this.props.isPrintReceivedMsg()) {
                logger.info("[RESPONSE] msg: {}", msg);
            }

            JsonObject json = buffer.toJsonObject();
            if (json.containsKey("op")) {
                // publish
                String op = json.getString("op");
                switch (op) {
                    case "publish":
                        String topic = json.getString("topic");
                        this.topicListeners.get(topic).forEach(handler -> {

                        });
                        break;
                    case "service_response":
                        RosResponse res = RosResponse.fromJsonObject(json);
                        this.bus.publish(res.getId(), json);
                        this.bus.consumer(res.getId()).unregister();
                        break;
                }
            }
        };

        Future<WebSocket> future = connect();
        future.onSuccess(socket -> {
            logger.info("WebSocket Connected! {}", socket);
            synchronized (this) {
                this.socket = socket;
                this.bus = vertx.eventBus();
                this.connected = true;
                socket.handler(onMessage);
                notifyAll();
            }
        }).onFailure(throwable -> {
            logger.error("WebSocket Connect Error! {}", props);
            synchronized (this) {
                this.connectedError = true;
                notifyAll();
            }
            if (props.isPrintStackTrace()) {
                throwable.printStackTrace();
            }
        });
    }


    /**
     * [Ros] WebSocket 연결
     *
     * @return callback 함수
     */
    public Future<WebSocket> connect() {
        HttpClientOptions options = new HttpClientOptions();
        if (props.getIdleTimeout() != 0) {
            options.setIdleTimeout(props.getIdleTimeout());
        }
        options.setConnectTimeout(props.getConnectTimeout());

        HttpClient client = this.vertx.createHttpClient();

        WebSocketConnectOptions wsOptions = new WebSocketConnectOptions();
        wsOptions.setHost(props.getHost());
        wsOptions.setPort(props.getPort());
        wsOptions.setTimeout(props.getConnectTimeout());

        return client.webSocket(wsOptions);
    }

    private Future<Void> send(RosOperation support) {
        String sendMsg = support.toJson();
        return send(sendMsg);
    }

    /**
     * [Ros] Ros 메세지 전송
     *
     * @param message - 보낼 메세지
     * @return 메세지 전송 성공 여부
     */
    private Future<Void> send(String message) {
        logger.info("ros:send message");
        synchronized (this){
            while (!hasConnected() || !hasConnectedError()) {
                if (hasConnected()) {
                    if (this.props.isPrintSendMsg()) {
                        logger.info("[REQUEST] msg: {}", message);
                    }
                    return this.socket.writeTextMessage(message);
                } else {
                    try {
                        wait();
                    } catch (Exception cause) {
                        return Future.failedFuture(cause);
                    }
                }
            }
        }

        return Future.failedFuture("WebSocket not connected!");
    }

    /**
     * [Topic] 토픽 발행 상태 알림
     *
     * @param topic 토픽명
     * @param type  메세지 유형
     * @return 전송 성공 여부
     */
    public Promise<RosAdvertise> advertise(String topic, String type) {
        logger.info("ros:advertise topic {}", topic);

        Promise<RosAdvertise> promise = Promise.promise();
        RosAdvertise op = RosAdvertise.builder(topic, type).build();

        send(op).onSuccess(unused -> {
            this.publishedTopics.add(topic);
            promise.complete(op);
        }).onFailure(promise::fail);

        return promise;
    }

    /**
     * [Topic] 토픽 발행 상태 알림
     *
     * @param topic 토픽명
     * @param type  메세지 유형
     * @return 전송 성공 여부
     */
    public Promise<RosAdvertise> advertise(String topic, MessageType type) {
        return advertise(topic, type.getName());
    }

    /**
     * [Topic] 토픽 발행 취소
     *
     * @param topic 토픽명
     * @return 전송 성공 여부
     */
    public Promise<RosUnadvertise> unadvertise(String topic) {
        logger.info("ros:unadvertise topic {}", topic);

        Promise<RosUnadvertise> promise = Promise.promise();

        RosUnadvertise op = RosUnadvertise.builder(topic).build();
        send(op).onSuccess(unused -> {
            this.publishedTopics.remove(topic);
            promise.complete(op);
        }).onFailure(promise::fail);

        return promise;
    }

    /**
     * [Topic] 토픽 발행 취소
     *
     * @param topic 토픽명
     * @return 전송 성공 여부
     */
    public Promise<RosUnadvertise> unadvertise(RosTopic topic) {
        return unadvertise(topic.getName());
    }

    /**
     * [Topic] 토픽 발행
     *
     * @param topic 토픽명
     * @param type  메시지 유형
     * @param msg   보낼 메세지
     * @return 전송 성공 여부
     */
    public Promise<RosTopic> publish(String topic, String type, Object msg) {
        return publish(RosTopic.builder(topic, type).msg(msg).build());
    }

    public Promise<RosTopic> publish(RosTopic topic) {
        logger.info("ros:publish topic {}", topic);

        Promise<RosTopic> promise = Promise.promise();
        advertise(topic.getName(), topic.getType()).future().onSuccess(advertise -> {
            send(topic).onSuccess(unused -> {
                promise.complete();
            }).onFailure(promise::fail);
        }).onFailure(promise::fail);

        return promise;
    }

    /**
     * [Topic] 발행한 토픽 목록
     *
     * @return 토픽 리스트
     */
    public Set<String> getPublishedTopics() {
        return publishedTopics;
    }

    /**
     * [Topic] 구독한 토픽 목록
     *
     * @return 토픽 리스트
     */
    public Set<String> getSubscribedTopics() {
        return topicListeners.keySet();
    }

    /**
     * [Topic] 토픽 구독
     *
     * @param op      토픽 구독
     * @param handler 토픽 메세지 처리자
     * @return
     */
    public Promise<RosSubscription> subscribe(RosSubscription op, Handler<Message<JsonObject>> handler) {
        logger.info("ros:subscribe topic {}", op.getTopic());
        Promise<RosSubscription> promise = Promise.promise();

        String topic = op.getTopic();
        send(op).onSuccess(unused -> {
            if (!this.topicListeners.containsKey(topic)) {
                this.topicListeners.put(topic, new ArrayList<>());
            }
            this.topicListeners.get(topic).add(handler);
            promise.complete(op);
        }).onFailure(promise::fail);

        return promise;
    }

    public Promise<RosSubscription> subscribe(String topic, String type, Handler<Message<JsonObject>> handler) {
        RosSubscription op = RosSubscription.builder(topic, type).build();
        return subscribe(op, handler);
    }

    public Promise<RosSubscription> subscribe(String topic, MessageType type, Handler<Message<JsonObject>> handler) {
        RosSubscription op = RosSubscription.builder(topic, type).build();
        return subscribe(op, handler);
    }

    /**
     * [Topic] 토픽 구독 해제
     *
     * @param topic 토픽명
     */
    public Promise<Void> unsubscribe(String topic) {
        logger.info("ros:unsubscribe topic {}", topic);
        Promise<Void> promise = Promise.promise();

        RosUnsubscription op = RosUnsubscription.builder(topic).build();
        send(op).onComplete(result -> {
            if (result.succeeded()) {
                this.topicListeners.remove(topic);
            }
            promise.complete();
        }).onComplete(AsyncResult::failed);

        return promise;
    }

    public void removeListener(String topic, Handler<Message<JsonObject>> handler) {
        List<Handler<Message<JsonObject>>> listeners = this.topicListeners.get(topic);
        if (listeners != null) {
            listeners.remove(handler);

            if (listeners.size() == 0) {
                unsubscribe(topic);
            }
        }
    }

    /**
     * [Service] Service 요청
     *
     * @param service 서비스명
     * @param args    요청변수
     * @param handler 서비스 응답 처리 함수
     * @return 콜백함수
     */
    public Promise<RosService> callService(String service, List<?> args, Handler<Message<JsonObject>> handler) {
        RosService op = RosService.builder(service).args(args).build();
        return callService(op, handler);
    }

    /**
     * [Service] Service 요청
     *
     * @param op      요청할 서비스 정보 객체
     * @param handler 서비스 응답 처리 함수
     * @return 콜백함수
     */
    public Promise<RosService> callService(RosService op, Handler<Message<JsonObject>> handler) {
        Promise<RosService> promise = Promise.promise();
        logger.info("ros:callService");

        send(op).onSuccess(unused -> {
            serviceListeners.put(op.getId(), handler);
            this.bus.consumer(op.getId(), handler);
            promise.complete(op);
        }).onFailure(promise::fail);

        return promise;
    }

    /**
     * [RosBridge] ROS Topic 목록 조회
     *
     * @param handler 응답 처리 함수
     * @return 콜백함수
     */
    public Promise<RosService> getTopics(Handler<Message<JsonObject>> handler) {
        logger.info("ros:getTopics");

        RosService service = RosService.builder("/rosapi/topics").build();
        return callService(service, handler);
    }

    /**
     * [RosBridge] ROS Service 목록 조회
     *
     * @param handler 응답 처리 함수
     * @return 콜백함수
     */
    public Promise<RosService> getServices(Handler<Message<JsonObject>> handler) {
        RosService service = RosService.builder("/rosapi/services").build();
        return callService(service, handler);
    }

    /**
     * [RosBridge] ROS Node 목록 조회
     *
     * @param handler 응답 처리 함수
     * @return 콜백함수
     */
    public Promise<RosService> getNodes(Handler<Message<JsonObject>> handler) {
        RosService service = RosService.builder("/rosapi/nodes").build();
        return callService(service, handler);
    }

    /**
     * [RosBridge] ROS Node 상세 정보 조회
     *
     * @param node     찾을 노드명
     * @param handler 응답 처리 함수
     * @return 콜백함수
     */
    public Promise<RosService> getNodeDetails(String node, Handler<Message<JsonObject>> handler) {
        RosService service = RosService.builder("/rosapi/node_details").args(node).build();
        return callService(service, handler);
    }
}
