package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class Polygon extends RosMessage {
    public static final String FIELD_POINTS = "points";
    public static final String TYPE = "geometry_msgs/Polygon";

    private final Point32[] points;

    /**
     * Create a new Polygon with no points.
     */
    public Polygon() {
        this(new Point32[]{});
    }

    /**
     * Create a new Polygon with the given set of points. The array of points
     * will be copied into this object.
     *
     * @param points The points of the polygon.
     */
    public Polygon(Point32[] points) {
        super(jsonBuilder().put(Polygon.FIELD_POINTS, jsonBuilder(Arrays.deepToString(points))), Polygon.TYPE);

        this.points = new Point32[points.length];
        System.arraycopy(points, 0, this.points, 0, points.length);
    }

    public static Polygon fromJsonString(String jsonString) {
        return Polygon.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Polygon fromMessage(RosMessage m) {
        return Polygon.fromJSONObject(m.getJsonObject());
    }

    public static Polygon fromJSONObject(JSONObject jsonObject) {
        JSONArray jsonPoints = jsonObject.getJSONArray(Polygon.FIELD_POINTS);

        if (jsonPoints != null) {
            // convert each point
            Point32[] points = new Point32[jsonPoints.length()];
            for (int i = 0; i < points.length; i++) {
                points[i] = Point32.fromJSONObject(jsonPoints.getJSONObject(i));
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

    @Override
    public Polygon clone() {
        return new Polygon(this.points);
    }
}
