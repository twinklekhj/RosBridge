package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class Int16MultiArray extends RosMessage {
    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Int16MultiArray";

    private final MultiArrayLayout layout;
    private final short[] data;

    /**
     * Create a new, empty Int16MultiArray.
     */
    public Int16MultiArray() {
        this(new MultiArrayLayout(), new short[]{});
    }

    /**
     * Create a new Int16MultiArray with the given layout and data. The array of
     * data will be copied into this object.
     *
     * @param layout The specification of data layout.
     * @param data   The array of data.
     */
    public Int16MultiArray(MultiArrayLayout layout, short[] data) {
        // build the JSON object
        super(jsonBuilder()
                .put(Int16MultiArray.FIELD_LAYOUT, layout.getJsonObject())
                .put(Int16MultiArray.FIELD_DATA, jsonBuilder(Arrays.toString(data))), Int16MultiArray.TYPE);
        this.layout = layout;
        // copy the array
        this.data = new short[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static Int16MultiArray fromJsonString(String jsonString) {
        return Int16MultiArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Int16MultiArray fromMessage(RosMessage m) {
        return Int16MultiArray.fromJSONObject(m.getJsonObject());
    }

    public static Int16MultiArray fromJSONObject(JSONObject jsonObject) {
        // check the layout
        MultiArrayLayout layout = jsonObject.has(Int16MultiArray.FIELD_LAYOUT) ? MultiArrayLayout.fromJSONObject(jsonObject.getJSONObject(Int16MultiArray.FIELD_LAYOUT)) : new MultiArrayLayout();

        // check the array
        short[] data = new short[]{};
        JSONArray jsonData = jsonObject.getJSONArray(Int16MultiArray.FIELD_DATA);
        if (jsonData != null) {
            // convert each data
            data = new short[jsonData.length()];
            for (int i = 0; i < data.length; i++) {
                data[i] = (short) jsonData.getInt(i);
            }
        }
        return new Int16MultiArray(layout, data);
    }

    public MultiArrayLayout getLayout() {
        return this.layout;
    }

    public int size() {
        return this.data.length;
    }

    public short get(int index) {
        return this.data[index];
    }

    public short[] getData() {
        return this.data;
    }

    @Override
    public Int16MultiArray clone() {
        return new Int16MultiArray(this.layout, this.data);
    }
}
