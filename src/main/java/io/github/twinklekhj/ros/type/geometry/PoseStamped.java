package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;


public class PoseStamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_POSE = "pose";

    public static final String TYPE = "geometry_msgs/PoseStamped";

    private final Header header;
    private final Pose pose;

    public PoseStamped() {
        this(new Header(), new Pose());
    }

    public PoseStamped(Header header, Pose pose) {
        super(jsonBuilder().put(PoseStamped.FIELD_HEADER, header.getJsonObject()).put(PoseStamped.FIELD_POSE, pose.getJsonObject()), PoseStamped.TYPE);
        this.header = header;
        this.pose = pose;
    }

    public static PoseStamped fromJsonString(String jsonString) {
        return PoseStamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static PoseStamped fromMessage(RosMessage m) {
        return PoseStamped.fromJsonObject(m.getJsonObject());
    }

    public static PoseStamped fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(PoseStamped.FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(PoseStamped.FIELD_HEADER)) : new Header();
        Pose pose = jsonObject.containsKey(PoseStamped.FIELD_POSE) ? Pose.fromJsonObject(jsonObject.getJsonObject(PoseStamped.FIELD_POSE)) : new Pose();
        return new PoseStamped(header, pose);
    }

    public Header getHeader() {
        return this.header;
    }


    public Pose getPose() {
        return this.pose;
    }

    @Override
    public PoseStamped clone() {
        return new PoseStamped(this.header, this.pose);
    }
}
