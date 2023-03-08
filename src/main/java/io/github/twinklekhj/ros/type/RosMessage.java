package io.github.twinklekhj.ros.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.json.JSONObject;

import java.util.Map;

@Builder
@AllArgsConstructor
public class RosMessage {
    public static final String EMPTY_MESSAGE = "{}";

    private final JSONObject jsonObject;
    private final String jsonString;
    private final String type;

    public RosMessage() {
        this(RosMessage.EMPTY_MESSAGE, "");
    }

    public RosMessage(String jsonString, String type) {
        this.jsonString = jsonString;
        this.jsonObject = jsonBuilder(jsonString);
        this.type = type;
    }

    public RosMessage(JSONObject jsonObject, String type) {
        this.jsonObject = jsonObject;
        this.jsonString = toJsonString(jsonObject);
        this.type = type;
    }

    public static JSONObject jsonBuilder() {
        return new JSONObject();
    }
    public static JSONObject jsonBuilder(String jsonString) {
        return new JSONObject(jsonString);
    }
    public static JSONObject jsonBuilder(Map<?,?> jsonMap) {
        return new JSONObject(jsonMap);
    }

    public static String toJsonString(JSONObject jsonObject) {
        return jsonObject.toString();
    }

    private static RosMessageBuilder builder() {
        return new RosMessageBuilder();
    }

    public static RosMessageBuilder builder(String jsonString, String type) {
        return builder().jsonString(jsonString).jsonObject(jsonBuilder(jsonString)).type(type);
    }

    public static RosMessageBuilder builder(JSONObject jsonObject, String type) {
        return builder().jsonString(toJsonString(jsonObject)).jsonObject(jsonObject).type(type);
    }

    public String getType() {
        return type;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getJsonString() {
        return jsonString;
    }
}
