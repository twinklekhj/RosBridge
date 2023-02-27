package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import org.json.JSONObject;

public class TwistWithCovarianceStamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_TWIST = "twist";

    public static final String TYPE = "geometry_msgs/TwistWithCovarianceStamped";

    private final Header header;
    private final TwistWithCovariance twist;

    /**
     * Create a new TwistWithCovarianceStamped with all 0s.
     */
    public TwistWithCovarianceStamped() {
        this(new Header(), new TwistWithCovariance());
    }

    /**
     * Create a new TwistWithCovarianceStamped with the given values.
     *
     * @param header The header value of the twist.
     * @param twist  The twist value of the twist.
     */
    public TwistWithCovarianceStamped(Header header, TwistWithCovariance twist) {
        // build the JSON object
        super(jsonBuilder().put(TwistWithCovarianceStamped.FIELD_HEADER, header.toJSONObject()).put(TwistWithCovarianceStamped.FIELD_TWIST, twist.toJSONObject()), TwistWithCovarianceStamped.TYPE);
        this.header = header;
        this.twist = twist;
    }

    public static TwistWithCovarianceStamped fromJsonString(String jsonString) {
        return TwistWithCovarianceStamped.fromMessage(new RosMessage(jsonString));
    }

    public static TwistWithCovarianceStamped fromMessage(RosMessage m) {
        return TwistWithCovarianceStamped.fromJSONObject(m.toJSONObject());
    }

    public static TwistWithCovarianceStamped fromJSONObject(JSONObject jsonObject) {
        // check the fields
        Header header = jsonObject.has(TwistWithCovarianceStamped.FIELD_HEADER) ? Header.fromJSONObject(jsonObject.getJSONObject(TwistWithCovarianceStamped.FIELD_HEADER)) : new Header();
        TwistWithCovariance twist = jsonObject.has(TwistWithCovarianceStamped.FIELD_TWIST) ? TwistWithCovariance.fromJSONObject(jsonObject.getJSONObject(TwistWithCovarianceStamped.FIELD_TWIST)) : new TwistWithCovariance();
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
