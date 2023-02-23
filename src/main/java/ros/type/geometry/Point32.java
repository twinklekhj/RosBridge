package ros.type.geometry;


import org.json.JSONObject;
import ros.type.RosMessage;

public class Point32 extends RosMessage {
    public static final String FIELD_X = "x";
    public static final String FIELD_Y = "y";
    public static final String FIELD_Z = "z";

    public static final String TYPE = "geometry_msgs/Point32";

    private final float x, y, z;

    public Point32() {
        this(0, 0, 0);
    }

    /**
     * Create a new Point32 with the given values.
     *
     * @param x The x value of the point.
     * @param y The y value of the point.
     * @param z The z value of the point.
     */
    public Point32(float x, float y, float z) {
        // build the JSON object
        super(builder().put(Point32.FIELD_X, x).put(Point32.FIELD_Y, y).put(Point32.FIELD_Z, z), Point32.TYPE);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Create a new Point32 based on the given JSON string. Any missing values
     * will be set to their defaults.
     *
     * @param jsonString The JSON string to parse.
     * @return A Point32 message based on the given JSON string.
     */
    public static Point32 fromJsonString(String jsonString) {
        // convert to a message
        return Point32.fromMessage(new RosMessage(jsonString));
    }

    /**
     * Create a new Point32 based on the given Message. Any missing values will
     * be set to their defaults.
     *
     * @param m The Message to parse.
     * @return A Point32 message based on the given Message.
     */
    public static Point32 fromMessage(RosMessage m) {
        // get it from the JSON object
        return Point32.fromJSONObject(m.toJSONObject());
    }

    /**
     * Create a new Point32 based on the given JSON object. Any missing values
     * will be set to their defaults.
     *
     * @param jsonObject The JSON object to parse.
     * @return A Point32 message based on the given JSON object.
     */
    public static Point32 fromJSONObject(JSONObject jsonObject) {
        // check the fields
        float x = jsonObject.has(Point32.FIELD_X) ? jsonObject.getFloat(Point32.FIELD_X) : 0.0f;
        float y = jsonObject.has(Point32.FIELD_Y) ? jsonObject.getFloat(Point32.FIELD_Y) : 0.0f;
        float z = jsonObject.has(Point32.FIELD_Z) ? jsonObject.getFloat(Point32.FIELD_Z) : 0.0f;
        return new Point32(x, y, z);
    }

    /**
     * Get the x value of this point.
     *
     * @return The x value of this point.
     */
    public float getX() {
        return this.x;
    }

    /**
     * Get the y value of this point.
     *
     * @return The y value of this point.
     */
    public float getY() {
        return this.y;
    }

    /**
     * Get the z value of this point.
     *
     * @return The z value of this point.
     */
    public float getZ() {
        return this.z;
    }

    /**
     * Create a clone of this Point32.
     */
    @Override
    public Point32 clone() {
        return new Point32(this.x, this.y, this.z);
    }
}
