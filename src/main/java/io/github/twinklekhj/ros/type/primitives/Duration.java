package io.github.twinklekhj.ros.type.primitives;

import org.json.JSONObject;

public class Duration extends TimeBase<Duration> {
    public static final String TYPE = "duration";

    public Duration() {
        super(Duration.TYPE);
    }

    public Duration(int secs, int nsecs) {
        super(secs, nsecs, Duration.TYPE);
    }

    public Duration(double sec) {
        super(sec, Duration.TYPE);
    }

    public Duration(long nano) {
        super(nano, Duration.TYPE);
    }

    public static Duration fromSec(double sec) {
        return new Duration(sec);
    }

    public static Duration fromNano(long nano) {
        return new Duration(nano);
    }

    public static Duration fromJsonString(String jsonString) {
        return Duration.fromJSONObject(new JSONObject(jsonString));
    }

    public static Duration fromJSONObject(JSONObject jsonObject) {
        int secs = jsonObject.has(FIELD_SECS) ? jsonObject.getInt(FIELD_SECS) : 0;
        int nsecs = jsonObject.has(FIELD_NSECS) ? jsonObject.getInt(FIELD_NSECS) : 0;
        return new Duration(secs, nsecs);
    }

    @Override
    public Duration add(Duration d) {
        return new Duration(this.toSec() + d.toSec());
    }

    @Override
    public Duration subtract(Duration d) {
        return new Duration(this.toSec() - d.toSec());
    }

    public boolean sleep() {
        try {
            Thread.sleep((this.secs * SECS_TO_MILLI) + ((long) ((double) this.nsecs / (double) MILLI_TO_NSECS)), this.nsecs % (int) MILLI_TO_NSECS);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public Duration clone() {
        return new Duration(this.secs, this.nsecs);
    }
}
