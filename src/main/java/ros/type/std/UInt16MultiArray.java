package ros.type.std;

import org.json.JSONArray;
import org.json.JSONObject;
import ros.type.RosMessage;
import ros.type.primitives.Primitive;

import java.util.Arrays;

public class UInt16MultiArray extends RosMessage {
    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/UInt16MultiArray";

    private final MultiArrayLayout layout;
    private final short[] data;

    /**
     * Create a new, empty UInt16MultiArray.
     */
    public UInt16MultiArray() {
        this(new MultiArrayLayout(), new short[]{});
    }

    /**
     * Create a new UInt16MultiArray with the given layout and data. The array
     * of data will be copied into this object. All values will be treated as an
     * unsigned representation.
     *
     * @param layout The specification of data layout.
     * @param data   The array of data.
     */
    public UInt16MultiArray(MultiArrayLayout layout, short[] data) {
        // build the JSON object
        super(builder()
                        .put(UInt16MultiArray.FIELD_LAYOUT, layout.toJSONObject())
                        .put(UInt16MultiArray.FIELD_DATA, builder(Arrays.toString(Primitive.fromUInt16(data))))
                , UInt16MultiArray.TYPE);

        this.layout = layout;
        // copy the array
        this.data = new short[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static UInt16MultiArray fromJsonString(String jsonString) {
        return UInt16MultiArray.fromMessage(new RosMessage(jsonString));
    }

    public static UInt16MultiArray fromMessage(RosMessage m) {
        return UInt16MultiArray.fromJSONObject(m.toJSONObject());
    }

    public static UInt16MultiArray fromJSONObject(JSONObject jsonObject) {
        // check the layout
        MultiArrayLayout layout = jsonObject.has(UInt16MultiArray.FIELD_LAYOUT)
                ? MultiArrayLayout.fromJSONObject(jsonObject.getJSONObject(UInt16MultiArray.FIELD_LAYOUT))
                : new MultiArrayLayout();

        // check the array
        short[] data = new short[]{};
        JSONArray jsonData = jsonObject
                .getJSONArray(UInt16MultiArray.FIELD_DATA);
        if (jsonData != null) {
            // convert each data
            data = new short[jsonData.length()];
            for (int i = 0; i < data.length; i++) {
                data[i] = Primitive.toUInt16(jsonData.getInt(i));
            }
        }
        return new UInt16MultiArray(layout, data);
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
    public UInt16MultiArray clone() {
        return new UInt16MultiArray(this.layout, this.data);
    }
}
