package io.github.twinklekhj.ros.type.navigation;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.geometry.PoseWithCovariance;
import io.github.twinklekhj.ros.type.geometry.TwistWithCovariance;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Odometry extends RosMessage {
    public static final String TYPE = "nav_msgs/Odometry";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_CHILD_FRAME_ID = "child_frame_id";
    public static final String FIELD_POSE = "pose";
    public static final String FIELD_TWIST = "twist";

    private Header header;
    private String childFrameID;
    private PoseWithCovariance pose;
    private TwistWithCovariance twist;

    public Odometry() {
        this(new Header(), "", new PoseWithCovariance(), new TwistWithCovariance());
    }

    public Odometry(String childFrameID, PoseWithCovariance pose, TwistWithCovariance twist) {
        this(new Header(), childFrameID, pose, twist);
    }

    public Odometry(Header header, String childFrameID, PoseWithCovariance pose, TwistWithCovariance twist) {
        this.header = header;
        this.childFrameID = childFrameID;
        this.pose = pose;
        this.twist = twist;

        JsonObject obj = jsonBuilder().put(FIELD_HEADER, header.getJsonObject())
                .put(FIELD_CHILD_FRAME_ID, childFrameID)
                .put(FIELD_POSE, pose.getJsonObject())
                .put(FIELD_TWIST, twist.getJsonObject());

        super.setJsonObject(obj);
        super.setType(TYPE);
    }

    public static Odometry fromJsonString(String jsonString) {
        return Odometry.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Odometry fromMessage(RosMessage m) {
        return Odometry.fromJsonObject(m.getJsonObject());
    }

    public static Odometry fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        String childFrameID = jsonObject.containsKey(FIELD_CHILD_FRAME_ID) ? jsonObject.getString(FIELD_CHILD_FRAME_ID) : "";
        PoseWithCovariance pose = jsonObject.containsKey(FIELD_POSE) ? PoseWithCovariance.fromJsonObject(jsonObject.getJsonObject(FIELD_POSE)) : new PoseWithCovariance();
        TwistWithCovariance twist = jsonObject.containsKey(FIELD_TWIST) ? TwistWithCovariance.fromJsonObject(jsonObject.getJsonObject(FIELD_TWIST)) : new TwistWithCovariance();

        return new Odometry(header, childFrameID, pose, twist);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    public String getChildFrameID() {
        return this.childFrameID;
    }

    public void setChildFrameID(String childFrameID) {
        this.childFrameID = childFrameID;
        this.jsonObject.put(FIELD_CHILD_FRAME_ID, childFrameID);
    }

    public PoseWithCovariance getPose() {
        return pose;
    }

    public void setPose(PoseWithCovariance pose) {
        this.pose = pose;
        this.jsonObject.put(FIELD_POSE, pose.getJsonObject());
    }

    public TwistWithCovariance getTwist() {
        return twist;
    }

    public void setTwist(TwistWithCovariance twist) {
        this.twist = twist;
        this.jsonObject.put(FIELD_TWIST, twist.getJsonObject());
    }

    @Override
    public Odometry clone() {
        return new Odometry(this.header, this.childFrameID, this.pose, this.twist);
    }
}
