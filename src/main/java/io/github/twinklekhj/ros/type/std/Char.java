package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import org.json.JSONObject;

public class Char extends RosMessage {
    public static final String FIELD_DATA = "data";

    public static final String TYPE = "std_msgs/Char";

    private final byte data;

    /**
     * Create a new Char with a default of 0.
     */
    public Char() {
        this((byte) 0);
    }

    /**
     * Create a new Char with the given data value treated as an unsigned 8-bit
     * integer.
     *
     * @param data The data value of the char.
     */
    public Char(byte data) {
        // build the JSON object
        super(jsonBuilder().put(Char.FIELD_DATA, Primitive.fromUInt8(data)), Char.TYPE);
        this.data = data;
    }

    public static Char fromJsonString(String jsonString) {
        return Char.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Char fromMessage(RosMessage m) {
        return Char.fromJSONObject(m.getJsonObject());
    }

    public static Char fromJSONObject(JSONObject jsonObject) {
        short data16 = jsonObject.has(Char.FIELD_DATA) ? (short) jsonObject.getInt(Char.FIELD_DATA) : 0;
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
