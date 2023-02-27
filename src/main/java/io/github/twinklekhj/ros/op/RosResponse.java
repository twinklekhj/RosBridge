package io.github.twinklekhj.ros.op;


import lombok.Builder;
import lombok.NonNull;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

@Builder
public class RosResponse implements RosOperation {
    private final Type op = Type.CALL_SERVICE;
    @NonNull
    private final String service;
    @NonNull
    private final boolean result;
    private String id;
    private List<?> values;

    private static RosResponseBuilder builder() {
        return new RosResponseBuilder();
    }

    public static RosResponseBuilder builder(String service, boolean result) {
        return builder().service(service).result(result);
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

    public List<?> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return new JSONObject()
                .put("op", this.op.code)
                .put("service", this.service)
                .put("values", Arrays.toString(this.values.toArray()))
                .put("result", this.result)
                .put("id", this.id).toString();
    }

    @Override
    public Type getOperation() {
        return this.op;
    }
}
