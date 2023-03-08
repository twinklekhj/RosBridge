package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONObject;

public class Duration extends RosMessage {
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Duration";

    private final Duration data;

    public Duration() {
        this(new Duration());
    }

    /**
     * Create a new Duration with the given duration primitive.
     *
     * @param data The data value of this duration.
     */
    public Duration(Duration data) {
        // build the JSON object
        super(jsonBuilder().put(Duration.FIELD_DATA, data.getJsonObject()), Duration.TYPE);
        this.data = data;
    }

    public static Duration fromJsonString(String jsonString) {
        return Duration.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Duration fromMessage(RosMessage m) {
        return Duration.fromJSONObject(m.getJsonObject());
    }

    public static Duration fromJSONObject(JSONObject jsonObject) {
        Duration data = jsonObject.has(Duration.FIELD_DATA) ? Duration.fromJSONObject(jsonObject.getJSONObject(Duration.FIELD_DATA)) : new Duration();
        return new Duration(data);
    }

    public Duration getData() {
        return this.data;
    }

    @Override
    public Duration clone() {
        // duration objects are mutable, create a clone
        return new Duration(this.data.clone());
    }
}
