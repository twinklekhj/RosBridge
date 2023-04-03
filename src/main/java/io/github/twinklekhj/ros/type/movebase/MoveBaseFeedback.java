package io.github.twinklekhj.ros.type.movebase;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.geometry.PoseStamped;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class MoveBaseFeedback extends RosMessage {
    public static final String TYPE = "move_base_msgs/MoveBaseFeedback";

    public static final String FIELD_POSE = "base_position";

    private PoseStamped pose;

    public MoveBaseFeedback() {
        this(new PoseStamped());
    }

    public MoveBaseFeedback(PoseStamped pose) {
        this.pose = pose;

        JsonObject json = jsonBuilder()
                .put(FIELD_POSE, pose.getJsonObject());

        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static MoveBaseFeedback fromJsonString(String jsonString) {
        return MoveBaseFeedback.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static MoveBaseFeedback fromMessage(RosMessage m) {
        return MoveBaseFeedback.fromJsonObject(m.getJsonObject());
    }

    public static MoveBaseFeedback fromJsonObject(JsonObject jsonObject) {
        PoseStamped pose = jsonObject.containsKey(FIELD_POSE) ? PoseStamped.fromJsonObject(jsonObject.getJsonObject(FIELD_POSE)) : new PoseStamped();

        return new MoveBaseFeedback(pose);
    }

    public PoseStamped getPose() {
        return pose;
    }

    public void setPose(PoseStamped pose) {
        this.pose = pose;
        this.jsonObject.put(FIELD_POSE, pose.getJsonObject());
    }

    @Override
    public MoveBaseFeedback clone() {
        return new MoveBaseFeedback(this.pose);
    }
}
