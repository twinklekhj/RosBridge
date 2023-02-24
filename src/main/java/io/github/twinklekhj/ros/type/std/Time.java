package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONObject;

public class Time extends RosMessage {
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Time";

    private final Time data;

    public Time() {
        this(new Time());
    }

    public Time(Time data) {
        super(builder().put(Time.FIELD_DATA, data.toJSONObject()), Time.TYPE);
        this.data = data;
    }

    public static Time fromJsonString(String jsonString) {
        return Time.fromMessage(new RosMessage(jsonString));
    }

    public static Time fromMessage(RosMessage m) {
        return Time.fromJSONObject(m.toJSONObject());
    }

    public static Time fromJSONObject(JSONObject jsonObject) {
        Time data = jsonObject.has(Time.FIELD_DATA) ? Time.fromJSONObject(jsonObject) : new Time();
        return new Time(data);
    }

    public Time getData() {
        return this.data;
    }

    @Override
    public Time clone() {
        // time objects are mutable, create a clone
        return new Time(this.data.clone());
    }
}
