package io.github.twinklekhj.ros.type.std;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class UInt32 extends RosMessage {
    public static final String TYPE = "std_msgs/UInt32";
    public static final String FIELD_DATA = "data";

    private final int data;

    public UInt32() {
        this(0);
    }

    public UInt32(int data) {
        super.setJsonObject(jsonBuilder().put(FIELD_DATA, Primitive.fromUInt32(data))); super.setType(TYPE);
        this.data = data;
    }

    public static UInt32 fromJsonString(String jsonString) {
        return UInt32.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static UInt32 fromMessage(RosMessage m) {
        return UInt32.fromJsonObject(m.getJsonObject());
    }

    public static UInt32 fromJsonObject(JsonObject jsonObject) {
        int data = jsonObject.containsKey(FIELD_DATA) ? Primitive.toUInt32(jsonObject.getLong(FIELD_DATA)) : 0;
        return new UInt32(data);
    }

    public int getData() {
        return this.data;
    }

    @Override
    public UInt32 clone() {
        return new UInt32(this.data);
    }
}
