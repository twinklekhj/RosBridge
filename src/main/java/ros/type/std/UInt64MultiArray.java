package ros.type.std;

import org.json.JSONArray;
import org.json.JSONObject;
import ros.type.RosMessage;
import ros.type.primitives.Primitive;

import java.util.Arrays;

public class UInt64MultiArray extends RosMessage {
    public static final java.lang.String FIELD_LAYOUT = "layout";
    public static final java.lang.String FIELD_DATA = "data";

    public static final java.lang.String TYPE = "std_msgs/UInt64MultiArray";

    private final MultiArrayLayout layout;
    private final long[] data;


    public UInt64MultiArray() {
        this(new MultiArrayLayout(), new long[]{});
    }

    public UInt64MultiArray(MultiArrayLayout layout, long[] data) {
        // build the JSON object
        super(new JSONObject()
                .put(UInt64MultiArray.FIELD_LAYOUT, layout.toJSONObject())
                .put(UInt64MultiArray.FIELD_DATA, builder(Arrays.toString(Primitive.fromUInt64(data)))), UInt64MultiArray.TYPE);

        this.layout = layout;
        // copy the array
        this.data = new long[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static UInt64MultiArray fromJsonString(java.lang.String jsonString) {
        return UInt64MultiArray.fromMessage(new RosMessage(jsonString));
    }


    public static UInt64MultiArray fromMessage(RosMessage m) {
        return UInt64MultiArray.fromJSONObject(m.toJSONObject());
    }

    public static UInt64MultiArray fromJSONObject(JSONObject jsonObject) {
        // check the layout
        MultiArrayLayout layout = jsonObject.has(UInt64MultiArray.FIELD_LAYOUT) ? MultiArrayLayout.fromJSONObject(jsonObject.getJSONObject(UInt64MultiArray.FIELD_LAYOUT)) : new MultiArrayLayout();

        // check the array
        long[] data = new long[]{};
        JSONArray jsonData = jsonObject.getJSONArray(UInt64MultiArray.FIELD_DATA);
        if (jsonData != null) {
            // convert each data
            data = new long[jsonData.length()];
            for (int i = 0; i < data.length; i++) {
                data[i] = Primitive.toUInt64(jsonData.getBigInteger(i));
            }
        }
        return new UInt64MultiArray(layout, data);
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
    public UInt64MultiArray clone() {
        return new UInt64MultiArray(this.layout, this.data);
    }
}
