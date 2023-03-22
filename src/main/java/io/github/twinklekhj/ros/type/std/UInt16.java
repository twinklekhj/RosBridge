package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class UInt16 extends RosMessage {
    public static final String TYPE = "std_msgs/UInt16";
    public static final String FIELD_DATA = "data";

    private final short data;

    public UInt16() {
        this((short) 0);
    }

    public UInt16(short data) {
        super.setJsonObject(jsonBuilder().put(UInt16.FIELD_DATA, Primitive.fromUInt16(data))); super.setType(TYPE);
        this.data = data;
    }

    public static UInt16 fromJsonString(String jsonString) {
        return UInt16.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static UInt16 fromMessage(RosMessage m) {
        // get it from the JSON object
        return UInt16.fromJsonObject(m.getJsonObject());
    }

    public static UInt16 fromJsonObject(JsonObject jsonObject) {
        // check the fields
        short data = jsonObject.containsKey(UInt16.FIELD_DATA) ? Primitive.toUInt16(jsonObject.getInteger(UInt16.FIELD_DATA)) : 0;
        return new UInt16(data);
    }

    public short getData() {
        return this.data;
    }

    @Override
    public UInt16 clone() {
        return new UInt16(this.data);
    }
}
