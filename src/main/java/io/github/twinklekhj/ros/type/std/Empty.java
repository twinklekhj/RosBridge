package io.github.twinklekhj.ros.type.std;

import io.github.twinklekhj.ros.type.RosMessage;
import org.json.JSONObject;

public class Empty extends RosMessage {
    public static final String TYPE = "std_msgs/Empty";

    public Empty() {
        super(RosMessage.EMPTY_MESSAGE, Empty.TYPE);
    }

    public static Empty fromJsonString(String jsonString) {
        return Empty.fromMessage(new RosMessage(jsonString));
    }

    public static Empty fromMessage(RosMessage m) {
        return Empty.fromJSONObject(m.toJSONObject());
    }

    public static Empty fromJSONObject(JSONObject jsonObject) {
        return new Empty();
    }

    @Override
    public Empty clone() {
        return new Empty();
    }
}
