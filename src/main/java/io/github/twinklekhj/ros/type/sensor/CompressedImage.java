package io.github.twinklekhj.ros.type.sensor;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;

@ToString
public class CompressedImage extends RosMessage {
    public static final String TYPE = "sensor_msgs/CompressedImage";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_FORMAT = "format";
    public static final String FIELD_DATA = "data";

    private Header header;
    private String format;
    private int[] data;

    public CompressedImage() {
        this(new Header(), "png", new int[]{});
    }

    public CompressedImage(String format, int[] data) {
        this(new Header(), format, new int[]{});
    }

    public CompressedImage(Header header, String format, int[] data) {
        this.header = header;
        this.format = format;
        this.data = new int[data.length];

        System.arraycopy(data, 0, this.data, 0, data.length);
        JsonObject json = jsonBuilder()
                .put(FIELD_HEADER, header.getJsonObject())
                .put(FIELD_FORMAT, format)
                .put(FIELD_DATA, data);

        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static CompressedImage fromJsonString(String jsonString) {
        return CompressedImage.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static CompressedImage fromMessage(RosMessage m) {
        return CompressedImage.fromJsonObject(m.getJsonObject());
    }

    public static CompressedImage fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        String format = jsonObject.containsKey(FIELD_FORMAT) ? jsonObject.getString(FIELD_FORMAT) : "";

        int[] data = {};
        JsonArray jsonData = jsonObject.getJsonArray(FIELD_DATA);
        if (jsonData != null) {
            data = new int[jsonData.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = jsonData.getInteger(i);
            }
        }
        return new CompressedImage(header, format, data);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
        this.jsonObject.put(FIELD_FORMAT, format);
    }

    public int[] getData() {
        return this.data;
    }

    public void setData(int... data) {
        this.data = data;
        System.arraycopy(data, 0, this.data, 0, data.length);
        this.jsonObject.put(FIELD_DATA, data);
    }

    @Override
    public CompressedImage clone() {
        return new CompressedImage(this.header, this.format, this.data);
    }
}
