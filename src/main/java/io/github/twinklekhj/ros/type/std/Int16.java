package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONObject;

public class Int16 extends RosMessage {
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Int16";

    private final short data;

    /**
     * Create a new Int16 with a default of 0.
     */
    public Int16() {
        this((short) 0);
    }

    /**
     * Create a new Int16 with the given data value.
     *
     * @param data The data value of the short.
     */
    public Int16(short data) {
        super(jsonBuilder().put(Int16.FIELD_DATA, data), Int16.TYPE);
        this.data = data;
    }

    public static Int16 fromJsonString(String jsonString) {
        return Int16.fromMessage(new RosMessage(jsonString));
    }

    public static Int16 fromMessage(RosMessage m) {
        return Int16.fromJSONObject(m.toJSONObject());
    }

    public static Int16 fromJSONObject(JSONObject jsonObject) {
        // check the fields
        short data = jsonObject.has(Int16.FIELD_DATA) ? (short) jsonObject.getInt(Int16.FIELD_DATA) : 0;
        return new Int16(data);
    }

    public short getData() {
        return this.data;
    }

    @Override
    public Int16 clone() {
        return new Int16(this.data);
    }
}
