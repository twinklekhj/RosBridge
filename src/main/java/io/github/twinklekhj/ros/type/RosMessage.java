package io.github.twinklekhj.ros.type;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Map;

@Builder
@AllArgsConstructor
public class RosMessage {
    public static final String EMPTY_MESSAGE = "{}";

    private final JsonObject jsonObject;
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

    public RosMessage(JsonObject jsonObject, String type) {
        this.jsonObject = jsonObject;
        this.jsonString = toJsonString(jsonObject);
        this.type = type;
    }

    public static JsonObject jsonBuilder() {
        return new JsonObject();
    }
    public static JsonObject jsonBuilder(String jsonString) {
        return new JsonObject(jsonString);
    }
    public static JsonObject jsonBuilder(Map<?,?> jsonMap) {
        return JsonObject.mapFrom(jsonMap);
    }

    public static String toJsonString(JsonObject jsonObject) {
        return jsonObject.toString();
    }

    private static RosMessageBuilder builder() {
        return new RosMessageBuilder();
    }

    public static RosMessageBuilder builder(String jsonString, String type) {
        return builder().jsonString(jsonString).jsonObject(jsonBuilder(jsonString)).type(type);
    }

    public static RosMessageBuilder builder(JsonObject jsonObject, String type) {
        return builder().jsonString(toJsonString(jsonObject)).jsonObject(jsonObject).type(type);
    }

    public String getType() {
        return type;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public String getJsonString() {
        return jsonString;
    }
}
