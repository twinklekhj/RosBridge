package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@ToString
public class PoseArray extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_POSES = "poses";

    public static final String TYPE = "geometry_msgs/PoseArray";

    private Header header;
    private Pose[] poses;

    public PoseArray() {
        this(new Header(), new Pose[]{});
    }

    public PoseArray(Header header, Pose[] poses) {
        this.header = header;
        this.poses = new Pose[poses.length];
        System.arraycopy(poses, 0, this.poses, 0, poses.length);

        super.setJsonObject(jsonBuilder().put(FIELD_HEADER, header.getJsonObject()).put(FIELD_POSES, jsonBuilder(Arrays.deepToString(poses))));
        super.setType(TYPE);
    }

    public static PoseArray fromJsonString(String jsonString) {
        return PoseArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static PoseArray fromMessage(RosMessage m) {
        return PoseArray.fromJsonObject(m.getJsonObject());
    }

    public static PoseArray fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();

        JsonArray jsonPoses = jsonObject.getJsonArray(FIELD_POSES);
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

    public void setPoses(Pose... poses) {
        this.poses = new Pose[poses.length];
        System.arraycopy(poses, 0, this.poses, 0, poses.length);

        this.jsonObject.put(FIELD_POSES, jsonBuilder(Arrays.deepToString(poses)));
    }

    public void setPoses(List<Pose> poses) {
        this.poses = new Pose[poses.size()];
        for (int i = 0; i < poses.size(); i++) {
            this.poses[i] = poses.get(0);
        }

        this.jsonObject.put(FIELD_POSES, jsonBuilder(Arrays.deepToString(this.poses)));
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    @Override
    public PoseArray clone() {
        return new PoseArray(this.header, this.poses);
    }
}
