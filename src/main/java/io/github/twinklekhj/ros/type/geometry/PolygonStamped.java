package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;


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
        super(jsonBuilder().put(PolygonStamped.FIELD_HEADER, header.getJsonObject()).put(PolygonStamped.FIELD_POLYGON, polygon.getJsonObject()), PolygonStamped.TYPE);
        this.header = header;
        this.polygon = polygon;
    }

    public static PolygonStamped fromJsonString(String jsonString) {
        return PolygonStamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static PolygonStamped fromMessage(RosMessage m) {
        return PolygonStamped.fromJsonObject(m.getJsonObject());
    }

    public static PolygonStamped fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(PolygonStamped.FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(PolygonStamped.FIELD_HEADER)) : new Header();
        Polygon polygon = jsonObject.containsKey(PolygonStamped.FIELD_POLYGON) ? Polygon.fromJsonObject(jsonObject.getJsonObject(PolygonStamped.FIELD_POLYGON)) : new Polygon();
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
