package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;


public class Pose extends RosMessage {
    public static final String FIELD_POSITION = "position";
    public static final String FIELD_ORIENTATION = "orientation";

    public static final String TYPE = "geometry_msgs/Pose";

    private final Point position;
    private final Quaternion orientation;

    /**
     * Create a new Pose with all 0s.
     */
    public Pose() {
        this(new Point(), new Quaternion());
    }

    /**
     * Create a new Pose with the given position and orientation values.
     *
     * @param position    The position value of the pose.
     * @param orientation The orientation value of the pose.
     */
    public Pose(Point position, Quaternion orientation) {
        // build the JSON object
        super(jsonBuilder().put(Pose.FIELD_POSITION, position.getJsonObject()).put(Pose.FIELD_ORIENTATION, orientation.getJsonObject()), Pose.TYPE);
        this.position = position;
        this.orientation = orientation;
    }

    public static Pose fromJsonString(String jsonString) {
        return Pose.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Pose fromMessage(RosMessage m) {
        return Pose.fromJsonObject(m.getJsonObject());
    }

    public static Pose fromJsonObject(JsonObject jsonObject) {
        Point position = jsonObject.containsKey(Pose.FIELD_POSITION) ? Point.fromJsonObject(jsonObject.getJsonObject(Pose.FIELD_POSITION)) : new Point();
        Quaternion orientation = jsonObject.containsKey(Pose.FIELD_ORIENTATION) ? Quaternion.fromJsonObject(jsonObject.getJsonObject(Pose.FIELD_ORIENTATION)) : new Quaternion();
        return new Pose(position, orientation);
    }

    public Point getPosition() {
        return this.position;
    }

    public Quaternion getOrientation() {
        return this.orientation;
    }

    @Override
    public Pose clone() {
        return new Pose(this.position, this.orientation);
    }
}
