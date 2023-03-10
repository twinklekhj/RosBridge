package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;

public class Int16MultiArray extends RosMessage {
    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Int16MultiArray";

    private final MultiArrayLayout layout;
    private final short[] data;

    public Int16MultiArray() {
        this(new MultiArrayLayout(), new short[]{});
    }

    public Int16MultiArray(MultiArrayLayout layout, short[] data) {
        super(jsonBuilder().put(Int16MultiArray.FIELD_LAYOUT, layout.getJsonObject()).put(Int16MultiArray.FIELD_DATA, jsonBuilder(Arrays.toString(data))), Int16MultiArray.TYPE);
        this.layout = layout;
        this.data = new short[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static Int16MultiArray fromJsonString(String jsonString) {
        return Int16MultiArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Int16MultiArray fromMessage(RosMessage m) {
        return Int16MultiArray.fromJsonObject(m.getJsonObject());
    }

    public static Int16MultiArray fromJsonObject(JsonObject jsonObject) {
        MultiArrayLayout layout = jsonObject.containsKey(Int16MultiArray.FIELD_LAYOUT) ? MultiArrayLayout.fromJsonObject(jsonObject.getJsonObject(Int16MultiArray.FIELD_LAYOUT)) : new MultiArrayLayout();

        short[] data = new short[]{};
        JsonArray jsonData = jsonObject.getJsonArray(Int16MultiArray.FIELD_DATA);
        if (jsonData != null) {
            data = new short[jsonData.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = jsonData.getInteger(i).shortValue();
            }
        }
        return new Int16MultiArray(layout, data);
    }

    public MultiArrayLayout getLayout() {
        return this.layout;
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

    @Override
    public Int16MultiArray clone() {
        return new Int16MultiArray(this.layout, this.data);
    }
}
