package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;


public class TransformStamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_CHILD_FRAME_ID = "child_frame_id";
    public static final String FIELD_TRANSFORM = "transform";

    public static final String TYPE = "geometry_msgs/TransformStamped";

    private final Header header;
    private final String childFrameID;
    private final Transform transform;

    public TransformStamped() {
        this(new Header(), "", new Transform());
    }

    public TransformStamped(Header header, String childFrameID, Transform transform) {
        super(jsonBuilder().put(TransformStamped.FIELD_HEADER, header.getJsonObject()).put(TransformStamped.FIELD_CHILD_FRAME_ID, childFrameID).put(TransformStamped.FIELD_TRANSFORM, transform.getJsonObject()), TransformStamped.TYPE);

        this.header = header;
        this.childFrameID = childFrameID;
        this.transform = transform;
    }

    public static TransformStamped fromJsonString(String jsonString) {
        return TransformStamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static TransformStamped fromMessage(RosMessage m) {
        return TransformStamped.fromJsonObject(m.getJsonObject());
    }

    public static TransformStamped fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(TransformStamped.FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(TransformStamped.FIELD_HEADER)) : new Header();
        String childFrameID = jsonObject.containsKey(TransformStamped.FIELD_CHILD_FRAME_ID) ? jsonObject.getString(TransformStamped.FIELD_CHILD_FRAME_ID) : "";
        Transform transform = jsonObject.containsKey(TransformStamped.FIELD_TRANSFORM) ? Transform.fromJsonObject(jsonObject.getJsonObject(TransformStamped.FIELD_TRANSFORM)) : new Transform();
        return new TransformStamped(header, childFrameID, transform);
    }

    public Header getHeader() {
        return this.header;
    }

    public String getChildFrameID() {
        return this.childFrameID;
    }

    public Transform getTransform() {
        return this.transform;
    }

    @Override
    public TransformStamped clone() {
        return new TransformStamped(this.header, this.childFrameID, this.transform);
    }
}
