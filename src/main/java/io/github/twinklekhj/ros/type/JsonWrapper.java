package io.github.twinklekhj.ros.type;

import org.json.JSONObject;

import java.util.Map;

public abstract class JsonWrapper {
    public static final String EMPTY_JSON = "{}";

    private final JSONObject jsonObject;
    private final String jsonString;

    public JsonWrapper() {
        this(JsonWrapper.EMPTY_JSON);
    }

    public JsonWrapper(String jsonString) {
        this.jsonString = jsonString;
        this.jsonObject = new JSONObject(jsonString);
    }

    public JsonWrapper(Map<String, Object> jsonMap) {
        this.jsonObject = new JSONObject(jsonMap);
        this.jsonString = this.jsonObject.toString();
    }

    public JsonWrapper(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        this.jsonString = this.jsonObject.toString();
    }

    protected static JSONObject jsonBuilder() {
        return new JSONObject();
    }

    protected static JSONObject jsonBuilder(Object obj) {
        return new JSONObject(obj);
    }

    public JSONObject toJSONObject() {
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
