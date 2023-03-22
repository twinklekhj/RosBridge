package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;

@ToString
public class Float64MultiArray extends RosMessage {
    public static final String TYPE = "std_msgs/Float64MultiArray";

    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    private MultiArrayLayout layout;
    private double[] data;

    public Float64MultiArray() {
        this(new MultiArrayLayout(), new double[]{});
    }

    public Float64MultiArray(MultiArrayLayout layout, double[] data) {
        this.layout = layout;
        this.data = new double[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);

        super.setJsonObject(jsonBuilder().put(Float64MultiArray.FIELD_LAYOUT, layout.getJsonObject()).put(Float64MultiArray.FIELD_DATA, jsonBuilder(Arrays.toString(data))));
        super.setType(TYPE);
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

    public void setLayout(MultiArrayLayout layout) {
        this.layout = layout;
        this.jsonObject.put(FIELD_LAYOUT, layout.getJsonObject());
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

    public void setData(double ...data) {
        this.data = data;
        System.arraycopy(data, 0, this.data, 0, data.length);
        this.jsonObject.put(FIELD_DATA, Arrays.toString(data));
    }

    @Override
    public Float64MultiArray clone() {
        return new Float64MultiArray(this.layout, this.data);
    }
}
