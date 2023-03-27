package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Int64 extends RosMessage {
    public static final String TYPE = "std_msgs/Int64";
    public static final String FIELD_DATA = "data";

    private final long data;

    public Int64() {
        this(0L);
    }

    public Int64(long data) {
        super(jsonBuilder().put(FIELD_DATA, data), TYPE);
        this.data = data;
    }

    public static Int64 fromJsonString(String jsonString) {
        return Int64.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Int64 fromMessage(RosMessage m) {
        return Int64.fromJsonObject(m.getJsonObject());
    }

    public static Int64 fromJsonObject(JsonObject jsonObject) {
        long data = jsonObject.containsKey(FIELD_DATA) ? jsonObject.getLong(FIELD_DATA) : 0L;
        return new Int64(data);
    }

    public long getData() {
        return this.data;
    }

    @Override
    public Int64 clone() {
        return new Int64(this.data);
    }
}
