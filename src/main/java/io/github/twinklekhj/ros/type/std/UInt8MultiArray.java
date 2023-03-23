package io.github.twinklekhj.ros.type.std;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;

@ToString
public class UInt8MultiArray extends RosMessage {
    public static final String TYPE = "std_msgs/UInt8MultiArray";

    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";


    private MultiArrayLayout layout;
    private byte[] data;

    public UInt8MultiArray() {
        this(new MultiArrayLayout(), new byte[]{});
    }

    public UInt8MultiArray(MultiArrayLayout layout, byte[] data) {
        this.layout = layout;
        this.data = new byte[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);

        super.setJsonObject(jsonBuilder().put(FIELD_LAYOUT, layout.getJsonObject()).put(FIELD_DATA, jsonBuilder(Arrays.toString(Primitive.fromUInt8(data)))));
        super.setType(TYPE);
    }

    public static UInt8MultiArray fromJsonString(String jsonString) {
        return UInt8MultiArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static UInt8MultiArray fromMessage(RosMessage m) {
        return UInt8MultiArray.fromJsonObject(m.getJsonObject());
    }

    public static UInt8MultiArray fromJsonObject(JsonObject jsonObject) {
        // check the layout
        MultiArrayLayout layout = jsonObject.containsKey(FIELD_LAYOUT) ? MultiArrayLayout.fromJsonObject(jsonObject.getJsonObject(FIELD_LAYOUT)) : new MultiArrayLayout();

        // check the array
        byte[] data = new byte[]{};
        JsonArray jsonData = jsonObject.getJsonArray(FIELD_DATA);
        if (jsonData != null) {
            // convert each data
            data = new byte[jsonData.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = Primitive.toUInt8(jsonData.getInteger(i).shortValue());
            }
        }
        return new UInt8MultiArray(layout, data);
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

    public void setData(byte... data) {
        this.data = data;
        System.arraycopy(data, 0, this.data, 0, data.length);
        this.jsonObject.put(FIELD_DATA, Arrays.toString(data));
    }

    @Override
    public UInt8MultiArray clone() {
        return new UInt8MultiArray(this.layout, this.data);
    }
}
