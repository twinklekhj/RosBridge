package ros.type.std;

import org.json.JSONObject;
import ros.type.RosMessage;

public class Bool extends RosMessage {
    public static final java.lang.String FIELD_DATA = "data";
    public static final String TYPE = "std_msgs/Bool";

    private final boolean data;

    /**
     * Create a new Bool with a default of false.
     */
    public Bool() {
        this(false);
    }

    /**
     * Create a new Bool with the given data value.
     *
     * @param data The data value of the boolean.
     */
    public Bool(boolean data) {
        // build the JSON object
        super(builder().put(FIELD_DATA, data), Bool.TYPE);
        this.data = data;
    }

    public static Bool fromJsonString(String jsonString) {
        return Bool.fromMessage(new RosMessage(jsonString));
    }

    public static Bool fromMessage(RosMessage m) {
        return Bool.fromJSONObject(m.toJSONObject());
    }

    public static Bool fromJSONObject(JSONObject jsonObject) {
        boolean data = jsonObject.has(Bool.FIELD_DATA) && jsonObject.getBoolean(Bool.FIELD_DATA);
        return new Bool(data);
    }

    public boolean getData() {
        return this.data;
    }

    @Override
    public Bool clone() {
        return new Bool(this.data);
    }
}
