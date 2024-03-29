package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class Empty extends RosMessage {
    public static final String TYPE = "std_msgs/Empty";

    public Empty() {
        super(RosMessage.EMPTY_MESSAGE, TYPE);
    }

    public static Empty fromJsonString(String jsonString) {
        return Empty.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Empty fromMessage(RosMessage m) {
        return Empty.fromJsonObject(m.getJsonObject());
    }

    public static Empty fromJsonObject(JsonObject jsonObject) {
        return new Empty();
    }

    @Override
    public Empty clone() {
        return new Empty();
    }
}
