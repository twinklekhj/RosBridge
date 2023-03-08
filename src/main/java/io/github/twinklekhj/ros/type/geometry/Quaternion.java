package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONObject;

public class Quaternion extends RosMessage {
    public static final String FIELD_X = "x";
    public static final String FIELD_Y = "y";
    public static final String FIELD_Z = "z";
    public static final String FIELD_W = "w";

    public static final String TYPE = "geometry_msgs/Quaternion";

    private final double x, y, z, w;

    public Quaternion() {
        this(0, 0, 0, 0);
    }

    /**
     * Create a new quaternion with the given values.
     *
     * @param x The x value of the quaternion.
     * @param y The y value of the quaternion.
     * @param z The z value of the quaternion.
     * @param w The w value of the quaternion.
     */
    public Quaternion(double x, double y, double z, double w) {
        // build the JSON object
        super(jsonBuilder().put(Quaternion.FIELD_X, x).put(Quaternion.FIELD_Y, y).put(Quaternion.FIELD_Z, z).put(Quaternion.FIELD_W, w), Quaternion.TYPE);
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Create a new Quaternion based on the given JSON string. Any missing
     * values will be set to their defaults.
     *
     * @param jsonString The JSON string to parse.
     * @return A Quaternion message based on the given JSON string.
     */
    public static Quaternion fromJsonString(String jsonString) {
        // convert to a message
        return Quaternion.fromMessage(new RosMessage(jsonString, TYPE));
    }

    /**
     * Create a new Quaternion based on the given Message. Any missing values
     * will be set to their defaults.
     *
     * @param m The Message to parse.
     * @return A Quaternion message based on the given Message.
     */
    public static Quaternion fromMessage(RosMessage m) {
        // get it from the JSON object
        return Quaternion.fromJSONObject(m.getJsonObject());
    }

    /**
     * Create a new Quaternion based on the given JSON object. Any missing
     * values will be set to their defaults.
     *
     * @param jsonObject The JSON object to parse.
     * @return A Quaternion message based on the given JSON object.
     */
    public static Quaternion fromJSONObject(JSONObject jsonObject) {
        // check the fields
        double x = jsonObject.has(Quaternion.FIELD_X) ? jsonObject.getDouble(Quaternion.FIELD_X) : 0.0;
        double y = jsonObject.has(Quaternion.FIELD_Y) ? jsonObject.getDouble(Quaternion.FIELD_Y) : 0.0;
        double z = jsonObject.has(Quaternion.FIELD_Z) ? jsonObject.getDouble(Quaternion.FIELD_Z) : 0.0;
        double w = jsonObject.has(Quaternion.FIELD_W) ? jsonObject.getDouble(Quaternion.FIELD_W) : 0.0;
        return new Quaternion(x, y, z, w);
    }

    /**
     * Get the x value of this quaternion.
     *
     * @return The x value of this quaternion.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Get the y value of this quaternion.
     *
     * @return The y value of this quaternion.
     */
    public double getY() {
        return this.y;
    }

    /**
     * Get the z value of this quaternion.
     *
     * @return The z value of this quaternion.
     */
    public double getZ() {
        return this.z;
    }

    /**
     * Get the w value of this quaternion.
     *
     * @return The w value of this quaternion.
     */
    public double getW() {
        return this.w;
    }

    /**
     * Create a clone of this Quaternion.
     */
    @Override
    public Quaternion clone() {
        return new Quaternion(this.x, this.y, this.z, this.w);
    }
}
