package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;


public class Point extends RosMessage {
    public static final String FIELD_X = "x";
    public static final String FIELD_Y = "y";
    public static final String FIELD_Z = "z";
    public static final String TYPE = "geometry_msgs/Point";

    private final double x, y, z;

    public Point() {
        this(0, 0, 0);
    }

    public Point(double x, double y, double z) {
        // build the JSON object
        super(jsonBuilder().put(Point.FIELD_X, x).put(Point.FIELD_Y, y).put(Point.FIELD_Z, z), Point.TYPE);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Point fromJsonString(String jsonString) {
        return fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Point fromMessage(RosMessage m) {
        // get it from the JSON object
        return fromJsonObject(m.getJsonObject());
    }

    public static Point fromJsonObject(JsonObject jsonObject) {
        // check the fields
        double x = jsonObject.containsKey(Point.FIELD_X) ? jsonObject.getDouble(Point.FIELD_X) : 0.0;
        double y = jsonObject.containsKey(Point.FIELD_Y) ? jsonObject.getDouble(Point.FIELD_Y) : 0.0;
        double z = jsonObject.containsKey(Point.FIELD_Z) ? jsonObject.getDouble(Point.FIELD_Z) : 0.0;
        return new Point(x, y, z);
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
    public Point clone() {
        return new Point(this.x, this.y, this.z);
    }
}
