package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;

public class Float64MultiArray extends RosMessage {
    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Float64MultiArray";

    private final MultiArrayLayout layout;
    private final double[] data;

    public Float64MultiArray() {
        this(new MultiArrayLayout(), new double[]{});
    }

    public Float64MultiArray(MultiArrayLayout layout, double[] data) {
        super(jsonBuilder().put(Float64MultiArray.FIELD_LAYOUT, layout.getJsonObject()).put(Float64MultiArray.FIELD_DATA, jsonBuilder(Arrays.toString(data))), Float64MultiArray.TYPE);
        this.layout = layout;
        this.data = new double[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static Float64MultiArray fromJsonString(String jsonString) {
        return Float64MultiArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Float64MultiArray fromMessage(RosMessage m) {
        return Float64MultiArray.fromJsonObject(m.getJsonObject());
    }

    public static Float64MultiArray fromJsonObject(JsonObject jsonObject) {
        MultiArrayLayout layout = jsonObject.containsKey(Float64MultiArray.FIELD_LAYOUT) ? MultiArrayLayout.fromJsonObject(jsonObject.getJsonObject(Float64MultiArray.FIELD_LAYOUT)) : new MultiArrayLayout();

        double[] data = new double[]{};
        JsonArray jsonData = jsonObject.getJsonArray(Float64MultiArray.FIELD_DATA);
        if (jsonData != null) {
            data = new double[jsonData.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = jsonData.getDouble(i);
            }
        }
        return new Float64MultiArray(layout, data);
    }

    public MultiArrayLayout getLayout() {
        return this.layout;
    }

    public int size() {
        return this.data.length;
    }

    public double get(int index) {
        return this.data[index];
    }

    public double[] getData() {
        return this.data;
    }

    @Override
    public Float64MultiArray clone() {
        return new Float64MultiArray(this.layout, this.data);
    }
}
