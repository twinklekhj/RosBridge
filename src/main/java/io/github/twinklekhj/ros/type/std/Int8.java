package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Int8 extends RosMessage {
    public static final String TYPE = "std_msgs/Int8";
    public static final String FIELD_DATA = "data";

    private final byte data;

    public Int8() {
        this((byte) 0);
    }

    public Int8(byte data) {
        super(jsonBuilder().put(Int8.FIELD_DATA, data), Int8.TYPE);
        this.data = data;
    }

    public static Int8 fromJsonString(String jsonString) {
        return Int8.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Int8 fromMessage(RosMessage m) {
        return Int8.fromJsonObject(m.getJsonObject());
    }

    public static Int8 fromJsonObject(JsonObject jsonObject) {
        byte data = jsonObject.containsKey(Int8.FIELD_DATA) ? jsonObject.getInteger(Int8.FIELD_DATA).byteValue() : 0;
        return new Int8(data);
    }

    public byte getData() {
        return this.data;
    }

    @Override
    public Int8 clone() {
        return new Int8(this.data);
    }
}
