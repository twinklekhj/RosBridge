package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class PoseWithCovarianceStamped extends RosMessage {
    public static final String TYPE = "geometry_msgs/PoseWithCovarianceStamped";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_POSE = "pose";

    private Header header;
    private PoseWithCovariance pose;


    public PoseWithCovarianceStamped() {
        this(new Header(), new PoseWithCovariance());
    }

    public PoseWithCovarianceStamped(PoseWithCovariance pose) {
        this(new Header(), pose);
    }

    public PoseWithCovarianceStamped(Header header, PoseWithCovariance pose) {
        this.header = header;
        this.pose = pose;

        super.setJsonObject(jsonBuilder().put(FIELD_HEADER, header.getJsonObject()).put(FIELD_POSE, pose.getJsonObject()));
        super.setType(TYPE);
    }

    public static PoseWithCovarianceStamped fromJsonString(String jsonString) {
        return PoseWithCovarianceStamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static PoseWithCovarianceStamped fromMessage(RosMessage m) {
        return PoseWithCovarianceStamped.fromJsonObject(m.getJsonObject());
    }


    public static PoseWithCovarianceStamped fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        PoseWithCovariance pose = jsonObject.containsKey(FIELD_POSE) ? PoseWithCovariance.fromJsonObject(jsonObject.getJsonObject(FIELD_POSE)) : new PoseWithCovariance();
        return new PoseWithCovarianceStamped(header, pose);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    public PoseWithCovariance getPose() {
        return this.pose;
    }

    public void setPose(PoseWithCovariance pose) {
        this.pose = pose;
        this.jsonObject.put(FIELD_POSE, pose.getJsonObject());
    }

    @Override
    public PoseWithCovarianceStamped clone() {
        return new PoseWithCovarianceStamped(this.header, this.pose);
    }
}
