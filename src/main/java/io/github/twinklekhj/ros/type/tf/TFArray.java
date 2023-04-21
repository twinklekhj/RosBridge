package io.github.twinklekhj.ros.type.tf;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.geometry.TransformStamped;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@ToString
public class TFArray extends RosMessage {
    public static final String TYPE = "tf2_msgs/TFArray";

    public static final String FIELD_TRANSFORMS = "transforms";

    private TransformStamped[] transforms;

    public TFArray() {
        this(new TransformStamped[]{});
    }

    public TFArray(TransformStamped... transforms) {
        this.transforms = new TransformStamped[transforms.length];
        System.arraycopy(transforms, 0, this.transforms, 0, transforms.length);

        JsonObject json = jsonBuilder().put(FIELD_TRANSFORMS, getArray(transforms));

        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static TFArray fromJsonString(String jsonString) {
        return TFArray.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static TFArray fromMessage(RosMessage m) {
        return TFArray.fromJsonObject(m.getJsonObject());
    }

    public static TFArray fromJsonObject(JsonObject jsonObject) {
        JsonArray jsonCells = jsonObject.getJsonArray(FIELD_TRANSFORMS);
        TransformStamped[] transforms = {};
        if (jsonCells != null) {
            transforms = new TransformStamped[jsonCells.size()];
            for (int i = 0; i < transforms.length; i++) {
                transforms[i] = TransformStamped.fromJsonObject(jsonCells.getJsonObject(i));
            }
        }

        return new TFArray(transforms);
    }

    public TransformStamped[] getTransforms() {
        return transforms;
    }

    public void setTransforms(TransformStamped... transforms) {
        this.transforms = new TransformStamped[transforms.length];
        System.arraycopy(transforms, 0, this.transforms, 0, transforms.length);

        this.jsonObject.put(FIELD_TRANSFORMS, getArray(transforms));
    }

    public void setTransforms(List<TransformStamped> transforms) {
        this.transforms = new TransformStamped[transforms.size()];
        for (int i = 0; i < transforms.size(); i++) {
            this.transforms[i] = transforms.get(0);
        }

        this.jsonObject.put(FIELD_TRANSFORMS, getArray(this.transforms));
    }

    @Override
    public TFArray clone() {
        return new TFArray(this.transforms);
    }
}
