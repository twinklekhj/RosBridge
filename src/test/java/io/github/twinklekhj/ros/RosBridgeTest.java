package io.github.twinklekhj.ros;

import io.github.twinklekhj.ros.op.RosTopic;
import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.sensor.Image;
import io.github.twinklekhj.ros.type.std.Int32;
import io.github.twinklekhj.ros.core.ConnProps;
import io.github.twinklekhj.ros.core.RosApi;
import io.github.twinklekhj.ros.core.RosBridge;
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

        props.setPrintStackTrace(true);
        props.setPrintSendMsg(true);
        props.setPrintReceivedMsg(true);

        bridge.start();

        /*
            If you're a publisher, create a topic
            - using the Type class/member variable of object
         */
        Int32 message = new Int32(8);
        RosTopic topic = RosTopic.builder("/test", message).build();
        // RosTopic topic = RosTopic.builder("/test", Int32.TYPE).msg(message).build();

        /*
          2. If you're a subscriber, create a topic
          - using the type in RosMessage.Type
          - using the Type static variable of object
          - using the interface RosCommand interface
         */
        RosTopic topic2 = RosTopic.builder("/test", RosMessage.Type.Primitive.Int32).build();
        RosTopic topic3 = RosTopic.builder("/test", Int32.TYPE).build();
        RosTopic topic4 = RosTopic.builder(Topics.TEST).build();

        bridge.subscribe(topic, response -> {
            logger.info("Subscribed topic: {}", response.body());
            context.completeNow();
        }).future().compose(subscription -> bridge.publish(topic).future()).compose(rosTopic -> bridge.unsubscribe(rosTopic).future()).onFailure(Assertions::fail);


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
            logger.info("all topics: {}", topics);
            return RosApi.getNodes(bridge).future();
        }).compose(nodes -> {
            logger.info("nodes: {}", nodes);
            return Future.succeededFuture();
        }).onSuccess(o -> {
            Assertions.assertTrue(true);
            context.completeNow();
        }).onFailure(Assertions::fail);

        RosApi.getTopicsForType(bridge, Image.TYPE).future().compose(topics -> {
            logger.info("image topics: {}", topics);
            return Future.succeededFuture();
        });

        context.awaitCompletion(10, TimeUnit.SECONDS);
    }
}

