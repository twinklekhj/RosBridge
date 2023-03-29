package io.github.twinklekhj.ros.type.actionlib;


import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;
import java.util.Optional;

@ToString
public class GoalStatus extends RosMessage {
    public static final String TYPE = "actionlib_msgs/GoalStatus";

    public static final String FIELD_GOAL_ID = "goal_id";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_TEXT = "text";

    private final GoalID goalID;
    private final Status status;
    private final String text;

    public GoalStatus() {
        this(new GoalID(), Status.PENDING, "");
    }

    public GoalStatus(GoalID goalID, Status status, String text) {
        this.goalID = goalID;
        this.status = status;
        this.text = text;

        JsonObject json = jsonBuilder().put(FIELD_GOAL_ID, goalID.getJsonObject()).put(FIELD_STATUS, status.value).put(FIELD_TEXT, text);
        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static GoalStatus fromJsonString(String jsonString) {
        return GoalStatus.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static GoalStatus fromMessage(RosMessage m) {
        return GoalStatus.fromJsonObject(m.getJsonObject());
    }

    public static GoalStatus fromJsonObject(JsonObject jsonObject) {
        GoalID position = jsonObject.containsKey(FIELD_GOAL_ID) ? GoalID.fromJsonObject(jsonObject.getJsonObject(FIELD_GOAL_ID)) : new GoalID();
        Status status = Status.findByValue(jsonObject.containsKey(FIELD_STATUS) ? jsonObject.getInteger(FIELD_STATUS) : 0).orElse(Status.PENDING);
        String text = jsonObject.containsKey(FIELD_TEXT) ? jsonObject.getString(FIELD_TEXT) : "";

        return new GoalStatus(position, status, text);
    }

    public GoalID getGoalID() {
        return goalID;
    }

    public Status getStatus() {
        return status;
    }

    public String getText() {
        return text;
    }

    @Override
    public GoalStatus clone() {
        return new GoalStatus(this.goalID, this.status, this.text);
    }

    public enum Status {
        PENDING(0, "The goal has yet to be processed by the action server"),
        ACTIVE(1, "The goal is currently being processed by the action server"),
        PREEMPTED(2, "The goal received a cancel request after it started executing and has since completed its execution (Terminal State)"),
        SUCCEEDED(3, "The goal was achieved successfully by the action server (Terminal State)"),
        ABORTE(4, "The goal was aborted during execution by the action server due to some failure (Terminal State)"),
        REJECTED(5, "The goal was rejected by the action server without being processed, because the goal was unattainable or invalid (Terminal State)"),
        PREEMPTING(6, "The goal received a cancel request after it started executing and has not yet completed execution"),
        RECALLING(7, "The goal received a cancel request before it started executing, but the action server has not yet confirmed that the goal is canceled"),
        RECALLED(8, "The goal received a cancel request before it started executing and was successfully cancelled (Terminal State)"),
        LOST(9, "An action client can determine that a goal is LOST. This should not be sent over the wire by an action server");

        final int value;
        final String description;

        Status(int value, String description) {
            this.value = value;
            this.description = description;
        }

        private static Optional<Status> findByValue(int value) {
            return Arrays.stream(Status.values()).filter(status -> status.value == value).findFirst();
        }
    }
}
