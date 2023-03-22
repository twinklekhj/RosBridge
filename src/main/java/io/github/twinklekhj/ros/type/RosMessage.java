package io.github.twinklekhj.ros.type;

import io.vertx.core.json.JsonObject;
import lombok.Builder;

import java.util.Map;

@Builder
public class RosMessage {
    public static final String EMPTY_MESSAGE = "{}";

    protected JsonObject jsonObject = new JsonObject();
    protected String type;

    public RosMessage() {
        this.jsonObject = new JsonObject();
        this.type = "";
    }

    public RosMessage(String jsonString, String type) {
        this.jsonObject = jsonBuilder(jsonString);
        this.type = type;
    }

    public RosMessage(Map<?, ?> jsonMap, String type) {
        this.jsonObject = jsonBuilder(jsonMap);
        this.type = type;
    }

    public RosMessage(JsonObject jsonObject, String type) {
        this.jsonObject = jsonObject;
        this.type = type;
    }

    public static JsonObject jsonBuilder() {
        return new JsonObject();
    }

    public static JsonObject jsonBuilder(String jsonString) {
        return new JsonObject(jsonString);
    }

    public static JsonObject jsonBuilder(Map<?, ?> jsonMap) {
        return JsonObject.mapFrom(jsonMap);
    }

    public static String toJsonString(JsonObject jsonObject) {
        return jsonObject.toString();
    }

    private static RosMessageBuilder builder() {
        return new RosMessageBuilder();
    }

    public static RosMessageBuilder builder(String jsonString, String type) {
        return builder().jsonObject(jsonBuilder(jsonString)).type(type);
    }

    public static RosMessageBuilder builder(JsonObject jsonObject, String type) {
        return builder().jsonObject(jsonObject).type(type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
