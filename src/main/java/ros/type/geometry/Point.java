package ros.type.geometry;

import org.json.JSONObject;
import ros.type.RosMessage;

public class Point extends RosMessage {
    public static final String FIELD_X = "x";
    public static final String FIELD_Y = "y";
    public static final String FIELD_Z = "z";


    public static final String TYPE = "geometry_msgs/Point";

    private final double x, y, z;

    public Point() {
        this(0, 0, 0);
    }

    /**
     * Create a new Point with the given values.
     *
     * @param x The x value of the point.
     * @param y The y value of the point.
     * @param z The z value of the point.
     */
    public Point(double x, double y, double z) {
        // build the JSON object
        super(builder().put(Point.FIELD_X, x).put(Point.FIELD_Y, y).put(Point.FIELD_Z, z), Point.TYPE);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Point fromJsonString(String jsonString) {
        return Point.fromMessage(new RosMessage(jsonString));
    }

    public static Point fromMessage(RosMessage m) {
        // get it from the JSON object
        return Point.fromJSONObject(m.toJSONObject());
    }

    public static Point fromJSONObject(JSONObject jsonObject) {
        // check the fields
        double x = jsonObject.has(Point.FIELD_X) ? jsonObject.getDouble(Point.FIELD_X) : 0.0;
        double y = jsonObject.has(Point.FIELD_Y) ? jsonObject.getDouble(Point.FIELD_Y) : 0.0;
        double z = jsonObject.has(Point.FIELD_Z) ? jsonObject.getDouble(Point.FIELD_Z) : 0.0;
        return new Point(x, y, z);
    }

    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public double getZ() {
        return this.z;
    }


    @Override
    public Point clone() {
        return new Point(this.x, this.y, this.z);
    }
}
