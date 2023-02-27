package io.github.twinklekhj.ros;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.twinklekhj.ros.listener.RosServiceDelegate;
import io.github.twinklekhj.ros.listener.RosSubscribeDelegate;
import io.github.twinklekhj.ros.listener.RosSubscribers;
import io.github.twinklekhj.ros.op.*;
import io.github.twinklekhj.ros.type.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
    protected String host;
    protected String port;
    protected Session session;
    protected Set<String> publishedTopics = new HashSet<>();
    protected Map<String, FragmentManager> fragmentManagers = new HashMap<>();
    protected Map<String, RosSubscribers> topicListeners = new HashMap<>();
    protected Map<String, RosServiceDelegate> serviceListeners = new HashMap<>();

    protected boolean hasConnected = false;
    protected boolean hasConnectError = false;

    protected boolean printSendMsg = false;
    protected boolean printReceivedMsg = false;

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
    public static RosBridge createConnection(String host, String port, boolean waitForConnection) {
        RosBridge bridge = new RosBridge();
        bridge.connect(Connection.builder(host, port, waitForConnection).build());

        return bridge;
    }

    /**
     * Connection 객체로 RosBridge 객체 생성후 WebSocket 연결
     *
     * @param connection 연결 옵션 객체
     * @return RosBridge 객체
     */
    public static RosBridge createConnection(Connection connection) {
        RosBridge bridge = new RosBridge();
        bridge.connect(connection);

        return bridge;
    }

    /**
     * 메시지 송신시 로그 사용여부
     *
     * @param flag 로깅 활성 여부
     */
    public void enablePrintMsgSend(boolean flag) {
        this.printSendMsg = flag;
    }

    /**
     * 메시지 수신시 로그 사용여부
     *
     * @param flag 로깅 활성여부
     */
    public void enablePrintMsgReceived(boolean flag) {
        this.printReceivedMsg = flag;
    }

    /**
     * WebSocket 연결
     *
     * @param connection - 웹소켓 연결 정보
     */
    private void connect(Connection connection) {
        this.host = connection.host;
        this.port = connection.port;

        WebSocketClient client = new WebSocketClient();

        try {
            if (connection.maxIdleTimeout != 0) {
                client.setMaxIdleTimeout(connection.maxIdleTimeout);
            }
            if (connection.connectTimeout != 0) {
                client.setConnectTimeout(connection.connectTimeout);
            }
            if (connection.stopTimeout != 0) {
                client.setStopTimeout(connection.stopTimeout);
            }
            client.start();

            String url = String.format("ws://%s:%s", connection.host, connection.port);
            URI echoUri = new URI(url);

            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(this, echoUri, request);
            logger.info("Connecting to: {}", echoUri);
            if (connection.wait) {
                waitForConnection();
            }
        } catch (Exception e) {
            logger.error("Error! Message: {}", e.getMessage());

            e.printStackTrace();
        }
    }

    /**
     * 연결될 때까지 기다린다
     */
    public void waitForConnection() {
        if (this.hasConnected || this.hasConnectError) {
            return;
        }

        synchronized (this) {
            while (!this.hasConnected && !this.hasConnectError) {
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

    public boolean awaitClose(int duration, TimeUnit unit) {
        try {
            return this.closeLatch.await(duration, unit);
        } catch (InterruptedException e) {
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
        logger.warn("WebSocket Error! msg: {}", e.getMessage());

        synchronized (this) {
            this.hasConnectError = true;
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
     * @param waitForConnection 연결 기다림 여부
     */
    public void reconnect(boolean waitForConnection) {
        Connection conn = Connection.builder(this.host, this.port, waitForConnection).build();
        this.connect(conn);
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
        JsonNode node;
        try {
            node = mapper.readTree(msg);
            if (node.has("op")) {
                // publish
                String op = node.get("op").asText();
                switch (op) {
                    case "publish":
                        String topic = node.get("topic").asText();
                        RosSubscribers subscriber = this.topicListeners.get(topic);
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
     *
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

    /**
     * [Topic] 토픽 구독
     *
     * @param op       토픽 구독
     * @param delegate 토픽 메세지 처리자
     */
    public void subscribe(RosSubscription op, RosSubscribeDelegate delegate) {
        String topic = op.getTopic();

        if (this.topicListeners.containsKey(topic)) {
            this.topicListeners.get(topic).addDelegate(delegate);

            return;
        }

        this.topicListeners.put(topic, new RosSubscribers(delegate));
        send(op);
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
    public void unsubscribe(String topic) {
        RosUnsubscribe op = RosUnsubscribe.builder(topic).build();
        if (send(op)) {
            this.topicListeners.remove(topic);
        }
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
     * @param service  - 서비스명
     * @param args     - 요청변수
     * @param delegate - 서비스 응답 처리 함수
     * @return 서비스 전송 성공여부
     */
    public boolean callService(String service, List<Object> args, RosServiceDelegate delegate) {
        RosService op = RosService.builder(service).args(args).build();
        return callService(op, delegate);
    }

    /**
     * [Service] Service 요청
     *
     * @param op       - 요청할 서비스 정보 객체
     * @param delegate - 서비스 응답 처리 함수
     * @return 서비스 전송 성공여부
     */
    public boolean callService(RosService op, RosServiceDelegate delegate) {
        serviceListeners.put(op.getId(), delegate);

        return send(op);
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

    @Builder
    @AllArgsConstructor
    public static class Connection {
        private String host = "127.0.0.1";
        private String port = "9090";
        private boolean wait = false;
        private boolean printSendMsg = false;
        private boolean printReceivedMsg = false;
        private long maxIdleTimeout = 0;
        private long connectTimeout = 0;
        private long stopTimeout = 0;

        public Connection() {
        }

        public static ConnectionBuilder builder(String host, String port) {
            return new ConnectionBuilder().host(host).port(port);
        }

        public static ConnectionBuilder builder(String host, String port, boolean wait) {
            return builder(host, port).wait(wait);
        }

        public Connection build() {
            return new Connection(this.host, this.port, this.wait, this.printSendMsg, this.printReceivedMsg, this.maxIdleTimeout, this.connectTimeout, this.stopTimeout);
        }

        public void setWait(boolean wait) {
            this.wait = wait;
        }

        public void setMaxIdleTimeout(long maxIdleTimeout) {
            this.maxIdleTimeout = maxIdleTimeout;
        }

        public void setConnectTimeout(long connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public void setPrintSendMsg(boolean printSendMsg) {
            this.printSendMsg = printSendMsg;
        }

        public void setPrintReceivedMsg(boolean printReceivedMsg) {
            this.printReceivedMsg = printReceivedMsg;
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
