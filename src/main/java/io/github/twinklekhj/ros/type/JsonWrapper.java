package io.github.twinklekhj.ros.type;



import io.vertx.core.json.JsonObject;

import java.util.Map;

public abstract class JsonWrapper {
    public static final String EMPTY_JSON = "{}";

    private final JsonObject jsonObject;
    private final String jsonString;

    public JsonWrapper() {
        this(JsonWrapper.EMPTY_JSON);
    }

    public JsonWrapper(String jsonString) {
        this.jsonString = jsonString;
        this.jsonObject = new JsonObject(jsonString);
    }

    public JsonWrapper(Map<String, Object> jsonMap) {
        this.jsonObject = new JsonObject(jsonMap);
        this.jsonString = this.jsonObject.toString();
    }

    public JsonWrapper(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        this.jsonString = this.jsonObject.toString();
    }

    protected static JsonObject jsonBuilder() {
        return new JsonObject();
    }

    protected static JsonObject jsonBuilder(Object obj) {
        return new JsonObject(obj.toString());
    }

    public JsonObject toJsonObject() {
        return this.jsonObject;
    }

    @Override
    public String toString() {
        return this.jsonString;
    }

    public abstract JsonWrapper clone();

    @Override
    public int hashCode() {
        return this.jsonString.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || (o instanceof JsonWrapper && this.jsonString.equals(o.toString()));
    }
}
