package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;


@ToString
public class Vector3 extends RosMessage {
    public static final String TYPE = "geometry_msgs/Vector3";

    public static final String FIELD_X = "x";
    public static final String FIELD_Y = "y";
    public static final String FIELD_Z = "z";

    private double x;
    private double y;
    private double z;

    public Vector3() {
        this(0, 0, 0);
    }

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        super.setJsonObject(jsonBuilder().put(FIELD_X, x).put(FIELD_Y, y).put(FIELD_Z, z));
        super.setType(TYPE);
    }

    public static Vector3 fromJsonObject(JsonObject jsonObject) {
        double x = jsonObject.containsKey(FIELD_X) ? jsonObject.getDouble(FIELD_X) : 0.0;
        double y = jsonObject.containsKey(FIELD_Y) ? jsonObject.getDouble(FIELD_Y) : 0.0;
        double z = jsonObject.containsKey(FIELD_Z) ? jsonObject.getDouble(FIELD_Z) : 0.0;
        return new Vector3(x, y, z);
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
        this.jsonObject.put(FIELD_X, x);
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
        this.jsonObject.put(FIELD_Y, x);
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
        this.jsonObject.put(FIELD_Z, x);
    }

    @Override
    public Vector3 clone() {
        return new Vector3(this.x, this.y, this.z);
    }
}
