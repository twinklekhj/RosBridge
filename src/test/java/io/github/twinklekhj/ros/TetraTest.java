package io.github.twinklekhj.ros;

import io.github.twinklekhj.ros.core.ConnProps;
import io.github.twinklekhj.ros.core.RosApi;
import io.github.twinklekhj.ros.core.RosBridge;
import io.github.twinklekhj.ros.op.RosSubscription;
import io.github.twinklekhj.ros.op.RosTopic;
import io.github.twinklekhj.ros.tf.TFClient;
import io.github.twinklekhj.ros.type.artags.AlvarMarkers;
import io.github.twinklekhj.ros.type.costmap.ObstacleArrayMsg;
import io.github.twinklekhj.ros.type.costmap.ObstacleMsg;
import io.github.twinklekhj.ros.type.geometry.Point32;
import io.github.twinklekhj.ros.type.geometry.Polygon;
import io.github.twinklekhj.ros.type.movebase.MoveBaseActionResult;
import io.github.twinklekhj.ros.type.navigation.OccupancyGrid;
import io.github.twinklekhj.ros.type.navigation.Odometry;
import io.github.twinklekhj.ros.type.navigation.Path;
import io.github.twinklekhj.ros.type.std.Float64;
import io.github.twinklekhj.ros.type.std.Int32;
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

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ExtendWith(VertxExtension.class)
public class TetraTest {
    private static final String serial = "TE2216001";
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
        RosApi.getNodeDetails(bridge, String.format("/%s/tetraDS", serial)).future().onSuccess(response -> {
            Map<String, Object> values = response.getValues();
            logger.info("node details - {}", values);

            List<?> subscribe = (List) values.get("subscribing");
            if (subscribe.size() > 0) {
                logger.info("It's Real");
                context.completeNow();
            }
        }).onFailure(Assertions::fail);

        context.awaitCompletion(1, TimeUnit.SECONDS);
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
            OccupancyGrid grid = OccupancyGrid.fromJsonObject(message.body());

            logger.info("map info: {}", grid.getInfo());

            context.completeNow();
        }).future().onFailure(Assertions::fail);

        context.awaitCompletion(1, TimeUnit.SECONDS);
    }


    @Test
    @DisplayName("Service 기능 테스트")
    public void testServiceFunction() throws InterruptedException {
        VertxTestContext context = new VertxTestContext();
        bridge.start();

        String serviceName = String.format("%s/setspeed_cmd", serial);
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

        context.awaitCompletion(1, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("ar markers")
    public void testArMarkers() throws InterruptedException {
        VertxTestContext context = new VertxTestContext();
        bridge.start();

        RosSubscription subscription = RosSubscription.builder(String.format("/%s/ar_pose_marker", serial), AlvarMarkers.TYPE).throttleRate(200).build();
        bridge.subscribe(subscription, message -> {
            logger.info("message: {}", message);
            context.completeNow();
        });

        context.awaitCompletion(1, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("test odom")
    public void testOdom() throws InterruptedException {
        VertxTestContext context = new VertxTestContext();
        bridge.start();

        RosSubscription subscription = RosSubscription.builder(String.format("/%s/odom", serial), Odometry.TYPE).throttleRate(200).build();
        bridge.subscribe(subscription, message -> {
            Odometry odometry = Odometry.fromJsonObject(message.body());
            logger.info("message: {}", message.body());
            logger.info("odometry: {}", odometry);
        });

        context.awaitCompletion(1, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("path test")
    public void testPath() throws InterruptedException {
        VertxTestContext context = new VertxTestContext();

        props.setPrintSendMsg(true);
        props.setPrintReceivedMsg(true);
        props.setMaxFrameSize(100000000);

        bridge.start();

        // 목적지 이동
        RosSubscription global = RosSubscription.builder(String.format("/%s/move_base/TebLocalPlannerROS/gloabl_plan", serial), Path.TYPE).throttleRate(200).build();
        bridge.subscribe(global, message -> {
            logger.info("message: {}", message.body());
        });

        // 이동 중인 경로
        RosSubscription local = RosSubscription.builder(String.format("/%s/move_base/TebLocalPlannerROS/local_plan", serial), Path.TYPE).throttleRate(200).build();
        bridge.subscribe(local, message -> {
            logger.info("message: {}", message.body());
            context.completeNow();
        });

        context.awaitCompletion(1, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("sensor test")
    public void testSensor() throws InterruptedException {
        VertxTestContext context = new VertxTestContext();

        props.setPrintSendMsg(true);
        props.setPrintReceivedMsg(true);
        props.setMaxFrameSize(100000000);

        bridge.start();

        // 로봇의 좌측 하단에 장착된 초음파 센서 데이터
        RosSubscription leftBottom = RosSubscription.builder(String.format("/%s/move_base/TebLocalPlannerROS/Ultrasonic_D_L", serial), Path.TYPE).throttleRate(200).build();
        bridge.subscribe(leftBottom, message -> {
            logger.info("message: {}", message.body());
        });

        // 로봇의 좌측 하단에 장착된 초음파 센서 데이터
        RosSubscription rightBottom = RosSubscription.builder(String.format("/%s/move_base/TebLocalPlannerROS/Ultrasonic_D_R", serial), Path.TYPE).throttleRate(200).build();
        bridge.subscribe(rightBottom, message -> {
            logger.info("message: {}", message.body());
            context.completeNow();
        });


        context.awaitCompletion(1, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("TFClient 테스트")
    public void testTFClient() throws InterruptedException {
        VertxTestContext context = new VertxTestContext();

        props.setPrintSendMsg(true);
        props.setPrintReceivedMsg(true);
        props.setPrintProcessMsg(true);

        bridge.start();
        bridge.waitForConnection();

        TFClient mapClient = new TFClient(bridge, serial);
        mapClient.setFixedFrame("map");
        mapClient.setAngularThresh(0.1);
        mapClient.setTransThresh(0.03);
        mapClient.setRate(500);

        TFClient odomClient = new TFClient(bridge, serial);
        mapClient.setFixedFrame(String.format("%s/odom", serial));
        mapClient.setAngularThresh(0.1);
        mapClient.setTransThresh(0.03);
        mapClient.setRate(500);

        mapClient.subscribe(String.format("%s/base_footprint", serial), tf -> {
            logger.info("tf: {}", tf);
            context.completeNow();
        });

        odomClient.subscribe("map", tf -> {
            logger.info("tf: {}", tf);
            context.completeNow();
        });

        context.awaitCompletion(1, TimeUnit.SECONDS);
    }


    @Test
    public void testMoveBase() throws InterruptedException {
        VertxTestContext context = new VertxTestContext();

        props.setPrintSendMsg(true);
        props.setPrintReceivedMsg(true);
        props.setMaxFrameSize(100000000);

        bridge.start();

        // 로봇의 좌측 하단에 장착된 초음파 센서 데이터
        RosSubscription leftBottom = RosSubscription.builder(String.format("/%s/move_base/result", serial), MoveBaseActionResult.TYPE).throttleRate(200).build();
        bridge.subscribe(leftBottom, message -> {
            logger.info("message: {}", message.body());
        });

        context.awaitCompletion(1, TimeUnit.SECONDS);
    }

    @Test
    public void subscribeTest() throws InterruptedException {
        VertxTestContext context = new VertxTestContext();

        props.setPrintSendMsg(true);
        props.setPrintReceivedMsg(true);
        props.setPrintProcessMsg(true);

        props.setMaxFrameSize(100000000);
        bridge.start();

        // 배터리
        RosSubscription battery = RosSubscription.builder(String.format("/%s/tetra_battery", serial), Int32.TYPE).throttleRate(1000).build();
        logger.info("subscribe battery: {}", battery);
        bridge.subscribe(battery, message -> {
            Int32 data = Int32.fromJsonObject(message.body());
            logger.info("battery - {}", data.getData());
        });

        // 배터리
        RosSubscription batteryVoltage = RosSubscription.builder(String.format("/%s/battery_voltage", serial), Float64.TYPE).throttleRate(1000).build();
        logger.info("subscribe battery voltage: {}", batteryVoltage);
        bridge.subscribe(batteryVoltage, message -> {
            Float64 data = Float64.fromJsonObject(message.body());
            logger.info("battery voltage- {}", data.getData());
        });

        // 배터리
        RosSubscription batteryCurrent = RosSubscription.builder(String.format("/%s/battery_current", serial), Float64.TYPE).throttleRate(1000).build();
        logger.info("subscribe battery current: {}", batteryCurrent);
        bridge.subscribe(batteryCurrent, message -> {
            Float64 data = Float64.fromJsonObject(message.body());
            logger.info("battery current- {}", data.getData());
        });

        context.awaitCompletion(1, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("obstacle publish 테스트")
    public void publishObstacle() throws InterruptedException {
        VertxTestContext context = new VertxTestContext();

        props.setPrintSendMsg(true);
        props.setPrintReceivedMsg(true);
        props.setPrintProcessMsg(true);

        props.setMaxFrameSize(100000000);
        bridge.start();

        ObstacleArrayMsg msg = new ObstacleArrayMsg();

        ObstacleMsg obstacle = new ObstacleMsg();
        Polygon polygon = new Polygon(new Point32(-1.28345F, -8.11423F, 0.0F), new Point32(-1.28345F, -8.11423F, 0.0F), new Point32(-1.28345F, -8.11423F, 0.0F), new Point32(-1.28345F, -8.11423F, 0.0F));
        obstacle.setPolygon(polygon);

        msg.setObstacles(obstacle);

        RosTopic topic = RosTopic.builder(String.format("/%s/move_base/TebLocalPlannerROS/obstacles", serial), ObstacleArrayMsg.TYPE).msg(msg).build();
        bridge.publish(topic).future().onSuccess(rosTopic -> {
            logger.info("topic: {}", rosTopic);
        }).onFailure(throwable -> {
            logger.info("error: {}", throwable.getMessage());
            throwable.printStackTrace();
        });
        context.awaitCompletion(10, TimeUnit.SECONDS);
    }
}

