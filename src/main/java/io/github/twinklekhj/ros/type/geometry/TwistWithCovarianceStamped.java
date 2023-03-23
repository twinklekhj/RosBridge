package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class TwistWithCovarianceStamped extends RosMessage {
    public static final String TYPE = "geometry_msgs/TwistWithCovarianceStamped";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_TWIST = "twist";

    private Header header;
    private TwistWithCovariance twist;

    public TwistWithCovarianceStamped() {
        this(new Header(), new TwistWithCovariance());
    }

    public TwistWithCovarianceStamped(TwistWithCovariance twist) {
        this(new Header(), twist);
    }

    public TwistWithCovarianceStamped(Header header, TwistWithCovariance twist) {
        this.header = header;
        this.twist = twist;

        super.setJsonObject(jsonBuilder().put(FIELD_HEADER, header.getJsonObject()).put(FIELD_TWIST, twist.getJsonObject()));
        super.setType(TYPE);
    }

    public static TwistWithCovarianceStamped fromJsonString(String jsonString) {
        return TwistWithCovarianceStamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static TwistWithCovarianceStamped fromMessage(RosMessage m) {
        return TwistWithCovarianceStamped.fromJsonObject(m.getJsonObject());
    }

    public static TwistWithCovarianceStamped fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        TwistWithCovariance twist = jsonObject.containsKey(FIELD_TWIST) ? TwistWithCovariance.fromJsonObject(jsonObject.getJsonObject(FIELD_TWIST)) : new TwistWithCovariance();
        return new TwistWithCovarianceStamped(header, twist);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    public TwistWithCovariance getTwist() {
        return this.twist;
    }

    public void setTwist(TwistWithCovariance twist) {
        this.twist = twist;
        this.jsonObject.put(FIELD_TWIST, twist.getJsonObject());
    }

    @Override
    public TwistWithCovarianceStamped clone() {
        return new TwistWithCovarianceStamped(this.header, this.twist);
    }
}
