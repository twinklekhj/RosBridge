package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Float32 extends RosMessage {
    public static final String TYPE = "std_msgs/Float32";
    public static final String FIELD_DATA = "data";

    private final float data;

    public Float32() {
        this(0f);
    }

    public Float32(float data) {
        super(jsonBuilder().put(FIELD_DATA, data), Float32.TYPE);
        this.data = data;
    }

    public static Float32 fromJsonString(String jsonString) {
        return Float32.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Float32 fromMessage(RosMessage m) {
        return Float32.fromJsonObject(m.getJsonObject());
    }

    public static Float32 fromJsonObject(JsonObject jsonObject) {
        float data = jsonObject.containsKey(FIELD_DATA) ? jsonObject.getDouble(FIELD_DATA).floatValue() : 0f;
        return new Float32(data);
    }

    public float getData() {
        return this.data;
    }

    @Override
    public Float32 clone() {
        return new Float32(this.data);
    }
}
