package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Int32 extends RosMessage {
    public static final String TYPE = "std_msgs/Int32";
    public static final String FIELD_DATA = "data";

    private final int data;

    public Int32() {
        this(0);
    }

    public Int32(int data) {
        super(jsonBuilder().put(Int32.FIELD_DATA, data), Int32.TYPE);
        this.data = data;
    }

    public static Int32 fromJsonString(String jsonString) {
        return Int32.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Int32 fromMessage(RosMessage m) {
        return Int32.fromJsonObject(m.getJsonObject());
    }

    public static Int32 fromJsonObject(JsonObject jsonObject) {
        int data = jsonObject.containsKey(Int32.FIELD_DATA) ? jsonObject.getInteger(Int32.FIELD_DATA) : 0;
        return new Int32(data);
    }

    public int getData() {
        return this.data;
    }

    @Override
    public Int32 clone() {
        return new Int32(this.data);
    }
}
