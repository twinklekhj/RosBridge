package io.github.twinklekhj.ros.op;


import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Advertise Service
 * <p>
 * topic - 게시를 알릴 service
 * type - service 유형
 * id - 공고할 ID
 */
@Builder
@RequiredArgsConstructor
@ToString
public class RosAdvertiseService implements RosOperation {
    private final Type op = Type.ADVERTISE_SERVICE;
    @Builder.Default
    private final String id = String.format("advertise_service_%s", RosOperation.current());
    @NonNull
    private final String service;
    @NonNull
    private final String type;

    private static RosAdvertiseServiceBuilder builder() {
        return new RosAdvertiseServiceBuilder();
    }

    public static RosAdvertiseServiceBuilder builder(String service, String type) {
        return builder().service(service).type(type);
    }

    public static RosAdvertiseServiceBuilder builder(RosCommand command) {
        return builder().service(command.getName()).type(command.getType());
    }

    public String getId() {
        return id;
    }

    @Override
    public JsonObject getJsonObject() {
        return new JsonObject()
                .put("op", this.op.code)
                .put("service", this.service)
                .put("type", this.type)
                .put("id", this.id);
    }

    @Override
    public Type getOperation() {
        return this.op;
    }
}
