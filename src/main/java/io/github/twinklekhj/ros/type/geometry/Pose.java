package io.github.twinklekhj.ros.type.geometry;


import org.json.JSONObject;
import io.github.twinklekhj.ros.type.RosMessage;

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
        super(builder().put(Pose.FIELD_POSITION, position.toJSONObject()).put(Pose.FIELD_ORIENTATION, orientation.toJSONObject()), Pose.TYPE);
        this.position = position;
        this.orientation = orientation;
    }

    public static Pose fromJsonString(String jsonString) {
        return Pose.fromMessage(new RosMessage(jsonString));
    }

    public static Pose fromMessage(RosMessage m) {
        return Pose.fromJSONObject(m.toJSONObject());
    }

    public static Pose fromJSONObject(JSONObject jsonObject) {
        Point position = jsonObject.has(Pose.FIELD_POSITION) ? Point.fromJSONObject(jsonObject.getJSONObject(Pose.FIELD_POSITION)) : new Point();
        Quaternion orientation = jsonObject.has(Pose.FIELD_ORIENTATION) ? Quaternion.fromJSONObject(jsonObject.getJSONObject(Pose.FIELD_ORIENTATION)) : new Quaternion();
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
