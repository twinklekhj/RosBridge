package io.github.twinklekhj.ros.op;


import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

/**
 * Unadvertise topic
 * topic - ROS MASTER 에서 토픽 삭제
 */
@Builder
@RequiredArgsConstructor
public class RosUnadvertise implements RosOperation {
    private final Type op = Type.UNADVERTISE_TOPIC;
    @Builder.Default
    private final String id = String.format("unadvertised_%s", RosOperation.current());
    @NonNull
    private final String topic;

    private static RosUnadvertiseBuilder builder() {
        return new RosUnadvertiseBuilder();
    }

    public static RosUnadvertiseBuilder builder(String topic) {
        return builder().topic(topic);
    }

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return new JSONObject()
                .put("op", this.op.code)
                .put("topic", this.topic)
                .put("id", this.id).toString();
    }

    @Override
    public Type getOperation() {
        return this.op;
    }
}
