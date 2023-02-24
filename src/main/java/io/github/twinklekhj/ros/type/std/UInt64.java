package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import org.json.JSONObject;

public class UInt64 extends RosMessage {
    public static final String FIELD_DATA = "data";
    public static final String TYPE = "std_msgs/UInt64";

    private final long data;

    /**
     * Create a new UInt64 with a default of 0.
     */
    public UInt64() {
        this(0L);
    }

    /**
     * Create a new UInt64 with the given data value treated as a 64-bit
     * unsigned integer.
     *
     * @param data The data value of the long.
     */
    public UInt64(long data) {
        // build the JSON object
        super(builder().put(UInt64.FIELD_DATA, Primitive.fromUInt64(data)), UInt64.TYPE);
        this.data = data;
    }

    public static UInt64 fromJsonString(String jsonString) {
        return UInt64.fromMessage(new RosMessage(jsonString));
    }

    public static UInt64 fromMessage(RosMessage m) {
        return UInt64.fromJSONObject(m.toJSONObject());
    }

    public static UInt64 fromJSONObject(JSONObject jsonObject) {
        // check the fields
        long data = jsonObject.has(UInt64.FIELD_DATA) ? Primitive.toUInt64(jsonObject.getBigInteger(UInt64.FIELD_DATA)) : 0L;
        return new UInt64(data);
    }

    public long getData() {
        return this.data;
    }

    @Override
    public UInt64 clone() {
        return new UInt64(this.data);
    }
}
