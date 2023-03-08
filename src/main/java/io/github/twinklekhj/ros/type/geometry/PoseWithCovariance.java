package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class PoseWithCovariance extends RosMessage {
    public static final String FIELD_POSE = "pose";
    public static final String FIELD_COVARIANCE = "covariance";

    public static final String TYPE = "geometry_msgs/PoseWithCovariance";

    public static final int COVARIANCE_ROWS = 6;

    public static final int COVARIANCE_COLUMNS = 6;

    public static final int COVARIANCE_SIZE = PoseWithCovariance.COVARIANCE_ROWS * PoseWithCovariance.COVARIANCE_COLUMNS;

    private final Pose pose;
    private final double[] covariance;
    private final double[][] covarianceMatrix;

    /**
     * Create a new PoseWithCovariance with all 0 values.
     */
    public PoseWithCovariance() {
        this(new Pose(), new double[]{});
    }

    /**
     * Create a new PoseWithCovariance with the given pose. The covariance
     * matrix will be all 0s.
     *
     * @param pose The pose value of the pose.
     */
    public PoseWithCovariance(Pose pose) {
        this(pose, new double[PoseWithCovariance.COVARIANCE_SIZE]);
    }

    /**
     * Create a new PoseWithCovariance with the given pose and covariance
     * matrix. If the given array is not of size
     * PoseWithCovariance.COVARIANCE_SIZE, all 0s will be used instead. The
     * values of the array will be copied into this object.
     *
     * @param pose       The pose value of the pose.
     * @param covariance The covariance matrix as an array.
     */
    public PoseWithCovariance(Pose pose, double[] covariance) {
        // build the JSON object
        super(jsonBuilder()
                .put(PoseWithCovariance.FIELD_POSE, pose.getJsonObject())
                .put(PoseWithCovariance.FIELD_COVARIANCE, jsonBuilder(covariance.length == PoseWithCovariance.COVARIANCE_SIZE ? Arrays.toString(covariance) : Arrays.toString(new double[PoseWithCovariance.COVARIANCE_SIZE]))), PoseWithCovariance.TYPE);

        this.pose = pose;
        // create the arrays
        this.covariance = new double[PoseWithCovariance.COVARIANCE_SIZE];
        this.covarianceMatrix = new double[PoseWithCovariance.COVARIANCE_ROWS][PoseWithCovariance.COVARIANCE_COLUMNS];
        if (covariance.length == PoseWithCovariance.COVARIANCE_SIZE) {
            // copy the 1-D array
            System.arraycopy(covariance, 0, this.covariance, 0, PoseWithCovariance.COVARIANCE_SIZE);
            // create a 2D matrix
            for (int i = 0; i < PoseWithCovariance.COVARIANCE_ROWS; i++) {
                System.arraycopy(this.covariance, i * PoseWithCovariance.COVARIANCE_COLUMNS, this.covarianceMatrix[i], 0, PoseWithCovariance.COVARIANCE_COLUMNS);
            }
        }
    }


    public static PoseWithCovariance fromJsonString(String jsonString) {
        return PoseWithCovariance.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static PoseWithCovariance fromMessage(RosMessage m) {
        return PoseWithCovariance.fromJSONObject(m.getJsonObject());
    }

    public static PoseWithCovariance fromJSONObject(JSONObject jsonObject) {
        Pose pose = jsonObject.has(PoseWithCovariance.FIELD_POSE) ? Pose.fromJSONObject(jsonObject.getJSONObject(PoseWithCovariance.FIELD_POSE)) : new Pose();

        JSONArray jsonArray = jsonObject.getJSONArray(PoseWithCovariance.FIELD_COVARIANCE);
        if (jsonArray != null) {
            double[] poses = new double[jsonArray.length()];
            for (int i = 0; i < poses.length; i++) {
                poses[i] = jsonArray.getDouble(i);
            }
            return new PoseWithCovariance(pose, poses);
        } else {
            return new PoseWithCovariance(pose, new double[PoseWithCovariance.COVARIANCE_SIZE]);
        }
    }

    public Pose getPose() {
        return this.pose;
    }

    public double[] getCovariance() {
        return this.covariance;
    }

    public double[][] getCovarianceMatrix() {
        return this.covarianceMatrix;
    }

    @Override
    public PoseWithCovariance clone() {
        return new PoseWithCovariance(this.pose, this.covariance);
    }
}
