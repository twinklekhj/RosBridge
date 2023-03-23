package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Twist extends RosMessage {
    public static final String FIELD_LINEAR = "linear";
    public static final String FIELD_ANGULAR = "angular";

    public static final String TYPE = "geometry_msgs/Twist";

    private Vector3 linear;
    private Vector3 angular;

    public Twist() {
        this(new Vector3(), new Vector3());
    }

    public Twist(Vector3 linear, Vector3 angular) {
        this.linear = linear;
        this.angular = angular;

        super.setJsonObject(jsonBuilder().put(FIELD_LINEAR, linear.getJsonObject()).put(FIELD_ANGULAR, angular.getJsonObject()));
        super.setType(TYPE);
    }

    public static Twist fromJsonString(String jsonString) {
        return Twist.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Twist fromMessage(RosMessage m) {
        return Twist.fromJsonObject(m.getJsonObject());
    }

    public static Twist fromJsonObject(JsonObject jsonObject) {
        Vector3 linear = jsonObject.containsKey(FIELD_LINEAR) ? Vector3.fromJsonObject(jsonObject.getJsonObject(FIELD_LINEAR)) : new Vector3();
        Vector3 angular = jsonObject.containsKey(FIELD_ANGULAR) ? Vector3.fromJsonObject(jsonObject.getJsonObject(FIELD_ANGULAR)) : new Vector3();
        return new Twist(linear, angular);
    }

    public Vector3 getLinear() {
        return this.linear;
    }

    public void setLinear(Vector3 linear) {
        this.linear = linear;
        this.jsonObject.put(FIELD_LINEAR, linear.getJsonObject());
    }

    public Vector3 getAngular() {
        return this.angular;
    }

    public void setAngular(Vector3 angular) {
        this.angular = angular;
        this.jsonObject.put(FIELD_ANGULAR, angular.getJsonObject());
    }

    @Override
    public Twist clone() {
        return new Twist(this.linear, this.angular);
    }
}
