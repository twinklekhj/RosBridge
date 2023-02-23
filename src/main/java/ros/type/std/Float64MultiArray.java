package ros.type.std;

import org.json.JSONArray;
import org.json.JSONObject;
import ros.type.RosMessage;

import java.util.Arrays;

public class Float64MultiArray extends RosMessage {
    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Float64MultiArray";

    private final MultiArrayLayout layout;
    private final double[] data;

    /**
     * Create a new, empty Float64MultiArray.
     */
    public Float64MultiArray() {
        this(new MultiArrayLayout(), new double[]{});
    }

    /**
     * Create a new Float64MultiArray with the given layout and data. The array
     * of data will be copied into this object.
     *
     * @param layout The specification of data layout.
     * @param data   The array of data.
     */
    public Float64MultiArray(MultiArrayLayout layout, double[] data) {
        // build the JSON object
        super(builder().put(Float64MultiArray.FIELD_LAYOUT, layout.toJSONObject()).put(Float64MultiArray.FIELD_DATA, builder(Arrays.toString(data))), Float64MultiArray.TYPE);
        this.layout = layout;
        // copy the array
        this.data = new double[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static Float64MultiArray fromJsonString(String jsonString) {
        return Float64MultiArray.fromMessage(new RosMessage(jsonString));
    }

    public static Float64MultiArray fromMessage(RosMessage m) {
        return Float64MultiArray.fromJSONObject(m.toJSONObject());
    }

    public static Float64MultiArray fromJSONObject(JSONObject jsonObject) {
        // check the layout
        MultiArrayLayout layout = jsonObject.has(Float64MultiArray.FIELD_LAYOUT) ? MultiArrayLayout.fromJSONObject(jsonObject.getJSONObject(Float64MultiArray.FIELD_LAYOUT)) : new MultiArrayLayout();

        // check the array
        double[] data = new double[]{};
        JSONArray jsonData = jsonObject.getJSONArray(Float64MultiArray.FIELD_DATA);
        if (jsonData != null) {
            // convert each data
            data = new double[jsonData.length()];
            for (int i = 0; i < data.length; i++) {
                data[i] = jsonData.getDouble(i);
            }
        }
        return new Float64MultiArray(layout, data);
    }

    public MultiArrayLayout getLayout() {
        return this.layout;
    }

    public int size() {
        return this.data.length;
    }

    public double get(int index) {
        return this.data[index];
    }

    public double[] getData() {
        return this.data;
    }

    @Override
    public Float64MultiArray clone() {
        return new Float64MultiArray(this.layout, this.data);
    }
}
