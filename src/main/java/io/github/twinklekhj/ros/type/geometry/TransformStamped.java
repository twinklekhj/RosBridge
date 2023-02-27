package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONObject;
import io.github.twinklekhj.ros.type.std.Header;

public class TransformStamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_CHILD_FRAME_ID = "child_frame_id";
    public static final String FIELD_TRANSFORM = "transform";

    public static final String TYPE = "geometry_msgs/TransformStamped";

    private final Header header;
    private final String childFrameID;
    private final Transform transform;

    /**
     * Create a new TransformStamped with all 0s.
     */
    public TransformStamped() {
        this(new Header(), "", new Transform());
    }

    /**
     * Create a new TransformStamped with the given values.
     *
     * @param header       The header value of the transform.
     * @param childFrameID The child frame ID value of the transform.
     * @param transform    The transform value of the transform.
     */
    public TransformStamped(Header header, String childFrameID, Transform transform) {
        // build the JSON object
        super(jsonBuilder().put(TransformStamped.FIELD_HEADER, header.toJSONObject()).put(TransformStamped.FIELD_CHILD_FRAME_ID, childFrameID).put(TransformStamped.FIELD_TRANSFORM, transform.toJSONObject()), TransformStamped.TYPE);

        this.header = header;
        this.childFrameID = childFrameID;
        this.transform = transform;
    }

    public static TransformStamped fromJsonString(String jsonString) {
        return TransformStamped.fromMessage(new RosMessage(jsonString));
    }

    public static TransformStamped fromMessage(RosMessage m) {
        return TransformStamped.fromJSONObject(m.toJSONObject());
    }

    public static TransformStamped fromJSONObject(JSONObject jsonObject) {
        // check the fields
        Header header = jsonObject.has(TransformStamped.FIELD_HEADER) ? Header.fromJSONObject(jsonObject.getJSONObject(TransformStamped.FIELD_HEADER)) : new Header();
        String childFrameID = jsonObject.has(TransformStamped.FIELD_CHILD_FRAME_ID) ? jsonObject.getString(TransformStamped.FIELD_CHILD_FRAME_ID) : "";
        Transform transform = jsonObject.has(TransformStamped.FIELD_TRANSFORM) ? Transform.fromJSONObject(jsonObject.getJSONObject(TransformStamped.FIELD_TRANSFORM)) : new Transform();
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
