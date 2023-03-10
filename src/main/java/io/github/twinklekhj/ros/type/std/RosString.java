package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;


public class RosString extends RosMessage {
    public static final java.lang.String FIELD_DATA = "data";
    public static final java.lang.String TYPE = "std_msgs/String";
    private final String data;

    public RosString() {
        this("");
    }

    public RosString(java.lang.String data) {
        super(jsonBuilder().put(FIELD_DATA, data), RosString.TYPE);
        this.data = data;
    }

    public static RosString fromJsonString(java.lang.String jsonString) {
        return RosString.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static RosString fromMessage(RosMessage m) {
        return RosString.fromJsonObject(m.getJsonObject());
    }

    public static RosString fromJsonObject(JsonObject jsonObject) {
        java.lang.String data = jsonObject.containsKey(RosString.FIELD_DATA) ? jsonObject.getString(RosString.FIELD_DATA) : "";
        return new RosString(data);
    }

    public java.lang.String getData() {
        return this.data;
    }

    @Override
    public RosString clone() {
        return new RosString(this.data);
    }
}
