package io.github.twinklekhj.ros.type.navigation;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;

@ToString
public class OccupancyGrid extends RosMessage {
    public static final String TYPE = "nav_msgs/OccupancyGrid";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_INFO = "info";
    public static final String FIELD_DATA = "data";

    private Header header;
    private MapMetaData info;
    private int[] data;

    public OccupancyGrid() {
        this(new Header(), new MapMetaData(), new int[]{});
    }

    public OccupancyGrid(Header header, MapMetaData info, int[] data) {
        this.header = header;
        this.info = info;
        this.data = new int[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);

        JsonObject obj = jsonBuilder().put(FIELD_HEADER, header.getJsonObject())
                .put(FIELD_DATA, jsonBuilder(Arrays.toString(data)))
                .put(FIELD_INFO, info.getJsonObject());

        super.setJsonObject(obj);
        super.setType(TYPE);
    }

    public static OccupancyGrid fromJsonString(String jsonString) {
        return fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static OccupancyGrid fromMessage(RosMessage m) {
        return fromJsonObject(m.getJsonObject());
    }

    public static OccupancyGrid fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        MapMetaData info = jsonObject.containsKey(FIELD_INFO) ? MapMetaData.fromJsonObject(jsonObject.getJsonObject(FIELD_INFO)) : new MapMetaData();

        JsonArray jsonData = jsonObject.getJsonArray(FIELD_DATA);
        int[] data = {};
        if (jsonData != null) {
            data = new int[jsonData.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = jsonData.getInteger(i);
            }
        }

        return new OccupancyGrid(header, info, data);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    public int[] getData() {
        return data;
    }

    public void setData(int ...data) {
        this.data = data;
        System.arraycopy(data, 0, this.data, 0, data.length);
        this.jsonObject.put(FIELD_DATA, Arrays.toString(data));
    }

    public MapMetaData getInfo() {
        return info;
    }

    public void setInfo(MapMetaData info) {
        this.info = info;
        this.jsonObject.put(FIELD_INFO, info.getJsonObject());
    }

    @Override
    public OccupancyGrid clone() {
        return new OccupancyGrid(this.header, this.info, this.data);
    }
}
