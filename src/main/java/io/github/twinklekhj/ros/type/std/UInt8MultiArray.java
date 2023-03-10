package io.github.twinklekhj.ros.type.std;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;

public class UInt8MultiArray extends RosMessage {
    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/UInt8MultiArray";

    private final MultiArrayLayout layout;
    private final byte[] data;

    public UInt8MultiArray() {
        this(new MultiArrayLayout(), new byte[]{});
    }

    public UInt8MultiArray(MultiArrayLayout layout, byte[] data) {
        super(jsonBuilder()
                        .put(UInt8MultiArray.FIELD_LAYOUT, layout.getJsonObject())
                        .put(UInt8MultiArray.FIELD_DATA, jsonBuilder(Arrays.toString(Primitive.fromUInt8(data))))
                , UInt8MultiArray.TYPE);

        this.layout = layout;
        this.data = new byte[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public static UInt8MultiArray fromJsonString(String jsonString) {
        return UInt8MultiArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static UInt8MultiArray fromMessage(RosMessage m) {
        return UInt8MultiArray.fromJsonObject(m.getJsonObject());
    }

    public static UInt8MultiArray fromJsonObject(JsonObject jsonObject) {
        // check the layout
        MultiArrayLayout layout = jsonObject.containsKey(UInt8MultiArray.FIELD_LAYOUT) ? MultiArrayLayout
                .fromJsonObject(jsonObject.getJsonObject(UInt8MultiArray.FIELD_LAYOUT))
                : new MultiArrayLayout();

        // check the array
        byte[] data = new byte[]{};
        JsonArray jsonData = jsonObject.getJsonArray(UInt8MultiArray.FIELD_DATA);
        if (jsonData != null) {
            // convert each data
            data = new byte[jsonData.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = Primitive.toUInt8(jsonData.getInteger(i).shortValue());
            }
        }
        return new UInt8MultiArray(layout, data);
    }

    public MultiArrayLayout getLayout() {
        return this.layout;
    }

    public int size() {
        return this.data.length;
    }

    public byte get(int index) {
        return this.data[index];
    }

    public byte[] getData() {
        return this.data;
    }

    @Override
    public UInt8MultiArray clone() {
        return new UInt8MultiArray(this.layout, this.data);
    }
}
