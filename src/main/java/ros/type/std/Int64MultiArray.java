package ros.type.std;

import org.json.JSONArray;
import org.json.JSONObject;
import ros.type.RosMessage;

import java.util.Arrays;

public class Int64MultiArray extends RosMessage {
    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Int64MultiArray";

    private final MultiArrayLayout layout;
    private final long[] data;

    /**
     * Create a new, empty Int64MultiArray.
     */
    public Int64MultiArray() {
        this(new MultiArrayLayout(), new long[]{});
    }

    /**
     * Create a new Int64MultiArray with the given layout and data. The array of
     * data will be copied longo this object.
     *
     * @param layout The specification of data layout.
     * @param data   The array of data.
     */
    public Int64MultiArray(MultiArrayLayout layout, long[] data) {
        // build the JSON object
        super(builder().put(Int64MultiArray.FIELD_LAYOUT, layout.toJSONObject()).put(Int64MultiArray.FIELD_DATA, builder(Arrays.toString(data))), Int64MultiArray.TYPE);
        this.layout = layout;
        // copy the array
        this.data = new long[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static Int64MultiArray fromJsonString(String jsonString) {
        return Int64MultiArray.fromMessage(new RosMessage(jsonString));
    }

    public static Int64MultiArray fromMessage(RosMessage m) {
        return Int64MultiArray.fromJSONObject(m.toJSONObject());
    }

    public static Int64MultiArray fromJSONObject(JSONObject jsonObject) {
        // check the layout
        MultiArrayLayout layout = jsonObject.has(Int64MultiArray.FIELD_LAYOUT) ? MultiArrayLayout.fromJSONObject(jsonObject.getJSONObject(Int64MultiArray.FIELD_LAYOUT)) : new MultiArrayLayout();

        // check the array
        long[] data = new long[]{};
        JSONArray jsonData = jsonObject.getJSONArray(Int64MultiArray.FIELD_DATA);
        if (jsonData != null) {
            // convert each data
            data = new long[jsonData.length()];
            for (int i = 0; i < data.length; i++) {
                data[i] = jsonData.getLong(i);
            }
        }
        return new Int64MultiArray(layout, data);
    }

    public MultiArrayLayout getLayout() {
        return this.layout;
    }

    public int size() {
        return this.data.length;
    }

    public long get(int index) {
        return this.data[index];
    }

    public long[] getData() {
        return this.data;
    }

    @Override
    public Int64MultiArray clone() {
        return new Int64MultiArray(this.layout, this.data);
    }
}
