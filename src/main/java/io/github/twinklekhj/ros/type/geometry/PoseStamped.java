package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class PoseStamped extends RosMessage {
    public static final String TYPE = "geometry_msgs/PoseStamped";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_POSE = "pose";

    private Header header;
    private Pose pose;

    public PoseStamped() {
        this(new Header(), new Pose());
    }

    public PoseStamped(Pose pose) {
        this(new Header(), pose);
    }

    public PoseStamped(Header header, Pose pose) {
        this.header = header;
        this.pose = pose;

        super.setJsonObject(jsonBuilder().put(FIELD_HEADER, header.getJsonObject()).put(FIELD_POSE, pose.getJsonObject()));
        super.setType(TYPE);
    }

    public static PoseStamped fromJsonString(String jsonString) {
        return PoseStamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static PoseStamped fromMessage(RosMessage m) {
        return PoseStamped.fromJsonObject(m.getJsonObject());
    }

    public static PoseStamped fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        Pose pose = jsonObject.containsKey(FIELD_POSE) ? Pose.fromJsonObject(jsonObject.getJsonObject(FIELD_POSE)) : new Pose();
        return new PoseStamped(header, pose);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    public Pose getPose() {
        return this.pose;
    }

    public void setPose(Pose pose) {
        this.pose = pose;
        this.jsonObject.put(FIELD_POSE, pose.getJsonObject());
    }

    @Override
    public PoseStamped clone() {
        return new PoseStamped(this.header, this.pose);
    }
}
