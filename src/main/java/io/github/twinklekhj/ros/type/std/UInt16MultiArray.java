package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;

public class UInt16MultiArray extends RosMessage {
    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/UInt16MultiArray";

    private final MultiArrayLayout layout;
    private final short[] data;

    public UInt16MultiArray() {
        this(new MultiArrayLayout(), new short[]{});
    }

    public UInt16MultiArray(MultiArrayLayout layout, short[] data) {
        super(jsonBuilder()
                        .put(UInt16MultiArray.FIELD_LAYOUT, layout.getJsonObject())
                        .put(UInt16MultiArray.FIELD_DATA, jsonBuilder(Arrays.toString(Primitive.fromUInt16(data))))
                , UInt16MultiArray.TYPE);

        this.layout = layout;
        this.data = new short[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static UInt16MultiArray fromJsonString(String jsonString) {
        return UInt16MultiArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static UInt16MultiArray fromMessage(RosMessage m) {
        return UInt16MultiArray.fromJsonObject(m.getJsonObject());
    }

    public static UInt16MultiArray fromJsonObject(JsonObject jsonObject) {
        MultiArrayLayout layout = jsonObject.containsKey(UInt16MultiArray.FIELD_LAYOUT)
                ? MultiArrayLayout.fromJsonObject(jsonObject.getJsonObject(UInt16MultiArray.FIELD_LAYOUT))
                : new MultiArrayLayout();

        short[] data = new short[]{};
        JsonArray jsonData = jsonObject.getJsonArray(UInt16MultiArray.FIELD_DATA);
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
    public UInt16MultiArray clone() {
        return new UInt16MultiArray(this.layout, this.data);
    }
}
