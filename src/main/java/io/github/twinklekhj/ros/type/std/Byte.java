package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;


public class Byte extends RosMessage {
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Byte";

    private final byte data;

    public Byte() {
        this((byte) 0);
    }

    public Byte(byte data) {
        super(jsonBuilder().put(Byte.FIELD_DATA, data), Byte.TYPE);
        this.data = data;
    }

    public static Byte fromJsonString(String jsonString) {
        return Byte.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Byte fromMessage(RosMessage m) {
        return Byte.fromJsonObject(m.getJsonObject());
    }

    public static Byte fromJsonObject(JsonObject jsonObject) {
        byte data = jsonObject.containsKey(Byte.FIELD_DATA) ? (byte) jsonObject.getInteger(Byte.FIELD_DATA).byteValue() : 0;
        return new Byte(data);
    }

    public byte getData() {
        return this.data;
    }

    @Override
    public Byte clone() {
        return new Byte(this.data);
    }
}
