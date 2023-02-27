package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class ByteMultiArray extends RosMessage {
    public static final java.lang.String FIELD_LAYOUT = "layout";
    public static final java.lang.String FIELD_DATA = "data";

    public static final java.lang.String TYPE = "std_msgs/ByteMultiArray";

    private final MultiArrayLayout layout;
    private final byte[] data;

    public ByteMultiArray() {
        this(new MultiArrayLayout(), new byte[]{});
    }

    /**
     * Create a new ByteMultiArray with the given layout and data. The array of
     * data will be copied into this object.
     *
     * @param layout The specification of data layout.
     * @param data   The array of data.
     */
    public ByteMultiArray(MultiArrayLayout layout, byte[] data) {
        // build the JSON object
        super(jsonBuilder()
                .put(ByteMultiArray.FIELD_LAYOUT, layout.toJSONObject())
                .put(ByteMultiArray.FIELD_DATA, jsonBuilder(Arrays.toString(data))), ByteMultiArray.TYPE);
        this.layout = layout;
        // copy the array
        this.data = new byte[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static ByteMultiArray fromJsonString(java.lang.String jsonString) {
        return ByteMultiArray.fromMessage(new RosMessage(jsonString));
    }

    public static ByteMultiArray fromMessage(RosMessage m) {
        return ByteMultiArray.fromJSONObject(m.toJSONObject());
    }

    public static ByteMultiArray fromJSONObject(JSONObject jsonObject) {
        // check the layout
        MultiArrayLayout layout = jsonObject.has(ByteMultiArray.FIELD_LAYOUT) ? MultiArrayLayout.fromJSONObject(jsonObject.getJSONObject(ByteMultiArray.FIELD_LAYOUT)) : new MultiArrayLayout();

        // check the array
        byte[] data = new byte[]{};
        JSONArray jsonData = jsonObject.getJSONArray(ByteMultiArray.FIELD_DATA);
        if (jsonData != null) {
            // convert each data
            data = new byte[jsonData.length()];
            for (int i = 0; i < data.length; i++) {
                data[i] = (byte) jsonData.getInt(i);
            }
        }
        return new ByteMultiArray(layout, data);
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
    public ByteMultiArray clone() {
        return new ByteMultiArray(this.layout, this.data);
    }
}
