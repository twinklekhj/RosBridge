package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.Builder;


@Builder
public class Vector3 extends RosMessage {
    public static final String TYPE = "geometry_msgs/Vector3";

    public static final String FIELD_X = "x";
    public static final String FIELD_Y = "y";
    public static final String FIELD_Z = "z";

    @Builder.Default
    private double x = 0;

    @Builder.Default
    private double y = 0;

    @Builder.Default
    private double z = 0;

    public Vector3() {
        this(0, 0, 0);
    }

    public Vector3(double x, double y, double z) {
        super(jsonBuilder().put(Vector3.FIELD_X, x).put(Vector3.FIELD_Y, y).put(Vector3.FIELD_Z, z), Vector3.TYPE);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3 fromJsonObject(JsonObject jsonObject) {
        double x = jsonObject.containsKey(Vector3.FIELD_X) ? jsonObject.getDouble(Vector3.FIELD_X) : 0.0;
        double y = jsonObject.containsKey(Vector3.FIELD_Y) ? jsonObject.getDouble(Vector3.FIELD_Y) : 0.0;
        double z = jsonObject.containsKey(Vector3.FIELD_Z) ? jsonObject.getDouble(Vector3.FIELD_Z) : 0.0;
        return new Vector3(x, y, z);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    @Override
    public Vector3 clone() {
        return new Vector3(this.x, this.y, this.z);
    }
}
