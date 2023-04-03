package io.github.twinklekhj.ros.type.movebase;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.actionlib.GoalStatus;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class MoveBaseActionFeedback extends RosMessage {
    public static final String TYPE = "move_base_msgs/MoveBaseActionFeedback";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_GOAL_STATUS = "status";
    public static final String FIELD_FEEDBACK = "feedback";

    private final Header header;
    private final GoalStatus status;
    private final MoveBaseFeedback feedback;

    public MoveBaseActionFeedback() {
        this(new Header(), new GoalStatus(), new MoveBaseFeedback());
    }

    public MoveBaseActionFeedback(Header header, GoalStatus status, MoveBaseFeedback feedback) {
        this.header = header;
        this.status = status;
        this.feedback = feedback;

        JsonObject json = jsonBuilder()
                .put(FIELD_HEADER, header.getJsonObject())
                .put(FIELD_GOAL_STATUS, status.getJsonObject())
                .put(FIELD_FEEDBACK, feedback.getJsonObject());
        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static MoveBaseActionFeedback fromJsonString(String jsonString) {
        return MoveBaseActionFeedback.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static MoveBaseActionFeedback fromMessage(RosMessage m) {
        return MoveBaseActionFeedback.fromJsonObject(m.getJsonObject());
    }

    public static MoveBaseActionFeedback fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        GoalStatus status = jsonObject.containsKey(FIELD_GOAL_STATUS) ? GoalStatus.fromJsonObject(jsonObject.getJsonObject(FIELD_GOAL_STATUS)) : new GoalStatus();
        MoveBaseFeedback feedback = jsonObject.containsKey(FIELD_FEEDBACK) ? MoveBaseFeedback.fromJsonObject(jsonObject.getJsonObject(FIELD_FEEDBACK)) : new MoveBaseFeedback();

        return new MoveBaseActionFeedback(header, status, feedback);
    }

    public Header getHeader() {
        return header;
    }

    public GoalStatus getStatus() {
        return status;
    }

    public MoveBaseFeedback getFeedback() {
        return feedback;
    }

    @Override
    public MoveBaseActionFeedback clone() {
        return new MoveBaseActionFeedback(this.header, this.status, this.feedback);
    }
}
