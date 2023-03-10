package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

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

    public ByteMultiArray(MultiArrayLayout layout, byte[] data) {
        super(jsonBuilder()
                .put(ByteMultiArray.FIELD_LAYOUT, layout.getJsonObject())
                .put(ByteMultiArray.FIELD_DATA, jsonBuilder(Arrays.toString(data))), ByteMultiArray.TYPE);
        this.layout = layout;
        this.data = new byte[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static ByteMultiArray fromJsonString(java.lang.String jsonString) {
        return ByteMultiArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static ByteMultiArray fromMessage(RosMessage m) {
        return ByteMultiArray.fromJsonObject(m.getJsonObject());
    }

    public static ByteMultiArray fromJsonObject(JsonObject jsonObject) {
        MultiArrayLayout layout = jsonObject.containsKey(ByteMultiArray.FIELD_LAYOUT) ? MultiArrayLayout.fromJsonObject(jsonObject.getJsonObject(ByteMultiArray.FIELD_LAYOUT)) : new MultiArrayLayout();

        byte[] data = new byte[]{};
        JsonArray jsonData = jsonObject.getJsonArray(ByteMultiArray.FIELD_DATA);
        if (jsonData != null) {
            data = new byte[jsonData.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = jsonData.getInteger(i).byteValue();
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
