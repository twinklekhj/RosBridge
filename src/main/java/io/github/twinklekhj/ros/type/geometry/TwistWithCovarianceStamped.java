package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;


public class TwistWithCovarianceStamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_TWIST = "twist";

    public static final String TYPE = "geometry_msgs/TwistWithCovarianceStamped";

    private final Header header;
    private final TwistWithCovariance twist;

    public TwistWithCovarianceStamped() {
        this(new Header(), new TwistWithCovariance());
    }

    public TwistWithCovarianceStamped(Header header, TwistWithCovariance twist) {
        super(jsonBuilder().put(TwistWithCovarianceStamped.FIELD_HEADER, header.getJsonObject()).put(TwistWithCovarianceStamped.FIELD_TWIST, twist.getJsonObject()), TwistWithCovarianceStamped.TYPE);
        this.header = header;
        this.twist = twist;
    }

    public static TwistWithCovarianceStamped fromJsonString(String jsonString) {
        return TwistWithCovarianceStamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static TwistWithCovarianceStamped fromMessage(RosMessage m) {
        return TwistWithCovarianceStamped.fromJsonObject(m.getJsonObject());
    }

    public static TwistWithCovarianceStamped fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(TwistWithCovarianceStamped.FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(TwistWithCovarianceStamped.FIELD_HEADER)) : new Header();
        TwistWithCovariance twist = jsonObject.containsKey(TwistWithCovarianceStamped.FIELD_TWIST) ? TwistWithCovariance.fromJsonObject(jsonObject.getJsonObject(TwistWithCovarianceStamped.FIELD_TWIST)) : new TwistWithCovariance();
        return new TwistWithCovarianceStamped(header, twist);
    }

    public Header getHeader() {
        return this.header;
    }

    public TwistWithCovariance getTwist() {
        return this.twist;
    }

    @Override
    public TwistWithCovarianceStamped clone() {
        return new TwistWithCovarianceStamped(this.header, this.twist);
    }
}
