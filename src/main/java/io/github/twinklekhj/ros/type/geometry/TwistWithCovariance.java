package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.Arrays;

public class TwistWithCovariance extends RosMessage {
    public static final String FIELD_TWIST = "twist";
    public static final String FIELD_COVARIANCE = "covariance";

    public static final String TYPE = "geometry_msgs/TwistWithCovariance";

    public static final int COVARIANCE_ROWS = 6;
    public static final int COVARIANCE_COLUMNS = 6;

    public static final int COVARIANCE_SIZE = TwistWithCovariance.COVARIANCE_ROWS * TwistWithCovariance.COVARIANCE_COLUMNS;

    private final Twist twist;
    private final double[] covariance;
    private final double[][] covarianceMatrix;

    /**
     * Create a new TwistWithCovariance with all 0 values.
     */
    public TwistWithCovariance() {
        this(new Twist(), new double[]{});
    }

    /**
     * Create a new TwistWithCovariance with the given twist. The covariance
     * matrix will be all 0s.
     *
     * @param twist The twist value of the twist.
     */
    public TwistWithCovariance(Twist twist) {
        this(twist, new double[TwistWithCovariance.COVARIANCE_SIZE]);
    }

    /**
     * Create a new TwistWithCovariance with the given twist and covariance
     * matrix. If the given array is not of size
     * TwistWithCovariance.COVARIANCE_SIZE, all 0s will be used instead. The
     * values of the array will be copied into this object.
     *
     * @param twist      The twist value of the twist.
     * @param covariance The covariance matrix as an array.
     */
    public TwistWithCovariance(Twist twist, double[] covariance) {
        super(jsonBuilder().put(TwistWithCovariance.FIELD_TWIST, twist.toJSONObject()).put(TwistWithCovariance.FIELD_COVARIANCE,
                jsonBuilder(new StringReader((covariance.length == TwistWithCovariance.COVARIANCE_SIZE) ? Arrays.toString(covariance) : Arrays.toString(new double[TwistWithCovariance.COVARIANCE_SIZE])))), TwistWithCovariance.TYPE);

        this.twist = twist;
        // create the arrays
        this.covariance = new double[TwistWithCovariance.COVARIANCE_SIZE];
        this.covarianceMatrix = new double[TwistWithCovariance.COVARIANCE_ROWS][TwistWithCovariance.COVARIANCE_COLUMNS];
        if (covariance.length == TwistWithCovariance.COVARIANCE_SIZE) {
            // copy the 1-D array
            System.arraycopy(covariance, 0, this.covariance, 0, TwistWithCovariance.COVARIANCE_SIZE);
            // create a 2D matrix
            for (int i = 0; i < TwistWithCovariance.COVARIANCE_ROWS; i++) {
                System.arraycopy(this.covariance, i * TwistWithCovariance.COVARIANCE_COLUMNS, this.covarianceMatrix[i], 0, TwistWithCovariance.COVARIANCE_COLUMNS);
            }
        }
    }

    public static TwistWithCovariance fromJsonString(String jsonString) {
        return TwistWithCovariance.fromMessage(new RosMessage(jsonString));
    }

    public static TwistWithCovariance fromMessage(RosMessage m) {
        return TwistWithCovariance.fromJSONObject(m.toJSONObject());
    }

    public static TwistWithCovariance fromJSONObject(JSONObject jsonObject) {
        // grab the twist if there is one
        Twist twist = jsonObject.has(TwistWithCovariance.FIELD_TWIST) ? Twist.fromJSONObject(jsonObject.getJSONObject(TwistWithCovariance.FIELD_TWIST)) : new Twist();

        // check the array
        JSONArray jsonArray = jsonObject.getJSONArray(TwistWithCovariance.FIELD_COVARIANCE);
        if (jsonArray != null) {
            // convert each value
            double[] twists = new double[jsonArray.length()];
            for (int i = 0; i < twists.length; i++) {
                twists[i] = jsonArray.getDouble(i);
            }
            return new TwistWithCovariance(twist, twists);
        } else {
            return new TwistWithCovariance(twist, new double[TwistWithCovariance.COVARIANCE_SIZE]);
        }
    }

    public Twist getTwist() {
        return this.twist;
    }

    public double[] getCovariance() {
        return this.covariance;
    }

    public double[][] getCovarianceMatrix() {
        return this.covarianceMatrix;
    }

    @Override
    public TwistWithCovariance clone() {
        return new TwistWithCovariance(this.twist, this.covariance);
    }
}
