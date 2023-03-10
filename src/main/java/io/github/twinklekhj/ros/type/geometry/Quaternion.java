package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;


public class Quaternion extends RosMessage {
    public static final String FIELD_X = "x";
    public static final String FIELD_Y = "y";
    public static final String FIELD_Z = "z";
    public static final String FIELD_W = "w";

    public static final String TYPE = "geometry_msgs/Quaternion";

    private final double x, y, z, w;

    public Quaternion() {
        this(0, 0, 0, 0);
    }

    public Quaternion(double x, double y, double z, double w) {
        super(jsonBuilder().put(Quaternion.FIELD_X, x).put(Quaternion.FIELD_Y, y).put(Quaternion.FIELD_Z, z).put(Quaternion.FIELD_W, w), Quaternion.TYPE);
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public static Quaternion fromJsonString(String jsonString) {
        return Quaternion.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Quaternion fromMessage(RosMessage m) {
        return Quaternion.fromJsonObject(m.getJsonObject());
    }

    public static Quaternion fromJsonObject(JsonObject jsonObject) {
        double x = jsonObject.containsKey(Quaternion.FIELD_X) ? jsonObject.getDouble(Quaternion.FIELD_X) : 0.0;
        double y = jsonObject.containsKey(Quaternion.FIELD_Y) ? jsonObject.getDouble(Quaternion.FIELD_Y) : 0.0;
        double z = jsonObject.containsKey(Quaternion.FIELD_Z) ? jsonObject.getDouble(Quaternion.FIELD_Z) : 0.0;
        double w = jsonObject.containsKey(Quaternion.FIELD_W) ? jsonObject.getDouble(Quaternion.FIELD_W) : 0.0;
        return new Quaternion(x, y, z, w);
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

    public double getW() {
        return this.w;
    }

    @Override
    public Quaternion clone() {
        return new Quaternion(this.x, this.y, this.z, this.w);
    }
}
