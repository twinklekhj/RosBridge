package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;


public class QuaternionStamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_QUATERNION = "quaternion";

    public static final String TYPE = "geometry_msgs/QuaternionStamped";

    private final Header header;
    private final Quaternion quaternion;

    public QuaternionStamped() {
        this(new Header(), new Quaternion());
    }

    public QuaternionStamped(Header header, Quaternion quaternion) {
        super(jsonBuilder().put(QuaternionStamped.FIELD_HEADER, header.getJsonObject()).put(QuaternionStamped.FIELD_QUATERNION, quaternion.getJsonObject()), QuaternionStamped.TYPE);
        this.header = header;
        this.quaternion = quaternion;
    }

    public static QuaternionStamped fromJsonString(String jsonString) {
        return QuaternionStamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static QuaternionStamped fromMessage(RosMessage m) {
        return QuaternionStamped.fromJsonObject(m.getJsonObject());
    }

    public static QuaternionStamped fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(QuaternionStamped.FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(QuaternionStamped.FIELD_HEADER)) : new Header();
        Quaternion quaternion = jsonObject.containsKey(QuaternionStamped.FIELD_QUATERNION) ? Quaternion.fromJsonObject(jsonObject.getJsonObject(QuaternionStamped.FIELD_QUATERNION)) : new Quaternion();
        return new QuaternionStamped(header, quaternion);
    }

    public Header getHeader() {
        return this.header;
    }

    public Quaternion getQuaternion() {
        return this.quaternion;
    }

    @Override
    public QuaternionStamped clone() {
        return new QuaternionStamped(this.header, this.quaternion);
    }
}
