package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import org.json.JSONObject;

public class UInt16 extends RosMessage {
    public static final java.lang.String FIELD_DATA = "data";

    public static final java.lang.String TYPE = "std_msgs/UInt16";

    private final short data;

    /**
     * Create a new UInt16 with a default of 0.
     */
    public UInt16() {
        this((short) 0);
    }

    /**
     * Create a new UInt16 with the given data value treated as a 16-bit
     * unsigned integer.
     *
     * @param data The data value of the short.
     */
    public UInt16(short data) {
        // build the JSON object
        super(builder().put(UInt16.FIELD_DATA, Primitive.fromUInt16(data)), UInt16.TYPE);
        this.data = data;
    }

    public static UInt16 fromJsonString(java.lang.String jsonString) {
        return UInt16.fromMessage(new RosMessage(jsonString));
    }

    public static UInt16 fromMessage(RosMessage m) {
        // get it from the JSON object
        return UInt16.fromJSONObject(m.toJSONObject());
    }

    public static UInt16 fromJSONObject(JSONObject jsonObject) {
        // check the fields
        short data = jsonObject.has(UInt16.FIELD_DATA) ? Primitive.toUInt16(jsonObject.getInt(UInt16.FIELD_DATA)) : 0;
        return new UInt16(data);
    }

    public short getData() {
        return this.data;
    }

    @Override
    public UInt16 clone() {
        return new UInt16(this.data);
    }
}
