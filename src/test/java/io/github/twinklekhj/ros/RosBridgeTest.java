package io.github.twinklekhj.ros;

import io.github.twinklekhj.ros.op.RosTopic;
import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Int32;
import io.github.twinklekhj.ros.ws.ConnProps;
import io.github.twinklekhj.ros.ws.RosApi;
import io.github.twinklekhj.ros.ws.RosBridge;
import io.github.twinklekhj.utils.PropertyUtil;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@ExtendWith(VertxExtension.class)
public class RosBridgeTest {
    private static final Logger logger = LoggerFactory.getLogger(RosBridgeTest.class);

    private final Vertx vertx;
    private final ConnProps props;
    private final RosBridge bridge;

    public RosBridgeTest() {
        this.vertx = Vertx.vertx();

        String host = PropertyUtil.getProperty("ros.host");
        int port = PropertyUtil.getPropertyInt("ros.port");

        this.props = ConnProps.builder(host, port).build();
        this.bridge = new RosBridge(vertx, props);
    }

    @Test
    @DisplayName("Ros Topic 테스트")
    public void testTopic() throws InterruptedException {
        VertxTestContext context = new VertxTestContext();
        bridge.start();

        RosMessage message = new Int32(8);
        RosTopic topic = RosTopic.builder("/test", RosMessage.Type.Primitive.Int32, message).build();
        bridge.subscribe(topic, response -> {
            logger.info("Subscribed topic: {}", response.body());
            context.completeNow();
        }).future().compose(subscription -> bridge.publish(topic).future()).onFailure(Assertions::fail);

        context.awaitCompletion(10, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("Service 테스트")
    public void testService() throws InterruptedException {
        VertxTestContext context = new VertxTestContext();

        props.setPrintStackTrace(true);
        props.setPrintSendMsg(true);
        props.setPrintReceivedMsg(true);

        bridge.start();

        RosApi.getTopics(bridge).future().compose(topics -> {
            logger.info("topics: {}", topics);
            return RosApi.getNodes(bridge).future();
        }).compose(nodes -> {
            logger.info("nodes: {}", nodes);
            return Future.succeededFuture();
        }).onSuccess(o -> {
            Assertions.assertTrue(true);
            context.completeNow();
        }).onFailure(Assertions::fail);

        context.awaitCompletion(10, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("Service 기능 테스트")
    public void testServiceFunction() throws InterruptedException {
        VertxTestContext context = new VertxTestContext();
        bridge.start();

        String serviceName = "/TE2216001/setspeed_cmd";
        RosApi.getServiceType(bridge, serviceName).future().compose(value -> {
            logger.info("type: {}", value);
            return RosApi.getServiceHost(bridge, serviceName).future();
        }).compose(value -> {
            logger.info("host: {}", value);
            return RosApi.getServiceNode(bridge, serviceName).future();
        }).compose(value -> {
            logger.info("node: {}", value);
            return Future.succeededFuture();
        }).onSuccess(o -> {
            Assertions.assertTrue(true);
            context.completeNow();
        }).onFailure(Assertions::fail);

        context.awaitCompletion(10, TimeUnit.SECONDS);
    }
}

