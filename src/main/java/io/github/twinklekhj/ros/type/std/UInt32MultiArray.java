package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;

@ToString
public class UInt32MultiArray extends RosMessage {
    public static final String TYPE = "std_msgs/UInt32MultiArray";

    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    private MultiArrayLayout layout;
    private int[] data;

    public UInt32MultiArray() {
        this(new MultiArrayLayout(), new int[]{});
    }

    public UInt32MultiArray(MultiArrayLayout layout, int[] data) {
        this.layout = layout;
        this.data = new int[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);

        super.setJsonObject(jsonBuilder().put(UInt32MultiArray.FIELD_LAYOUT, layout.getJsonObject()).put(UInt32MultiArray.FIELD_DATA, jsonBuilder(Arrays.toString(Primitive.fromUInt32(data)))));
        super.setType(TYPE);
    }

    public static UInt32MultiArray fromJsonString(String jsonString) {
        return UInt32MultiArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static UInt32MultiArray fromMessage(RosMessage m) {
        return UInt32MultiArray.fromJsonObject(m.getJsonObject());
    }

    public static UInt32MultiArray fromJsonObject(JsonObject jsonObject) {
        MultiArrayLayout layout = jsonObject.containsKey(UInt32MultiArray.FIELD_LAYOUT) ? MultiArrayLayout.fromJsonObject(jsonObject.getJsonObject(UInt32MultiArray.FIELD_LAYOUT)) : new MultiArrayLayout();

        int[] data = new int[]{};
        JsonArray jsonData = jsonObject.getJsonArray(UInt32MultiArray.FIELD_DATA);
        if (jsonData != null) {
            data = new int[jsonData.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = Primitive.toUInt32(jsonData.getLong(i));
            }
        }
        return new UInt32MultiArray(layout, data);
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

    public int get(int index) {
        return this.data[index];
    }

    public int[] getData() {
        return this.data;
    }

    public void setData(int... data) {
        this.data = data;
        System.arraycopy(data, 0, this.data, 0, data.length);
        this.jsonObject.put(FIELD_DATA, Arrays.toString(data));
    }

    @Override
    public UInt32MultiArray clone() {
        return new UInt32MultiArray(this.layout, this.data);
    }
}
