package ros.type.geometry;

import org.json.JSONObject;
import ros.type.RosMessage;
import ros.type.std.Header;

public class WrenchStamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_WRENCH = "wrench";

    public static final String TYPE = "geometry_msgs/WrenchStamped";

    private final Header header;
    private final Wrench wrench;

    /**
     * Create a new WrenchStamped with all 0s.
     */
    public WrenchStamped() {
        this(new Header(), new Wrench());
    }

    /**
     * Create a new WrenchStamped with the given values.
     *
     * @param header The header value of the wrench.
     * @param wrench The wrench value of the wrench.
     */
    public WrenchStamped(Header header, Wrench wrench) {
        // build the JSON object
        super(builder().put(WrenchStamped.FIELD_HEADER, header.toJSONObject()).put(WrenchStamped.FIELD_WRENCH, wrench.toJSONObject()), WrenchStamped.TYPE);
        this.header = header;
        this.wrench = wrench;
    }

    public static WrenchStamped fromJsonString(String jsonString) {
        return WrenchStamped.fromMessage(new RosMessage(jsonString));
    }

    public static WrenchStamped fromMessage(RosMessage m) {
        return WrenchStamped.fromJSONObject(m.toJSONObject());
    }

    public static WrenchStamped fromJSONObject(JSONObject jsonObject) {
        // check the fields
        Header header = jsonObject.has(WrenchStamped.FIELD_HEADER) ? Header.fromJSONObject(jsonObject.getJSONObject(WrenchStamped.FIELD_HEADER)) : new Header();
        Wrench wrench = jsonObject.has(WrenchStamped.FIELD_WRENCH) ? Wrench.fromJSONObject(jsonObject.getJSONObject(WrenchStamped.FIELD_WRENCH)) : new Wrench();
        return new WrenchStamped(header, wrench);
    }

    public Header getHeader() {
        return this.header;
    }

    public Wrench getWrench() {
        return this.wrench;
    }

    @Override
    public WrenchStamped clone() {
        return new WrenchStamped(this.header, this.wrench);
    }
}
