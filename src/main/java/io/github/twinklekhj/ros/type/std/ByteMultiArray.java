package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;

@ToString
public class ByteMultiArray extends RosMessage {
    public static final String TYPE = "std_msgs/ByteMultiArray";

    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    private MultiArrayLayout layout;
    private byte[] data;

    public ByteMultiArray() {
        this(new MultiArrayLayout(), new byte[]{});
    }

    public ByteMultiArray(MultiArrayLayout layout, byte[] data) {
        this.layout = layout;
        this.data = new byte[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);

        super.setJsonObject(jsonBuilder().put(ByteMultiArray.FIELD_LAYOUT, layout.getJsonObject()).put(ByteMultiArray.FIELD_DATA, Arrays.toString(data)));
        super.setType(TYPE);
    }

    public static ByteMultiArray fromJsonString(String jsonString) {
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

    public void setLayout(MultiArrayLayout layout) {
        this.layout = layout;
        this.jsonObject.put(FIELD_LAYOUT, layout.getJsonObject());
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

    public void setData(byte ...data) {
        this.data = data;
        System.arraycopy(data, 0, this.data, 0, data.length);
        this.jsonObject.put(FIELD_DATA, Arrays.toString(data));
    }

    @Override
    public ByteMultiArray clone() {
        return new ByteMultiArray(this.layout, this.data);
    }
}
