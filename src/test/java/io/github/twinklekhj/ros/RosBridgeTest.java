package io.github.twinklekhj.ros;

import io.github.twinklekhj.ros.op.RosService;
import io.github.twinklekhj.ros.op.RosTopic;
import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Int32;
import io.github.twinklekhj.ros.ws.ConnProps;
import io.github.twinklekhj.ros.ws.RosBridge;
import io.github.twinklekhj.utils.PropertyUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RosBridgeTest {
    private static final Logger logger = LoggerFactory.getLogger(RosBridgeTest.class);
    public final RosBridge bridge;
    private final ConnProps props;

    /**
     * Test 객체 생성
     */
    RosBridgeTest() {
        String host = PropertyUtil.getProperty("ros.host");
        int port = PropertyUtil.getPropertyInt("ros.port");

        this.props = ConnProps.builder(host, port).wait(true).idleTimeout(10000).printSendMsg(true).build();
        this.bridge = RosBridge.createBridge(props);
    }

    public static void main(String[] args) {
        logger.info("===== Welcome To RosBridge Test =====");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        if (this.bridge.hasConnected()) {
            this.bridge.close();
        }
    }

    @Test
    @DisplayName("Ros Bridge 테스트")
    public void bridgeTest() {
        if (bridge.hasConnected()) {
            logger.info("===== RosBridge is connected =====");
        }

        bridge.awaitClose(3000, TimeUnit.MILLISECONDS);
        Assertions.assertTrue(bridge.hasConnected(), "ROS Bridge 연결 실패");
    }

    @Test
    @DisplayName("Ros Topic 테스트")
    public void testTopic() {
        if (bridge.hasConnected()) {
            logger.info("===== RosBridge is connected =====");

            RosMessage message = new Int32(8);
            RosTopic topic = RosTopic.builder("/test", message.getType(), message).build();

            // 토픽 구독
            bridge.subscribe(topic.getName(), topic.getType(), (paramJsonNode, paramString) -> {
                logger.info("Subscribed Topic: [{}]", paramJsonNode);

                Assertions.assertTrue(true);
                bridge.countDownLatch();
            });

            bridge.publish(topic); // 토픽 발행
            bridge.unadvertise(topic); // 토픽 발행 취소
        }

        bridge.awaitClose(10000, TimeUnit.MILLISECONDS);
        Assertions.assertEquals(0, bridge.getLatchCount(), "ROS Topic 테스트 실패");
    }

    @Test
    @DisplayName("Ros Service 테스트")
    public void testService() {
        if (bridge.hasConnected()) {
            logger.info("===== RosBridge is connected =====");

            // 호출할 서비스 생성
            Object[] serverParams = {"hello"};
            RosService service = RosService.builder("serverTest", Arrays.asList(serverParams)).build();

            // 서비스 호출
            bridge.callService(service, response -> {
                logger.info("Response: {}", response.getValues());

                Assertions.assertTrue(true);
                bridge.countDownLatch();
            });
        }

        bridge.awaitClose(3000, TimeUnit.MILLISECONDS);
        Assertions.assertEquals(0, bridge.getLatchCount(), "ROS Service 테스트 실패");
    }

    @Test
    @DisplayName("ROS Topic 목록 조회")
    public void getTopics() {
        bridge.getTopics(response -> {
            logger.info("values: {}", response.getValues());
        });

        bridge.awaitClose(3000, TimeUnit.MILLISECONDS);
    }

    @Test
    @DisplayName("ROS Service 목록 조회")
    public void getServices() {
        bridge.getServices(response -> {
            logger.info("values: {}", response.getValues());
        });

        bridge.awaitClose(3000, TimeUnit.MILLISECONDS);
    }

    @Test
    @DisplayName("ROS Node 목록 조회")
    public void getNodes() {
        props.setPrintStackTrace(true);

        bridge.getNodes(response -> {
            Map<String, Object> values = response.getValues();
            logger.info("values: {}", values);
        });

        bridge.awaitClose(3000, TimeUnit.MILLISECONDS);
    }

    @Test
    @DisplayName("ROS Node 상세 정보 조회")
    public void getNodeDetails() {
        String node = "/rosapi";
        bridge.getNodeDetails(node, response -> {
            logger.info("response: {}", response);
        });

        bridge.awaitClose(3000, TimeUnit.MILLISECONDS);
    }

    @Test
    @DisplayName("TETRA Test")
    public void tetraTest() {
        bridge.enablePrintStackTrace(true);

        bridge.getNodes(response -> {
            List<String> devices = new ArrayList<>();

            logger.info("response: {}", response);
            Map<String, Object> nodes = response.getValues();

            logger.info("nodes: {}", nodes);
            nodes.forEach((node, value) -> {
                String[] names = node.toString().split("/");
                System.err.println(Arrays.toString(names));
                if (names.length > 2 && names[2].equals("tetraDS")) {
                    devices.add(names[1]);
                }
            });

            logger.info("devices: {}", devices);
        });

        bridge.awaitClose(3000, TimeUnit.MILLISECONDS);
    }
}
