package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class UInt8 extends RosMessage {
    public static final String TYPE = "std_msgs/UInt8";
    public static final String FIELD_DATA = "data";

    private final byte data;

    public UInt8() {
        this((byte) 0);
    }

    public UInt8(byte data) {
        this.data = data;
        super.setJsonObject(jsonBuilder().put(FIELD_DATA, Primitive.fromUInt8(data)));
        super.setType(TYPE);
    }

    public static UInt8 fromJsonString(String jsonString) {
        return UInt8.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static UInt8 fromMessage(RosMessage m) {
        return UInt8.fromJsonObject(m.getJsonObject());
    }

    public static UInt8 fromJsonObject(JsonObject jsonObject) {
        byte data = jsonObject.containsKey(FIELD_DATA) ? Primitive.toUInt8(jsonObject.getInteger(FIELD_DATA).shortValue()) : 0;
        return new UInt8(data);
    }

    public byte getData() {
        return this.data;
    }

    @Override
    public UInt8 clone() {
        return new UInt8(this.data);
    }
}
