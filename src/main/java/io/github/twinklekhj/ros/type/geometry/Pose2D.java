package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONObject;

public class Pose2D extends RosMessage {
    public static final String FIELD_X = "x";
    public static final String FIELD_Y = "y";
    public static final String FIELD_THETA = "theta";

    public static final String TYPE = "geometry_msgs/Pose2D";

    private final double x, y, theta;

    public Pose2D() {
        this(0, 0, 0);
    }

    /**
     * Create a new Pose2D with the given values.
     *
     * @param x     The x value of the pose.
     * @param y     The y value of the pose.
     * @param theta The theta value of the pose.
     */
    public Pose2D(double x, double y, double theta) {
        // build the JSON object
        super(jsonBuilder().put(Pose2D.FIELD_X, x).put(Pose2D.FIELD_Y, y).put(Pose2D.FIELD_THETA, theta), Pose2D.TYPE);
        this.x = x;
        this.y = y;
        this.theta = theta;
    }

    public static Pose2D fromJsonString(String jsonString) {
        return Pose2D.fromMessage(new RosMessage(jsonString, TYPE));
    }


    public static Pose2D fromMessage(RosMessage m) {
        return Pose2D.fromJSONObject(m.getJsonObject());
    }

    public static Pose2D fromJSONObject(JSONObject jsonObject) {
        // check the fields
        double x = jsonObject.has(Pose2D.FIELD_X) ? jsonObject.getDouble(Pose2D.FIELD_X) : 0.0;
        double y = jsonObject.has(Pose2D.FIELD_Y) ? jsonObject.getDouble(Pose2D.FIELD_Y) : 0.0;
        double theta = jsonObject.has(Pose2D.FIELD_THETA) ? jsonObject.getDouble(Pose2D.FIELD_THETA) : 0.0;
        return new Pose2D(x, y, theta);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getTheta() {
        return this.theta;
    }

    /**
     * Create a clone of this Pose2D.
     */
    @Override
    public Pose2D clone() {
        return new Pose2D(this.x, this.y, this.theta);
    }
}
