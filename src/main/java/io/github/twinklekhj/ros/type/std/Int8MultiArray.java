package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class Int8MultiArray extends RosMessage {
    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Int8MultiArray";

    private final MultiArrayLayout layout;
    private final byte[] data;

    /**
     * Create a new, empty Int8MultiArray.
     */
    public Int8MultiArray() {
        this(new MultiArrayLayout(), new byte[]{});
    }

    /**
     * Create a new Int8MultiArray with the given layout and data. The array of
     * data will be copied into this object.
     *
     * @param layout The specification of data layout.
     * @param data   The array of data.
     */
    public Int8MultiArray(MultiArrayLayout layout, byte[] data) {
        // build the JSON object
        super(builder().put(Int8MultiArray.FIELD_LAYOUT, layout.toJSONObject()).put(Int8MultiArray.FIELD_DATA, builder(Arrays.toString(data))), Int8MultiArray.TYPE);
        this.layout = layout;
        // copy the array
        this.data = new byte[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static Int8MultiArray fromJsonString(String jsonString) {
        return Int8MultiArray.fromMessage(new RosMessage(jsonString));
    }

    public static Int8MultiArray fromMessage(RosMessage m) {
        return Int8MultiArray.fromJSONObject(m.toJSONObject());
    }

    public static Int8MultiArray fromJSONObject(JSONObject jsonObject) {
        // check the layout
        MultiArrayLayout layout = jsonObject.has(Int8MultiArray.FIELD_LAYOUT) ? MultiArrayLayout.fromJSONObject(jsonObject.getJSONObject(Int8MultiArray.FIELD_LAYOUT)) : new MultiArrayLayout();

        // check the array
        byte[] data = new byte[]{};
        JSONArray jsonData = jsonObject.getJSONArray(Int8MultiArray.FIELD_DATA);
        if (jsonData != null) {
            // convert each data
            data = new byte[jsonData.length()];
            for (int i = 0; i < data.length; i++) {
                data[i] = (byte) jsonData.getInt(i);
            }
        }
        return new Int8MultiArray(layout, data);
    }

    public MultiArrayLayout getLayout() {
        return this.layout;
    }

    public int size() {
        return this.data.length;
    }

    public byte get(int index) {
        return this.data[index];
    }

    public byte[] getData() {
        return this.data;
    }

    @Override
    public Int8MultiArray clone() {
        return new Int8MultiArray(this.layout, this.data);
    }
}
