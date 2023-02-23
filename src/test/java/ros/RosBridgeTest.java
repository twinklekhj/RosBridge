package ros;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ros.op.RosService;
import ros.op.RosTopic;
import ros.type.RosMessage;
import ros.type.std.Int32;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

public class RosBridgeTest {
    private static final Logger logger = LoggerFactory.getLogger(RosBridgeTest.class);

    public static void main(String[] args) {
        Properties properties = readProperties("config/config.properties");

        logger.info("===== Welcome To RosBridge Application =====");
        String host = Objects.requireNonNull(properties).getProperty("ros.host");
        String port = Objects.requireNonNull(properties).getProperty("ros.port");

        String url = String.format("ws://%s:%s", host, port);

        RosBridge bridge = RosBridge.createConnection(url, true);
        bridge.enableMsgSend(true);

        if (bridge.hasConnected) {
            logger.info("===== RosBridge is connected =====");

            // 서비스 생성
            Object[] serverParams = {"hello"};
            RosService service = RosService.builder("serverTest", Arrays.asList(serverParams)).build();

            // 서비스 호출
            bridge.callService(service, response -> {
                logger.info("HI!!!!!");
            });

            // 메세지 생성
            RosMessage message = new Int32(8);

            // 토픽 생성
            RosTopic topic = RosTopic.builder("/test", message.getType(), message).build();

            // 토픽 구독
            bridge.subscribe(topic.getTopic(), topic.getType(), (paramJsonNode, paramString) -> {
                System.err.println("I'm Tester!!!");
            });

            // 토픽 발행
            bridge.publish(topic);

            // 토픽 발행 취소
            bridge.unadvertise(topic);
        }
    }

    /**
     * [Properties] 파일을 가져와 Properties 객체 생성
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

}
