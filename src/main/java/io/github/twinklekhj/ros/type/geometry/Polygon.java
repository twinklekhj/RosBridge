package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ToString
public class Polygon extends RosMessage {
    public static final String TYPE = "geometry_msgs/Polygon";

    public static final String FIELD_POINTS = "points";

    private Point32[] points;

    public Polygon() {
        this(new Point32[]{});
    }

    public Polygon(Point32... points) {
        this.points = new Point32[points.length];
        System.arraycopy(points, 0, this.points, 0, points.length);

        super.setJsonObject(jsonBuilder()
                .put(FIELD_POINTS, getArray(this.points)));
        super.setType(TYPE);
    }

    public static Polygon fromJsonString(String jsonString) {
        return Polygon.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Polygon fromMessage(RosMessage m) {
        return Polygon.fromJsonObject(m.getJsonObject());
    }

    public static Polygon fromJsonObject(JsonObject jsonObject) {
        JsonArray jsonPoints = jsonObject.getJsonArray(FIELD_POINTS);

        if (jsonPoints != null) {
            Point32[] points = new Point32[jsonPoints.size()];
            for (int i = 0; i < points.length; i++) {
                points[i] = Point32.fromJsonObject(jsonPoints.getJsonObject(i));
            }
            return new Polygon(points);
        } else {
            return new Polygon();
        }
    }

    public int size() {
        return this.points.length;
    }

    public Point32 get(int index) {
        return this.points[index];
    }

    public Point32[] getPoints() {
        return this.points;
    }

    public void setPoints(Point32... points) {
        this.points = new Point32[points.length];
        System.arraycopy(points, 0, this.points, 0, points.length);

        this.jsonObject.put(FIELD_POINTS, getArray(this.points));
    }

    public void setPoints(List<Point32> points) {
        this.points = new Point32[points.size()];
        for(int i=0; i< points.size(); i++){
            this.points[i] = points.get(0);
        }
        this.jsonObject.put(FIELD_POINTS, getArray(this.points));
    }

    @Override
    public Polygon clone() {
        return new Polygon(this.points);
    }
}
