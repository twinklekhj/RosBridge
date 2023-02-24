package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.primitives.Primitive;
import org.json.JSONObject;


/**
 * The std_msgs/Header message.
 * Standard metadata for higher-level stamped data types.
 * This is generally used to communicate timestamped data in a particular coordinate frame.
 */
public class Header extends RosMessage {
    public static final String FIELD_SEQ = "seq";
    public static final String FIELD_STAMP = "stamp";
    public static final String FIELD_FRAME_ID = "frame_id";

    public static final String TYPE = "std_msgs/Header";

    private final int seq;
    private final Time stamp;
    private final String frameID;

    /**
     * Create a new Header with all empty values.
     */
    public Header() {
        this(0, new Time(), "");
    }

    /**
     * Create a new Header with the given values.
     *
     * @param seq     The sequence number treated as an unsigned 32-bit integer.
     * @param stamp   The timestamp.
     * @param frameID The frame ID.
     */
    public Header(int seq, Time stamp, String frameID) {
        // build the JSON object
        super(builder().put(Header.FIELD_SEQ, Primitive.fromUInt32(seq)).put(Header.FIELD_STAMP, stamp.toJSONObject()).put(Header.FIELD_FRAME_ID, frameID), Header.TYPE);
        this.seq = seq;
        this.stamp = stamp;
        this.frameID = frameID;
    }

    /**
     * Create a new Header based on the given JSON string. Any missing values
     * will be set to their defaults.
     *
     * @param jsonString The JSON string to parse.
     * @return A Header message based on the given JSON string.
     */
    public static Header fromJsonString(String jsonString) {
        // convert to a message
        return Header.fromMessage(new RosMessage(jsonString));
    }

    /**
     * Create a new Header based on the given Message. Any missing values will
     * be set to their defaults.
     *
     * @param m The Message to parse.
     * @return A Header message based on the given Message.
     */
    public static Header fromMessage(RosMessage m) {
        // get it from the JSON object
        return Header.fromJSONObject(m.toJSONObject());
    }

    /**
     * Create a new Header based on the given JSON object. Any missing values
     * will be set to their defaults.
     *
     * @param jsonObject The JSON object to parse.
     * @return A Header message based on the given JSON object.
     */
    public static Header fromJSONObject(JSONObject jsonObject) {
        // check the fields
        long seq64 = jsonObject.has(Header.FIELD_SEQ) ? jsonObject.getLong(Header.FIELD_SEQ) : 0;
        Time stamp = jsonObject.has(Header.FIELD_STAMP) ? Time.fromJSONObject(jsonObject.getJSONObject(Header.FIELD_SEQ)) : new Time();
        String frameID = jsonObject.has(Header.FIELD_FRAME_ID) ? jsonObject.getString(Header.FIELD_FRAME_ID) : "";

        // convert to a 32-bit number
        int seq32 = Primitive.toUInt32(seq64);
        return new Header(seq32, stamp, frameID);
    }

    /**
     * Get the sequence value of this header which should be treated as an
     * unsigned 32-bit integer.
     *
     * @return The sequence value of this header.
     */
    public int getSeq() {
        return this.seq;
    }

    /**
     * Get the timestamp value of this header.
     *
     * @return The timestamp value of this header.
     */
    public Time getStamp() {
        return this.stamp;
    }

    /**
     * Get the frame ID value of this header.
     *
     * @return The frame ID value of this header.
     */
    public String getFrameID() {
        return this.frameID;
    }

    /**
     * Create a clone of this Header.
     */
    @Override
    public Header clone() {
        // time primitives are mutable, create a clone
        return new Header(this.seq, this.stamp.clone(), this.frameID);
    }
}
