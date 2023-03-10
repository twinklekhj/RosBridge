package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

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
        super(jsonBuilder().put(PoseArray.FIELD_HEADER, header.getJsonObject()).put(PoseArray.FIELD_POSES, jsonBuilder(Arrays.deepToString(poses))), PoseArray.TYPE);

        this.header = header;
        this.poses = new Pose[poses.length];
        System.arraycopy(poses, 0, this.poses, 0, poses.length);
    }

    public static PoseArray fromJsonString(String jsonString) {
        return PoseArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static PoseArray fromMessage(RosMessage m) {
        return PoseArray.fromJsonObject(m.getJsonObject());
    }

    public static PoseArray fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(PoseArray.FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(PoseArray.FIELD_HEADER)) : new Header();

        JsonArray jsonPoses = jsonObject.getJsonArray(PoseArray.FIELD_POSES);
        if (jsonPoses != null) {
            // convert each pose
            Pose[] poses = new Pose[jsonPoses.size()];
            for (int i = 0; i < poses.length; i++) {
                poses[i] = Pose.fromJsonObject(jsonPoses.getJsonObject(i));
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
