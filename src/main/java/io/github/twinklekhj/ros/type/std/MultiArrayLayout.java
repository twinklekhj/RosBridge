package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;

public class MultiArrayLayout extends RosMessage {
    public static final String TYPE = "std_msgs/MultiArrayLayout";

    public static final String FIELD_DIM = "dim";
    public static final String FIELD_DATA_OFFSET = "data_offset";

    private final MultiArrayDimension[] dim;
    private final int dataOffset;

    public MultiArrayLayout() {
        this(new MultiArrayDimension[]{}, 0);
    }

    public MultiArrayLayout(MultiArrayDimension[] dim, int dataOffset) {
        this.dim = new MultiArrayDimension[dim.length];
        this.dataOffset = dataOffset;

        System.arraycopy(dim, 0, this.dim, 0, dim.length);

        super.setJsonObject(jsonBuilder().put(FIELD_DIM, jsonBuilder(Arrays.deepToString(dim))).put(FIELD_DATA_OFFSET, Primitive.fromUInt32(dataOffset)));
        super.setType(TYPE);
    }

    public static MultiArrayLayout fromJsonString(String jsonString) {
        return MultiArrayLayout.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static MultiArrayLayout fromMessage(RosMessage m) {
        return MultiArrayLayout.fromJsonObject(m.getJsonObject());
    }

    public static MultiArrayLayout fromJsonObject(JsonObject jsonObject) {
        MultiArrayDimension[] dim = new MultiArrayDimension[]{};
        JsonArray jsonDim = jsonObject.getJsonArray(FIELD_DIM);
        if (jsonDim != null) {
            // convert each property
            dim = new MultiArrayDimension[jsonDim.size()];
            for (int i = 0; i < dim.length; i++) {
                dim[i] = MultiArrayDimension.fromJsonObject(jsonDim.getJsonObject(i));
            }
        }

        // check the offset
        int dataOffset = jsonObject.containsKey(FIELD_DATA_OFFSET) ? Primitive.toUInt32(jsonObject.getLong(FIELD_DATA_OFFSET)) : 0;

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
