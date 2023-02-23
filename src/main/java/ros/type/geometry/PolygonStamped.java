package ros.type.geometry;


import org.json.JSONObject;
import ros.type.RosMessage;
import ros.type.std.Header;

public class PolygonStamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_POLYGON = "polygon";

    public static final String TYPE = "geometry_msgs/PolygonStamped";

    private final Header header;
    private final Polygon polygon;

    public PolygonStamped() {
        this(new Header(), new Polygon());
    }

    /**
     * Create a new PolygonStamped with the given values.
     *
     * @param header  The header value of the polygon.
     * @param polygon The polygon value of the polygon.
     */
    public PolygonStamped(Header header, Polygon polygon) {
        // build the JSON object
        super(builder().put(PolygonStamped.FIELD_HEADER, header.toJSONObject()).put(PolygonStamped.FIELD_POLYGON, polygon.toJSONObject()), PolygonStamped.TYPE);
        this.header = header;
        this.polygon = polygon;
    }

    public static PolygonStamped fromJsonString(String jsonString) {
        return PolygonStamped.fromMessage(new RosMessage(jsonString));
    }

    public static PolygonStamped fromMessage(RosMessage m) {
        return PolygonStamped.fromJSONObject(m.toJSONObject());
    }

    public static PolygonStamped fromJSONObject(JSONObject jsonObject) {
        Header header = jsonObject.has(PolygonStamped.FIELD_HEADER) ? Header.fromJSONObject(jsonObject.getJSONObject(PolygonStamped.FIELD_HEADER)) : new Header();
        Polygon polygon = jsonObject.has(PolygonStamped.FIELD_POLYGON) ? Polygon.fromJSONObject(jsonObject.getJSONObject(PolygonStamped.FIELD_POLYGON)) : new Polygon();
        return new PolygonStamped(header, polygon);
    }

    public Header getHeader() {
        return this.header;
    }

    public Polygon getPolygon() {
        return this.polygon;
    }

    @Override
    public PolygonStamped clone() {
        return new PolygonStamped(this.header, this.polygon);
    }
}
