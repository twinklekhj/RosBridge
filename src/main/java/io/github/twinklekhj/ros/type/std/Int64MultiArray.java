package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;

public class Int64MultiArray extends RosMessage {
    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Int64MultiArray";

    private final MultiArrayLayout layout;
    private final long[] data;

    public Int64MultiArray() {
        this(new MultiArrayLayout(), new long[]{});
    }

    public Int64MultiArray(MultiArrayLayout layout, long[] data) {
        super(jsonBuilder().put(Int64MultiArray.FIELD_LAYOUT, layout.getJsonObject()).put(Int64MultiArray.FIELD_DATA, jsonBuilder(Arrays.toString(data))), Int64MultiArray.TYPE);
        this.layout = layout;
        this.data = new long[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static Int64MultiArray fromJsonString(String jsonString) {
        return Int64MultiArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Int64MultiArray fromMessage(RosMessage m) {
        return Int64MultiArray.fromJsonObject(m.getJsonObject());
    }

    public static Int64MultiArray fromJsonObject(JsonObject jsonObject) {
        MultiArrayLayout layout = jsonObject.containsKey(Int64MultiArray.FIELD_LAYOUT) ? MultiArrayLayout.fromJsonObject(jsonObject.getJsonObject(Int64MultiArray.FIELD_LAYOUT)) : new MultiArrayLayout();

        long[] data = new long[]{};
        JsonArray jsonData = jsonObject.getJsonArray(Int64MultiArray.FIELD_DATA);
        if (jsonData != null) {
            data = new long[jsonData.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = jsonData.getLong(i);
            }
        }
        return new Int64MultiArray(layout, data);
    }

    public MultiArrayLayout getLayout() {
        return this.layout;
    }

    public int size() {
        return this.data.length;
    }

    public long get(int index) {
        return this.data[index];
    }

    public long[] getData() {
        return this.data;
    }

    @Override
    public Int64MultiArray clone() {
        return new Int64MultiArray(this.layout, this.data);
    }
}
