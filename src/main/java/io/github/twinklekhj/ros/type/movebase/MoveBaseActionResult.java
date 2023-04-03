package io.github.twinklekhj.ros.type.movebase;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.actionlib.GoalStatus;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class MoveBaseActionResult extends RosMessage {
    public static final String TYPE = "move_base_msgs/MoveBaseActionResult";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_GOAL_STATUS = "status";
    public static final String FIELD_RESULT = "result";

    private final Header header;
    private final GoalStatus status;
    private final MoveBaseResult result;

    public MoveBaseActionResult() {
        this(new Header(), new GoalStatus(), new MoveBaseResult());
    }

    public MoveBaseActionResult(Header header, GoalStatus status, MoveBaseResult result) {
        this.header = header;
        this.status = status;
        this.result = result;

        JsonObject json = jsonBuilder()
                .put(FIELD_HEADER, header.getJsonObject())
                .put(FIELD_GOAL_STATUS, status.getJsonObject())
                .put(FIELD_RESULT, result.getJsonObject());
        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static MoveBaseActionResult fromJsonString(String jsonString) {
        return MoveBaseActionResult.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static MoveBaseActionResult fromMessage(RosMessage m) {
        return MoveBaseActionResult.fromJsonObject(m.getJsonObject());
    }

    public static MoveBaseActionResult fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        GoalStatus status = jsonObject.containsKey(FIELD_GOAL_STATUS) ? GoalStatus.fromJsonObject(jsonObject.getJsonObject(FIELD_GOAL_STATUS)) : new GoalStatus();
        MoveBaseResult result = jsonObject.containsKey(FIELD_RESULT) ? MoveBaseResult.fromJsonObject(jsonObject.getJsonObject(FIELD_RESULT)) : new MoveBaseResult();

        return new MoveBaseActionResult(header, status, result);
    }

    public Header getHeader() {
        return header;
    }

    public GoalStatus getStatus() {
        return status;
    }

    public MoveBaseResult getResult() {
        return result;
    }

    @Override
    public MoveBaseActionResult clone() {
        return new MoveBaseActionResult(this.header, this.status, this.result);
    }
}
