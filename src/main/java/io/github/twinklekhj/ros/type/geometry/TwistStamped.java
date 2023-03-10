package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;


public class TwistStamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_TWIST = "twist";

    public static final String TYPE = "geometry_msgs/TwistStamped";

    private final Header header;
    private final Twist twist;

    public TwistStamped() {
        this(new Header(), new Twist());
    }

    public TwistStamped(Header header, Twist twist) {
        // build the JSON object
        super(jsonBuilder().put(TwistStamped.FIELD_HEADER, header.getJsonObject()).put(TwistStamped.FIELD_TWIST, twist.getJsonObject()), TwistStamped.TYPE);
        this.header = header;
        this.twist = twist;
    }

    public static TwistStamped fromJsonString(String jsonString) {
        return TwistStamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static TwistStamped fromMessage(RosMessage m) {
        return TwistStamped.fromJsonObject(m.getJsonObject());
    }

    public static TwistStamped fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(TwistStamped.FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(TwistStamped.FIELD_HEADER)) : new Header();
        Twist twist = jsonObject.containsKey(TwistStamped.FIELD_TWIST) ? Twist.fromJsonObject(jsonObject.getJsonObject(TwistStamped.FIELD_TWIST)) : new Twist();
        return new TwistStamped(header, twist);
    }

    public Header getHeader() {
        return this.header;
    }

    public Twist getTwist() {
        return this.twist;
    }

    @Override
    public TwistStamped clone() {
        return new TwistStamped(this.header, this.twist);
    }
}
