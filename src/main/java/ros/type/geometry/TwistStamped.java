package ros.type.geometry;


import org.json.JSONObject;
import ros.type.RosMessage;
import ros.type.std.Header;

public class TwistStamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_TWIST = "twist";

    public static final String TYPE = "geometry_msgs/TwistStamped";

    private final Header header;
    private final Twist twist;

    /**
     * Create a new TwistStamped with all 0s.
     */
    public TwistStamped() {
        this(new Header(), new Twist());
    }

    /**
     * Create a new TwistStamped with the given values.
     *
     * @param header The header value of the twist.
     * @param twist  The twist value of the twist.
     */
    public TwistStamped(Header header, Twist twist) {
        // build the JSON object
        super(builder().put(TwistStamped.FIELD_HEADER, header.toJSONObject()).put(TwistStamped.FIELD_TWIST, twist.toJSONObject()), TwistStamped.TYPE);
        this.header = header;
        this.twist = twist;
    }

    public static TwistStamped fromJsonString(String jsonString) {
        return TwistStamped.fromMessage(new RosMessage(jsonString));
    }


    public static TwistStamped fromMessage(RosMessage m) {
        // get it from the JSON object
        return TwistStamped.fromJSONObject(m.toJSONObject());
    }

    public static TwistStamped fromJSONObject(JSONObject jsonObject) {
        // check the fields
        Header header = jsonObject.has(TwistStamped.FIELD_HEADER) ? Header.fromJSONObject(jsonObject.getJSONObject(TwistStamped.FIELD_HEADER)) : new Header();
        Twist twist = jsonObject.has(TwistStamped.FIELD_TWIST) ? Twist.fromJSONObject(jsonObject.getJSONObject(TwistStamped.FIELD_TWIST)) : new Twist();
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
