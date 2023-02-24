package io.github.twinklekhj.ros.op;


import lombok.Builder;
import lombok.NonNull;
import org.json.JSONObject;

/**
 * Unadvertise Service
 * <p>
 * topic - 게시 알림을 취소할 토픽명
 */
@Builder
public class RosUnadvertiseService implements RosOperation {
    private final Type op = Type.UNADVERTISE_TOPIC;
    @Builder.Default
    private final String id = String.format("unadvertised_service_%s", RosOperation.current());
    @NonNull
    private final String service;

    private static RosUnadvertiseServiceBuilder builder() {
        return new RosUnadvertiseServiceBuilder();
    }

    public static RosUnadvertiseServiceBuilder builder(String service) {
        return builder().service(service);
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return new JSONObject().put("op", this.op.code).put("service", this.service).put("id", this.id).toString();
    }

    @Override
    public Type getOperation() {
        return this.op;
    }
}