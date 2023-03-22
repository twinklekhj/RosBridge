package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Int16 extends RosMessage {
    public static final String TYPE = "std_msgs/Int16";
    public static final String FIELD_DATA = "data";

    private final short data;

    public Int16() {
        this((short) 0);
    }

    public Int16(short data) {
        super(jsonBuilder().put(Int16.FIELD_DATA, data), Int16.TYPE);
        this.data = data;
    }

    public static Int16 fromJsonString(String jsonString) {
        return Int16.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Int16 fromMessage(RosMessage m) {
        return Int16.fromJsonObject(m.getJsonObject());
    }

    public static Int16 fromJsonObject(JsonObject jsonObject) {
        short data = jsonObject.containsKey(Int16.FIELD_DATA) ? jsonObject.getInteger(Int16.FIELD_DATA).shortValue() : 0;
        return new Int16(data);
    }

    public short getData() {
        return this.data;
    }

    @Override
    public Int16 clone() {
        return new Int16(this.data);
    }
}
