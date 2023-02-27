package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONObject;

public class Float32 extends RosMessage {
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Float32";

    private final float data;

    /**
     * Create a new Float32 with a default of 0.
     */
    public Float32() {
        this(0f);
    }

    /**
     * Create a new Float32 with the given data value.
     *
     * @param data The data value of the float.
     */
    public Float32(float data) {
        super(jsonBuilder().put(Float32.FIELD_DATA, data), Float32.TYPE);
        this.data = data;
    }

    public static Float32 fromJsonString(String jsonString) {
        return Float32.fromMessage(new RosMessage(jsonString));
    }

    public static Float32 fromMessage(RosMessage m) {
        return Float32.fromJSONObject(m.toJSONObject());
    }

    public static Float32 fromJSONObject(JSONObject jsonObject) {
        float data = jsonObject.has(Float32.FIELD_DATA) ? (float) jsonObject.getDouble(Float32.FIELD_DATA) : 0f;
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
