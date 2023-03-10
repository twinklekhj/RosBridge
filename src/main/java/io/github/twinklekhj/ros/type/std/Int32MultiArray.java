package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;

public class Int32MultiArray extends RosMessage {
    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Int32MultiArray";

    private final MultiArrayLayout layout;
    private final int[] data;

    public Int32MultiArray() {
        this(new MultiArrayLayout(), new int[]{});
    }

    public Int32MultiArray(MultiArrayLayout layout, int[] data) {
        super(jsonBuilder()
                .put(Int32MultiArray.FIELD_LAYOUT, layout.getJsonObject())
                .put(Int32MultiArray.FIELD_DATA, jsonBuilder(Arrays.toString(data))), Int32MultiArray.TYPE);
        this.layout = layout;
        this.data = new int[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static Int32MultiArray fromJsonString(String jsonString) {
        return Int32MultiArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Int32MultiArray fromMessage(RosMessage m) {
        return Int32MultiArray.fromJsonObject(m.getJsonObject());
    }

    public static Int32MultiArray fromJsonObject(JsonObject jsonObject) {
        MultiArrayLayout layout = jsonObject.containsKey(Int32MultiArray.FIELD_LAYOUT) ? MultiArrayLayout.fromJsonObject(jsonObject.getJsonObject(Int32MultiArray.FIELD_LAYOUT)) : new MultiArrayLayout();

        int[] data = new int[]{};
        JsonArray jsonData = jsonObject.getJsonArray(Int32MultiArray.FIELD_DATA);
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

    public int size() {
        return this.data.length;
    }

    public int get(int index) {
        return this.data[index];
    }

    public int[] getData() {
        return this.data;
    }

    @Override
    public Int32MultiArray clone() {
        return new Int32MultiArray(this.layout, this.data);
    }
}
