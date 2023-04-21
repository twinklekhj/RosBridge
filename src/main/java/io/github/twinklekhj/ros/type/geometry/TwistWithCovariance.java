package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;

@ToString
public class TwistWithCovariance extends RosMessage {
    public static final String TYPE = "geometry_msgs/TwistWithCovariance";

    public static final String FIELD_TWIST = "twist";
    public static final String FIELD_COVARIANCE = "covariance";

    public static final int COVARIANCE_ROWS = 6;
    public static final int COVARIANCE_COLUMNS = 6;

    public static final int COVARIANCE_SIZE = TwistWithCovariance.COVARIANCE_ROWS * TwistWithCovariance.COVARIANCE_COLUMNS;

    private Twist twist;
    private double[] covariance;
    private final double[][] covarianceMatrix;

    public TwistWithCovariance() {
        this(new Twist(), new double[]{});
    }

    public TwistWithCovariance(Twist twist) {
        this(twist, new double[TwistWithCovariance.COVARIANCE_SIZE]);
    }

    public TwistWithCovariance(Twist twist, double[] covariance) {
        this.twist = twist;
        this.covariance = new double[TwistWithCovariance.COVARIANCE_SIZE];
        this.covarianceMatrix = new double[TwistWithCovariance.COVARIANCE_ROWS][TwistWithCovariance.COVARIANCE_COLUMNS];
        if (covariance.length == TwistWithCovariance.COVARIANCE_SIZE) {
            System.arraycopy(covariance, 0, this.covariance, 0, TwistWithCovariance.COVARIANCE_SIZE);
            for (int i = 0; i < TwistWithCovariance.COVARIANCE_ROWS; i++) {
                System.arraycopy(this.covariance, i * TwistWithCovariance.COVARIANCE_COLUMNS, this.covarianceMatrix[i], 0, TwistWithCovariance.COVARIANCE_COLUMNS);
            }
        }

        JsonObject json = jsonBuilder()
                .put(FIELD_TWIST, twist.getJsonObject())
                .put(FIELD_COVARIANCE, covariance.length == TwistWithCovariance.COVARIANCE_SIZE ? covariance : new double[TwistWithCovariance.COVARIANCE_SIZE]);

        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static TwistWithCovariance fromJsonString(String jsonString) {
        return TwistWithCovariance.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static TwistWithCovariance fromMessage(RosMessage m) {
        return TwistWithCovariance.fromJsonObject(m.getJsonObject());
    }

    public static TwistWithCovariance fromJsonObject(JsonObject jsonObject) {
        Twist twist = jsonObject.containsKey(FIELD_TWIST) ? Twist.fromJsonObject(jsonObject.getJsonObject(FIELD_TWIST)) : new Twist();

        JsonArray jsonArray = jsonObject.getJsonArray(FIELD_COVARIANCE);
        if (jsonArray != null) {
            double[] twists = new double[jsonArray.size()];
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

    public void setTwist(Twist twist) {
        this.twist = twist;
        this.jsonObject.put(FIELD_TWIST, twist.getJsonObject());
    }

    public double[] getCovariance() {
        return this.covariance;
    }

    public void setCovariance(double... covariance) {
        this.covariance = covariance;
        this.jsonObject.put(FIELD_COVARIANCE, covariance.length == TwistWithCovariance.COVARIANCE_SIZE ? covariance : new double[TwistWithCovariance.COVARIANCE_SIZE]);
    }

    public double[][] getCovarianceMatrix() {
        return this.covarianceMatrix;
    }

    @Override
    public TwistWithCovariance clone() {
        return new TwistWithCovariance(this.twist, this.covariance);
    }
}
