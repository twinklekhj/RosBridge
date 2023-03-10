package io.github.twinklekhj.ros.ws;

import io.github.twinklekhj.ros.op.*;
import io.github.twinklekhj.ros.type.MessageType;
import io.github.twinklekhj.ros.ws.listener.RosServiceDelegate;
import io.github.twinklekhj.ros.ws.listener.RosSubscribeDelegate;
import io.github.twinklekhj.ros.ws.listener.RosSubscribers;
import io.vertx.core.json.JsonObject;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * RosBridge 총괄 클래스
 *
 * @author khj
 * @since 2023.02.15
 */
@WebSocket
public class RosBridge {
    private static final Logger logger = LoggerFactory.getLogger(RosBridge.class);
    protected final CountDownLatch closeLatch;
    protected ConnProps props;

    protected Session session;
    protected Set<String> publishedTopics = new HashSet<>();
    protected Map<String, FragmentManager> fragmentManagers = new HashMap<>();
    protected Map<String, RosSubscribers> topicListeners = new HashMap<>();
    protected Map<String, RosServiceDelegate> serviceListeners = new HashMap<>();

    protected boolean connected = false;
    protected boolean connectedError = false;


    private RosBridge() {
        this.closeLatch = new CountDownLatch(1);
    }

    /**
     * RosBridge 객체 생성후 WebSocket 연결
     *
     * @param host              ROS BRIDGE HOST
     * @param port              ROS BRIDGE PORT
     * @param waitForConnection 기다림 여부
     * @return RosBridge 객체
     */
    public static RosBridge createBridge(String host, int port, boolean waitForConnection) {
        ConnProps props = ConnProps.builder(host, port, waitForConnection).build();
        return createBridge(props);
    }

    /**
     * Connection 객체로 RosBridge 객체 생성후 WebSocket 연결
     *
     * @param props 연결 옵션 객체
     * @return RosBridge 객체
     */
    public static RosBridge createBridge(ConnProps props) {
        RosBridge bridge = new RosBridge();
        bridge.connect(props);
        bridge.enablePrintMsgSend(props.printSendMsg);
        bridge.enablePrintMsgReceived(props.printReceivedMsg);
        bridge.enablePrintStackTrace(props.printStackTrace);

        return bridge;
    }


    /**
     * 메시지 송신시 로그 사용여부
     *
     * @param flag 로깅 활성 여부
     */
    public void enablePrintMsgSend(boolean flag) {
        this.props.setPrintSendMsg(flag);
    }

    /**
     * 메시지 수신시 로그 사용여부
     *
     * @param flag 로깅 활성여부
     */
    public void enablePrintMsgReceived(boolean flag) {
        this.props.setPrintReceivedMsg(flag);
    }

    /**
     * 에러 발생시 Stack Trace 출력여부
     *
     * @param flag 에러 출력여부
     */
    public void enablePrintStackTrace(boolean flag) {
        this.props.setPrintStackTrace(flag);
    }

    /**
     * WebSocket 연결
     *
     * @param props - 웹소켓 연결 정보
     */
    private void connect(ConnProps props) {
        this.props = props;

        WebSocketClient client = new WebSocketClient();

        try {
            if (props.idleTimeout != 0) {
                client.setMaxIdleTimeout(props.idleTimeout);
            }
            if (props.connectTimeout != 0) {
                client.setConnectTimeout(props.connectTimeout);
            }
            if (props.stopTimeout != 0) {
                client.setStopTimeout(props.stopTimeout);
            }
            client.start();

            String url = String.format("ws://%s:%s", props.host, props.port);
            URI echoUri = new URI(url);

            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(this, echoUri, request);
            logger.info("Connecting to: {}", echoUri);
            if (props.wait) {
                waitForConnection();
            }
        } catch (Exception e) {
            logger.error("Error! Message: {}", e.getMessage());
            if (props.printStackTrace) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 연결될 때까지 기다린다
     */
    public void waitForConnection() {
        if (this.connected || this.connectedError) {
            return;
        }

        synchronized (this) {
            while (!this.connected && !this.connectedError) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    logger.error("Error! Message: {}", e.getMessage());
                    if (this.props.isPrintStackTrace()) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 연결상태 여부
     *
     * @return 연결상태
     */
    public boolean hasConnected() {
        return this.connected;
    }

    public boolean awaitClose(int duration, TimeUnit unit) {
        try {
            return this.closeLatch.await(duration, unit);
        } catch (InterruptedException e) {
            logger.error("Error! message: {}", e.getMessage());
            if (this.props.isPrintStackTrace()) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public void countDownLatch() {
        this.closeLatch.countDown();
    }

    public long getLatchCount() {
        return closeLatch.getCount();
    }

    /**
     * WebSocket 연결 Event
     *
     * @param session - 연결 세션
     */
    @OnWebSocketConnect
    public void onConnect(Session session) {
        logger.info("WebSocket Connected! Session: {}", session);

        this.session = session;
        this.connected = true;
        synchronized (this) {
            notifyAll();
        }
    }

    /**
     * WebSocket 연결 종료 Event
     *
     * @param statusCode 상태코드
     * @param reason     종료 이유
     */
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        logger.info("Connection closed. code: {}, reason: {}", statusCode, reason);
        this.session = null;
        this.closeLatch.countDown();
    }

    @OnWebSocketError
    public void onError(Session session, Throwable e) {
        logger.warn("WebSocket Error! msg: {}", e.getMessage());
        if (this.props.isPrintStackTrace()) {
            e.printStackTrace();
        }

        synchronized (this) {
            this.connectedError = true;
            notifyAll();
        }
        if (this.session == null) {
            this.closeLatch.countDown();
        }
    }

    /**
     * WebSocket 연결 해제
     */
    public void close() {
        this.session.close();
    }

    /**
     * WebSocket 재연결
     *
     * @param flag 연결 기다림 여부
     */
    public void reconnect(boolean flag) {
        props.setWait(flag);
        this.connect(props);
    }

    /**
     * WebSocket 메세지 수신 Event
     *
     * @param msg 수신받은 메세지
     */
    @OnWebSocketMessage
    public void onMessage(String msg) {
        if (this.props.isPrintReceivedMsg()) {
            logger.info("[RESPONSE] msg: {}", msg);
        }
        JsonObject json = new JsonObject(msg);
        if (json.containsKey("op")) {
            // publish
            String op = json.getString("op");
            switch (op) {
                case "publish":
                    String topic = json.getString("topic");
                    RosSubscribers subscriber = this.topicListeners.get(topic);
                    if (subscriber != null) {
                        subscriber.receive(json, msg);
                    }
                    break;
                case "service_response":
                    RosResponse res = RosResponse.fromJsonObject(json);
                    RosServiceDelegate delegate = serviceListeners.remove(res.getId());
                    if (delegate != null) {
                        delegate.receive(res);
                    }
                    break;
                case "fragment":
                    processFragment(json);
                    break;
            }
        }
    }

    private boolean send(RosOperation support) {
        String sendMsg = support.toString();
        return send(sendMsg);
    }

    /**
     * [Ros] Ros 메세지 전송
     *
     * @param message - 보낼 메세지
     * @return 메세지 전송 성공 여부
     */
    private boolean send(String message) {
        if (this.props.isPrintSendMsg()) {
            logger.info("[REQUEST] msg: {}", message);
        }
        try {
            Future<Void> fut = this.session.getRemote().sendStringByFuture(message);
            fut.get(2L, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            logger.error("Error! Message: {}", message);
            if (this.props.isPrintStackTrace()) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * [Topic] 토픽 발행 상태 알림
     *
     * @param topic 토픽명
     * @param type  메세지 유형
     * @return 전송 성공 여부
     */
    public boolean advertise(String topic, String type) {
        RosAdvertise op = RosAdvertise.builder(topic, type).build();

        if (!this.publishedTopics.contains(topic)) {
            if (send(op)) {
                this.publishedTopics.add(topic);
                return true;
            }
        }
        return false;
    }

    /**
     * [Topic] 토픽 발행 상태 알림
     *
     * @param topic 토픽명
     * @param type  메세지 유형
     * @return 전송 성공 여부
     */
    public boolean advertise(String topic, MessageType type) {
        return advertise(topic, type.getName());
    }

    /**
     * [Topic] 토픽 발행 취소
     *
     * @param topic 토픽명
     * @return 전송 성공 여부
     */
    public boolean unadvertise(String topic) {
        RosUnadvertise op = RosUnadvertise.builder(topic).build();
        if (this.publishedTopics.contains(topic)) {
            if (send(op)) {
                this.publishedTopics.remove(topic);
                return true;
            }
        }
        return false;
    }

    /**
     * [Topic] 토픽 발행 취소
     *
     * @param topic 토픽명
     * @return 전송 성공 여부
     */
    public boolean unadvertise(RosTopic topic) {
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
    public boolean publish(String topic, String type, Object msg) {
        RosTopic op = RosTopic.builder(topic, type).msg(msg).build();
        return publish(op);
    }

    public boolean publish(RosTopic topic) {
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
     * @param op       토픽 구독
     * @param delegate 토픽 메세지 처리자
     */
    public boolean subscribe(RosSubscription op, RosSubscribeDelegate delegate) {
        String topic = op.getTopic();

        if (this.topicListeners.containsKey(topic)) {
            this.topicListeners.get(topic).addDelegate(delegate);
            return false;
        }

        this.topicListeners.put(topic, new RosSubscribers(delegate));
        return send(op);
    }

    public RosSubscription subscribe(String topic, String type, RosSubscribeDelegate delegate) {
        RosSubscription op = RosSubscription.builder(topic, type).build();
        subscribe(op, delegate);
        return op;
    }

    public RosSubscription subscribe(String topic, MessageType type, RosSubscribeDelegate delegate) {
        RosSubscription op = RosSubscription.builder(topic, type).build();
        subscribe(op, delegate);
        return op;
    }

    /**
     * [Topic] 토픽 구독 해제
     *
     * @param topic 토픽명
     */
    public boolean unsubscribe(String topic) {
        RosUnsubscription op = RosUnsubscription.builder(topic).build();
        boolean flag = send(op);
        if (flag) {
            this.topicListeners.remove(topic);
        }
        return flag;
    }

    public void removeListener(String topic, RosSubscribeDelegate delegate) {
        RosSubscribers listeners = this.topicListeners.get(topic);
        if (listeners != null) {
            listeners.removeDelegate(delegate);

            if (listeners.numDelegates() == 0) {
                unsubscribe(topic);
            }
        }
    }

    /**
     * [Service] Service 요청
     *
     * @param service  서비스명
     * @param args     요청변수
     * @param delegate 서비스 응답 처리 함수
     * @return 서비스 전송 성공여부
     */
    public boolean callService(String service, List<?> args, RosServiceDelegate delegate) {
        RosService op = RosService.builder(service).args(args).build();
        return callService(op, delegate);
    }

    /**
     * [Service] Service 요청
     *
     * @param op       요청할 서비스 정보 객체
     * @param delegate 서비스 응답 처리 함수
     * @return 서비스 전송 성공여부
     */
    public boolean callService(RosService op, RosServiceDelegate delegate) {
        serviceListeners.put(op.getId(), delegate);

        return send(op);
    }

    /**
     * [RosBridge] ROS Topic 목록 조회
     *
     * @param delegate 서비스 응답 처리 함수
     */
    public void getTopics(RosServiceDelegate delegate) {
        RosService service = RosService.builder("/rosapi/topics").build();
        callService(service, delegate);
    }

    /**
     * [RosBridge] ROS Service 목록 조회
     *
     * @param delegate 응답 처리 함수
     */
    public void getServices(RosServiceDelegate delegate) {
        RosService service = RosService.builder("/rosapi/services").build();
        callService(service, delegate);
    }

    /**
     * [RosBridge] ROS Node 목록 조회
     *
     * @param delegate 응답 처리 함수
     */
    public void getNodes(RosServiceDelegate delegate) {
        RosService service = RosService.builder("/rosapi/nodes").build();
        callService(service, delegate);
    }

    /**
     * [RosBridge] ROS Node 상세 정보 조회
     *
     * @param node     찾을 노드명
     * @param delegate 서비스 응답 처리 함수
     */
    public void getNodeDetails(String node, RosServiceDelegate delegate) {
        RosService service = RosService.builder("/rosapi/node_details").args(node).build();
        callService(service, delegate);
    }

    /**
     * Fragment 처리하기
     *
     * @param node - 조각
     */
    protected void processFragment(JsonObject node) {
        String id = node.getString("id");

        FragmentManager manager = this.fragmentManagers.get(id);
        if (manager == null) {
            manager = new FragmentManager(node);
            this.fragmentManagers.put(id, manager);
        }
        boolean complete = manager.updateFragment(node);

        if (complete) {
            String fullMsg = manager.generateFullMessage();
            this.fragmentManagers.remove(id);
            onMessage(fullMsg);
        }
    }

    /**
     * Fragments 관리자
     */
    public static class FragmentManager {
        protected String id;
        protected String[] fragments;
        protected Set<Integer> completedFragments;

        public FragmentManager(JsonObject fragmentJson) {
            int total = fragmentJson.getInteger("total");
            this.fragments = new String[total];
            this.completedFragments = new HashSet<>(total);
            this.id = fragmentJson.getString("id");
        }

        public boolean updateFragment(JsonObject fragmentJson) {
            String data = fragmentJson.getString("data");
            int num = fragmentJson.getInteger("num");
            this.fragments[num] = data;
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

        public String generateFullMessage() {
            if (!complete()) {
                throw new RuntimeException("Cannot generate full message from fragments, because not all fragments have arrived.");
            }

            StringBuilder buf = new StringBuilder(this.fragments[0].length() * this.fragments.length);
            for (String frag : this.fragments) {
                buf.append(frag);
            }

            return buf.toString();
        }
    }
}
