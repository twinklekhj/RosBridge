package io.github.twinklekhj.ros.type.actionlib;


import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Goal extends RosMessage {
    public static final String TYPE = "actionlib_msgs/Goal";

    public static final String FIELD_GOAL_ID = "goal_id";
    public static final String FIELD_GOAL = "goal";

    private GoalID goalID;
    private JsonObject goal;

    private GoalStatus status;
    private JsonObject result;
    private JsonObject feedback;

    public Goal() {
        this(new GoalID(), new JsonObject());
    }

    public Goal(JsonObject goal) {
        this(new GoalID(), goal);
    }

    public Goal(GoalID goalID, JsonObject goal) {
        this.goalID = goalID;
        this.goal = goal;

        JsonObject json = jsonBuilder().put(FIELD_GOAL_ID, goalID.getJsonObject()).put(FIELD_GOAL, goal);
        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static Goal fromJsonString(String jsonString) {
        return Goal.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Goal fromMessage(RosMessage m) {
        return Goal.fromJsonObject(m.getJsonObject());
    }

    public static Goal fromJsonObject(JsonObject jsonObject) {
        GoalID position = jsonObject.containsKey(FIELD_GOAL_ID) ? GoalID.fromJsonObject(jsonObject.getJsonObject(FIELD_GOAL_ID)) : new GoalID();
        JsonObject goal = jsonObject.containsKey(FIELD_GOAL) ? jsonObject.getJsonObject(FIELD_GOAL) : new JsonObject();

        return new Goal(position, goal);
    }

    public GoalID getGoalID() {
        return goalID;
    }

    public void setGoalID(GoalID goalID) {
        this.goalID = goalID;
        this.jsonObject.put(FIELD_GOAL_ID, goalID.getJsonObject());
    }

    public JsonObject getGoal() {
        return goal;
    }

    public void setGoal(JsonObject goal) {
        this.goal = goal;
        this.jsonObject.put(FIELD_GOAL, goal);
    }

    public GoalStatus getStatus() {
        return status;
    }

    public void setStatus(GoalStatus status) {
        this.status = status;
    }

    public JsonObject getResult() {
        return result;
    }

    public void setResult(JsonObject result) {
        this.result = result;
    }

    public JsonObject getFeedback() {
        return feedback;
    }

    public void setFeedback(JsonObject feedback) {
        this.feedback = feedback;
    }

    @Override
    public Goal clone() {
        return new Goal(this.goalID, this.goal);
    }

}
