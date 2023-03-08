package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import lombok.Builder;
import org.json.JSONObject;

@Builder
public class Vector3 extends RosMessage {
    public static final String TYPE = "geometry_msgs/Vector3";

    public static final String FIELD_X = "x";
    public static final String FIELD_Y = "y";
    public static final String FIELD_Z = "z";

    @Builder.Default
    private double x = 0;

    @Builder.Default
    private double y = 0;

    @Builder.Default
    private double z = 0;

    /**
     * Create a new Vector3 with all 0s.
     */
    public Vector3() {
        this(0, 0, 0);
    }

    /**
     * Create a new Vector3 with the given values.
     *
     * @param x The x value of the vector.
     * @param y The y value of the vector.
     * @param z The z value of the vector.
     */
    public Vector3(double x, double y, double z) {
        // build the JSON object
        super(jsonBuilder().put(Vector3.FIELD_X, x).put(Vector3.FIELD_Y, y).put(Vector3.FIELD_Z, z), Vector3.TYPE);
        this.x = x;
        this.y = y;
        this.z = z;
    }


    /**
     * Create a new Vector3 based on the given JSON object. Any missing values
     * will be set to their defaults.
     *
     * @param jsonObject The JSON object to parse.
     * @return A Vector3 message based on the given JSON object.
     */
    public static Vector3 fromJSONObject(JSONObject jsonObject) {
        // check the fields
        double x = jsonObject.has(Vector3.FIELD_X) ? jsonObject.getDouble(Vector3.FIELD_X) : 0.0;
        double y = jsonObject.has(Vector3.FIELD_Y) ? jsonObject.getDouble(Vector3.FIELD_Y) : 0.0;
        double z = jsonObject.has(Vector3.FIELD_Z) ? jsonObject.getDouble(Vector3.FIELD_Z) : 0.0;
        return new Vector3(x, y, z);
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
    public Vector3 clone() {
        return new Vector3(this.x, this.y, this.z);
    }
}
