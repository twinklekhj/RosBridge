package io.github.twinklekhj.ros.type.std;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import org.json.JSONObject;

public class UInt32 extends RosMessage {
    public static final String FIELD_DATA = "data";
    public static final String TYPE = "std_msgs/UInt32";

    private final int data;

    /**
     * Create a new UInt32 with a default of 0.
     */
    public UInt32() {
        this(0);
    }

    /**
     * Create a new UInt32 with the given data value treated as a 32-bit
     * unsigned integer.
     *
     * @param data The data value of the int.
     */
    public UInt32(int data) {
        // build the JSON object
        super(jsonBuilder().put(UInt32.FIELD_DATA, Primitive.fromUInt32(data)), UInt32.TYPE);
        this.data = data;
    }

    public static UInt32 fromJsonString(String jsonString) {
        return UInt32.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static UInt32 fromMessage(RosMessage m) {
        return UInt32.fromJSONObject(m.getJsonObject());
    }

    public static UInt32 fromJSONObject(JSONObject jsonObject) {
        int data = jsonObject.has(UInt32.FIELD_DATA) ? Primitive.toUInt32(jsonObject.getLong(UInt32.FIELD_DATA)) : 0;
        return new UInt32(data);
    }

    public int getData() {
        return this.data;
    }

    @Override
    public UInt32 clone() {
        return new UInt32(this.data);
    }
}
