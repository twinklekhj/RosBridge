package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONObject;
import io.github.twinklekhj.ros.type.primitives.Primitive;

public class UInt8 extends RosMessage {
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/UInt8";

    private final byte data;

    public UInt8() {
        this((byte) 0);
    }

    /**
     * Create a new UInt8 with the given data value treated as a 8-bit unsigned
     * integer.
     *
     * @param data The data value of the byte.
     */
    public UInt8(byte data) {
        // build the JSON object
        super(builder().put(UInt8.FIELD_DATA, Primitive.fromUInt8(data)), UInt8.TYPE);
        this.data = data;
    }

    public static UInt8 fromJsonString(String jsonString) {
        return UInt8.fromMessage(new RosMessage(jsonString));
    }

    public static UInt8 fromMessage(RosMessage m) {
        return UInt8.fromJSONObject(m.toJSONObject());
    }

    public static UInt8 fromJSONObject(JSONObject jsonObject) {
        byte data = jsonObject.has(UInt8.FIELD_DATA) ? Primitive.toUInt8((short) jsonObject.getInt(UInt8.FIELD_DATA)) : 0;
        return new UInt8(data);
    }

    public byte getData() {
        return this.data;
    }

    @Override
    public UInt8 clone() {
        return new UInt8(this.data);
    }
}
