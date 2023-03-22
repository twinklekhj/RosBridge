package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Point32 extends RosMessage {
    public static final String TYPE = "geometry_msgs/Point32";

    public static final String FIELD_X = "x";
    public static final String FIELD_Y = "y";
    public static final String FIELD_Z = "z";

    private float x;
    private float y;
    private float z;

    public Point32() {
        this(0, 0, 0);
    }

    public Point32(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;

        super.setJsonObject(jsonBuilder().put(Point.FIELD_X, x).put(Point.FIELD_Y, y).put(Point.FIELD_Z, z));
        super.setType(TYPE);
    }

    public static Point32 fromJsonString(String jsonString) {
        return Point32.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Point32 fromMessage(RosMessage m) {
        return Point32.fromJsonObject(m.getJsonObject());
    }

    public static Point32 fromJsonObject(JsonObject jsonObject) {
        float x = jsonObject.containsKey(Point32.FIELD_X) ? jsonObject.getFloat(Point32.FIELD_X) : 0.0f;
        float y = jsonObject.containsKey(Point32.FIELD_Y) ? jsonObject.getFloat(Point32.FIELD_Y) : 0.0f;
        float z = jsonObject.containsKey(Point32.FIELD_Z) ? jsonObject.getFloat(Point32.FIELD_Z) : 0.0f;
        return new Point32(x, y, z);
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
        this.jsonObject.put(FIELD_X, x);
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
        this.jsonObject.put(FIELD_Y, x);
    }

    public float getZ() {
        return this.z;
    }

    public void setZ(float z) {
        this.z = z;
        this.jsonObject.put(FIELD_Z, x);
    }

    @Override
    public Point32 clone() {
        return new Point32(this.x, this.y, this.z);
    }
}
