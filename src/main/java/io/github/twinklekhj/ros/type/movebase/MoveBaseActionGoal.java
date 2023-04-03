package io.github.twinklekhj.ros.type.movebase;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.actionlib.GoalID;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class MoveBaseActionGoal extends RosMessage {
    public static final String TYPE = "move_base_msgs/MoveBaseActionGoal";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_GOAL_ID = "goal_id";
    public static final String FIELD_GOAL = "goal";

    private final Header header;
    private final GoalID goalID;
    private final MoveBaseGoal goal;

    public MoveBaseActionGoal() {
        this(new Header(), new GoalID(), new MoveBaseGoal());
    }

    public MoveBaseActionGoal(Header header, GoalID goalID, MoveBaseGoal goal) {
        this.header = header;
        this.goalID = goalID;
        this.goal = goal;

        JsonObject json = jsonBuilder()
                .put(FIELD_HEADER, header.getJsonObject())
                .put(FIELD_GOAL_ID, goalID.getJsonObject())
                .put(FIELD_GOAL, goal.getJsonObject());
        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static MoveBaseActionGoal fromJsonString(String jsonString) {
        return MoveBaseActionGoal.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static MoveBaseActionGoal fromMessage(RosMessage m) {
        return MoveBaseActionGoal.fromJsonObject(m.getJsonObject());
    }

    public static MoveBaseActionGoal fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        GoalID goalID = jsonObject.containsKey(FIELD_GOAL_ID) ? GoalID.fromJsonObject(jsonObject.getJsonObject(FIELD_GOAL_ID)) : new GoalID();
        MoveBaseGoal goal = jsonObject.containsKey(FIELD_GOAL) ? MoveBaseGoal.fromJsonObject(jsonObject.getJsonObject(FIELD_GOAL)) : new MoveBaseGoal();

        return new MoveBaseActionGoal(header, goalID, goal);
    }

    public GoalID getGoalID() {
        return goalID;
    }

    public Header getHeader() {
        return header;
    }

    public MoveBaseGoal getGoal() {
        return goal;
    }

    @Override
    public MoveBaseActionGoal clone() {
        return new MoveBaseActionGoal(this.header, this.goalID, this.goal);
    }
}
