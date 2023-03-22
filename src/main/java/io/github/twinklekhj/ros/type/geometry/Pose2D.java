package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Pose2D extends RosMessage {
    public static final String FIELD_X = "x";
    public static final String FIELD_Y = "y";
    public static final String FIELD_THETA = "theta";

    public static final String TYPE = "geometry_msgs/Pose2D";

    private double x;
    private double y;
    private double theta;

    public Pose2D() {
        this(0, 0, 0);
    }

    public Pose2D(double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;

        super.setJsonObject(jsonBuilder().put(Pose2D.FIELD_X, x).put(Pose2D.FIELD_Y, y).put(Pose2D.FIELD_THETA, theta));
        super.setType(TYPE);
    }

    public static Pose2D fromJsonString(String jsonString) {
        return Pose2D.fromMessage(new RosMessage(jsonString, TYPE));
    }


    public static Pose2D fromMessage(RosMessage m) {
        return Pose2D.fromJsonObject(m.getJsonObject());
    }

    public static Pose2D fromJsonObject(JsonObject jsonObject) {
        double x = jsonObject.containsKey(Pose2D.FIELD_X) ? jsonObject.getDouble(Pose2D.FIELD_X) : 0.0;
        double y = jsonObject.containsKey(Pose2D.FIELD_Y) ? jsonObject.getDouble(Pose2D.FIELD_Y) : 0.0;
        double theta = jsonObject.containsKey(Pose2D.FIELD_THETA) ? jsonObject.getDouble(Pose2D.FIELD_THETA) : 0.0;
        return new Pose2D(x, y, theta);
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
        this.jsonObject.put(FIELD_Y, y);
    }

    public double getTheta() {
        return this.theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
        this.jsonObject.put(FIELD_THETA, theta);
    }

    @Override
    public Pose2D clone() {
        return new Pose2D(this.x, this.y, this.theta);
    }
}
