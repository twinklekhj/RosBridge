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
 * @since 2023.02.15
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

        Promise<WebSocket> connectPromise = connect();
        Future<WebSocket> future = connectPromise.future();
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
        });
    }

    public Promise<WebSocket> connect() {
        Promise<WebSocket> promise = Promise.promise();

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

        client.webSocket(wsOptions).onSuccess(promise::complete).onFailure(throwable -> {
            promise.fail(String.format("Error! Message: %s", throwable.getMessage()));
            if (props.isPrintStackTrace()) {
                throwable.printStackTrace();
            }
        });

        return promise;
    }

    private Future<Void> send(RosOperation support) {
        String sendMsg = support.toString();
        return send(sendMsg);
    }

    /**
     * [Ros] Ros 메세지 전송
     *
     * @param message - 보낼 메세지
     * @return 메세지 전송 성공 여부
     */
    private Future<Void> send(String message) {
        while (!hasConnected() || !hasConnectedError()) {
            if (hasConnected()) {
                if (this.props.isPrintSendMsg()) {
                    logger.info("[REQUEST] msg: {}", message);
                }
                return this.socket.writeTextMessage(message);
            } else {
                try {
                    wait();
                } catch (Exception ignored) {

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
    public Future<Void> advertise(String topic, String type) {
        RosAdvertise op = RosAdvertise.builder(topic, type).build();
        this.publishedTopics.add(topic);
        return send(op);
    }

    /**
     * [Topic] 토픽 발행 상태 알림
     *
     * @param topic 토픽명
     * @param type  메세지 유형
     * @return 전송 성공 여부
     */
    public Future<Void> advertise(String topic, MessageType type) {
        return advertise(topic, type.getName());
    }

    /**
     * [Topic] 토픽 발행 취소
     *
     * @param topic 토픽명
     * @return 전송 성공 여부
     */
    public Future<Void> unadvertise(String topic) {
        RosUnadvertise op = RosUnadvertise.builder(topic).build();
        this.publishedTopics.remove(topic);
        return send(op);
    }

    /**
     * [Topic] 토픽 발행 취소
     *
     * @param topic 토픽명
     * @return 전송 성공 여부
     */
    public Future<Void> unadvertise(RosTopic topic) {
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
    public Future<Void> publish(String topic, String type, Object msg) {
        return publish(RosTopic.builder(topic, type).msg(msg).build());
    }

    public Future<Void> publish(RosTopic topic) {
        advertise(topic.getName(), topic.getType());
        return send(topic);
    }

    public Set<String> getPublishedTopics() {
        return publishedTopics;
    }

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
    public void subscribe(RosSubscription op, Handler<Message<JsonObject>> handler) {
        String topic = op.getTopic();
        Future<Void> future = send(op);
        future.onSuccess(unused -> {
            if (!this.topicListeners.containsKey(topic)) {
                this.topicListeners.put(topic, new ArrayList<>());
            }
            this.topicListeners.get(topic).add(handler);
        });
    }

    public RosSubscription subscribe(String topic, String type, Handler<Message<JsonObject>> handler) {
        RosSubscription op = RosSubscription.builder(topic, type).build();
        subscribe(op, handler);
        return op;
    }

    public RosSubscription subscribe(String topic, MessageType type, Handler<Message<JsonObject>> handler) {
        RosSubscription op = RosSubscription.builder(topic, type).build();
        subscribe(op, handler);
        return op;
    }

    /**
     * [Topic] 토픽 구독 해제
     *
     * @param topic 토픽명
     */
    public Future<Void> unsubscribe(String topic) {
        RosUnsubscription op = RosUnsubscription.builder(topic).build();
        return send(op).onComplete(result -> {
            if (result.succeeded()) {
                this.topicListeners.remove(topic);
            }
        });
    }

    public Future<Void> removeListener(String topic, Handler<Message<JsonObject>> handler) {
        List<Handler<Message<JsonObject>>> listeners = this.topicListeners.get(topic);
        if (listeners != null) {
            listeners.remove(handler);

            if (listeners.size() == 0) {
                return unsubscribe(topic);
            }
        }

        return Future.succeededFuture();
    }

    /**
     * [Service] Service 요청
     *
     * @param service 서비스명
     * @param args    요청변수
     * @param handler 서비스 응답 처리 함수
     * @return 서비스 전송 성공여부
     */
    public Future<Void> callService(String service, List<?> args, Handler<Message<JsonObject>> handler) {
        RosService op = RosService.builder(service).args(args).build();
        return callService(op, handler);
    }

    /**
     * [Service] Service 요청
     *
     * @param op      요청할 서비스 정보 객체
     * @param handler 서비스 응답 처리 함수
     * @return 서비스 전송 성공여부
     */
    public Future<Void> callService(RosService op, Handler<Message<JsonObject>> handler) {
        return send(op).onComplete(ar -> {
            if (ar.succeeded()) {
                serviceListeners.put(op.getId(), handler);
                this.bus.consumer(op.getId(), handler);
            }
        }).onFailure(throwable -> {
            logger.error("Error! Message: {}", throwable.getMessage());
            if (props.isPrintStackTrace()) {
                throwable.printStackTrace();
            }
        });
    }

    /**
     * [RosBridge] ROS Topic 목록 조회
     */
    public void getTopics() {

        RosService service = RosService.builder("/rosapi/topics").build();

        Promise<RosResponse> promise = Promise.promise();
        promise.future().onComplete(rosResponseAsyncResult -> {
            if (rosResponseAsyncResult.succeeded()) {
                RosResponse res = rosResponseAsyncResult.result();
                logger.info("res: {}", res.getValues());
            }
        });
    }
}
