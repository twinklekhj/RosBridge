package io.github.twinklekhj.ros.type.sensor;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class LaserScan extends RosMessage {
    public static final String TYPE = "sensor_msgs/LaserScan";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_RANGES = "ranges";
    public static final String FIELD_INTENSITIES = "intensities";
    public static final String FIELD_ANGLE_MIN = "angle_min";
    public static final String FIELD_ANGLE_MAX = "angle_max";
    public static final String FIELD_ANGLE_INCREMENT = "angle_increment";
    public static final String FIELD_TIME_INCREMENT = "time_increment";
    public static final String FIELD_SCAN_TIME = "scan_time";
    public static final String FIELD_RANGE_MIN = "range_min";
    public static final String FIELD_RANGE_MAX = "range_max";

    private final Header header;

    private final float angleMin; // start angle of the scan [rad]
    private final float angleMax; // end angle of the scan [rad]

    private final float angleIncrement; // angular distance between measurements [rad]
    private final float timeIncrement; // time between measurements [seconds]

    private final float scanTime; // time between scans [seconds]

    private final float rangeMin; // minimum range value [m]
    private final float rangeMax; // maximum range value [m]

    private final float[] ranges; // range data [m]
    private final float[] intensities; // intensity data [device-specific units]

    public LaserScan() {
        this(new Header(), 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, new float[]{}, new float[]{});
    }


    public LaserScan(Header header, float angleMin, float angleMax, float angleIncrement, float timeIncrement, float scanTime, float rangeMin, float rangeMax, float[] ranges, float[] intensities) {
        this.header = header;
        this.angleMin = angleMin;
        this.angleMax = angleMax;
        this.angleIncrement = angleIncrement;
        this.timeIncrement = timeIncrement;
        this.scanTime = scanTime;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.intensities = intensities;
        this.ranges = new float[ranges.length];

        System.arraycopy(ranges, 0, this.ranges, 0, ranges.length);
        System.arraycopy(intensities, 0, this.intensities, 0, intensities.length);

        JsonObject json = jsonBuilder().put(FIELD_HEADER, header.getJsonObject()).put(FIELD_RANGES, ranges).put(FIELD_INTENSITIES, intensities).put(FIELD_ANGLE_MIN, angleMin).put(FIELD_ANGLE_MIN, angleMax).put(FIELD_ANGLE_INCREMENT, angleIncrement).put(FIELD_TIME_INCREMENT, timeIncrement).put(FIELD_SCAN_TIME, scanTime).put(FIELD_RANGE_MIN, rangeMin).put(FIELD_RANGE_MAX, rangeMax);
        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static LaserScan fromJsonString(String jsonString) {
        return LaserScan.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static LaserScan fromMessage(RosMessage m) {
        return LaserScan.fromJsonObject(m.getJsonObject());
    }

    public static LaserScan fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();

        float angleMin = jsonObject.containsKey(FIELD_ANGLE_MIN) ? jsonObject.getFloat(FIELD_ANGLE_MIN) : 0.0F;
        float angleMax = jsonObject.containsKey(FIELD_ANGLE_MAX) ? jsonObject.getFloat(FIELD_ANGLE_MAX) : 0.0F;

        float angleIncrement = jsonObject.containsKey(FIELD_ANGLE_INCREMENT) ? jsonObject.getFloat(FIELD_ANGLE_INCREMENT) : 0.0F;
        float timeIncrement = jsonObject.containsKey(FIELD_TIME_INCREMENT) ? jsonObject.getFloat(FIELD_TIME_INCREMENT) : 0.0F;
        float scanTime = jsonObject.containsKey(FIELD_SCAN_TIME) ? jsonObject.getFloat(FIELD_SCAN_TIME) : 0.0F;

        float rangeMin = jsonObject.containsKey(FIELD_RANGE_MIN) ? jsonObject.getFloat(FIELD_RANGE_MIN) : 0.0F;
        float rangeMax = jsonObject.containsKey(FIELD_RANGE_MAX) ? jsonObject.getFloat(FIELD_RANGE_MAX) : 0.0F;

        float[] ranges = {};
        JsonArray jsonData = jsonObject.getJsonArray(FIELD_RANGES);
        if (jsonData != null) {
            ranges = new float[jsonData.size()];
            for (int i = 0; i < ranges.length; i++) {
                ranges[i] = jsonData.getFloat(i) != null ? jsonData.getFloat(i) : 0.0F;
            }
        }

        float[] intensities = null;
        jsonData = jsonObject.getJsonArray(FIELD_INTENSITIES);
        if (jsonData != null) {
            intensities = new float[jsonData.size()];
            for (int i = 0; i < intensities.length; i++) {
                intensities[i] = jsonData.getFloat(i);
            }
        }
        return new LaserScan(header, angleMin, angleMax, angleIncrement, timeIncrement, scanTime, rangeMin, rangeMax, ranges, intensities);
    }

    public Header getHeader() {
        return this.header;
    }

    public float[] getRanges() {
        return ranges;
    }

    public float[] getIntensities() {
        return intensities;
    }

    public float getAngleMin() {
        return angleMin;
    }

    public float getAngleMax() {
        return angleMax;
    }

    public float getAngleIncrement() {
        return angleIncrement;
    }

    public float getTimeIncrement() {
        return timeIncrement;
    }

    public float getRangeMin() {
        return rangeMin;
    }

    public float getRangeMax() {
        return rangeMax;
    }

    public float getScanTime() {
        return scanTime;
    }

    @Override
    public LaserScan clone() {
        return new LaserScan(this.header, this.angleMin, this.angleMax, this.angleIncrement, this.timeIncrement, this.scanTime, this.rangeMin, this.rangeMax, this.ranges, this.intensities);
    }
}
