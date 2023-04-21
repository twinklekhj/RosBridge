package io.github.twinklekhj.ros.type.sensor;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;

@ToString
public class Joy extends RosMessage {
    public static final String TYPE = "sensor_msgs/CompressedImage";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_AXES = "axes";
    public static final String FIELD_BUTTONS = "buttons";

    private final Header header;
    private final float[] axes;
    private final float[] buttons;

    public Joy() {
        this(new Header(), new float[]{}, new float[]{});
    }

    public Joy(float[] axes, float[] buttons) {
        this(new Header(), new float[]{}, new float[]{});
    }

    public Joy(Header header, float[] axes, float[] buttons) {
        this.header = header;
        this.buttons = buttons;
        this.axes = new float[axes.length];

        System.arraycopy(axes, 0, this.axes, 0, axes.length);
        System.arraycopy(buttons, 0, this.buttons, 0, buttons.length);

        JsonObject json = jsonBuilder()
                .put(FIELD_HEADER, header.getJsonObject())
                .put(FIELD_AXES, axes)
                .put(FIELD_BUTTONS, buttons);

        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static Joy fromJsonString(String jsonString) {
        return Joy.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Joy fromMessage(RosMessage m) {
        return Joy.fromJsonObject(m.getJsonObject());
    }

    public static Joy fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();

        float[] axes = {};
        JsonArray jsonData = jsonObject.getJsonArray(FIELD_AXES);
        if (jsonData != null) {
            axes = new float[jsonData.size()];
            for (int i = 0; i < axes.length; i++) {
                axes[i] = jsonData.getInteger(i);
            }
        }

        float[] buttons = {};
        jsonData = jsonObject.getJsonArray(FIELD_BUTTONS);
        if (jsonData != null) {
            buttons = new float[jsonData.size()];
            for (int i = 0; i < buttons.length; i++) {
                buttons[i] = jsonData.getInteger(i);
            }
        }
        return new Joy(header, axes, buttons);
    }

    public Header getHeader() {
        return this.header;
    }

    public float[] getAxes() {
        return axes;
    }

    public float[] getButtons() {
        return buttons;
    }

    @Override
    public Joy clone() {
        return new Joy(this.header, this.axes, this.buttons);
    }
}
