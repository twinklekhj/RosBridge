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
import ros.topic.RosSubscriber;

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
    protected Map<String, FragmentManager> fragementManagers = new HashMap<>();
    protected Map<String, RosSubscriber> listeners;
    protected boolean hasConnected = false;
    protected boolean printMsgSend = false;
    protected boolean printMsgReceived = false;

    private RosBridge(String url) {
        this.closeLatch = new CountDownLatch(1);
        this.url = url;
    }

    /**
     * RosBridge 객체 생성후 WebSocket 연결
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
        this.printMsgSend = flag;
    }

    /**
     * 메시지 수신시 로그 사용여부
     *
     * @param flag 로깅 활성여부
     */
    public void enableMsgReceived(boolean flag) {
        this.printMsgReceived = flag;
    }

    /**
     * WebSocket 연결
     * @param rosBridgeURI      ROS 브릿지 주소
     * @param waitForConnection 연결 기다림 여부
     */
    private void connect(String rosBridgeURI, boolean waitForConnection) {
        WebSocketClient client = new WebSocketClient();

        try {
            client.start();
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
    public void onError(Session session, Throwable e){
        logger.error("Error! session: [address: {}, opened ? {}], msg: {}", session.getRemoteAddress(), session.isOpen(), e.getMessage());
    }
    /**
     * WebSocket 연결 해제
     */
    public void close(){
        this.session.close();
    }

    /**
     * WebSocket 재연결
     * @param waitForConnection 연결 기다림 여부
     */
    public void reconnect(boolean waitForConnection){
        this.connect(this.url, waitForConnection);
    }

    /**
     * WebSocket 메세지 수신 Event
     * @param msg 수신받은 메세지
     */
    @OnWebSocketMessage
    public void onMessage(String msg) {
        if (this.printMsgReceived) {
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
                        RosSubscriber subscriber = this.listeners.get(topic);
                        if (subscriber != null) {
                            subscriber.receive(node, msg);
                        }
                        break;
                    case "service_response":
                        String id = node.get("id").asText();
                        List<?> values = node.findValues("values");
                        boolean result = node.get("result").asBoolean();
                        String service = node.get("service").asText();

                        RosOpServiceRes res = RosOpServiceRes.Builder.builder(service, result).id(id).values(values).build();

                        logger.info("service response: {}", res);
                        break;
                    // fragment
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

    private boolean send(RosOpRoot support) {
        String sendMsg = support.toString();
        if (printMsgSend){
            logger.info("[REQUEST] msg: {}", sendMsg);
        }
        try {
            Future<Void> fut = this.session.getRemote().sendStringByFuture(sendMsg);
            fut.get(2L, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            logger.error("op: {}", support.getOperation());
        }
        return false;
    }

    private boolean send(String message) {
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
    public void advertise(String topic, String type) {
        if (!this.publishedTopics.contains(topic)) {

            RosOpAdvertise op = new RosOpAdvertise(topic, type);
            if (send(op)) {
                this.publishedTopics.add(topic);
            }
        }
    }

    /**
     * [Topic] 토픽 발행
     *
     * @param topic 토픽명
     * @param type  메시지 유형
     * @param msg   보낼 메세지
     */
    public void publish(String topic, String type, Object msg) {
        RosOpPublish op = new RosOpPublish(topic, type, msg);
        send(op);
    }

    /**
     * [Topic] 토픽 구독
     *
     * @param op       - 토픽 구독
     * @param delegate - 토픽 메세지 처리자
     */
    public void subscribe(RosOpSubscribe op, RosListenDelegate delegate) {
        String topic = op.getTopic();
        if (this.listeners.containsKey(topic)) {
            this.listeners.get(topic).addDelegate(delegate);

            return;
        }

        this.listeners.put(topic, new RosSubscriber(delegate));

        send(op);
    }

    /**
     * [Topic] 토픽 구독 해제
     *
     * @param topic 토픽명
     */
    public void unsubscribe(String topic) {
        RosOpUnsubscribe op = new RosOpUnsubscribe(topic);
        if (send(op)) {
            this.publishedTopics.add(topic);
        }

        this.listeners.remove(topic);
    }

    public void removeListener(String topic, RosListenDelegate delegate) {
        RosSubscriber subscriber = this.listeners.get(delegate);
        if (subscriber != null) {
            subscriber.removeDelegate(delegate);

            if (subscriber.numDelegates() == 0) {
                unsubscribe(topic);
            }
        }
    }

    public void callService(String service, List<Object> args) {
        RosOpServiceReq op = RosOpServiceReq.Builder.builder(service).args(args).build();
        send(op);
    }

    /**
     * Fragment 처리하기
     *
     * @param node - 조각
     */
    protected void processFragment(JsonNode node) {
        String id = node.get("id").textValue();

        FragmentManager manager = this.fragementManagers.get(id);
        if (manager == null) {
            manager = new FragmentManager(node);
            this.fragementManagers.put(id, manager);
        }
        boolean complete = manager.updateFragment(node);

        if (complete) {
            String fullMsg = manager.generateFullMessage();
            this.fragementManagers.remove(id);
            onMessage(fullMsg);
        }
    }

    /**
     * Fragments 관리자
     */
    public static class FragmentManager {
        protected String id;
        protected String[] fragments;
        protected Set<Integer> completedFragements;

        public FragmentManager(JsonNode fragmentJson) {
            int total = fragmentJson.get("total").intValue();
            this.fragments = new String[total];
            this.completedFragements = new HashSet<>(total);
            this.id = fragmentJson.get("id").textValue();
        }

        public boolean updateFragment(JsonNode fragmentJson) {
            String data = fragmentJson.get("data").asText();
            int num = fragmentJson.get("num").intValue();
            this.fragments[num] = data;
            this.completedFragements.add(num);
            return complete();
        }

        public boolean complete() {
            return (this.completedFragements.size() == this.fragments.length);
        }

        public int numFragments() {
            return this.fragments.length;
        }

        public int numCompletedFragments() {
            return this.completedFragements.size();
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
