package io.github.twinklekhj.ros.type.artags;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.geometry.PoseStamped;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class AlvarMarker extends RosMessage {
    public static final String TYPE = "ar_track_alvar/AlvarMarker";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_ID = "id";
    public static final String FIELD_CONFIDENCE = "confidence";
    public static final String FIELD_POSE = "pose";

    private Header header;
    private int id;
    private int confidence;
    private PoseStamped pose;

    public AlvarMarker() {
        this(new Header(), 0, 0, new PoseStamped());
    }

    public AlvarMarker(int id, int confidence, PoseStamped pose) {
        this(new Header(), id, 0, pose);
    }

    public AlvarMarker(Header header, int id, int confidence, PoseStamped pose) {
        this.header = header;
        this.id = id;
        this.confidence = confidence;
        this.pose = pose;

        JsonObject json = jsonBuilder()
                .put(FIELD_HEADER, header.getJsonObject())
                .put(FIELD_ID, id)
                .put(FIELD_CONFIDENCE, confidence)
                .put(FIELD_POSE, pose.getJsonObject());

        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static AlvarMarker fromJsonString(String jsonString) {
        return AlvarMarker.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static AlvarMarker fromMessage(RosMessage m) {
        return AlvarMarker.fromJsonObject(m.getJsonObject());
    }

    public static AlvarMarker fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        int id = jsonObject.containsKey(FIELD_ID) ? jsonObject.getInteger(FIELD_ID) : 0;
        int confidence = jsonObject.containsKey(FIELD_CONFIDENCE) ? jsonObject.getInteger(FIELD_CONFIDENCE) : 0;
        PoseStamped pose = jsonObject.containsKey(FIELD_POSE) ? PoseStamped.fromJsonObject(jsonObject.getJsonObject(FIELD_POSE)) : new PoseStamped();

        return new AlvarMarker(header, id, confidence, pose);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this.jsonObject.put(FIELD_ID, id);
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
        this.jsonObject.put(FIELD_CONFIDENCE, confidence);
    }

    public PoseStamped getPose() {
        return pose;
    }

    public void setPose(PoseStamped pose) {
        this.pose = pose;
        this.jsonObject.put(FIELD_POSE, pose.getJsonObject());
    }

    @Override
    public AlvarMarker clone() {
        return new AlvarMarker(this.header, this.id, this.confidence, this.pose);
    }
}
