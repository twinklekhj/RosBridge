package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;


public class Int64 extends RosMessage {
    public static final String FIELD_DATA = "data";
    public static final String TYPE = "std_msgs/Int64";

    private final long data;

    public Int64() {
        this(0L);
    }

    public Int64(long data) {
        super(jsonBuilder().put(Int64.FIELD_DATA, data), Int64.TYPE);
        this.data = data;
    }

    public static Int64 fromJsonString(String jsonString) {
        return Int64.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Int64 fromMessage(RosMessage m) {
        return Int64.fromJsonObject(m.getJsonObject());
    }

    public static Int64 fromJsonObject(JsonObject jsonObject) {
        long data = jsonObject.containsKey(Int64.FIELD_DATA) ? jsonObject.getLong(Int64.FIELD_DATA) : 0L;
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
