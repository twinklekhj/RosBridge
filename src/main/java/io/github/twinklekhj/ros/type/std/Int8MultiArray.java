package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;

public class Int8MultiArray extends RosMessage {
    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Int8MultiArray";

    private final MultiArrayLayout layout;
    private final byte[] data;

    public Int8MultiArray() {
        this(new MultiArrayLayout(), new byte[]{});
    }

    public Int8MultiArray(MultiArrayLayout layout, byte[] data) {
        super(jsonBuilder().put(Int8MultiArray.FIELD_LAYOUT, layout.getJsonObject()).put(Int8MultiArray.FIELD_DATA, jsonBuilder(Arrays.toString(data))), Int8MultiArray.TYPE);
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
        MultiArrayLayout layout = jsonObject.containsKey(Int8MultiArray.FIELD_LAYOUT) ? MultiArrayLayout.fromJsonObject(jsonObject.getJsonObject(Int8MultiArray.FIELD_LAYOUT)) : new MultiArrayLayout();

        byte[] data = new byte[]{};
        JsonArray jsonData = jsonObject.getJsonArray(Int8MultiArray.FIELD_DATA);
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

    @Override
    public Int8MultiArray clone() {
        return new Int8MultiArray(this.layout, this.data);
    }
}
