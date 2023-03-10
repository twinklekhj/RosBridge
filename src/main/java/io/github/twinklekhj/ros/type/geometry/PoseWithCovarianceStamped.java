package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;


public class PoseWithCovarianceStamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_POSE = "pose";

    public static final String TYPE = "geometry_msgs/PoseWithCovarianceStamped";

    private final Header header;
    private final PoseWithCovariance pose;


    public PoseWithCovarianceStamped() {
        this(new Header(), new PoseWithCovariance());
    }

    public PoseWithCovarianceStamped(Header header, PoseWithCovariance pose) {
        // build the JSON object
        super(jsonBuilder().put(PoseWithCovarianceStamped.FIELD_HEADER, header.getJsonObject()).put(PoseWithCovarianceStamped.FIELD_POSE, pose.getJsonObject()), PoseWithCovarianceStamped.TYPE);
        this.header = header;
        this.pose = pose;
    }

    public static PoseWithCovarianceStamped fromJsonString(String jsonString) {
        // convert to a message
        return PoseWithCovarianceStamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static PoseWithCovarianceStamped fromMessage(RosMessage m) {
        // get it from the JSON object
        return PoseWithCovarianceStamped.fromJsonObject(m.getJsonObject());
    }


    public static PoseWithCovarianceStamped fromJsonObject(JsonObject jsonObject) {
        // check the fields
        Header header = jsonObject.containsKey(PoseWithCovarianceStamped.FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(PoseWithCovarianceStamped.FIELD_HEADER)) : new Header();
        PoseWithCovariance pose = jsonObject.containsKey(PoseWithCovarianceStamped.FIELD_POSE) ? PoseWithCovariance.fromJsonObject(jsonObject.getJsonObject(PoseWithCovarianceStamped.FIELD_POSE)) : new PoseWithCovariance();
        return new PoseWithCovarianceStamped(header, pose);
    }

    public Header getHeader() {
        return this.header;
    }

    public PoseWithCovariance getPose() {
        return this.pose;
    }

    @Override
    public PoseWithCovarianceStamped clone() {
        return new PoseWithCovarianceStamped(this.header, this.pose);
    }
}
