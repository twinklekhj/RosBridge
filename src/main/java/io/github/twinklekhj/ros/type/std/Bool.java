package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;


public class Bool extends RosMessage {
    public static final java.lang.String FIELD_DATA = "data";
    public static final String TYPE = "std_msgs/Bool";

    private final boolean data;

    public Bool() {
        this(false);
    }

    public Bool(boolean data) {
        // build the JSON object
        super(jsonBuilder().put(FIELD_DATA, data), Bool.TYPE);
        this.data = data;
    }

    public static Bool fromJsonString(String jsonString) {
        return Bool.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Bool fromMessage(RosMessage m) {
        return Bool.fromJsonObject(m.getJsonObject());
    }

    public static Bool fromJsonObject(JsonObject jsonObject) {
        boolean data = jsonObject.containsKey(Bool.FIELD_DATA) && jsonObject.getBoolean(Bool.FIELD_DATA);
        return new Bool(data);
    }

    public boolean getData() {
        return this.data;
    }

    @Override
    public Bool clone() {
        return new Bool(this.data);
    }
}
