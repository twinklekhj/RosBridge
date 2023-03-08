package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONObject;

public class Transform extends RosMessage {
    public static final String FIELD_TRANSLATION = "translation";
    public static final String FIELD_ROTATION = "rotation";

    public static final String TYPE = "geometry_msgs/Transform";

    private final Vector3 translation;
    private final Quaternion rotation;

    /**
     * Create a new Transform with all 0s.
     */
    public Transform() {
        this(new Vector3(), new Quaternion());
    }

    /**
     * Create a new Transform with the given translation and rotation values.
     *
     * @param translation The translation value of the transform.
     * @param rotation    The rotation value of the transform.
     */
    public Transform(Vector3 translation, Quaternion rotation) {
        // build the JSON object
        super(jsonBuilder().put(Transform.FIELD_TRANSLATION, translation.getJsonObject()).put(Transform.FIELD_ROTATION, rotation.getJsonObject()), Transform.TYPE);
        this.translation = translation;
        this.rotation = rotation;
    }

    public static Transform fromJsonString(String jsonString) {
        return Transform.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Transform fromMessage(RosMessage m) {
        return Transform.fromJSONObject(m.getJsonObject());
    }

    public static Transform fromJSONObject(JSONObject jsonObject) {
        // check the fields
        Vector3 translation = jsonObject.has(Transform.FIELD_TRANSLATION) ? Vector3.fromJSONObject(jsonObject.getJSONObject(Transform.FIELD_TRANSLATION)) : new Vector3();
        Quaternion rotation = jsonObject.has(Transform.FIELD_ROTATION) ? Quaternion.fromJSONObject(jsonObject.getJSONObject(Transform.FIELD_ROTATION)) : new Quaternion();
        return new Transform(translation, rotation);
    }

    public Vector3 getTranslation() {
        return this.translation;
    }

    public Quaternion getRotation() {
        return this.rotation;
    }

    @Override
    public Transform clone() {
        return new Transform(this.translation, this.rotation);
    }
}
