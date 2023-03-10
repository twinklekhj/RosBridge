package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;

public class PointStamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_POINT = "point";

    public static final String TYPE = "geometry_msgs/PointStamped";

    private final Header header;
    private final Point point;

    public PointStamped() {
        this(new Header(), new Point());
    }

    public PointStamped(Header header, Point point) {
        // build the JSON object
        super(jsonBuilder().put(PointStamped.FIELD_HEADER, header.getJsonObject()).put(PointStamped.FIELD_POINT, point.getJsonObject()), PointStamped.TYPE);
        this.header = header;
        this.point = point;
    }

    public static PointStamped fromJsonString(String jsonString) {
        return PointStamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static PointStamped fromMessage(RosMessage m) {
        return PointStamped.fromJsonObject(m.getJsonObject());
    }

    public static PointStamped fromJsonObject(JsonObject jsonObject) {
        // check the fields
        Header header = jsonObject.containsKey(PointStamped.FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(PointStamped.FIELD_HEADER)) : new Header();
        Point point = jsonObject.containsKey(PointStamped.FIELD_POINT) ? Point.fromJsonObject(jsonObject.getJsonObject(PointStamped.FIELD_POINT)) : new Point();
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
