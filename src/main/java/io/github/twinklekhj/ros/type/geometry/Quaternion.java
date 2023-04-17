package io.github.twinklekhj.ros.type.geometry;


import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Quaternion extends RosMessage {
    public static final String TYPE = "geometry_msgs/Quaternion";

    public static final String FIELD_X = "x";
    public static final String FIELD_Y = "y";
    public static final String FIELD_Z = "z";
    public static final String FIELD_W = "w";

    double x;
    double y;
    double z;
    double w;

    public Quaternion() {
        this(0, 0, 0, 1.0);
    }

    public Quaternion(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;

        super.setJsonObject(jsonBuilder().put(FIELD_X, x).put(FIELD_Y, y).put(FIELD_Z, z).put(FIELD_W, w));
        super.setType(TYPE);
    }

    public static Quaternion fromJsonString(String jsonString) {
        return Quaternion.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Quaternion fromMessage(RosMessage m) {
        return Quaternion.fromJsonObject(m.getJsonObject());
    }

    public static Quaternion fromJsonObject(JsonObject jsonObject) {
        double x = jsonObject.containsKey(FIELD_X) ? jsonObject.getDouble(FIELD_X) : 0.0;
        double y = jsonObject.containsKey(FIELD_Y) ? jsonObject.getDouble(FIELD_Y) : 0.0;
        double z = jsonObject.containsKey(FIELD_Z) ? jsonObject.getDouble(FIELD_Z) : 0.0;
        double w = jsonObject.containsKey(FIELD_W) ? jsonObject.getDouble(FIELD_W) : 0.0;
        return new Quaternion(x, y, z, w);
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
        this.jsonObject.put(FIELD_X, x);
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
        this.jsonObject.put(FIELD_Y, x);
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
        this.jsonObject.put(FIELD_Z, x);
    }

    public double getW() {
        return this.w;
    }

    public void setW(double w) {
        this.w = w;
        this.jsonObject.put(FIELD_W, w);
    }

    public void conjugate(){
        this.x *= -1;
        this.y *= -1;
        this.z *= -1;

        applyJson();
    }

    public double norm(){
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w + this.w);
    }

    public void normalize(){
        double norm = norm();
        if(norm == 0.0){
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.w = 1;
        } else {
            norm = 1 / norm;
            this.x = this.x * norm;
            this.y = this.y * norm;
            this.z = this.z * norm;
            this.w = this.w * norm;
        }

        applyJson();
    }

    public void invert(){
        this.conjugate();
        this.normalize();
    }

    public void multiply(Quaternion q){
        double newX = this.x * q.w + this.y * q.z - this.z * q.y + this.w * q.x;
        double newY = -this.x * q.z + this.y * q.w + this.z * q.x + this.w * q.y;
        double newZ = this.x * q.y - this.y * q.x + this.z * q.w + this.w * q.z;
        double newW = -this.x * q.x - this.y * q.y - this.z * q.z + this.w * q.w;

        this.x = newX;
        this.y = newY;
        this.z = newZ;
        this.w = newW;

        applyJson();
    }

    private void applyJson(){
        this.jsonObject.put(FIELD_X, this.x);
        this.jsonObject.put(FIELD_Y, this.y);
        this.jsonObject.put(FIELD_Z, this.z);
        this.jsonObject.put(FIELD_W, this.w);
    }

    @Override
    public Quaternion clone() {
        return new Quaternion(this.x, this.y, this.z, this.w);
    }
}
