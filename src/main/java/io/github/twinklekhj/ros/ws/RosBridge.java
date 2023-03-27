package io.github.twinklekhj.ros.ws;

import io.github.twinklekhj.ros.op.*;
import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.ws.codec.RosResponseCodec;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketConnectOptions;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * RosBridge 총괄 클래스
 *
 * @author khj
 * @since 2023.03.10
 */
public class RosBridge extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(RosBridge.class);
    private static final MessageCodec rosResponseCodec = new RosResponseCodec();
    private static final DeliveryOptions rosResponseDelivery = new DeliveryOptions().setCodecName(rosResponseCodec.name());

    protected final Vertx vertx;
    protected final ConnProps props;

    protected WebSocket webSocket;
    protected EventBus bus;

    protected Set<String> publishedTopics = new HashSet<>();
    protected Map<String, Set<String>> topicListeners = new HashMap<>();
    protected Set<String> serviceListeners = new HashSet<>();

    protected Set<String> codecs = new HashSet<>();
    protected Map<String, FragmentManager> fragmentManagers = new HashMap<>();

    protected boolean connected = false;
    protected boolean connectedError = false;

    public RosBridge(Vertx vertx, ConnProps props) {
        this.vertx = vertx;
        this.props = props;
    }

    public boolean hasConnected() {
        return connected;
    }

    public boolean hasConnectedError() {
        return connectedError;
    }

    public void onMessage(Buffer buffer) {
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

                    Set<String> listeners = this.topicListeners.get(topic);
                    listeners.forEach(listener -> {
                        this.bus.publish(listener, json);
                    });
                    break;
                case "service_response":
                    RosResponse res = RosResponse.fromJsonObject(json);
                    this.bus.send(res.getId(), res, rosResponseDelivery);
                    this.bus.consumer(res.getId()).unregister();
                    this.serviceListeners.remove(res.getId());

                    break;
                case "fragment":
                    processFragment(buffer);
                    break;
            }
        }
    }

    /**
     * [Vertx] EventBus
     *
     * @return bus
     */
    public EventBus getBus() {
        return bus;
    }

    /**
     * [RosBridge] 연결 객체
     *
     * @return props
     */
    public ConnProps getProps() {
        return props;
    }

    /**
     * [Service] 남아있는 서비스 목록
     *
     * @return 서비스 리스트
     */
    public Set<String> getServiceListeners() {
        return serviceListeners;
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
     * [Topic] 토픽 구독 여부 확인
     *
     * @param topic 토픽명
     * @return 토픽 구독 여부
     */
    public boolean isSubscribed(String topic) {
        return topicListeners.containsKey(topic);
    }

    /**
     * [Topic] 토픽 구독 여부 확인
     *
     * @param topic 토픽
     * @return 토픽 구독 여부
     */
    public boolean isSubscribed(RosTopic topic) {
        return topicListeners.containsKey(topic.getName());
    }

    @Override
    public void start() {
        Future<WebSocket> future = connect();
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

        // HttpClient 생성 Option
        HttpClientOptions httpOptions = new HttpClientOptions();
        httpOptions.setMaxWebSocketFrameSize(props.getMaxFrameSize());
        httpOptions.setMaxWebSocketMessageSize(props.getMaxMessageSize());

        // HttpClient 생성
        HttpClient client = this.vertx.createHttpClient(httpOptions);

        // WebSocketClient 생성 Option
        WebSocketConnectOptions wsOptions = new WebSocketConnectOptions();
        wsOptions.setHost(props.getHost());
        wsOptions.setPort(props.getPort());
        wsOptions.setTimeout(props.getConnectTimeout());

        // WebSocketClient 생성
        Future<WebSocket> future = client.webSocket(wsOptions);
        future.onSuccess(webSocket -> {
            logger.info("WebSocket Connected! {}", webSocket);
            synchronized (this) {
                this.webSocket = webSocket;
                this.bus = vertx.eventBus();
                if (!codecs.contains(rosResponseCodec.name())) {
                    this.bus.registerCodec(rosResponseCodec);
                    codecs.add(rosResponseCodec.name());
                }
                this.connected = true;
                webSocket.handler(this::onMessage);
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

        return future;
    }

    public Future<Void> close() {
        return this.webSocket.close();
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
        if (props.isPrintProcessMsg()) {
            logger.info("ros:send message");
        }

        synchronized (this) {
            while (!this.connected || !this.connectedError) {
                if (hasConnected()) {
                    if (this.props.isPrintSendMsg()) {
                        logger.info("[REQUEST] msg: {}", message);
                    }
                    return this.webSocket.writeTextMessage(message);
                } else if (hasConnectedError()) {
                    return Future.failedFuture("WebSocket not connected!");
                } else {
                    try {
                        wait();
                    } catch (Exception cause) {
                        return Future.failedFuture(cause);
                    }
                }
            }
        }

        return Future.failedFuture("unhandled error!");
    }

    /**
     * [Topic] 토픽 발행 공고
     *
     * @param op 발행정보
     * @return 콜백함수
     */
    public Promise<RosAdvertise> advertise(RosAdvertise op) {
        if (props.isPrintProcessMsg()) {
            logger.info("ros:advertise topic, {}", op.getTopic());
        }

        Promise<RosAdvertise> promise = Promise.promise();

        send(op).onSuccess(unused -> {
            this.publishedTopics.add(op.getTopic());
            promise.complete(op);
        }).onFailure(promise::fail);

        return promise;
    }

    /**
     * [Topic] 토픽 발행 공고
     *
     * @param topic 토픽정보
     * @return 콜백함수
     */
    public Promise<RosAdvertise> advertise(RosTopic topic) {
        RosAdvertise op = RosAdvertise.builder(topic.getName(), topic.getType()).build();
        return advertise(op);
    }

    /**
     * [Topic] 토픽 발행 취소
     *
     * @param op 발행취소 정보
     * @return 콜백함수
     */
    private Promise<RosUnadvertise> unadvertise(RosUnadvertise op) {
        if (props.isPrintProcessMsg()) {
            logger.info("ros:unadvertise, {}", op);
        }

        Promise<RosUnadvertise> promise = Promise.promise();

        send(op).onSuccess(unused -> {
            this.publishedTopics.remove(op.getTopic());
            promise.complete(op);
        }).onFailure(promise::fail);

        return promise;
    }

    public Promise<RosUnadvertise> unadvertise(String topic) {
        return unadvertise(RosUnadvertise.builder(topic).build());
    }

    /**
     * [Topic] 토픽 발행 취소
     *
     * @param topic 토픽명
     * @return 콜백함수
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
     * @return 콜백함수
     */
    public Promise<RosTopic> publish(String topic, String type, Object msg) {
        return publish(RosTopic.builder(topic, type).msg(msg).build());
    }

    /**
     * [Topic] 토픽 발행
     *
     * @param topic 토픽
     * @return 콜백함수
     */
    public Promise<RosTopic> publish(RosTopic topic) {
        if (props.isPrintProcessMsg()) {
            logger.info("ros:publish, {}", topic);
        }

        Promise<RosTopic> promise = Promise.promise();
        advertise(topic).future().onSuccess(advertise -> {
            send(topic).onSuccess(unused -> {
                promise.complete(topic);
            }).onFailure(promise::fail);
        }).onFailure(promise::fail);

        return promise;
    }


    /**
     * [Topic] 토픽 구독
     *
     * @param op      토픽 구독
     * @param handler 토픽 메세지 처리자
     * @return 콜백함수
     */
    public Promise<RosSubscription> subscribe(RosSubscription op, Handler<Message<JsonObject>> handler) {
        if (props.isPrintProcessMsg()) {
            logger.info("ros:subscribe, {}", op);
        }
        Promise<RosSubscription> promise = Promise.promise();

        String topic = op.getTopic();
        send(op).onSuccess(unused -> {
            if (!this.topicListeners.containsKey(topic)) {
                this.topicListeners.put(topic, new HashSet<>());
            }
            this.topicListeners.get(topic).add(op.getId());
            this.bus.consumer(op.getId(), handler);

            promise.complete(op);
        }).onFailure(promise::fail);

        return promise;
    }

    /**
     * [Topic] 토픽 구독
     *
     * @param topic   토픽명
     * @param type    토픽 메세지 유형
     * @param handler 토픽 메세지 처리자
     * @return 콜백함수
     */
    public Promise<RosSubscription> subscribe(String topic, String type, Handler<Message<JsonObject>> handler) {
        return subscribe(RosSubscription.builder(topic, type).build(), handler);
    }

    /**
     * [Topic] 토픽 구독
     *
     * @param topic   토픽명
     * @param type    토픽 메세지 유형
     * @param handler 토픽 메세지 처리자
     * @return 콜백함수
     */
    public Promise<RosSubscription> subscribe(String topic, RosMessage.Type type, Handler<Message<JsonObject>> handler) {
        return subscribe(RosSubscription.builder(topic, type).build(), handler);
    }

    /**
     * [Topic] 토픽 구독
     *
     * @param topic   토픽 정보
     * @param handler 토픽 메세지 처리자
     * @return 콜백함수
     */
    public Promise<RosSubscription> subscribe(RosTopic topic, Handler<Message<JsonObject>> handler) {
        return subscribe(RosSubscription.builder(topic.getName(), topic.getType()).build(), handler);
    }

    /**
     * [Topic] 토픽 구독 해제
     *
     * @param op 구독해제 정보
     * @return 콜백함수
     */
    private Promise<RosUnsubscription> unsubscribe(RosUnsubscription op) {
        if (props.isPrintProcessMsg()) {
            logger.info("ros:unsubscribe, {}", op);
        }

        Promise<RosUnsubscription> promise = Promise.promise();

        send(op).onComplete(result -> {
            if (result.succeeded()) {
                Set<String> topics = this.topicListeners.get(op.getTopic());

                // 선택 구독 해제
                if (!op.getId().equals("")) {
                    topics.remove(op.getId());
                    this.bus.consumer(op.getId()).unregister();
                }
                // 전체 구독 해제
                else if (topics != null && !topics.isEmpty()) {
                    topics.forEach(name -> {
                        this.bus.consumer(name).unregister();
                    });

                    this.topicListeners.remove(op.getTopic());
                }
            }
            promise.complete(op);
        }).onComplete(AsyncResult::failed);

        return promise;
    }

    /**
     * [Topic] 토픽 구독 해제
     *
     * @param topic 토픽명
     * @return 콜백함수
     */
    public Promise<RosUnsubscription> unsubscribe(String topic) {
        RosUnsubscription op = RosUnsubscription.builder(topic).build();
        return unsubscribe(op);
    }

    /**
     * [Topic] 토픽 구독 해제
     *
     * @param topic 토픽명
     * @return 콜백함수
     */
    public Promise<RosUnsubscription> unsubscribe(RosTopic topic) {
        RosUnsubscription op = RosUnsubscription.builder(topic.getName()).build();
        return unsubscribe(op);
    }

    /**
     * [Topic] 토픽 구독 해제
     *
     * @param topic 토픽명
     * @return 콜백함수
     */
    public Promise<RosUnsubscription> unsubscribe(String topic, String id) {
        RosUnsubscription op = RosUnsubscription.builder(topic).id(id).build();
        return unsubscribe(op);
    }


    /**
     * [Service] Service 요청
     *
     * @param service 서비스명
     * @param handler 서비스 응답 처리 함수
     * @return 요청 콜백함수
     */
    public Promise<RosService> callService(String service, Handler<Message<RosResponse>> handler) {
        return callService(RosService.builder(service).build(), handler);
    }

    /**
     * [Service] Service 요청
     *
     * @param service 서비스명
     * @return 응답 콜백함수
     */
    public Promise<RosResponse> callService(String service) {
        return callService(RosService.builder(service).build());
    }

    /**
     * [Service] Service 요청
     *
     * @param service 서비스명
     * @return 응답 콜백함수
     */
    public Promise<RosResponse> callService(RosService service) {
        Promise<RosResponse> promise = Promise.promise();
        callService(service, message -> {
            promise.complete(message.body());
        }).future().onFailure(promise::fail);
        return promise;
    }

    /**
     * [Service] Service 요청
     *
     * @param op      요청할 서비스 정보 객체
     * @param handler 서비스 응답 처리 함수
     * @return 콜백함수
     */
    public Promise<RosService> callService(RosService op, Handler<Message<RosResponse>> handler) {
        Promise<RosService> promise = Promise.promise();
        if (props.isPrintProcessMsg()) {
            logger.info("ros:callService, {}", op);
        }
        while (serviceListeners.contains(op.getId())) {
            op.setId(op.getId() + "_");
        }

        send(op).onSuccess(unused -> {
            serviceListeners.add(op.getId());
            this.bus.consumer(op.getId(), handler);

            promise.complete(op);
        }).onFailure(promise::fail);

        return promise;
    }

    /**
     * Fragment 처리하기
     *
     * @param buffer - 조각
     */
    protected void processFragment(Buffer buffer) {
        JsonObject json = buffer.toJsonObject();
        String id = json.getString("id");

        FragmentManager manager = this.fragmentManagers.get(id);
        if (manager == null) {
            manager = new FragmentManager(json);
            this.fragmentManagers.put(id, manager);
        }

        boolean complete = manager.updateFragment(buffer);
        if (complete) {
            Buffer fullMsg = manager.generateFullMessage();
            this.fragmentManagers.remove(id);

            onMessage(fullMsg);
        }
    }


    /**
     * Fragments 관리자
     */
    public static class FragmentManager {
        protected String id;
        protected Buffer[] fragments;
        protected Set<Integer> completedFragments;

        public FragmentManager(JsonObject json) {
            int total = json.getInteger("total");

            this.id = json.getString("id");
            this.fragments = new Buffer[total];
            this.completedFragments = new HashSet<>(total);
        }

        public boolean updateFragment(Buffer buffer) {
            JsonObject json = buffer.toJsonObject();
            String data = json.getString("data");
            int num = json.getInteger("num");

            this.fragments[num] = Buffer.buffer(data);
            this.completedFragments.add(num);

            return complete();
        }

        public boolean complete() {
            return (this.completedFragments.size() == this.fragments.length);
        }

        public int numFragments() {
            return this.fragments.length;
        }

        public int numCompletedFragments() {
            return this.completedFragments.size();
        }

        public Buffer generateFullMessage() {
            if (!complete()) {
                throw new RuntimeException("Cannot generate full message from fragments, because not all fragments have arrived.");
            }
            Buffer result = Buffer.buffer();
            for (Buffer fragment : this.fragments) {
                result.appendBuffer(fragment);
            }

            return result;
        }
    }
}
