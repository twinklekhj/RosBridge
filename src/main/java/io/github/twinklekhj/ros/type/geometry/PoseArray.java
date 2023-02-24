package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONArray;
import org.json.JSONObject;
import io.github.twinklekhj.ros.type.std.Header;

import java.util.Arrays;

public class PoseArray extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_POSES = "poses";

    public static final String TYPE = "geometry_msgs/PoseArray";

    private final Header header;
    private final Pose[] poses;

    public PoseArray() {
        this(new Header(), new Pose[]{});
    }

    /**
     * Create a new PoseArray with the given set of poses and header. The array
     * of poses will be copied into this object.
     *
     * @param header The message header.
     * @param poses  The poses of the pose array.
     */
    public PoseArray(Header header, Pose[] poses) {
        super(builder().put(PoseArray.FIELD_HEADER, header.toJSONObject()).put(PoseArray.FIELD_POSES, builder(Arrays.deepToString(poses))), PoseArray.TYPE);

        this.header = header;
        this.poses = new Pose[poses.length];
        System.arraycopy(poses, 0, this.poses, 0, poses.length);
    }

    public static PoseArray fromJsonString(String jsonString) {
        return PoseArray.fromMessage(new RosMessage(jsonString));
    }

    public static PoseArray fromMessage(RosMessage m) {
        return PoseArray.fromJSONObject(m.toJSONObject());
    }

    public static PoseArray fromJSONObject(JSONObject jsonObject) {
        Header header = jsonObject.has(PoseArray.FIELD_HEADER) ? Header.fromJSONObject(jsonObject.getJSONObject(PoseArray.FIELD_HEADER)) : new Header();

        JSONArray jsonPoses = jsonObject.getJSONArray(PoseArray.FIELD_POSES);
        if (jsonPoses != null) {
            // convert each pose
            Pose[] poses = new Pose[jsonPoses.length()];
            for (int i = 0; i < poses.length; i++) {
                poses[i] = Pose.fromJSONObject(jsonPoses.getJSONObject(i));
            }
            return new PoseArray(header, poses);
        } else {
            return new PoseArray(header, new Pose[]{});
        }
    }

    public int size() {
        return this.poses.length;
    }

    public Pose get(int index) {
        return this.poses[index];
    }

    public Pose[] getPoses() {
        return this.poses;
    }

    public Header getHeader() {
        return this.header;
    }

    @Override
    public PoseArray clone() {
        return new PoseArray(this.header, this.poses);
    }
}
