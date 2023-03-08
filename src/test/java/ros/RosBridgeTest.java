package ros;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.twinklekhj.ros.RosBridge;
import io.github.twinklekhj.ros.op.RosService;
import io.github.twinklekhj.ros.op.RosTopic;
import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Int32;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RosBridgeTest {
    public static final String fileName = "config/config.properties";
    private static final Logger logger = LoggerFactory.getLogger(RosBridgeTest.class);
    public RosBridge bridge;

    /**
     * Test 객체 생성
     */
    RosBridgeTest() {
        Properties properties = readProperties(fileName);

        String host = Objects.requireNonNull(properties).getProperty("ros.host");
        String port = Objects.requireNonNull(properties).getProperty("ros.port");

        RosBridge.Connection connection = RosBridge.Connection.builder(host, port).wait(true).maxIdleTimeout(10000).printSendMsg(true).build();

        this.bridge = RosBridge.createConnection(connection);
    }

    public static void main(String[] args) {
        logger.info("===== Welcome To RosBridge Test =====");
    }

    /**
     * [Properties] 파일을 가져와 Properties 객체 생성
     *
     * @param fileName 파일명
     * @return Properties 객체 반환
     */
    public static Properties readProperties(String fileName) {
        Properties prop = new Properties();
        InputStream inputStream = RosBridgeTest.class.getClassLoader().getResourceAsStream(fileName);

        try {
            if (inputStream != null) {
                prop.load(inputStream);
                return prop;
            } else {
                throw new FileNotFoundException(String.format("Can not Found Properties File - %s", fileName));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
        bridge.getNodes(response -> {
            JsonNode values = response.getValues();
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
    public void tetraTest(){
        bridge.enablePrintStackTrace(true);

        bridge.getNodes(response -> {
            List<String> devices = new ArrayList<>();

            logger.info("response: {}", response);
            JsonNode values = response.getValues();
            JsonNode nodes = values.get("nodes");

            logger.info("nodes: {}", nodes);
            nodes.forEach(node -> {
                String[] names = node.asText().split("/");
                System.err.println(Arrays.toString(names));
                if(names.length > 2 && names[2].equals("tetraDS")){
                    devices.add(names[1]);
                }
            });

            logger.info("devices: {}", devices);
        });

        bridge.awaitClose(3000, TimeUnit.MILLISECONDS);
    }
}
