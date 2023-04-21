package io.github.twinklekhj.ros.type.artags;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@ToString
public class AlvarMarkers extends RosMessage {
    public static final String TYPE = "ar_track_alvar/AlvarMarkers";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_MARKERS = "markers";

    private Header header;
    private AlvarMarker[] markers;

    public AlvarMarkers() {
        this(new Header());
    }

    public AlvarMarkers(AlvarMarker... markers) {
        this(new Header(), markers);
    }

    public AlvarMarkers(Header header, AlvarMarker... markers) {
        this.header = header;
        this.markers = new AlvarMarker[markers.length];
        System.arraycopy(markers, 0, this.markers, 0, markers.length);

        JsonObject json = jsonBuilder()
                .put(FIELD_HEADER, header.getJsonObject())
                .put(FIELD_MARKERS, getArray(markers));

        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static AlvarMarkers fromJsonString(String jsonString) {
        return AlvarMarkers.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static AlvarMarkers fromMessage(RosMessage m) {
        return AlvarMarkers.fromJsonObject(m.getJsonObject());
    }

    public static AlvarMarkers fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        JsonArray jsonCells = jsonObject.getJsonArray(FIELD_MARKERS);
        AlvarMarker[] status_list = {};
        if (jsonCells != null) {
            status_list = new AlvarMarker[jsonCells.size()];
            for (int i = 0; i < status_list.length; i++) {
                status_list[i] = AlvarMarker.fromJsonObject(jsonCells.getJsonObject(i));
            }
        }

        return new AlvarMarkers(header, status_list);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    public AlvarMarker[] getMarkers() {
        return markers;
    }

    public void setMarkers(AlvarMarker... markers) {
        this.markers = new AlvarMarker[markers.length];
        System.arraycopy(markers, 0, this.markers, 0, markers.length);

        this.jsonObject.put(FIELD_MARKERS, getArray(markers));
    }

    public void setMarkers(List<AlvarMarker> markers) {
        this.markers = new AlvarMarker[markers.size()];
        for (int i = 0; i < markers.size(); i++) {
            this.markers[i] = markers.get(0);
        }

        this.jsonObject.put(FIELD_MARKERS, getArray(this.markers));
    }

    @Override
    public AlvarMarkers clone() {
        return new AlvarMarkers(this.header, this.markers);
    }
}
