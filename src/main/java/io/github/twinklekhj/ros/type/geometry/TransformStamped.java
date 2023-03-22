package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class TransformStamped extends RosMessage {
    public static final String TYPE = "geometry_msgs/TransformStamped";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_CHILD_FRAME_ID = "child_frame_id";
    public static final String FIELD_TRANSFORM = "transform";

    private Header header;
    private String childFrameID;
    private Transform transform;

    public TransformStamped() {
        this(new Header(), "", new Transform());
    }

    public TransformStamped(String childFrameID, Transform transform) {
        this(new Header(), childFrameID, new Transform());
    }

    public TransformStamped(Header header, String childFrameID, Transform transform) {
        this.header = header;
        this.childFrameID = childFrameID;
        this.transform = transform;

        super.setJsonObject(jsonBuilder().put(TransformStamped.FIELD_HEADER, header.getJsonObject()).put(TransformStamped.FIELD_CHILD_FRAME_ID, childFrameID).put(TransformStamped.FIELD_TRANSFORM, transform.getJsonObject()));
        super.setType(TYPE);
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

    public Transform getTransform() {
        return this.transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
        this.jsonObject.put(FIELD_TRANSFORM, transform.getJsonObject());
    }

    @Override
    public TransformStamped clone() {
        return new TransformStamped(this.header, this.childFrameID, this.transform);
    }
}
