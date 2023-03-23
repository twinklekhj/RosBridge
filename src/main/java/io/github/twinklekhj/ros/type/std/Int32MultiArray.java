package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;

@ToString
public class Int32MultiArray extends RosMessage {
    public static final String TYPE = "std_msgs/Int32MultiArray";

    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    private MultiArrayLayout layout;
    private int[] data;

    public Int32MultiArray() {
        this(new MultiArrayLayout(), new int[]{});
    }

    public Int32MultiArray(MultiArrayLayout layout, int[] data) {
        this.layout = layout;
        this.data = new int[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);

        super.setJsonObject(jsonBuilder().put(FIELD_LAYOUT, layout.getJsonObject()).put(FIELD_DATA, jsonBuilder(Arrays.toString(data))));
        super.setType(TYPE);
    }

    public static Int32MultiArray fromJsonString(String jsonString) {
        return Int32MultiArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Int32MultiArray fromMessage(RosMessage m) {
        return Int32MultiArray.fromJsonObject(m.getJsonObject());
    }

    public static Int32MultiArray fromJsonObject(JsonObject jsonObject) {
        MultiArrayLayout layout = jsonObject.containsKey(FIELD_LAYOUT) ? MultiArrayLayout.fromJsonObject(jsonObject.getJsonObject(FIELD_LAYOUT)) : new MultiArrayLayout();

        int[] data = new int[]{};
        JsonArray jsonData = jsonObject.getJsonArray(FIELD_DATA);
        if (jsonData != null) {
            data = new int[jsonData.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = jsonData.getInteger(i);
            }
        }
        return new Int32MultiArray(layout, data);
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
    public Int32MultiArray clone() {
        return new Int32MultiArray(this.layout, this.data);
    }
}
