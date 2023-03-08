package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import org.json.JSONObject;

public class MultiArrayDimension extends RosMessage {
    public static final String FIELD_LABEL = "label";
    public static final String FIELD_SIZE = "size";
    public static final String FIELD_STRIDE = "stride";

    public static final String TYPE = "std_msgs/MultiArrayDimension";

    private final String label;
    private final int size, stride;

    /**
     * Create a new MultiArrayDimension with all empty values.
     */
    public MultiArrayDimension() {
        this("", 0, 0);
    }

    /**
     * Create a new MultiArrayDimension with the given values.
     *
     * @param label  The label of given dimension.
     * @param size   The size of given dimension (in type units) treated as an
     *               unsigned 32-bit integer.
     * @param stride The stride of given dimension treated as an unsigned 32-bit
     *               integer.
     */
    public MultiArrayDimension(String label, int size, int stride) {
        // build the JSON object
        super(jsonBuilder()
                .put(MultiArrayDimension.FIELD_LABEL, label)
                .put(MultiArrayDimension.FIELD_SIZE, Primitive.fromUInt32(size))
                .put(MultiArrayDimension.FIELD_STRIDE, Primitive.fromUInt32(stride)), MultiArrayDimension.TYPE);
        this.label = label;
        this.size = size;
        this.stride = stride;
    }

    public static MultiArrayDimension fromJsonString(String jsonString) {
        return MultiArrayDimension.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static MultiArrayDimension fromMessage(RosMessage m) {
        return MultiArrayDimension.fromJSONObject(m.getJsonObject());
    }

    /**
     * Create a new MultiArrayDimension based on the given JSON object. Any
     * missing values will be set to their defaults.
     *
     * @param jsonObject The JSON object to parse.
     * @return A MultiArrayDimension message based on the given JSON object.
     */
    public static MultiArrayDimension fromJSONObject(JSONObject jsonObject) {
        // check the fields
        String label = jsonObject.has(MultiArrayDimension.FIELD_LABEL) ? jsonObject.getString(MultiArrayDimension.FIELD_LABEL) : "";
        long size64 = jsonObject.has(MultiArrayDimension.FIELD_SIZE) ? jsonObject.getLong(MultiArrayDimension.FIELD_SIZE) : 0L;
        long stride64 = jsonObject.has(MultiArrayDimension.FIELD_STRIDE) ? jsonObject.getLong(MultiArrayDimension.FIELD_STRIDE) : 0L;

        // convert to a 32-bit number
        int size32 = Primitive.toUInt32(size64);
        int stride32 = Primitive.toUInt32(stride64);
        return new MultiArrayDimension(label, size32, stride32);
    }

    public String getLabel() {
        return this.label;
    }

    public int getSize() {
        return this.size;
    }

    public int getStride() {
        return this.stride;
    }

    @Override
    public MultiArrayDimension clone() {
        return new MultiArrayDimension(this.label, this.size, this.stride);
    }
}
