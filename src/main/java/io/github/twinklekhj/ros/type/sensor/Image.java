package io.github.twinklekhj.ros.type.sensor;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;

/**
 * @implNote http://docs.ros.org/en/noetic/api/sensor_msgs/html/msg/Image.html
 */
@ToString
public class Image extends RosMessage {
    public static final String TYPE = "sensor_msgs/Image";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_HEIGHT = "height";
    public static final String FIELD_WIDTH = "width";
    public static final String FIELD_ENCODING = "encoding";
    public static final String FIELD_DATA = "data";
    public static final String FIELD_IS_BIGEDIAN = "is_bigendian";
    public static final String FIELD_STEP = "step";
    private final int height;
    private final int width;
    private final int is_bigendian; // is this data bigendian?
    private final int step; // Full row length in bytes
    private final Header header;
    private final String encoding;
    private final int[] data;

    public Image() {
        this(new Header(), 0, 0, "", 0, 0, new int[]{});
    }

    public Image(int height, int width, String encoding, int is_bigendian, int step, int[] data) {
        this(new Header(), height, width, encoding, is_bigendian, step, new int[]{});
    }

    public Image(Header header, int height, int width, String encoding, int is_bigendian, int step, int[] data) {
        this.header = header;
        this.height = height;
        this.width = width;
        this.encoding = encoding;
        this.data = new int[data.length];
        this.is_bigendian = is_bigendian;
        this.step = step;

        System.arraycopy(data, 0, this.data, 0, data.length);
        JsonObject json = jsonBuilder()
                .put(FIELD_HEADER, header.getJsonObject())
                .put(FIELD_ENCODING, encoding)
                .put(FIELD_DATA, Arrays.toString(data));

        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static Image fromJsonString(String jsonString) {
        return Image.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Image fromMessage(RosMessage m) {
        return Image.fromJsonObject(m.getJsonObject());
    }

    public static Image fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        int height = jsonObject.containsKey(FIELD_HEIGHT) ? jsonObject.getInteger(FIELD_HEIGHT) : 0;
        int width = jsonObject.containsKey(FIELD_WIDTH) ? jsonObject.getInteger(FIELD_WIDTH) : 0;
        int is_bigedian = jsonObject.containsKey(FIELD_IS_BIGEDIAN) ? jsonObject.getInteger(FIELD_IS_BIGEDIAN) : 0;
        int step = jsonObject.containsKey(FIELD_STEP) ? jsonObject.getInteger(FIELD_STEP) : 0;

        String encoding = jsonObject.containsKey(FIELD_ENCODING) ? jsonObject.getString(FIELD_ENCODING) : "";

        int[] data = {};
        JsonArray jsonData = jsonObject.getJsonArray(FIELD_DATA);
        if (jsonData != null) {
            data = new int[jsonData.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = jsonData.getInteger(i);
            }
        }
        return new Image(header, height, width, encoding, is_bigedian, step, data);
    }

    public Header getHeader() {
        return header;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getIs_bigendian() {
        return is_bigendian;
    }

    public int getStep() {
        return step;
    }

    public int[] getData() {
        return data;
    }

    public String getEncoding() {
        return encoding;
    }

    @Override
    public Image clone() {
        return new Image(this.header, this.height, this.width, this.encoding, this.is_bigendian, this.step, this.data);
    }
}
