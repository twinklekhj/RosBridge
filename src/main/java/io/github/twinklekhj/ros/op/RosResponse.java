package io.github.twinklekhj.ros.op;


import io.vertx.core.json.JsonObject;
import lombok.*;

import java.util.Collections;
import java.util.Map;


@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class RosResponse implements RosOperation {
    private final Type op = Type.CALL_SERVICE;
    @NonNull
    private final String service;
    @NonNull
    private final boolean result;

    private String id;
    private Map<String, Object> values;

    private static RosResponseBuilder builder() {
        return new RosResponseBuilder();
    }

    public static RosResponseBuilder builder(String service, boolean result) {
        return builder().service(service).result(result);
    }

    public static RosResponse fromString(String str) {
        JsonObject node = new JsonObject(str);
        return fromJsonObject(node);
    }

    public static RosResponse fromJsonObject(JsonObject node) {
        Object valuesObj = node.getMap().get("values");

        String id = node.getString("id");

        Map<String, Object> values = Collections.emptyMap();
        if (valuesObj instanceof Map) {
            values = (Map<String, Object>) valuesObj;
        } else if (valuesObj instanceof JsonObject) {
            values = ((JsonObject) valuesObj).getMap();
        }

        boolean result = node.getBoolean("result");
        String service = node.getString("service");

        return new RosResponse(service, result, id, values);
    }

    public String getService() {
        return service;
    }

    public boolean getResult() {
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    @Override
    public String toJson() {
        return new JsonObject()
                .put("op", this.op.code)
                .put("service", this.service)
                .put("values", this.values)
                .put("result", this.result)
                .put("id", this.id).toString();
    }

    @Override
    public Type getOperation() {
        return this.op;
    }
}
