package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONObject;

public class RosString extends RosMessage {
    public static final java.lang.String FIELD_DATA = "data";
    public static final java.lang.String TYPE = "std_msgs/String";
    private final String data;

    public RosString() {
        this("");
    }

    public RosString(java.lang.String data) {
        // build the JSON object
        super(jsonBuilder().put(FIELD_DATA, data), RosString.TYPE);
        this.data = data;
    }

    public static RosString fromJsonString(java.lang.String jsonString) {
        // convert to a message
        return RosString.fromMessage(new RosMessage(jsonString));
    }

    public static RosString fromMessage(RosMessage m) {
        return RosString.fromJSONObject(m.toJSONObject());
    }

    public static RosString fromJSONObject(JSONObject jsonObject) {
        java.lang.String data = jsonObject.has(RosString.FIELD_DATA) ? jsonObject.getString(RosString.FIELD_DATA) : "";
        return new RosString(data);
    }

    public java.lang.String getData() {
        return this.data;
    }

    /**
     * Create a clone of this String.
     */
    @Override
    public RosString clone() {
        return new RosString(this.data);
    }
}
