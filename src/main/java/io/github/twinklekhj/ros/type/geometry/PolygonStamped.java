package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class PolygonStamped extends RosMessage {
    public static final String TYPE = "geometry_msgs/PolygonStamped";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_POLYGON = "polygon";

    private Header header;
    private Polygon polygon;

    public PolygonStamped() {
        this(new Header(), new Polygon());
    }

    public PolygonStamped(Polygon polygon) {
        this(new Header(), polygon);
    }

    public PolygonStamped(Header header, Polygon polygon) {
        this.header = header;
        this.polygon = polygon;

        super.setJsonObject(jsonBuilder().put(FIELD_HEADER, header.getJsonObject()).put(FIELD_POLYGON, polygon.getJsonObject()));
        super.setType(TYPE);
    }

    public static PolygonStamped fromJsonString(String jsonString) {
        return PolygonStamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static PolygonStamped fromMessage(RosMessage m) {
        return PolygonStamped.fromJsonObject(m.getJsonObject());
    }

    public static PolygonStamped fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        Polygon polygon = jsonObject.containsKey(FIELD_POLYGON) ? Polygon.fromJsonObject(jsonObject.getJsonObject(FIELD_POLYGON)) : new Polygon();
        return new PolygonStamped(header, polygon);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    public Polygon getPolygon() {
        return this.polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
        this.jsonObject.put(FIELD_POLYGON, polygon.getJsonObject());
    }

    @Override
    public PolygonStamped clone() {
        return new PolygonStamped(this.header, this.polygon);
    }
}
