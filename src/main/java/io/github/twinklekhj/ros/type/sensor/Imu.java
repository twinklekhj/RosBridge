package io.github.twinklekhj.ros.type.sensor;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.geometry.Quaternion;
import io.github.twinklekhj.ros.type.geometry.Vector3;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class Imu extends RosMessage {
    public static final String TYPE = "sensor_msgs/Imu";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_ORIENTATION = "orientation";
    public static final String FIELD_ORIENTATION_COVARIANCE = "orientation_covariance";
    public static final String FIELD_ANGULAR = "angular_velocity";
    public static final String FIELD_ANGULAR_COVARIANCE = "angular_velocity_covariance";
    public static final String FIELD_LINEAR = "linear_acceleration";
    public static final String FIELD_LINEAR_COVARIANCE = "linear_acceleration_covariance";

    private final Header header;

    private final Quaternion orientation;
    private final float[] orientation_covariance;

    private final Vector3 angular_velocity;
    private final float[] angular_velocity_covariance;

    private final Vector3 linear_acceleration;
    private final float[] linear_acceleration_covariance;


    public static Imu fromJsonString(String jsonString) {
        return Imu.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Imu fromMessage(RosMessage m) {
        return Imu.fromJsonObject(m.getJsonObject());
    }

    public static Imu fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        Quaternion orientation = jsonObject.containsKey(FIELD_ORIENTATION) ? Quaternion.fromJsonObject(jsonObject.getJsonObject(FIELD_ORIENTATION)) : new Quaternion();
        Vector3 angular_velocity = jsonObject.containsKey(FIELD_ANGULAR) ? Vector3.fromJsonObject(jsonObject.getJsonObject(FIELD_ANGULAR)) : new Vector3();
        Vector3 linear_acceleration = jsonObject.containsKey(FIELD_LINEAR) ? Vector3.fromJsonObject(jsonObject.getJsonObject(FIELD_LINEAR)) : new Vector3();

        JsonArray jsonData;
        float[] orientation_covariance = {};
        jsonData = jsonObject.getJsonArray(FIELD_ORIENTATION_COVARIANCE);
        if (jsonData != null) {
            orientation_covariance = new float[jsonData.size()];
            for (int i = 0; i < orientation_covariance.length; i++) {
                orientation_covariance[i] = jsonData.getInteger(i);
            }
        }

        float[] angular_velocity_covariance = {};
        jsonData = jsonObject.getJsonArray(FIELD_ANGULAR_COVARIANCE);
        if (jsonData != null) {
            angular_velocity_covariance = new float[jsonData.size()];
            for (int i = 0; i < angular_velocity_covariance.length; i++) {
                angular_velocity_covariance[i] = jsonData.getInteger(i);
            }
        }

        float[] linear_acceleration_covariance = {};
        jsonData = jsonObject.getJsonArray(FIELD_LINEAR_COVARIANCE);
        if (jsonData != null) {
            linear_acceleration_covariance = new float[jsonData.size()];
            for (int i = 0; i < linear_acceleration_covariance.length; i++) {
                linear_acceleration_covariance[i] = jsonData.getInteger(i);
            }
        }
        return new Imu(header, orientation, orientation_covariance, angular_velocity, angular_velocity_covariance, linear_acceleration, linear_acceleration_covariance);
    }

    public Header getHeader() {
        return header;
    }

    public Vector3 getAngular_velocity() {
        return angular_velocity;
    }

    public float[] getAngular_velocity_covariance() {
        return angular_velocity_covariance;
    }

    public Vector3 getLinear_acceleration() {
        return linear_acceleration;
    }

    public float[] getLinear_acceleration_covariance() {
        return linear_acceleration_covariance;
    }

    public Quaternion getOrientation() {
        return orientation;
    }

    public float[] getOrientation_covariance() {
        return orientation_covariance;
    }

    @Override
    public Imu clone() {
        return new Imu(this.header, this.orientation, this.orientation_covariance, this.angular_velocity, this.angular_velocity_covariance, this.linear_acceleration, this.linear_acceleration_covariance);
    }
}

