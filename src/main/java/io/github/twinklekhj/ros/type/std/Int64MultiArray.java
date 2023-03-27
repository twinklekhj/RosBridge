package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;

@ToString
public class Int64MultiArray extends RosMessage {
    public static final String TYPE = "std_msgs/Int64MultiArray";

    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    private MultiArrayLayout layout;
    private long[] data;

    public Int64MultiArray() {
        this(new MultiArrayLayout(), new long[]{});
    }

    public Int64MultiArray(MultiArrayLayout layout, long[] data) {
        this.layout = layout;
        this.data = new long[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);

        super.setJsonObject(jsonBuilder().put(FIELD_LAYOUT, layout.getJsonObject()).put(FIELD_DATA, Arrays.toString(data)));
        super.setType(TYPE);
    }

    public static Int64MultiArray fromJsonString(String jsonString) {
        return Int64MultiArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Int64MultiArray fromMessage(RosMessage m) {
        return Int64MultiArray.fromJsonObject(m.getJsonObject());
    }

    public static Int64MultiArray fromJsonObject(JsonObject jsonObject) {
        MultiArrayLayout layout = jsonObject.containsKey(FIELD_LAYOUT) ? MultiArrayLayout.fromJsonObject(jsonObject.getJsonObject(FIELD_LAYOUT)) : new MultiArrayLayout();

        long[] data = new long[]{};
        JsonArray jsonData = jsonObject.getJsonArray(FIELD_DATA);
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

    public void setLayout(MultiArrayLayout layout) {
        this.layout = layout;
        this.jsonObject.put(FIELD_LAYOUT, layout.getJsonObject());
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

    public void setData(long... data) {
        this.data = data;
        System.arraycopy(data, 0, this.data, 0, data.length);
        this.jsonObject.put(FIELD_DATA, Arrays.toString(data));
    }

    @Override
    public Int64MultiArray clone() {
        return new Int64MultiArray(this.layout, this.data);
    }
}
