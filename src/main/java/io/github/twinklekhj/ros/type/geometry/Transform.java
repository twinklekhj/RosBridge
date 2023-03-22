package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Transform extends RosMessage {
    public static final String TYPE = "geometry_msgs/Transform";

    public static final String FIELD_TRANSLATION = "translation";
    public static final String FIELD_ROTATION = "rotation";

    private Vector3 translation;
    private Quaternion rotation;

    public Transform() {
        this(new Vector3(), new Quaternion());
    }

    public Transform(Vector3 translation, Quaternion rotation) {
        this.translation = translation;
        this.rotation = rotation;

        super.setJsonObject(jsonBuilder().put(Transform.FIELD_TRANSLATION, translation.getJsonObject()).put(Transform.FIELD_ROTATION, rotation.getJsonObject()));
        super.setType(TYPE);
    }

    public static Transform fromJsonString(String jsonString) {
        return Transform.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Transform fromMessage(RosMessage m) {
        return Transform.fromJsonObject(m.getJsonObject());
    }

    public static Transform fromJsonObject(JsonObject jsonObject) {
        Vector3 translation = jsonObject.containsKey(Transform.FIELD_TRANSLATION) ? Vector3.fromJsonObject(jsonObject.getJsonObject(Transform.FIELD_TRANSLATION)) : new Vector3();
        Quaternion rotation = jsonObject.containsKey(Transform.FIELD_ROTATION) ? Quaternion.fromJsonObject(jsonObject.getJsonObject(Transform.FIELD_ROTATION)) : new Quaternion();
        return new Transform(translation, rotation);
    }

    public Vector3 getTranslation() {
        return this.translation;
    }

    public void setTranslation(Vector3 translation) {
        this.translation = translation;
        this.jsonObject.put(FIELD_TRANSLATION, translation.getJsonObject());
    }

    public Quaternion getRotation() {
        return this.rotation;
    }

    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
        this.jsonObject.put(FIELD_ROTATION, rotation.getJsonObject());
    }

    @Override
    public Transform clone() {
        return new Transform(this.translation, this.rotation);
    }
}
