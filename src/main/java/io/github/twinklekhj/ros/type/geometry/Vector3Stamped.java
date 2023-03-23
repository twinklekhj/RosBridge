package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Vector3Stamped extends RosMessage {
    public static final String TYPE = "geometry_msgs/Vector3Stamped";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_VECTOR = "vector";

    private Header header;
    private Vector3 vector;

    public Vector3Stamped() {
        this(new Header(), new Vector3());
    }

    public Vector3Stamped(Vector3 vector) {
        this(new Header(), vector);
    }

    public Vector3Stamped(Header header, Vector3 vector) {
        this.header = header;
        this.vector = vector;

        super.setJsonObject(jsonBuilder().put(FIELD_HEADER, header.getJsonObject()).put(FIELD_VECTOR, vector.getJsonObject()));
        super.setType(TYPE);
    }

    public static Vector3Stamped fromJsonString(String jsonString) {
        return Vector3Stamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Vector3Stamped fromMessage(RosMessage m) {
        return Vector3Stamped.fromJsonObject(m.getJsonObject());
    }

    public static Vector3Stamped fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        Vector3 vector = jsonObject.containsKey(FIELD_VECTOR) ? Vector3.fromJsonObject(jsonObject.getJsonObject(FIELD_VECTOR)) : new Vector3();
        return new Vector3Stamped(header, vector);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    public Vector3 getVector3() {
        return this.vector;
    }

    public void setVector(Vector3 vector) {
        this.vector = vector;
        this.jsonObject.put(FIELD_VECTOR, vector.getJsonObject());
    }

    @Override
    public Vector3Stamped clone() {
        return new Vector3Stamped(this.header, this.vector);
    }
}
