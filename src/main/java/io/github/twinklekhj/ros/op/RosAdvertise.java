package io.github.twinklekhj.ros.op;


import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


/**
 * [RosOperation] 토픽 게시
 *
 * @author khj
 */

@Builder
@RequiredArgsConstructor
@ToString
public class RosAdvertise implements RosOperation {
    private final Type op = Type.ADVERTISE_TOPIC;
    @Builder.Default
    private final String id = String.format("advertise_%s", RosOperation.current());
    @NonNull
    private final String topic;
    @NonNull
    private final String type;

    private static RosAdvertiseBuilder builder() {
        return new RosAdvertiseBuilder();
    }

    public static RosAdvertiseBuilder builder(String topic, String type) {
        return builder().topic(topic).type(type);
    }

    public static RosAdvertiseBuilder builder(RosCommand command) {
        return builder().topic(command.getName()).type(command.getType());
    }

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getType() {
        return type;
    }

    @Override
    public JsonObject getJsonObject(){
        return new JsonObject()
                .put("op", this.op.code)
                .put("topic", this.topic)
                .put("type", this.type)
                .put("id", this.id);
    }

    @Override
    public Type getOperation() {
        return this.op;
    }
}
