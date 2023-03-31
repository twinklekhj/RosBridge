package io.github.twinklekhj.ros.type.actionlib;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@ToString
public class GoalStatusArray extends RosMessage {
    public static final String TYPE = "actionlib_msgs/GoalStatusArray";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_STATUS_LIST = "status_list";

    private Header header;
    private GoalStatus[] status_list;

    public GoalStatusArray() {
        this(new Header());
    }

    public GoalStatusArray(GoalStatus... status_list) {
        this(new Header(), status_list);
    }

    public GoalStatusArray(Header header, GoalStatus... status_list) {
        this.header = header;
        this.status_list = new GoalStatus[status_list.length];
        System.arraycopy(status_list, 0, this.status_list, 0, status_list.length);

        JsonObject json = jsonBuilder()
                .put(FIELD_HEADER, header.getJsonObject())
                .put(FIELD_STATUS_LIST, jsonBuilder(Arrays.deepToString(status_list)));

        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static GoalStatusArray fromJsonString(String jsonString) {
        return GoalStatusArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static GoalStatusArray fromMessage(RosMessage m) {
        return GoalStatusArray.fromJsonObject(m.getJsonObject());
    }

    public static GoalStatusArray fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        JsonArray jsonCells = jsonObject.getJsonArray(FIELD_STATUS_LIST);
        GoalStatus[] status_list = {};
        if (jsonCells != null) {
            status_list = new GoalStatus[jsonCells.size()];
            for (int i = 0; i < status_list.length; i++) {
                status_list[i] = GoalStatus.fromJsonObject(jsonCells.getJsonObject(i));
            }
        }

        return new GoalStatusArray(header, status_list);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    public GoalStatus[] getGoalStatus() {
        return status_list;
    }

    public void setGoalStatus(GoalStatus... status_list) {
        this.status_list = new GoalStatus[status_list.length];
        System.arraycopy(status_list, 0, this.status_list, 0, status_list.length);

        this.jsonObject.put(FIELD_STATUS_LIST, jsonBuilder(Arrays.deepToString(status_list)));
    }

    public void setGoalStatus(List<GoalStatus> status_list) {
        this.status_list = new GoalStatus[status_list.size()];
        for (int i = 0; i < status_list.size(); i++) {
            this.status_list[i] = status_list.get(0);
        }

        this.jsonObject.put(FIELD_STATUS_LIST, jsonBuilder(Arrays.deepToString(this.status_list)));
    }

    @Override
    public GoalStatusArray clone() {
        return new GoalStatusArray(this.header, this.status_list);
    }
}
