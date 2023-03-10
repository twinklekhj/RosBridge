package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;


public class Wrench extends RosMessage {
    public static final String FIELD_FORCE = "force";
    public static final String FIELD_TORQUE = "torque";

    public static final String TYPE = "geometry_msgs/Wrench";

    private final Vector3 force;
    private final Vector3 torque;

    public Wrench() {
        this(new Vector3(), new Vector3());
    }

    public Wrench(Vector3 force, Vector3 torque) {
        super(jsonBuilder().put(Wrench.FIELD_FORCE, force.getJsonObject()).put(Wrench.FIELD_TORQUE, torque.getJsonObject()), Wrench.TYPE);
        this.force = force;
        this.torque = torque;
    }

    public static Wrench fromJsonString(String jsonString) {
        return Wrench.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Wrench fromMessage(RosMessage m) {
        return Wrench.fromJsonObject(m.getJsonObject());
    }

    public static Wrench fromJsonObject(JsonObject jsonObject) {
        Vector3 force = jsonObject.containsKey(Wrench.FIELD_FORCE) ? Vector3.fromJsonObject(jsonObject.getJsonObject(Wrench.FIELD_FORCE)) : new Vector3();
        Vector3 torque = jsonObject.containsKey(Wrench.FIELD_TORQUE) ? Vector3.fromJsonObject(jsonObject.getJsonObject(Wrench.FIELD_TORQUE)) : new Vector3();
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
