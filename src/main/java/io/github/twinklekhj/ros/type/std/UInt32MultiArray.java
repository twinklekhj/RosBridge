package io.github.twinklekhj.ros.type.std;

import org.json.JSONArray;
import org.json.JSONObject;
import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;

import java.util.Arrays;

public class UInt32MultiArray extends RosMessage {
    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/UInt32MultiArray";

    private final MultiArrayLayout layout;
    private final int[] data;

    /**
     * Create a new, empty UInt32MultiArray.
     */
    public UInt32MultiArray() {
        this(new MultiArrayLayout(), new int[]{});
    }

    /**
     * Create a new UInt32MultiArray with the given layout and data. The array
     * of data will be copied into this object. All values will be treated as an
     * unsigned representation.
     *
     * @param layout The specification of data layout.
     * @param data   The array of data.
     */
    public UInt32MultiArray(MultiArrayLayout layout, int[] data) {
        // build the JSON object
        super(jsonBuilder().put(UInt32MultiArray.FIELD_LAYOUT, layout.toJSONObject()).put(UInt32MultiArray.FIELD_DATA, jsonBuilder(Arrays.toString(Primitive.fromUInt32(data)))), UInt32MultiArray.TYPE);

        this.layout = layout;
        // copy the array
        this.data = new int[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static UInt32MultiArray fromJsonString(String jsonString) {
        return UInt32MultiArray.fromMessage(new RosMessage(jsonString));
    }

    public static UInt32MultiArray fromMessage(RosMessage m) {
        return UInt32MultiArray.fromJSONObject(m.toJSONObject());
    }

    public static UInt32MultiArray fromJSONObject(JSONObject jsonObject) {
        // check the layout
        MultiArrayLayout layout = jsonObject.has(UInt32MultiArray.FIELD_LAYOUT) ? MultiArrayLayout.fromJSONObject(jsonObject.getJSONObject(UInt32MultiArray.FIELD_LAYOUT)) : new MultiArrayLayout();

        // check the array
        int[] data = new int[]{};
        JSONArray jsonData = jsonObject.getJSONArray(UInt32MultiArray.FIELD_DATA);
        if (jsonData != null) {
            // convert each data
            data = new int[jsonData.length()];
            for (int i = 0; i < data.length; i++) {
                data[i] = Primitive.toUInt32(jsonData.getLong(i));
            }
        }
        return new UInt32MultiArray(layout, data);
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
    public UInt32MultiArray clone() {
        return new UInt32MultiArray(this.layout, this.data);
    }
}
