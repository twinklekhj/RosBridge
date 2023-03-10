package io.github.twinklekhj.ros;

import io.github.twinklekhj.ros.op.RosResponse;
import io.github.twinklekhj.ros.ws.ConnProps;
import io.github.twinklekhj.ros.ws.RosVerticle;
import io.github.twinklekhj.utils.PropertyUtil;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@ExtendWith(VertxExtension.class)
public class RosVerticleTest {
    private static final Logger logger = LoggerFactory.getLogger(RosVerticleTest.class);

    private final Vertx vertx;
    private final ConnProps props;
    private final RosVerticle socket;

    public RosVerticleTest() {
        this.vertx = Vertx.vertx();

        String host = PropertyUtil.getProperty("ros.host");
        int port = PropertyUtil.getPropertyInt("ros.port");

        this.props = ConnProps.builder(host, port).build();
        this.socket = new RosVerticle(vertx, props);
    }

    public static void main(String[] args) {
    }

    @Test
    @DisplayName("Topics 테스트")
    public void testService() throws InterruptedException {
        VertxTestContext context = new VertxTestContext();
        props.setPrintStackTrace(true);
        props.setWait(true);
        props.setPrintSendMsg(true);
        props.setPrintReceivedMsg(true);

        socket.start();
        socket.getTopics(message -> {
            RosResponse res = RosResponse.fromJsonObject(message.body());
            JsonArray topics = (JsonArray) res.getValues().get("topics");
            logger.info("topics: {}", topics);
        });

        context.awaitCompletion(10, TimeUnit.SECONDS);
    }
}
