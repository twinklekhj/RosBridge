package io.github.twinklekhj.ros.type.costmap;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.geometry.Polygon;
import io.github.twinklekhj.ros.type.geometry.Quaternion;
import io.github.twinklekhj.ros.type.geometry.TwistWithCovariance;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class ObstacleMsg extends RosMessage {
    public static final String TYPE = "costmap_converter/ObstacleMsg";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_POLYGON = "polygon";
    public static final String FIELD_RADIUS = "radius";
    public static final String FIELD_ID = "id";
    public static final String FIELD_ORIENTATION = "orientation";
    public static final String FIELD_VELOCITIES = "velocities";

    // Specify the radius for circular/point obstacles
    private double radius;
    // Obstacle ID - Specify IDs in order to provide (temporal) relationships between obstacles among multiple messages.
    private long id;
    // Individual velocities (centroid)
    private TwistWithCovariance velocities;
    private Header header;
    // Obstacle footprint (polygon descriptions)
    private Polygon polygon;
    // Individual orientation (centroid)
    private Quaternion orientation;

    public ObstacleMsg() {
        this(new Header(), new Polygon(), 0, 0, new Quaternion(), new TwistWithCovariance());
    }

    public ObstacleMsg(Polygon polygon, double radius, long id, Quaternion orientation, TwistWithCovariance velocities) {
        this(new Header(), polygon, radius, id, orientation, velocities);
    }

    public ObstacleMsg(Header header, Polygon polygon, double radius, long id, Quaternion orientation, TwistWithCovariance velocities) {
        this.header = header;
        this.polygon = polygon;
        this.radius = radius;
        this.id = id;
        this.orientation = orientation;
        this.velocities = velocities;

        JsonObject json = jsonBuilder()
                .put(FIELD_HEADER, header.getJsonObject())
                .put(FIELD_POLYGON, polygon.getJsonObject())
                .put(FIELD_ORIENTATION, orientation.getJsonObject())
                .put(FIELD_ID, id).put(FIELD_RADIUS, radius)
                .put(FIELD_VELOCITIES, velocities.getJsonObject());

        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static ObstacleMsg fromJsonString(String jsonString) {
        return ObstacleMsg.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static ObstacleMsg fromMessage(RosMessage m) {
        return ObstacleMsg.fromJsonObject(m.getJsonObject());
    }

    public static ObstacleMsg fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        Polygon polygon = jsonObject.containsKey(FIELD_POLYGON) ? Polygon.fromJsonObject(jsonObject.getJsonObject(FIELD_POLYGON)) : new Polygon();
        double radius = jsonObject.containsKey(FIELD_RADIUS) ? jsonObject.getDouble(FIELD_RADIUS) : 0;
        long id = jsonObject.containsKey(FIELD_ID) ? jsonObject.getLong(FIELD_ID) : 0;
        Quaternion orientation = jsonObject.containsKey(FIELD_ORIENTATION) ? Quaternion.fromJsonObject(jsonObject.getJsonObject(FIELD_ORIENTATION)) : new Quaternion();
        TwistWithCovariance velocities = jsonObject.containsKey(FIELD_VELOCITIES) ? TwistWithCovariance.fromJsonObject(jsonObject.getJsonObject(FIELD_VELOCITIES)) : new TwistWithCovariance();

        return new ObstacleMsg(header, polygon, radius, id, orientation, velocities);
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

    public Quaternion getOrientation() {
        return this.orientation;
    }

    public void setOrientation(Quaternion orientation) {
        this.orientation = orientation;
        this.jsonObject.put(FIELD_ORIENTATION, orientation.getJsonObject());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        this.jsonObject.put(FIELD_ID, id);
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        this.jsonObject.put(FIELD_RADIUS, radius);
    }

    public TwistWithCovariance getVelocities() {
        return velocities;
    }

    public void setVelocities(TwistWithCovariance velocities) {
        this.velocities = velocities;
        this.jsonObject.put(FIELD_VELOCITIES, velocities.getJsonObject());
    }

    @Override
    public ObstacleMsg clone() {
        return new ObstacleMsg(this.header, this.polygon, this.radius, this.id, this.orientation, this.velocities);
    }
}
