package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Wrench extends RosMessage {
    public static final String TYPE = "geometry_msgs/Wrench";

    public static final String FIELD_FORCE = "force";
    public static final String FIELD_TORQUE = "torque";

    private Vector3 force;
    private Vector3 torque;

    public Wrench() {
        this(new Vector3(), new Vector3());
    }

    public Wrench(Vector3 force, Vector3 torque) {
        this.force = force;
        this.torque = torque;

        super.setJsonObject(jsonBuilder().put(Wrench.FIELD_FORCE, force.getJsonObject()).put(Wrench.FIELD_TORQUE, torque.getJsonObject()));
        super.setType(TYPE);
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

    public void setForce(Vector3 force) {
        this.force = force;
        this.jsonObject.put(FIELD_FORCE, force.getJsonObject());
    }

    public Vector3 getTorque() {
        return this.torque;
    }

    public void setTorque(Vector3 torque) {
        this.torque = torque;
        this.jsonObject.put(FIELD_TORQUE, torque.getJsonObject());
    }

    @Override
    public Wrench clone() {
        return new Wrench(this.force.clone(), this.torque.clone());
    }
}
