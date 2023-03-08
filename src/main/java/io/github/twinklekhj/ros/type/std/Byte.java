package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONObject;

public class Byte extends RosMessage {
    public static final String FIELD_DATA = "data";

    /**
     * The message type.
     */
    public static final String TYPE = "std_msgs/Byte";

    private final byte data;

    /**
     * Create a new Byte with a default of 0.
     */
    public Byte() {
        this((byte) 0);
    }

    /**
     * Create a new Byte with the given data value.
     *
     * @param data The data value of the byte.
     */
    public Byte(byte data) {
        super(jsonBuilder().put(Byte.FIELD_DATA, data), Byte.TYPE);
        this.data = data;
    }

    public static Byte fromJsonString(String jsonString) {
        return Byte.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Byte fromMessage(RosMessage m) {
        // get it from the JSON object
        return Byte.fromJSONObject(m.getJsonObject());
    }

    public static Byte fromJSONObject(JSONObject jsonObject) {
        byte data = jsonObject.has(Byte.FIELD_DATA) ? (byte) jsonObject.getInt(Byte.FIELD_DATA) : 0;
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
