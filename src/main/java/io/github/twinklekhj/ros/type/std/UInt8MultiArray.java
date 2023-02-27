package io.github.twinklekhj.ros.type.std;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class UInt8MultiArray extends RosMessage {
    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/UInt8MultiArray";

    private final MultiArrayLayout layout;
    private final byte[] data;

    /**
     * Create a new, empty UInt8MultiArray.
     */
    public UInt8MultiArray() {
        this(new MultiArrayLayout(), new byte[]{});
    }

    /**
     * Create a new UInt8MultiArray with the given layout and data. The array of
     * data will be copied into this object. All values will be treated as an
     * unsigned representation.
     *
     * @param layout The specification of data layout.
     * @param data   The array of data.
     */
    public UInt8MultiArray(MultiArrayLayout layout, byte[] data) {
        // build the JSON object
        super(jsonBuilder()
                        .put(UInt8MultiArray.FIELD_LAYOUT, layout.toJSONObject())
                        .put(UInt8MultiArray.FIELD_DATA, jsonBuilder(Arrays.toString(Primitive.fromUInt8(data))))
                , UInt8MultiArray.TYPE);

        this.layout = layout;
        // copy the array
        this.data = new byte[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static UInt8MultiArray fromJsonString(String jsonString) {
        return UInt8MultiArray.fromMessage(new RosMessage(jsonString));
    }

    public static UInt8MultiArray fromMessage(RosMessage m) {
        return UInt8MultiArray.fromJSONObject(m.toJSONObject());
    }

    public static UInt8MultiArray fromJSONObject(JSONObject jsonObject) {
        // check the layout
        MultiArrayLayout layout = jsonObject
                .has(UInt8MultiArray.FIELD_LAYOUT) ? MultiArrayLayout
                .fromJSONObject(jsonObject
                        .getJSONObject(UInt8MultiArray.FIELD_LAYOUT))
                : new MultiArrayLayout();

        // check the array
        byte[] data = new byte[]{};
        JSONArray jsonData = jsonObject.getJSONArray(UInt8MultiArray.FIELD_DATA);
        if (jsonData != null) {
            // convert each data
            data = new byte[jsonData.length()];
            for (int i = 0; i < data.length; i++) {
                data[i] = Primitive.toUInt8((short) jsonData.getInt(i));
            }
        }
        return new UInt8MultiArray(layout, data);
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
    public UInt8MultiArray clone() {
        return new UInt8MultiArray(this.layout, this.data);
    }
}
