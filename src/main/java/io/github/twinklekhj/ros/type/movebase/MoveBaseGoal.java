package io.github.twinklekhj.ros.type.movebase;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.geometry.PoseStamped;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class MoveBaseGoal extends RosMessage {
    public static final String TYPE = "move_base_msgs/MoveBaseGoal";

    public static final String FIELD_POSE = "target_pose";

    private PoseStamped pose;

    public MoveBaseGoal() {
        this(new PoseStamped());
    }

    public MoveBaseGoal(PoseStamped pose) {
        this.pose = pose;

        JsonObject json = jsonBuilder()
                .put(FIELD_POSE, pose.getJsonObject());

        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static MoveBaseGoal fromJsonString(String jsonString) {
        return MoveBaseGoal.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static MoveBaseGoal fromMessage(RosMessage m) {
        return MoveBaseGoal.fromJsonObject(m.getJsonObject());
    }

    public static MoveBaseGoal fromJsonObject(JsonObject jsonObject) {
        PoseStamped pose = jsonObject.containsKey(FIELD_POSE) ? PoseStamped.fromJsonObject(jsonObject.getJsonObject(FIELD_POSE)) : new PoseStamped();

        return new MoveBaseGoal(pose);
    }

    public PoseStamped getPose() {
        return pose;
    }

    public void setPose(PoseStamped pose) {
        this.pose = pose;
        this.jsonObject.put(FIELD_POSE, pose.getJsonObject());
    }

    @Override
    public MoveBaseGoal clone() {
        return new MoveBaseGoal(this.pose);
    }
}
