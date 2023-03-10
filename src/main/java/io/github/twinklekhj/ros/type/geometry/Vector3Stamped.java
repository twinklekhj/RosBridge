package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;


public class Vector3Stamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_VECTOR = "vector";

    public static final String TYPE = "geometry_msgs/Vector3Stamped";

    private final Header header;
    private final Vector3 vector;

    public Vector3Stamped() {
        this(new Header(), new Vector3());
    }

    public Vector3Stamped(Header header, Vector3 vector) {
        super(jsonBuilder().put(Vector3Stamped.FIELD_HEADER, header.getJsonObject()).put(Vector3Stamped.FIELD_VECTOR, vector.getJsonObject()), Vector3Stamped.TYPE);
        this.header = header;
        this.vector = vector;
    }

    public static Vector3Stamped fromJsonString(String jsonString) {
        return Vector3Stamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Vector3Stamped fromMessage(RosMessage m) {
        return Vector3Stamped.fromJsonObject(m.getJsonObject());
    }

    public static Vector3Stamped fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(Vector3Stamped.FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(Vector3Stamped.FIELD_HEADER)) : new Header();
        Vector3 vector = jsonObject.containsKey(Vector3Stamped.FIELD_VECTOR) ? Vector3.fromJsonObject(jsonObject.getJsonObject(Vector3Stamped.FIELD_VECTOR)) : new Vector3();
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
