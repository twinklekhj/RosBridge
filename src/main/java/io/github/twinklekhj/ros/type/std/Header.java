package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import io.github.twinklekhj.ros.type.primitives.Time;
import io.vertx.core.json.JsonObject;
import lombok.ToString;


@ToString
public class Header extends RosMessage {
    public static final String TYPE = "std_msgs/Header";

    public static final String FIELD_SEQ = "seq";
    public static final String FIELD_STAMP = "stamp";
    public static final String FIELD_FRAME_ID = "frame_id";

    private int seq;
    private Time stamp;
    private String frameID;

    /**
     * Create a new Header with all empty values.
     */
    public Header() {
        this(0, new Time(), "");
    }


    /**
     * Create a new Header with frame id;
     *
     * @param frameID The frame ID.
     */
    public Header(String frameID) {
        this(0, new Time(), frameID);
    }


    /**
     * Create a new Header with the given values.
     *
     * @param seq     The sequence number treated as an unsigned 32-bit integer.
     * @param stamp   The timestamp.
     * @param frameID The frame ID.
     */
    public Header(int seq, Time stamp, String frameID) {
        this.seq = seq;
        this.stamp = stamp;
        this.frameID = frameID;

        super.setJsonObject(jsonBuilder().put(FIELD_SEQ, Primitive.fromUInt32(seq)).put(FIELD_STAMP, stamp.toJsonObject()).put(FIELD_FRAME_ID, frameID));
        super.setType(TYPE);
    }

    public static Header fromJsonString(String jsonString) {
        return Header.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Header fromMessage(RosMessage m) {
        return Header.fromJsonObject(m.getJsonObject());
    }

    public static Header fromJsonObject(JsonObject jsonObject) {
        long seq64 = jsonObject.containsKey(FIELD_SEQ) ? jsonObject.getLong(FIELD_SEQ) : 0;
        Time stamp = jsonObject.containsKey(FIELD_STAMP) ? Time.fromJsonObject(jsonObject.getJsonObject(FIELD_STAMP)) : new Time();
        String frameID = jsonObject.containsKey(FIELD_FRAME_ID) ? jsonObject.getString(FIELD_FRAME_ID) : "";

        // convert to a 32-bit number
        int seq32 = Primitive.toUInt32(seq64);
        return new Header(seq32, stamp, frameID);
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
        this.jsonObject.put(FIELD_SEQ, Primitive.fromUInt32(seq));
    }

    public Time getStamp() {
        return stamp;
    }

    public void setStamp(Time stamp) {
        this.stamp = stamp;
        this.jsonObject.put(FIELD_STAMP, stamp.toJsonObject());
    }

    public String getFrameID() {
        return frameID;
    }

    public void setFrameID(String frameID) {
        this.frameID = frameID;
        this.jsonObject.put(FIELD_FRAME_ID, frameID);
    }

    @Override
    public Header clone() {
        return new Header(this.seq, this.stamp.clone(), this.frameID);
    }
}
