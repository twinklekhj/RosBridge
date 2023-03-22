package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class PointStamped extends RosMessage {
    public static final String TYPE = "geometry_msgs/PointStamped";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_POINT = "point";


    private Header header;
    private Point point;

    public PointStamped() {
        this(new Header(), new Point());
    }

    public PointStamped(Point point) {
        this(new Header(), point);
    }

    public PointStamped(Header header, Point point) {
        this.header = header;
        this.point = point;

        super.setJsonObject(jsonBuilder().put(PointStamped.FIELD_HEADER, header.getJsonObject()).put(PointStamped.FIELD_POINT, point.getJsonObject()));
        super.setType(TYPE);
    }

    public static PointStamped fromJsonString(String jsonString) {
        return PointStamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static PointStamped fromMessage(RosMessage m) {
        return PointStamped.fromJsonObject(m.getJsonObject());
    }

    public static PointStamped fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(PointStamped.FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(PointStamped.FIELD_HEADER)) : new Header();
        Point point = jsonObject.containsKey(PointStamped.FIELD_POINT) ? Point.fromJsonObject(jsonObject.getJsonObject(PointStamped.FIELD_POINT)) : new Point();
        return new PointStamped(header, point);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    public Point getPoint() {
        return this.point;
    }

    public void setPoint(Point point) {
        this.point = point;
        this.jsonObject.put(FIELD_POINT, point.getJsonObject());
    }

    @Override
    public PointStamped clone() {
        return new PointStamped(this.header, this.point);
    }
}
