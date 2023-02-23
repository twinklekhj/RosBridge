package ros.type.std;

import org.json.JSONObject;
import ros.type.RosMessage;

public class Int32 extends RosMessage {
    public static final java.lang.String FIELD_DATA = "data";

    public static final java.lang.String TYPE = "std_msgs/Int32";

    private final int data;

    /**
     * Create a new Int32 with a default of 0.
     */
    public Int32() {
        this(0);
    }

    /**
     * Create a new Int32 with the given data value.
     *
     * @param data The data value of the int.
     */
    public Int32(int data) {
        // build the JSON object
        super(builder().put(Int32.FIELD_DATA, data), Int32.TYPE);
        this.data = data;
    }

    public static Int32 fromJsonString(java.lang.String jsonString) {
        return Int32.fromMessage(new RosMessage(jsonString));
    }

    public static Int32 fromMessage(RosMessage m) {
        return Int32.fromJSONObject(m.toJSONObject());
    }

    public static Int32 fromJSONObject(JSONObject jsonObject) {
        // check the fields
        int data = jsonObject.has(Int32.FIELD_DATA) ? jsonObject.getInt(Int32.FIELD_DATA) : 0;
        return new Int32(data);
    }

    public int getData() {
        return this.data;
    }

    @Override
    public Int32 clone() {
        return new Int32(this.data);
    }
}
