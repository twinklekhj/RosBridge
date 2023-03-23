package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Point extends RosMessage {
    public static final String TYPE = "geometry_msgs/Point";

    public static final String FIELD_X = "x";
    public static final String FIELD_Y = "y";
    public static final String FIELD_Z = "z";

    private double x;
    private double y;
    private double z;

    public Point() {
        this(0, 0, 0);
    }

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        super.setJsonObject(jsonBuilder().put(FIELD_X, x).put(FIELD_Y, y).put(FIELD_Z, z));
        super.setType(TYPE);
    }

    public static Point fromJsonString(String jsonString) {
        return fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Point fromMessage(RosMessage m) {
        return fromJsonObject(m.getJsonObject());
    }

    public static Point fromJsonObject(JsonObject jsonObject) {
        double x = jsonObject.containsKey(FIELD_X) ? jsonObject.getDouble(FIELD_X) : 0.0;
        double y = jsonObject.containsKey(FIELD_Y) ? jsonObject.getDouble(FIELD_Y) : 0.0;
        double z = jsonObject.containsKey(FIELD_Z) ? jsonObject.getDouble(FIELD_Z) : 0.0;
        return new Point(x, y, z);
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
    public Point clone() {
        return new Point(this.x, this.y, this.z);
    }
}
