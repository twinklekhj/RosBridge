package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class Int32MultiArray extends RosMessage {
    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Int32MultiArray";

    private final MultiArrayLayout layout;
    private final int[] data;

    /**
     * Create a new, empty Int32MultiArray.
     */
    public Int32MultiArray() {
        this(new MultiArrayLayout(), new int[]{});
    }

    /**
     * Create a new Int32MultiArray with the given layout and data. The array of
     * data will be copied into this object.
     *
     * @param layout The specification of data layout.
     * @param data   The array of data.
     */
    public Int32MultiArray(MultiArrayLayout layout, int[] data) {
        // build the JSON object
        super(jsonBuilder()
                .put(Int32MultiArray.FIELD_LAYOUT, layout.getJsonObject())
                .put(Int32MultiArray.FIELD_DATA, jsonBuilder(Arrays.toString(data))), Int32MultiArray.TYPE);
        this.layout = layout;
        // copy the array
        this.data = new int[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static Int32MultiArray fromJsonString(String jsonString) {
        return Int32MultiArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Int32MultiArray fromMessage(RosMessage m) {
        return Int32MultiArray.fromJSONObject(m.getJsonObject());
    }

    public static Int32MultiArray fromJSONObject(JSONObject jsonObject) {
        // check the layout
        MultiArrayLayout layout = jsonObject.has(Int32MultiArray.FIELD_LAYOUT) ? MultiArrayLayout.fromJSONObject(jsonObject.getJSONObject(Int32MultiArray.FIELD_LAYOUT)) : new MultiArrayLayout();

        // check the array
        int[] data = new int[]{};
        JSONArray jsonData = jsonObject.getJSONArray(Int32MultiArray.FIELD_DATA);
        if (jsonData != null) {
            // convert each data
            data = new int[jsonData.length()];
            for (int i = 0; i < data.length; i++) {
                data[i] = jsonData.getInt(i);
            }
        }
        return new Int32MultiArray(layout, data);
    }

    public MultiArrayLayout getLayout() {
        return this.layout;
    }

    public int size() {
        return this.data.length;
    }

    public int get(int index) {
        return this.data[index];
    }

    public int[] getData() {
        return this.data;
    }

    @Override
    public Int32MultiArray clone() {
        return new Int32MultiArray(this.layout, this.data);
    }
}
