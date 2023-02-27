package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONObject;

public class Int64 extends RosMessage {
    public static final String FIELD_DATA = "data";
    public static final String TYPE = "std_msgs/Int64";

    private final long data;

    /**
     * Create a new Int64 with a default of 0.
     */
    public Int64() {
        this(0L);
    }

    /**
     * Create a new Int64 with the given data value.
     *
     * @param data The data value of the long.
     */
    public Int64(long data) {
        super(jsonBuilder().put(Int64.FIELD_DATA, data), Int64.TYPE);
        this.data = data;
    }

    public static Int64 fromJsonString(String jsonString) {
        return Int64.fromMessage(new RosMessage(jsonString));
    }

    public static Int64 fromMessage(RosMessage m) {
        return Int64.fromJSONObject(m.toJSONObject());
    }

    public static Int64 fromJSONObject(JSONObject jsonObject) {
        // check the fields
        long data = jsonObject.has(Int64.FIELD_DATA) ? jsonObject.getLong(Int64.FIELD_DATA) : 0L;
        return new Int64(data);
    }

    public long getData() {
        return this.data;
    }

    @Override
    public Int64 clone() {
        return new Int64(this.data);
    }
}
