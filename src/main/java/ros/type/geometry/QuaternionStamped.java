package ros.type.geometry;


import org.json.JSONObject;
import ros.type.RosMessage;
import ros.type.std.Header;


public class QuaternionStamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_QUATERNION = "quaternion";

    public static final String TYPE = "geometry_msgs/QuaternionStamped";

    private final Header header;
    private final Quaternion quaternion;

    /**
     * Create a new QuaternionStamped with all 0s.
     */
    public QuaternionStamped() {
        this(new Header(), new Quaternion());
    }

    /**
     * Create a new QuaternionStamped with the given values.
     *
     * @param header     The header value of the quaternion.
     * @param quaternion The quaternion value of the quaternion.
     */
    public QuaternionStamped(Header header, Quaternion quaternion) {
        // build the JSON object
        super(builder().put(QuaternionStamped.FIELD_HEADER, header.toJSONObject()).put(QuaternionStamped.FIELD_QUATERNION, quaternion.toJSONObject()), QuaternionStamped.TYPE);
        this.header = header;
        this.quaternion = quaternion;
    }

    public static QuaternionStamped fromJsonString(String jsonString) {
        // convert to a message
        return QuaternionStamped.fromMessage(new RosMessage(jsonString));
    }

    public static QuaternionStamped fromMessage(RosMessage m) {
        // get it from the JSON object
        return QuaternionStamped.fromJSONObject(m.toJSONObject());
    }

    public static QuaternionStamped fromJSONObject(JSONObject jsonObject) {
        // check the fields
        Header header = jsonObject.has(QuaternionStamped.FIELD_HEADER) ? Header.fromJSONObject(jsonObject.getJSONObject(QuaternionStamped.FIELD_HEADER)) : new Header();
        Quaternion quaternion = jsonObject.has(QuaternionStamped.FIELD_QUATERNION) ? Quaternion.fromJSONObject(jsonObject.getJSONObject(QuaternionStamped.FIELD_QUATERNION)) : new Quaternion();
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
