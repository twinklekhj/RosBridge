package ros;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class RosBridgeTest {
    private static final String host = "192.168.0.59";
    private static final String port = "9090";
    private static final Logger logger = LoggerFactory.getLogger(RosBridgeTest.class);

    public static void main(String[] args) {
        logger.info("===== Welcome To RosBridge Application =====");
        String url = String.format("ws://%s:%s", host, port);

        RosBridge ros = RosBridge.createConnection(url, true);
        ros.enableMsgSend(true);
        ros.enableMsgReceived(true);
        
        if (ros.hasConnected) {
            logger.info("===== RosBridge is connected =====");

            String[] serverTestParams = {"hello"};
            ros.callService("serverTest", Arrays.asList(serverTestParams));
        }
    }
}
