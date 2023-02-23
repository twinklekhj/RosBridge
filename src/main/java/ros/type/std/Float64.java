package ros.type.std;

import org.json.JSONObject;
import ros.type.RosMessage;

public class Float64 extends RosMessage {
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Float64";

    private final double data;

    /**
     * Create a new Float64 with a default of 0.
     */
    public Float64() {
        this(0);
    }

    /**
     * Create a new Float64 with the given data value.
     *
     * @param data The data value of the double.
     */
    public Float64(double data) {
        // build the JSON object
        super(builder().put(Float64.FIELD_DATA, data), Float64.TYPE);
        this.data = data;
    }

    public static Float64 fromJsonString(String jsonString) {
        return Float64.fromMessage(new RosMessage(jsonString));
    }

    public static Float64 fromMessage(RosMessage m) {
        // get it from the JSON object
        return Float64.fromJSONObject(m.toJSONObject());
    }

    public static Float64 fromJSONObject(JSONObject jsonObject) {
        // check the fields
        double data = jsonObject.has(Float64.FIELD_DATA) ? jsonObject.getDouble(Float64.FIELD_DATA) : 0;
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
