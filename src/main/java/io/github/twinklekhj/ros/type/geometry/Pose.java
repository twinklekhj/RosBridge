package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Pose extends RosMessage {
    public static final String TYPE = "geometry_msgs/Pose";

    public static final String FIELD_POSITION = "position";
    public static final String FIELD_ORIENTATION = "orientation";

    private Vector3 position;
    private Quaternion orientation;

    public Pose() {
        this(new Vector3(), new Quaternion());
    }

    public Pose(Vector3 position, Quaternion orientation) {
        this.position = position;
        this.orientation = orientation;

        JsonObject json = jsonBuilder()
                .put(FIELD_POSITION, position.getJsonObject())
                .put(FIELD_ORIENTATION, orientation.getJsonObject());
        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static Pose fromJsonString(String jsonString) {
        return Pose.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Pose fromMessage(RosMessage m) {
        return Pose.fromJsonObject(m.getJsonObject());
    }

    public static Pose fromJsonObject(JsonObject jsonObject) {
        Vector3 position = jsonObject.containsKey(FIELD_POSITION) ? Vector3.fromJsonObject(jsonObject.getJsonObject(FIELD_POSITION)) : new Vector3();
        Quaternion orientation = jsonObject.containsKey(FIELD_ORIENTATION) ? Quaternion.fromJsonObject(jsonObject.getJsonObject(FIELD_ORIENTATION)) : new Quaternion();
        return new Pose(position, orientation);
    }

    public Vector3 getPosition() {
        return this.position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
        this.jsonObject.put(FIELD_POSITION, position.getJsonObject());
    }

    public Quaternion getOrientation() {
        return this.orientation;
    }

    public void setOrientation(Quaternion orientation) {
        this.orientation = orientation;
        this.jsonObject.put(FIELD_ORIENTATION, orientation.getJsonObject());
    }

    /**
     * Apply a transform against this pose.
     * @param tf The transform to be applied.
     */
    public void applyTransform(Transform tf){
        this.position.multiplyQuaternion(tf.rotation);
        this.position.add(tf.translation);

        Quaternion tmp = tf.rotation.clone();
        tmp.multiply(this.orientation);
        this.orientation = tmp;

        applyJson();
    }

    public void applyTransform(Pose tf){
        this.position.multiplyQuaternion(tf.orientation);
        this.position.add(tf.position);

        Quaternion tmp = tf.orientation.clone();
        tmp.multiply(this.orientation);
        this.orientation = tmp;

        applyJson();
    }

    /**
     * Multiply this pose with another pose without altering this pose.
     * @param pose The other pose
     * @return Pose
     */
    public Pose multiply (Pose pose){
        Pose copy = pose.clone();
        copy.applyTransform(pose);

        return copy;
    }

    /**
     * Compute the inverse of this pose.
     * @return pose
     */
    public Pose getInverse (){
        Pose inverse = this.clone();
        inverse.orientation.invert();
        inverse.position.multiplyQuaternion(inverse.orientation);
        inverse.position.x *= -1;
        inverse.position.y *= -1;
        inverse.position.z *= -1;

        return inverse;
    }

    private void applyJson(){
        this.jsonObject.put(FIELD_POSITION, position.getJsonObject());
        this.jsonObject.put(FIELD_ORIENTATION, orientation.getJsonObject());
    }

    @Override
    public Pose clone() {
        return new Pose(this.position, this.orientation);
    }
}
