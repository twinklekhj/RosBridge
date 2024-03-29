package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Float64 extends RosMessage {
    public static final String TYPE = "std_msgs/Float64";
    public static final String FIELD_DATA = "data";

    private final double data;

    public Float64() {
        this(0);
    }

    public Float64(double data) {
        super(jsonBuilder().put(FIELD_DATA, data), Float64.TYPE);
        this.data = data;
    }

    public static Float64 fromJsonString(String jsonString) {
        return Float64.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Float64 fromMessage(RosMessage m) {
        return Float64.fromJsonObject(m.getJsonObject());
    }

    public static Float64 fromJsonObject(JsonObject jsonObject) {
        double data = jsonObject.containsKey(FIELD_DATA) ? jsonObject.getDouble(FIELD_DATA) : 0;
        return new Float64(data);
    }

    public double getData() {
        return this.data;
    }

    @Override
    public Float64 clone() {
        return new Float64(this.data);
    }
}
