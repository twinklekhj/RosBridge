package io.github.twinklekhj.ros.type;

import org.json.JSONObject;

public class RosMessage extends JsonWrapper {
    public static final String EMPTY_MESSAGE = EMPTY_JSON;
    private String type;

    public RosMessage() {
        this(RosMessage.EMPTY_MESSAGE, "");
    }

    public RosMessage(String jsonString) {
        this(jsonString, "");
    }

    public RosMessage(String jsonString, String type) {
        super(jsonString);
        this.type = type;
    }

    public RosMessage(JSONObject jsonObject) {
        this(jsonObject, "");
    }

    public RosMessage(JSONObject jsonObject, String type) {
        super(jsonObject);
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public RosMessage clone() {
        return new RosMessage(this.toJSONObject(), this.type);
    }
}
