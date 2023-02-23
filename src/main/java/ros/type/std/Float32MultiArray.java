package ros.type.std;

import org.json.JSONArray;
import org.json.JSONObject;
import ros.type.RosMessage;

import java.util.Arrays;

public class Float32MultiArray extends RosMessage {
    public static final java.lang.String FIELD_LAYOUT = "layout";
    public static final java.lang.String FIELD_DATA = "data";

    public static final java.lang.String TYPE = "std_msgs/Float32MultiArray";

    private final MultiArrayLayout layout;
    private final float[] data;

    /**
     * Create a new, empty Float32MultiArray.
     */
    public Float32MultiArray() {
        this(new MultiArrayLayout(), new float[]{});
    }

    /**
     * Create a new Float32MultiArray with the given layout and data. The array
     * of data will be copied into this object.
     *
     * @param layout The specification of data layout.
     * @param data   The array of data.
     */
    public Float32MultiArray(MultiArrayLayout layout, float[] data) {
        // build the JSON object
        super(builder()
                .put(Float32MultiArray.FIELD_LAYOUT, layout.toJSONObject())
                .put(Float32MultiArray.FIELD_DATA, builder(Arrays.toString(data))), Float32MultiArray.TYPE);
        this.layout = layout;
        // copy the array
        this.data = new float[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static Float32MultiArray fromJsonString(java.lang.String jsonString) {
        return Float32MultiArray.fromMessage(new RosMessage(jsonString));
    }

    public static Float32MultiArray fromMessage(RosMessage m) {
        return Float32MultiArray.fromJSONObject(m.toJSONObject());
    }

    public static Float32MultiArray fromJSONObject(JSONObject jsonObject) {
        // check the layout
        MultiArrayLayout layout = jsonObject
                .has(Float32MultiArray.FIELD_LAYOUT) ? MultiArrayLayout
                .fromJSONObject(jsonObject
                        .getJSONObject(Float32MultiArray.FIELD_LAYOUT))
                : new MultiArrayLayout();

        // check the array
        float[] data = new float[]{};
        JSONArray jsonData = jsonObject
                .getJSONArray(Float32MultiArray.FIELD_DATA);
        if (jsonData != null) {
            // convert each data
            data = new float[jsonData.length()];
            for (int i = 0; i < data.length; i++) {
                data[i] = (float) jsonData.getDouble(i);
            }
        }
        return new Float32MultiArray(layout, data);
    }

    public MultiArrayLayout getLayout() {
        return this.layout;
    }

    public int size() {
        return this.data.length;
    }

    public float get(int index) {
        return this.data[index];
    }

    public float[] getData() {
        return this.data;
    }

    @Override
    public Float32MultiArray clone() {
        return new Float32MultiArray(this.layout, this.data);
    }
}
