package io.github.twinklekhj.ros.type.primitives;

import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@ToString(callSuper = true)
public class Time extends TimeBase<Time> {
    public static final String TYPE = "time";

    public Time() {
        super(Time.TYPE);
    }

    /**
     * Create a new Time with the given seconds and nanoseconds values.
     *
     * @param secs  The seconds value of this time.
     * @param nsecs The nanoseconds value of this time.
     */
    public Time(int secs, int nsecs) {
        super(secs, nsecs, TYPE);
    }

    /**
     * Create a new Time with the given time in seconds (and partial seconds).
     *
     * @param sec The time in seconds.
     */
    public Time(double sec) {
        super(sec, TYPE);
    }

    /**
     * Create a new Time with the given time in nanoseconds.
     *
     * @param nano The time in nanoseconds.
     */
    public Time(long nano) {
        super(nano, TYPE);
    }

    /**
     * Sleep until the given time.
     *
     * @param t The time to sleep until.
     * @return If the sleep was successful.
     */
    public static boolean sleepUntil(Time t) {
        // use a duration to sleep with
        return Duration.fromSec(t.subtract(Time.now()).toSec()).sleep();
    }

    /**
     * Create a new Time message based on the current system time. Note that
     * this might not match the current ROS time.
     *
     * @return The new Time message.
     */
    public static Time now() {
        return Time.fromSec(((double) System.currentTimeMillis()) * TimeBase.MILLI_TO_SECS);
    }

    /**
     * Create a new Time message based on the given seconds.
     *
     * @param sec The time in seconds.
     * @return The new Time primitive.
     */
    public static Time fromSec(double sec) {
        return new Time(sec);
    }

    /**
     * Create a new Time message based on the given nanoseconds.
     *
     * @param nano The time in nanoseconds.
     * @return The new Time primitive.
     */
    public static Time fromNano(long nano) {
        return new Time(nano);
    }

    /**
     * Create a new Time from the given Java Data object.
     *
     * @param date The Date to create a Time from.
     * @return The resulting Time primitive.
     */
    public static Time fromDate(Date date) {
        return Time.fromSec(((double) date.getTime()) * TimeBase.MILLI_TO_SECS);
    }

    /**
     * Create a new Time based on the given JSON string. Any missing values will
     * be set to their defaults.
     *
     * @param jsonString The JSON string to parse.
     * @return A Time message based on the given JSON string.
     */
    public static Time fromJsonString(String jsonString) {
        // convert to a JSON object
        return Time.fromJsonObject(new JsonObject(jsonString));
    }

    /**
     * Create a new Time based on the given JSON object. Any missing values will
     * be set to their defaults.
     *
     * @param jsonObject The JSON object to parse.
     * @return A Time message based on the given JSON object.
     */
    public static Time fromJsonObject(JsonObject jsonObject) {
        // check the fields
        int secs = jsonObject.containsKey(FIELD_SECS) ? jsonObject.getInteger(FIELD_SECS) : 0;
        int nsecs = jsonObject.containsKey(FIELD_NSECS) ? jsonObject.getInteger(FIELD_NSECS) : 0;
        return new Time(secs, nsecs);
    }

    /**
     * Add the given Time to this Time and return a new Time with that value.
     *
     * @param t The Time to add.
     * @return A new Time with the new value.
     */
    @Override
    public Time add(Time t) {
        return new Time(this.toSec() + t.toSec());
    }

    /**
     * Subtract the given Time from this Time and return a new Time with that
     * value.
     *
     * @param t The Time to subtract.
     * @return A new Time with the new value.
     */
    @Override
    public Time subtract(Time t) {
        return new Time(this.toSec() - t.toSec());
    }

    /**
     * Check if this Time is valid. A time is valid if it is non-zero.
     *
     * @return If this Time is valid.
     */
    public boolean isValid() {
        return !this.isZero();
    }

    /**
     * Crate a new Java Date object based on this message.
     *
     * @return A new Java Date object based on this message.
     */
    public Date toDate() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis((long) (this.toSec() * (double) TimeBase.SECS_TO_MILLI));
        return c.getTime();
    }

    public LocalDateTime toLocalDateTime(){
        return this.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Create a clone of this Time.
     */
    @Override
    public Time clone() {
        return new Time(this.secs, this.nsecs);
    }
}
