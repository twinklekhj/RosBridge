package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import org.json.JSONObject;

public class Vector3Stamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_VECTOR = "vector";

    public static final String TYPE = "geometry_msgs/Vector3Stamped";

    private final Header header;
    private final Vector3 vector;

    /**
     * Create a new Vector3Stamped with all 0s.
     */
    public Vector3Stamped() {
        this(new Header(), new Vector3());
    }

    /**
     * Create a new Vector3Stamped with the given values.
     *
     * @param header The header value of the vector.
     * @param vector The vector value of the vector.
     */
    public Vector3Stamped(Header header, Vector3 vector) {
        // build the JSON object
        super(jsonBuilder().put(Vector3Stamped.FIELD_HEADER, header.getJsonObject()).put(Vector3Stamped.FIELD_VECTOR, vector.getJsonObject()), Vector3Stamped.TYPE);
        this.header = header;
        this.vector = vector;
    }

    public static Vector3Stamped fromJsonString(String jsonString) {
        return Vector3Stamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Vector3Stamped fromMessage(RosMessage m) {
        return Vector3Stamped.fromJSONObject(m.getJsonObject());
    }

    /**
     * Create a new Vector3Stamped based on the given JSON object. Any missing
     * values will be set to their defaults.
     *
     * @param jsonObject The JSON object to parse.
     * @return A Vector3Stamped message based on the given JSON object.
     */
    public static Vector3Stamped fromJSONObject(JSONObject jsonObject) {
        // check the fields
        Header header = jsonObject.has(Vector3Stamped.FIELD_HEADER) ? Header.fromJSONObject(jsonObject.getJSONObject(Vector3Stamped.FIELD_HEADER)) : new Header();
        Vector3 vector = jsonObject.has(Vector3Stamped.FIELD_VECTOR) ? Vector3.fromJSONObject(jsonObject.getJSONObject(Vector3Stamped.FIELD_VECTOR)) : new Vector3();
        return new Vector3Stamped(header, vector);
    }

    public Header getHeader() {
        return this.header;
    }

    public Vector3 getVector3() {
        return this.vector;
    }

    @Override
    public Vector3Stamped clone() {
        return new Vector3Stamped(this.header, this.vector);
    }
}
