package io.github.twinklekhj.ros.type;

import io.vertx.core.json.JsonObject;
import lombok.Builder;

import java.util.Map;

@Builder
public class RosMessage {
    public static final String EMPTY_MESSAGE = "{}";

    protected JsonObject jsonObject = new JsonObject();
    protected String type;

    public RosMessage() {
        this.jsonObject = new JsonObject();
        this.type = "";
    }

    public RosMessage(String jsonString, String type) {
        this.jsonObject = jsonBuilder(jsonString);
        this.type = type;
    }

    public RosMessage(Map<?, ?> jsonMap, String type) {
        this.jsonObject = jsonBuilder(jsonMap);
        this.type = type;
    }

    public RosMessage(JsonObject jsonObject, String type) {
        this.jsonObject = jsonObject;
        this.type = type;
    }

    public static JsonObject jsonBuilder() {
        return new JsonObject();
    }

    public static JsonObject jsonBuilder(String jsonString) {
        return new JsonObject(jsonString);
    }

    public static JsonObject jsonBuilder(Map<?, ?> jsonMap) {
        return JsonObject.mapFrom(jsonMap);
    }

    public static String toJsonString(JsonObject jsonObject) {
        return jsonObject.toString();
    }

    private static RosMessageBuilder builder() {
        return new RosMessageBuilder();
    }

    public static RosMessageBuilder builder(String jsonString, String type) {
        return builder().jsonObject(jsonBuilder(jsonString)).type(type);
    }

    public static RosMessageBuilder builder(JsonObject jsonObject, String type) {
        return builder().jsonObject(jsonObject).type(type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public interface Type {
        String getName();

        enum Primitive implements Type {
            Bool("std_msgs/Bool"),
            Byte("std_msgs/Byte"),
            ByteMultiArray("std_msgs/ByteMultiArray"),
            Char("std_msgs/Char"),
            ColorRGBA("std_msgs/ColorRGBA"),
            Duration("std_msgs/Duration"),
            Empty("std_msgs/Empty"),
            Float32("std_msgs/Float32"),
            Float32MultiArray("std_msgs/Float32MultiArray"),
            Float64("std_msgs/Float64"),
            Float64MultiArray("std_msgs/Float64MultiArray"),
            Header("std_msgs/Header"),
            Int16("std_msgs/Int16"),
            Int16MultiArray("std_msgs/Int16MultiArray"),
            Int32("std_msgs/Int32"),
            Int32MultiArray("std_msgs/Int32MultiArray"),
            Int64("std_msgs/Int64"),
            Int64MultiArray("std_msgs/Int64MultiArray"),
            Int8("std_msgs/Int8"),
            Int8MultiArray("std_msgs/Int8MultiArray"),
            MultiArrayDimension("std_msgs/MultiArrayDimension"),
            MultiArrayLayout("std_msgs/MultiArrayLayout"),
            String("std_msgs/String"),
            Time("std_msgs/Time"),
            UInt16("std_msgs/UInt16"),
            UInt16MultiArray("std_msgs/UInt16MultiArray"),
            UInt32("std_msgs/UInt32"),
            UInt32MultiArray("std_msgs/UInt32MultiArray"),
            UInt64("std_msgs/UInt64"),
            UInt64MultiArray("std_msgs/UInt64MultiArray"),
            UInt8("std_msgs/UInt8"),
            UInt8MultiArray("std_msgs/UInt8MultiArray"),
            ;
            private String name;

            Primitive(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        enum Sensor implements Type {
            BatteryState("sensor_msgs/BatteryState"),
            CameraInfo("sensor_msgs/CameraInfo"),
            ChannelFloat32("sensor_msgs/ChannelFloat32"),
            CompressedImage("sensor_msgs/CompressedImage"),
            FluidPressure("sensor_msgs/FluidPressure"),
            Illuminance("sensor_msgs/Illuminance"),
            Image("sensor_msgs/Image"),
            Imu("sensor_msgs/Imu"),
            JointState("sensor_msgs/JointState"),
            Joy("sensor_msgs/Joy"),
            JoyFeedback("sensor_msgs/JoyFeedback"),
            JoyFeedbackArray("sensor_msgs/JoyFeedbackArray"),
            LaserEcho("sensor_msgs/LaserEcho"),
            LaserScan("sensor_msgs/LaserScan"),
            MagneticField("sensor_msgs/MagneticField"),
            MultiDOFJointState("sensor_msgs/MultiDOFJointState"),
            MultiEchoLaserScan("sensor_msgs/MultiEchoLaserScan"),
            NavSatFix("sensor_msgs/NavSatFix"),
            NavSatStatus("sensor_msgs/NavSatStatus"),
            PointCloud("sensor_msgs/PointCloud"),
            PointCloud2("sensor_msgs/PointCloud2"),
            PointField("sensor_msgs/PointField"),
            Range("sensor_msgs/Range"),
            RegionOfInterest("sensor_msgs/RegionOfInterest"),
            RelativeHumidity("sensor_msgs/RelativeHumidity"),
            Temperature("sensor_msgs/Temperature"),
            TimeReference("sensor_msgs/TimeReference"),
            ;
            private final String name;

            Sensor(String name) {
                this.name = name;
            }

            @Override
            public String getName() {
                return this.name;
            }
        }

        enum Geometry implements Type {
            Accel("geometry_msgs/Accel"),
            AccelStamped("geometry_msgs/AccelStamped"),
            AccelWithCovariance("geometry_msgs/AccelWithCovariance"),
            AccelWithCovarianceStamped("geometry_msgs/AccelWithCovarianceStamped"),
            Inertia("geometry_msgs/Inertia"),
            InertiaStamped("geometry_msgs/InertiaStamped"),
            Point("geometry_msgs/Point"),
            Point32("geometry_msgs/Point32"),
            PointStamped("geometry_msgs/PointStamped"),
            Polygon("geometry_msgs/Polygon"),
            PolygonStamped("geometry_msgs/PolygonStamped"),
            Pose("geometry_msgs/Pose"),
            Pose2D("geometry_msgs/Pose2D"),
            PoseArray("geometry_msgs/PoseArray"),
            PoseStamped("geometry_msgs/PoseStamped"),
            PoseWithCovariance("geometry_msgs/PoseWithCovariance"),
            PoseWithCovarianceStamped("geometry_msgs/PoseWithCovarianceStamped"),
            Quaternion("geometry_msgs/Quaternion"),
            QuaternionStamped("geometry_msgs/QuaternionStamped"),
            Transform("geometry_msgs/Transform"),
            TransformStamped("geometry_msgs/TransformStamped"),
            Twist("geometry_msgs/Twist"),
            TwistStamped("geometry_msgs/TwistStamped"),
            TwistWithCovariance("geometry_msgs/TwistWithCovariance"),
            TwistWithCovarianceStamped("geometry_msgs/TwistWithCovarianceStamped"),
            Vector3("geometry_msgs/Vector3"),
            Vector3Stamped("geometry_msgs/Vector3Stamped"),
            Wrench("geometry_msgs/Wrench"),
            WrenchStamped("geometry_msgs/WrenchStamped"),
            ;
            private final String name;

            Geometry(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        enum Navigation implements Type {
            GridCells("nav_msgs/GridCells"),
            MapMetaData("nav_msgs/MapMetaData"),
            OccupancyGrid("nav_msgs/OccupancyGrid"),
            Odometry("nav_msgs/Odometry"),
            Path("nav_msgs/Path"),
            ;
            private final String name;

            Navigation(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        enum RosGraph implements Type {
            Log("rosgraph_msgs/Log"),
            Clock("rosgraph_msgs/Clock"),
            ;
            private final String name;

            RosGraph(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }
    }
}
