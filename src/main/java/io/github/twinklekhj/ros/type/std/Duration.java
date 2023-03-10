package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;


public class Duration extends RosMessage {
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Duration";

    private final Duration data;

    public Duration() {
        this(new Duration());
    }

    public Duration(Duration data) {
        super(jsonBuilder().put(Duration.FIELD_DATA, data.getJsonObject()), Duration.TYPE);
        this.data = data;
    }

    public static Duration fromJsonString(String jsonString) {
        return Duration.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Duration fromMessage(RosMessage m) {
        return Duration.fromJsonObject(m.getJsonObject());
    }

    public static Duration fromJsonObject(JsonObject jsonObject) {
        Duration data = jsonObject.containsKey(Duration.FIELD_DATA) ? Duration.fromJsonObject(jsonObject.getJsonObject(Duration.FIELD_DATA)) : new Duration();
        return new Duration(data);
    }

    public Duration getData() {
        return this.data;
    }

    @Override
    public Duration clone() {
        return new Duration(this.data.clone());
    }
}
