package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONObject;

public class Wrench extends RosMessage {
    public static final String FIELD_FORCE = "force";
    public static final String FIELD_TORQUE = "torque";

    public static final String TYPE = "geometry_msgs/Wrench";

    private final Vector3 force;
    private final Vector3 torque;

    /**
     * Create a new Wrench with all 0s.
     */
    public Wrench() {
        this(new Vector3(), new Vector3());
    }

    /**
     * Create a new Wrench with the given force and torque values.
     *
     * @param force  The force value of the wrench.
     * @param torque The torque value of the wrench.
     */
    public Wrench(Vector3 force, Vector3 torque) {
        // build the JSON object
        super(jsonBuilder().put(Wrench.FIELD_FORCE, force.getJsonObject()).put(Wrench.FIELD_TORQUE, torque.getJsonObject()), Wrench.TYPE);
        this.force = force;
        this.torque = torque;
    }

    public static Wrench fromJsonString(String jsonString) {
        return Wrench.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Wrench fromMessage(RosMessage m) {
        return Wrench.fromJSONObject(m.getJsonObject());
    }

    public static Wrench fromJSONObject(JSONObject jsonObject) {
        // check the fields
        Vector3 force = jsonObject.has(Wrench.FIELD_FORCE) ? Vector3.fromJSONObject(jsonObject.getJSONObject(Wrench.FIELD_FORCE)) : new Vector3();
        Vector3 torque = jsonObject.has(Wrench.FIELD_TORQUE) ? Vector3.fromJSONObject(jsonObject.getJSONObject(Wrench.FIELD_TORQUE)) : new Vector3();
        return new Wrench(force, torque);
    }

    public Vector3 getForce() {
        return this.force;
    }

    public Vector3 getTorque() {
        return this.torque;
    }

    @Override
    public Wrench clone() {
        return new Wrench(this.force.clone(), this.torque.clone());
    }
}
