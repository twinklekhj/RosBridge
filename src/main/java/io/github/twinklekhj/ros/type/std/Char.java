package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import io.vertx.core.json.JsonObject;


public class Char extends RosMessage {
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Char";

    private final byte data;

    public Char() {
        this((byte) 0);
    }

    public Char(byte data) {
        // build the JSON object
        super(jsonBuilder().put(Char.FIELD_DATA, Primitive.fromUInt8(data)), Char.TYPE);
        this.data = data;
    }

    public static Char fromJsonString(String jsonString) {
        return Char.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Char fromMessage(RosMessage m) {
        return Char.fromJsonObject(m.getJsonObject());
    }

    public static Char fromJsonObject(JsonObject jsonObject) {
        short data16 = jsonObject.containsKey(Char.FIELD_DATA) ? jsonObject.getInteger(Char.FIELD_DATA).shortValue() : 0;
        byte data8 = Primitive.toUInt8(data16);
        return new Char(data8);
    }

    public byte getData() {
        return this.data;
    }

    @Override
    public Char clone() {
        return new Char(this.data);
    }
}
