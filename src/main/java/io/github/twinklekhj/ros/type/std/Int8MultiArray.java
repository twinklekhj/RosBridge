package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;

@ToString
public class Int8MultiArray extends RosMessage {
    public static final String TYPE = "std_msgs/Int8MultiArray";

    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    private MultiArrayLayout layout;
    private byte[] data;

    public Int8MultiArray() {
        this(new MultiArrayLayout(), new byte[]{});
    }

    public Int8MultiArray(MultiArrayLayout layout, byte[] data) {
        super.setJsonObject(jsonBuilder().put(FIELD_LAYOUT, layout.getJsonObject()).put(FIELD_DATA, Arrays.toString(data))); super.setType(TYPE);
        this.layout = layout;
        this.data = new byte[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static Int8MultiArray fromJsonString(String jsonString) {
        return Int8MultiArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Int8MultiArray fromMessage(RosMessage m) {
        return Int8MultiArray.fromJsonObject(m.getJsonObject());
    }

    public static Int8MultiArray fromJsonObject(JsonObject jsonObject) {
        MultiArrayLayout layout = jsonObject.containsKey(FIELD_LAYOUT) ? MultiArrayLayout.fromJsonObject(jsonObject.getJsonObject(FIELD_LAYOUT)) : new MultiArrayLayout();

        byte[] data = new byte[]{};
        JsonArray jsonData = jsonObject.getJsonArray(FIELD_DATA);
        if (jsonData != null) {
            data = new byte[jsonData.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = jsonData.getInteger(i).byteValue();
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

    public void setLayout(MultiArrayLayout layout) {
        this.layout = layout;
        this.jsonObject.put(FIELD_LAYOUT, layout.getJsonObject());
    }
    public void setData(byte ...data) {
        this.data = data;
        System.arraycopy(data, 0, this.data, 0, data.length);
        this.jsonObject.put(FIELD_DATA, Arrays.toString(data));
    }

    @Override
    public Int8MultiArray clone() {
        return new Int8MultiArray(this.layout, this.data);
    }
}
