package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONObject;
import io.github.twinklekhj.ros.type.std.Header;

public class PoseStamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_POSE = "pose";

    public static final String TYPE = "geometry_msgs/PoseStamped";

    private final Header header;
    private final Pose pose;

    /**
     * Create a new PoseStamped with all 0s.
     */
    public PoseStamped() {
        this(new Header(), new Pose());
    }

    /**
     * Create a new PoseStamped with the given values.
     *
     * @param header The header value of the pose.
     * @param pose   The pose value of the pose.
     */
    public PoseStamped(Header header, Pose pose) {
        super(jsonBuilder().put(PoseStamped.FIELD_HEADER, header.toJSONObject()).put(PoseStamped.FIELD_POSE, pose.toJSONObject()), PoseStamped.TYPE);
        this.header = header;
        this.pose = pose;
    }

    public static PoseStamped fromJsonString(String jsonString) {
        return PoseStamped.fromMessage(new RosMessage(jsonString));
    }

    public static PoseStamped fromMessage(RosMessage m) {
        return PoseStamped.fromJSONObject(m.toJSONObject());
    }

    public static PoseStamped fromJSONObject(JSONObject jsonObject) {
        Header header = jsonObject.has(PoseStamped.FIELD_HEADER) ? Header.fromJSONObject(jsonObject.getJSONObject(PoseStamped.FIELD_HEADER)) : new Header();
        Pose pose = jsonObject.has(PoseStamped.FIELD_POSE) ? Pose.fromJSONObject(jsonObject.getJSONObject(PoseStamped.FIELD_POSE)) : new Pose();
        return new PoseStamped(header, pose);
    }

    public Header getHeader() {
        return this.header;
    }


    public Pose getPose() {
        return this.pose;
    }

    @Override
    public PoseStamped clone() {
        return new PoseStamped(this.header, this.pose);
    }
}
