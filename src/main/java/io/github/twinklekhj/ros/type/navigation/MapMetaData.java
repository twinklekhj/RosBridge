package io.github.twinklekhj.ros.type.navigation;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.geometry.Pose;
import io.github.twinklekhj.ros.type.std.Time;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class MapMetaData extends RosMessage {
    public static final String TYPE = "nav_msgs/MapMetaData";

    public static final String FIELD_STAMP = "map_load_time";
    public static final String FIELD_WIDTH = "width";
    public static final String FIELD_HEIGHT = "height";
    public static final String FIELD_RESOLUTION = "resolution";
    public static final String FIELD_ORIGIN = "origin";

    private Time stamp;
    private float resolution;
    private float width;
    private float height;
    private Pose origin;

    public MapMetaData() {
        this(new Time(), 0, 0, 0, new Pose());
    }

    public MapMetaData(Time stamp, float width, float height, float resolution, Pose origin) {
        this.stamp = stamp;
        this.resolution = resolution;
        this.width = width;
        this.height = height;
        this.origin = origin;

        JsonObject obj = jsonBuilder().put(FIELD_STAMP, stamp).put(FIELD_WIDTH, width).put(FIELD_HEIGHT, height).put(FIELD_RESOLUTION, resolution).put(FIELD_ORIGIN, origin);
        super.setJsonObject(obj);
        super.setType(TYPE);
    }

    public static MapMetaData fromJsonString(String jsonString) {
        return MapMetaData.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static MapMetaData fromMessage(RosMessage m) {
        return MapMetaData.fromJsonObject(m.getJsonObject());
    }

    public static MapMetaData fromJsonObject(JsonObject jsonObject) {
        Time stamp = jsonObject.containsKey(FIELD_STAMP) ? Time.fromJsonObject(jsonObject.getJsonObject(FIELD_STAMP)) : new Time();
        float width = jsonObject.containsKey(FIELD_WIDTH) ? jsonObject.getFloat(FIELD_WIDTH) : 0.0f;
        float height = jsonObject.containsKey(FIELD_HEIGHT) ? jsonObject.getFloat(FIELD_HEIGHT) : 0.0f;
        float resolution = jsonObject.containsKey(FIELD_RESOLUTION) ? jsonObject.getFloat(FIELD_RESOLUTION) : 0.0f;
        Pose origin = jsonObject.containsKey(FIELD_ORIGIN) ? Pose.fromJsonObject(jsonObject.getJsonObject(FIELD_ORIGIN)) : new Pose();

        return new MapMetaData(stamp, width, height, resolution, origin);
    }

    public Time getStamp() {
        return stamp;
    }

    public void setStamp(Time stamp) {
        this.stamp = stamp;
        this.jsonObject.put(FIELD_STAMP, stamp.getJsonObject());
    }

    public Pose getOrigin() {
        return origin;
    }

    public void setOrigin(Pose origin) {
        this.origin = origin;
        this.jsonObject.put(FIELD_ORIGIN, origin.getJsonObject());
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
        this.jsonObject.put(FIELD_WIDTH, width);
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
        this.jsonObject.put(FIELD_HEIGHT, height);
    }

    public float getResolution() {
        return resolution;
    }

    public void setResolution(float resolution) {
        this.resolution = resolution;
        this.jsonObject.put(FIELD_RESOLUTION, resolution);
    }

    @Override
    public MapMetaData clone() {
        return new MapMetaData(this.stamp, this.width, this.height, this.resolution, this.origin);
    }
}
