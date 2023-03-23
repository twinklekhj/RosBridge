package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class RosString extends RosMessage {
    public static final String TYPE = "std_msgs/String";
    public static final String FIELD_DATA = "data";

    private final String data;

    public RosString() {
        this("");
    }

    public RosString(String data) {
        this.data = data;
        super.setJsonObject(jsonBuilder().put(FIELD_DATA, data));
        super.setType(TYPE);
    }

    public static RosString fromJsonString(String jsonString) {
        return RosString.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static RosString fromMessage(RosMessage m) {
        return RosString.fromJsonObject(m.getJsonObject());
    }

    public static RosString fromJsonObject(JsonObject jsonObject) {
        String data = jsonObject.containsKey(FIELD_DATA) ? jsonObject.getString(FIELD_DATA) : "";
        return new RosString(data);
    }

    public String getData() {
        return this.data;
    }

    @Override
    public RosString clone() {
        return new RosString(this.data);
    }
}
