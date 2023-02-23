package ros.type.geometry;

import ros.type.RosMessage;
import ros.type.std.Header;


/**
 * The geometry_msgs/PointStamped message. This represents a Point with
 * reference coordinate frame and timestamp.
 *
 * @author Russell Toris -- rctoris@wpi.edu
 * @version April 1, 2014
 */
public class PointStamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_POINT = "point";

    /**
     * The message type.
     */
    public static final String TYPE = "geometry_msgs/PointStamped";

    private final Header header;
    private final Point point;

    /**
     * Create a new PointStamped with all 0s.
     */
    public PointStamped() {
        this(new Header(), new Point());
    }

    /**
     * Create a new PointStamped with the given values.
     *
     * @param header The header value of the point.
     * @param point  The point value of the point.
     */
    public PointStamped(Header header, Point point) {
        // build the JSON object
        super(builder().put(PointStamped.FIELD_HEADER, header.toJSONObject()).put(PointStamped.FIELD_POINT, point.toJSONObject()), PointStamped.TYPE);
        this.header = header;
        this.point = point;
    }

    public static PointStamped fromJsonString(String jsonString) {
        return PointStamped.fromMessage(new RosMessage(jsonString));
    }

    public static PointStamped fromMessage(RosMessage m) {
        return PointStamped.fromJSONObject(m.toJSONObject());
    }

    public static PointStamped fromJSONObject(org.json.JSONObject jsonObject) {
        // check the fields
        Header header = jsonObject.has(PointStamped.FIELD_HEADER) ? Header.fromJSONObject(jsonObject.getJSONObject(PointStamped.FIELD_HEADER)) : new Header();
        Point point = jsonObject.has(PointStamped.FIELD_POINT) ? Point.fromJSONObject(jsonObject.getJSONObject(PointStamped.FIELD_POINT)) : new Point();
        return new PointStamped(header, point);
    }

    public Header getHeader() {
        return this.header;
    }

    public Point getPoint() {
        return this.point;
    }

    @Override
    public PointStamped clone() {
        return new PointStamped(this.header, this.point);
    }
}
