package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;


public class Time extends RosMessage {
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Time";

    private final Time data;

    public Time() {
        this(new Time());
    }

    public Time(Time data) {
        super(jsonBuilder().put(Time.FIELD_DATA, data.getJsonObject()), Time.TYPE);
        this.data = data;
    }

    public static Time fromJsonString(String jsonString) {
        return Time.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Time fromMessage(RosMessage m) {
        return Time.fromJsonObject(m.getJsonObject());
    }

    public static Time fromJsonObject(JsonObject jsonObject) {
        Time data = jsonObject.containsKey(Time.FIELD_DATA) ? Time.fromJsonObject(jsonObject) : new Time();
        return new Time(data);
    }

    public Time getData() {
        return this.data;
    }

    @Override
    public Time clone() {
        return new Time(this.data.clone());
    }
}
