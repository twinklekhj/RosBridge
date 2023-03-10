package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;

public class Float32MultiArray extends RosMessage {
    public static final java.lang.String FIELD_LAYOUT = "layout";
    public static final java.lang.String FIELD_DATA = "data";

    public static final java.lang.String TYPE = "std_msgs/Float32MultiArray";

    private final MultiArrayLayout layout;
    private final float[] data;

    public Float32MultiArray() {
        this(new MultiArrayLayout(), new float[]{});
    }

    public Float32MultiArray(MultiArrayLayout layout, float[] data) {
        super(jsonBuilder().put(Float32MultiArray.FIELD_LAYOUT, layout.getJsonObject()).put(Float32MultiArray.FIELD_DATA, jsonBuilder(Arrays.toString(data))), Float32MultiArray.TYPE);

        this.layout = layout;
        this.data = new float[data.length];

        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static Float32MultiArray fromJsonString(java.lang.String jsonString) {
        return Float32MultiArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Float32MultiArray fromMessage(RosMessage m) {
        return Float32MultiArray.fromJsonObject(m.getJsonObject());
    }

    public static Float32MultiArray fromJsonObject(JsonObject jsonObject) {
        MultiArrayLayout layout = jsonObject.containsKey(Float32MultiArray.FIELD_LAYOUT) ? MultiArrayLayout.fromJsonObject(jsonObject.getJsonObject(Float32MultiArray.FIELD_LAYOUT)) : new MultiArrayLayout();

        float[] data = new float[]{};
        JsonArray jsonData = jsonObject.getJsonArray(Float32MultiArray.FIELD_DATA);
        if (jsonData != null) {
            data = new float[jsonData.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = jsonData.getDouble(i).floatValue();
            }
        }
        return new Float32MultiArray(layout, data);
    }

    public MultiArrayLayout getLayout() {
        return this.layout;
    }

    public int size() {
        return this.data.length;
    }

    public float get(int index) {
        return this.data[index];
    }

    public float[] getData() {
        return this.data;
    }

    @Override
    public Float32MultiArray clone() {
        return new Float32MultiArray(this.layout, this.data);
    }
}
