package ros;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ros.op.*;
import ros.topic.RosListenDelegate;
import ros.topic.RosListeners;
import ros.topic.RosServiceDelegate;
import ros.type.MessageType;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author khj
 * @apiNote RosBridge 총괄
 * @since 2023.02.15
 */
@WebSocket
public class RosBridge {
    private static final Logger logger = LoggerFactory.getLogger(RosBridge.class);
    protected final String url;
    protected final CountDownLatch closeLatch;
    protected Session session;
    protected Set<String> publishedTopics = new HashSet<>();
    protected Map<String, FragmentManager> fragmentManagers = new HashMap<>();
    protected Map<String, RosListeners> topicListeners = new HashMap<>();
    protected Map<String, RosServiceDelegate> serviceListeners = new HashMap<>();

    protected boolean hasConnected = false;
    protected boolean printSendMsg = false;
    protected boolean printReceivedMsg = false;
    protected long maxIdleTimeout = 300000;

    private RosBridge(String url) {
        this.closeLatch = new CountDownLatch(1);
        this.url = url;
    }

    /**
     * RosBridge 객체 생성후 WebSocket 연결
     *
     * @param rosBridgeURI      ROS 브릿지 주소
     * @param waitForConnection 연결 기다림 여부
     */
    public static RosBridge createConnection(String rosBridgeURI, boolean waitForConnection) {
        RosBridge bridge = new RosBridge(rosBridgeURI);
        bridge.connect(rosBridgeURI, waitForConnection);

        return bridge;
    }

    /**
     * 메시지 송신시 로그 사용여부
     *
     * @param flag 로깅 활성 여부
     */
    public void enableMsgSend(boolean flag) {
        this.printSendMsg = flag;
    }

    /**
     * 메시지 수신시 로그 사용여부
     *
     * @param flag 로깅 활성여부
     */
    public void enableMsgReceived(boolean flag) {
        this.printReceivedMsg = flag;
    }

    /**
     * 최대 유휴시간 지정
     *
     * @param maxIdleTimeout 유휴 시간
     */
    public void setMaxIdleTimeout(long maxIdleTimeout) {
        this.maxIdleTimeout = maxIdleTimeout;
    }

    /**
     * WebSocket 연결
     *
     * @param rosBridgeURI      ROS 브릿지 주소
     * @param waitForConnection 연결 기다림 여부
     */
    private void connect(String rosBridgeURI, boolean waitForConnection) {
        WebSocketClient client = new WebSocketClient();

        try {
            client.start();
            client.setMaxIdleTimeout(this.maxIdleTimeout);
            URI echoUri = new URI(rosBridgeURI);

            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(this, echoUri, request);
            logger.info("Connecting to: {}", echoUri);
            if (waitForConnection) {
                waitForConnection();
            }
        } catch (Exception e) {
            logger.error("Error! Message: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @apiNote 연결될 때까지 기다림
     */
    public void waitForConnection() {
        if (this.hasConnected) {
            return;
        }

        synchronized (this) {
            while (!this.hasConnected) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
        return this.hasConnected;
    }

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
    }

    /**
     * WebSocket 연결 Event
     *
     * @param session - 연결 세션
     */
    @OnWebSocketConnect
    public void onConnect(Session session) {
        logger.info("Got connect for ros: {}", session);

        this.session = session;
        this.hasConnected = true;
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
        logger.error("Error! msg: {}", e.getMessage());
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
     * @param waitForConnection 연결 기다림 여부
     */
    public void reconnect(boolean waitForConnection) {
        this.connect(this.url, waitForConnection);
    }

    /**
     * WebSocket 메세지 수신 Event
     *
     * @param msg 수신받은 메세지
     */
    @OnWebSocketMessage
    public void onMessage(String msg) {
        if (this.printReceivedMsg) {
            logger.info("[RESPONSE] msg: {}", msg);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = mapper.readTree(msg);
            if (node.has("op")) {
                // publish
                String op = node.get("op").asText();
                switch (op) {
                    case "publish":
                        String topic = node.get("topic").asText();
                        RosListeners subscriber = this.topicListeners.get(topic);
                        if (subscriber != null) {
                            subscriber.receive(node, msg);
                        }
                        break;
                    case "service_response":
                        String id = node.get("id").asText();
                        List<?> values = node.findValues("values");
                        boolean result = node.get("result").asBoolean();
                        String service = node.get("service").asText();

                        RosResponse res = RosResponse.builder(service, result).id(id).values(values).build();
                        RosServiceDelegate delegate = serviceListeners.remove(res.getId());
                        if (delegate != null) {
                            delegate.receive(res);
                        }
                        break;
                    case "fragment":
                        processFragment(node);
                        break;
                }
            }
        } catch (IOException e) {
            logger.error("Could not parse ROSBridge web socket message into JSON data");
            e.printStackTrace();
        }
    }

    private boolean send(RosOperation support) {
        String sendMsg = support.toString();
        return send(sendMsg);
    }

    /**
     * [Ros] Ros 메세지 전송
     * @param message - 보낼 메세지
     * @return 메세지 전송 성공 여부
     */
    private boolean send(String message) {
        if (printSendMsg) {
            logger.info("[REQUEST] msg: {}", message);
        }
        try {
            Future<Void> fut = this.session.getRemote().sendStringByFuture(message);
            fut.get(2L, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            logger.error("Error! Message: {}", message);
        }
        return false;
    }

    /**
     * [Topic] 토픽 발행 상태 알림
     *
     * @param topic 토픽명
     * @param type  메세지 유형
     */
    public RosAdvertise advertise(String topic, String type) {
        RosAdvertise op = RosAdvertise.builder(topic, type).build();

        if (!this.publishedTopics.contains(topic)) {
            if (send(op)) {
                this.publishedTopics.add(topic);
            }
        }
        return op;
    }

    /**
     * [Topic] 토픽 발행 상태 알림
     *
     * @param topic 토픽명
     * @param type  메세지 유형
     */
    public void advertise(String topic, MessageType type) {
        advertise(topic, type.getName());
    }

    /**
     * [Topic] 토픽 발행 취소
     *
     * @param topic 토픽명
     */
    public RosUnadvertise unadvertise(String topic){
        RosUnadvertise op = RosUnadvertise.builder(topic).build();
        if (this.publishedTopics.contains(topic)) {
            if (send(op)) {
                this.publishedTopics.remove(topic);
            }
        }
        return op;
    }
    public RosUnadvertise unadvertise(RosTopic topicOp){
        String topic = topicOp.getTopic();
        return unadvertise(topic);
    }
    /**
     * [Topic] 토픽 발행
     *
     * @param topic 토픽명
     * @param type  메시지 유형
     * @param msg   보낼 메세지
     */
    public RosTopic publish(String topic, String type, Object msg) {
        RosTopic op = RosTopic.builder(topic, type).msg(msg).build();
        publish(op);
        return op;
    }

    public void publish(RosTopic op) {
        advertise(op.getTopic(), op.getType());
        send(op);
    }

    /**
     * [Topic] 토픽 구독
     *
     * @param op       - 토픽 구독
     * @param delegate - 토픽 메세지 처리자
     */
    public void subscribe(RosSubscription op, RosListenDelegate delegate) {
        String topic = op.getTopic();

        if (this.topicListeners.containsKey(topic)) {
            this.topicListeners.get(topic).addDelegate(delegate);

            return;
        }

        this.topicListeners.put(topic, new RosListeners(delegate));
        send(op);
    }

    public RosSubscription subscribe(String topic, String type, RosListenDelegate delegate){
        RosSubscription op = RosSubscription.builder(topic, type).build();
        subscribe(op, delegate);
        return op;
    }

    public RosSubscription subscribe(String topic, MessageType type, RosListenDelegate delegate){
        RosSubscription op = RosSubscription.builder(topic, type).build();
        subscribe(op, delegate);
        return op;
    }

    /**
     * [Topic] 토픽 구독 해제
     *
     * @param topic 토픽명
     */
    public void unsubscribe(String topic) {
        RosUnsubscribe op = RosUnsubscribe.builder(topic).build();
        if (send(op)) {
            this.topicListeners.remove(topic);
        }
    }

    public void removeListener(String topic, RosListenDelegate delegate) {
        RosListeners listeners = this.topicListeners.get(topic);
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
     * @param service - 서비스명
     * @param args - 요청변수
     * @param delegate - 서비스 응답 처리 함수
     */
    public RosService callService(String service, List<Object> args, RosServiceDelegate delegate) {
        RosService op = RosService.builder(service).args(args).build();
        callService(op, delegate);
        return op;
    }

    /**
     * [Service] Service 요청
     *
     * @param op - 요청할 서비스 정보 객체
     * @param delegate - 서비스 응답 처리 함수
     */
    public void callService(RosService op, RosServiceDelegate delegate) {
        serviceListeners.put(op.getId(), delegate);

        send(op);
    }

    /**
     * Fragment 처리하기
     *
     * @param node - 조각
     */
    protected void processFragment(JsonNode node) {
        String id = node.get("id").textValue();

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

        public FragmentManager(JsonNode fragmentJson) {
            int total = fragmentJson.get("total").intValue();
            this.fragments = new String[total];
            this.completedFragments = new HashSet<>(total);
            this.id = fragmentJson.get("id").textValue();
        }

        public boolean updateFragment(JsonNode fragmentJson) {
            String data = fragmentJson.get("data").asText();
            int num = fragmentJson.get("num").intValue();
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
