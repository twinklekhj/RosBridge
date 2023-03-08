package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class MultiArrayLayout extends RosMessage {
    public static final String FIELD_DIM = "dim";
    public static final String FIELD_DATA_OFFSET = "data_offset";

    public static final String TYPE = "std_msgs/MultiArrayLayout";

    private final MultiArrayDimension[] dim;
    private final int dataOffset;

    /**
     * Create a new MultiArrayLayout with empty values.
     */
    public MultiArrayLayout() {
        this(new MultiArrayDimension[]{}, 0);
    }

    /**
     * Create a new MultiArrayLayout with the given set of layouts. The array of
     * layouts will be copied into this object. The data offset is treated as a
     * 32-bit unsigned integer. The array of properties will be copied into this
     * object.
     *
     * @param dim        The array of dimension properties.
     * @param dataOffset The padding bytes at front of the data.
     */
    public MultiArrayLayout(MultiArrayDimension[] dim, int dataOffset) {
        // build the JSON object
        super(jsonBuilder()
                .put(MultiArrayLayout.FIELD_DIM, jsonBuilder(Arrays.deepToString(dim)))
                .put(MultiArrayLayout.FIELD_DATA_OFFSET, Primitive.fromUInt32(dataOffset)), MultiArrayLayout.TYPE);

        // copy the array
        this.dim = new MultiArrayDimension[dim.length];
        System.arraycopy(dim, 0, this.dim, 0, dim.length);
        this.dataOffset = dataOffset;
    }

    public static MultiArrayLayout fromJsonString(String jsonString) {
        // convert to a message
        return MultiArrayLayout.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static MultiArrayLayout fromMessage(RosMessage m) {
        return MultiArrayLayout.fromJSONObject(m.getJsonObject());
    }

    public static MultiArrayLayout fromJSONObject(JSONObject jsonObject) {
        // check the array
        MultiArrayDimension[] dim = new MultiArrayDimension[]{};
        JSONArray jsonDim = jsonObject.getJSONArray(MultiArrayLayout.FIELD_DIM);
        if (jsonDim != null) {
            // convert each property
            dim = new MultiArrayDimension[jsonDim.length()];
            for (int i = 0; i < dim.length; i++) {
                dim[i] = MultiArrayDimension.fromJSONObject(jsonDim.getJSONObject(i));
            }
        }

        // check the offset
        int dataOffset = jsonObject.has(MultiArrayLayout.FIELD_DATA_OFFSET) ? Primitive.toUInt32(jsonObject.getLong(MultiArrayLayout.FIELD_DATA_OFFSET)) : 0;

        return new MultiArrayLayout(dim, dataOffset);
    }

    public int size() {
        return this.dim.length;
    }

    public MultiArrayDimension get(int index) {
        return this.dim[index];
    }

    public MultiArrayDimension[] getDim() {
        return this.dim;
    }

    public int getDataOffset() {
        return this.dataOffset;
    }

    @Override
    public MultiArrayLayout clone() {
        return new MultiArrayLayout(this.dim, this.dataOffset);
    }
}
