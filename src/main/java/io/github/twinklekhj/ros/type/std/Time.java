package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Time extends RosMessage {
    public static final String TYPE = "std_msgs/Time";
    public static final String FIELD_DATA = "data";

    private final io.github.twinklekhj.ros.type.primitives.Time data;

    public Time() {
        this(new io.github.twinklekhj.ros.type.primitives.Time());
    }

    public Time(io.github.twinklekhj.ros.type.primitives.Time data) {
        this.data = data;
        super.setJsonObject(jsonBuilder().put(FIELD_DATA, data.toJsonObject()));
        super.setType(TYPE);
    }

    public static Time fromJsonString(String jsonString) {
        return Time.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Time fromMessage(RosMessage m) {
        return Time.fromJsonObject(m.getJsonObject());
    }

    public static Time fromJsonObject(JsonObject jsonObject) {
        io.github.twinklekhj.ros.type.primitives.Time data = jsonObject.containsKey(FIELD_DATA) ?
                io.github.twinklekhj.ros.type.primitives.Time.fromJsonObject(jsonObject) : new io.github.twinklekhj.ros.type.primitives.Time();
        return new Time(data);
    }

    public io.github.twinklekhj.ros.type.primitives.Time getData() {
        return this.data;
    }

    @Override
    public Time clone() {
        return new Time(this.data.clone());
    }
}
