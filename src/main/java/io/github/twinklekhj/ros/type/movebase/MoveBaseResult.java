package io.github.twinklekhj.ros.type.movebase;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class MoveBaseResult extends RosMessage {
    public static final String TYPE = "move_base_msgs/MoveBaseResult";

    public MoveBaseResult() {
        JsonObject json = jsonBuilder();
        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static MoveBaseResult fromJsonString(String jsonString) {
        return fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static MoveBaseResult fromMessage(RosMessage m) {
        return fromJsonObject(m.getJsonObject());
    }

    public static MoveBaseResult fromJsonObject(JsonObject jsonObject) {
        return new MoveBaseResult();
    }

    @Override
    public MoveBaseFeedback clone() {
        return new MoveBaseFeedback();
    }
}
