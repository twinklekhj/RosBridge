package io.github.twinklekhj.ros;

import io.github.twinklekhj.ros.op.RosSubscription;
import io.github.twinklekhj.ros.op.RosTopic;
import io.github.twinklekhj.ros.type.artags.AlvarMarkers;
import io.github.twinklekhj.ros.type.navigation.OccupancyGrid;
import io.github.twinklekhj.ros.ws.ConnProps;
import io.github.twinklekhj.ros.ws.RosApi;
import io.github.twinklekhj.ros.ws.RosBridge;
import io.github.twinklekhj.utils.PropertyUtil;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@ExtendWith(VertxExtension.class)
public class TetraTest {
    private static final Logger logger = LoggerFactory.getLogger(TetraTest.class);

    private final Vertx vertx;
    private final ConnProps props;
    private final RosBridge bridge;

    public TetraTest() {
        this.vertx = Vertx.vertx();

        String host = PropertyUtil.getProperty("ros.host");
        int port = PropertyUtil.getPropertyInt("ros.port");

        this.props = ConnProps.builder(host, port).build();
        this.bridge = new RosBridge(vertx, props);
    }

    @Test
    @DisplayName("Node Details 테스트")
    public void testNodeDetails() throws InterruptedException {
        VertxTestContext context = new VertxTestContext();

        props.setPrintStackTrace(true);
        props.setPrintSendMsg(true);
        props.setPrintReceivedMsg(true);

        bridge.start();
        RosApi.getNodeDetails(bridge, "/TE2216001/tetraDS").future().onSuccess(response -> {
            Map<String, Object> values = response.getValues();
            logger.info("node details - {}", values);
            logger.info("services - {}", values.get("services"));

        }).onFailure(Assertions::fail);

        context.awaitCompletion(10, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("Map Topic 테스트")
    public void subscribeMapTopic() throws InterruptedException {
        VertxTestContext context = new VertxTestContext();

        props.setPrintStackTrace(true);
        props.setPrintSendMsg(true);
        props.setMaxFrameSize(10000000);

        bridge.start();

        RosTopic topic = RosTopic.builder("/map", OccupancyGrid.TYPE).build();
        RosSubscription subscription = RosSubscription.builder(topic).throttleRate(5000).build();
        if (bridge.isSubscribed(topic)) {
            return;
        }

        logger.info("subscription: {}", subscription);

        bridge.subscribe(subscription, message -> {
            JsonObject topics = message.body();
            OccupancyGrid grid = OccupancyGrid.fromJsonObject(topics.getJsonObject("msg"));
            int[] data = grid.getData();

            logger.info("info: {}", grid.getInfo());
        }).future().onFailure(Assertions::fail);

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

    @Test
    @DisplayName("ar markers")
    public void testArMarkers() throws InterruptedException {
        VertxTestContext context = new VertxTestContext();
        bridge.start();

        RosSubscription subscription = RosSubscription.builder("/TE2216001/ar_pose_marker", AlvarMarkers.TYPE).throttleRate(200).build();
        bridge.subscribe(subscription, message -> {
            logger.info("message: {}", message);
        });

        context.awaitCompletion(10, TimeUnit.SECONDS);
    }
}

