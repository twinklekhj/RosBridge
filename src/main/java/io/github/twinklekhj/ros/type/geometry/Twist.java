package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;


public class Twist extends RosMessage {
    public static final String FIELD_LINEAR = "linear";
    public static final String FIELD_ANGULAR = "angular";

    public static final String TYPE = "geometry_msgs/Twist";

    private final Vector3 linear;
    private final Vector3 angular;

    public Twist() {
        this(new Vector3(), new Vector3());
    }

    public Twist(Vector3 linear, Vector3 angular) {
        super(jsonBuilder().put(Twist.FIELD_LINEAR, linear.getJsonObject()).put(Twist.FIELD_ANGULAR, angular.getJsonObject()), Twist.TYPE);
        this.linear = linear;
        this.angular = angular;
    }

    public static Twist fromJsonString(String jsonString) {
        return Twist.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Twist fromMessage(RosMessage m) {
        return Twist.fromJsonObject(m.getJsonObject());
    }

    public static Twist fromJsonObject(JsonObject jsonObject) {
        // check the fields
        Vector3 linear = jsonObject.containsKey(Twist.FIELD_LINEAR) ? Vector3.fromJsonObject(jsonObject.getJsonObject(Twist.FIELD_LINEAR)) : new Vector3();
        Vector3 angular = jsonObject.containsKey(Twist.FIELD_ANGULAR) ? Vector3.fromJsonObject(jsonObject.getJsonObject(Twist.FIELD_ANGULAR)) : new Vector3();
        return new Twist(linear, angular);
    }

    public Vector3 getLinear() {
        return this.linear;
    }

    public Vector3 getAngular() {
        return this.angular;
    }

    @Override
    public Twist clone() {
        return new Twist(this.linear, this.angular);
    }
}
