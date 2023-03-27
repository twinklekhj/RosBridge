package io.github.twinklekhj.ros.type.actionlib;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class GoalID extends RosMessage {
    public static final String TYPE = "actionlib_msgs/GoalID";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_ID = "id";

    private Header header;
    private String id;

    public GoalID() {
        this(new Header(), "");
    }

    public GoalID(String id) {
        this(new Header(), id);
    }

    public GoalID(Header header, String id) {
        this.header = header;
        this.id = id;

        JsonObject json = jsonBuilder()
                .put(FIELD_HEADER, header.getJsonObject())
                .put(FIELD_ID, id);

        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static GoalID fromJsonString(String jsonString) {
        return GoalID.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static GoalID fromMessage(RosMessage m) {
        return GoalID.fromJsonObject(m.getJsonObject());
    }

    public static GoalID fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        String id = jsonObject.containsKey(FIELD_ID) ? jsonObject.getString(FIELD_ID) : "";

        return new GoalID(header, id);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.jsonObject.put(FIELD_ID, id);
    }

    @Override
    public GoalID clone() {
        return new GoalID(this.header, this.id);
    }
}
