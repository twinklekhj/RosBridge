package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class UInt64 extends RosMessage {
    public static final String TYPE = "std_msgs/UInt64";
    public static final String FIELD_DATA = "data";

    private final long data;

    public UInt64() {
        this(0L);
    }

    public UInt64(long data) {
        super.setJsonObject(jsonBuilder().put(FIELD_DATA, Primitive.fromUInt64(data))); super.setType(TYPE);
        this.data = data;
    }

    public static UInt64 fromJsonString(String jsonString) {
        return UInt64.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static UInt64 fromMessage(RosMessage m) {
        return UInt64.fromJsonObject(m.getJsonObject());
    }

    public static UInt64 fromJsonObject(JsonObject jsonObject) {
        long data = jsonObject.containsKey(FIELD_DATA) ? Primitive.toUInt64(jsonObject.getLong(FIELD_DATA)) : 0L;
        return new UInt64(data);
    }

    public long getData() {
        return this.data;
    }

    @Override
    public UInt64 clone() {
        return new UInt64(this.data);
    }
}
