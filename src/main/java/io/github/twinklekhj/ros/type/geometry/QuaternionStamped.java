package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class QuaternionStamped extends RosMessage {
    public static final String TYPE = "geometry_msgs/QuaternionStamped";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_QUATERNION = "quaternion";

    private Header header;
    private Quaternion quaternion;

    public QuaternionStamped() {
        this(new Header(), new Quaternion());
    }

    public QuaternionStamped(Quaternion quaternion) {
        this(new Header(), quaternion);
    }

    public QuaternionStamped(Header header, Quaternion quaternion) {
        this.header = header;
        this.quaternion = quaternion;

        super.setJsonObject(jsonBuilder().put(FIELD_HEADER, header.getJsonObject()).put(FIELD_QUATERNION, quaternion.getJsonObject()));
        super.setType(TYPE);
    }

    public static QuaternionStamped fromJsonString(String jsonString) {
        return QuaternionStamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static QuaternionStamped fromMessage(RosMessage m) {
        return QuaternionStamped.fromJsonObject(m.getJsonObject());
    }

    public static QuaternionStamped fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        Quaternion quaternion = jsonObject.containsKey(FIELD_QUATERNION) ? Quaternion.fromJsonObject(jsonObject.getJsonObject(FIELD_QUATERNION)) : new Quaternion();
        return new QuaternionStamped(header, quaternion);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    public Quaternion getQuaternion() {
        return this.quaternion;
    }

    public void setQuaternion(Quaternion quaternion) {
        this.quaternion = quaternion;
        this.jsonObject.put(FIELD_QUATERNION, quaternion.getJsonObject());
    }

    @Override
    public QuaternionStamped clone() {
        return new QuaternionStamped(this.header, this.quaternion);
    }
}
