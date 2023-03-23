package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;

@ToString
public class UInt16MultiArray extends RosMessage {
    public static final String TYPE = "std_msgs/UInt16MultiArray";

    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    private MultiArrayLayout layout;
    private short[] data;

    public UInt16MultiArray() {
        this(new MultiArrayLayout(), new short[]{});
    }

    public UInt16MultiArray(MultiArrayLayout layout, short[] data) {
        this.layout = layout;
        this.data = new short[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);

        super.setJsonObject(jsonBuilder().put(FIELD_LAYOUT, layout.getJsonObject()).put(FIELD_DATA, jsonBuilder(Arrays.toString(Primitive.fromUInt16(data)))));
        super.setType(TYPE);
    }

    public static UInt16MultiArray fromJsonString(String jsonString) {
        return UInt16MultiArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static UInt16MultiArray fromMessage(RosMessage m) {
        return UInt16MultiArray.fromJsonObject(m.getJsonObject());
    }

    public static UInt16MultiArray fromJsonObject(JsonObject jsonObject) {
        MultiArrayLayout layout = jsonObject.containsKey(FIELD_LAYOUT) ? MultiArrayLayout.fromJsonObject(jsonObject.getJsonObject(FIELD_LAYOUT)) : new MultiArrayLayout();

        short[] data = new short[]{};
        JsonArray jsonData = jsonObject.getJsonArray(FIELD_DATA);
        if (jsonData != null) {
            data = new short[jsonData.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = Primitive.toUInt16(jsonData.getInteger(i));
            }
        }
        return new UInt16MultiArray(layout, data);
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

    public short get(int index) {
        return this.data[index];
    }

    public short[] getData() {
        return this.data;
    }

    public void setData(short... data) {
        this.data = data;
        System.arraycopy(data, 0, this.data, 0, data.length);
        this.jsonObject.put(FIELD_DATA, Arrays.toString(data));
    }

    @Override
    public UInt16MultiArray clone() {
        return new UInt16MultiArray(this.layout, this.data);
    }
}
