package io.github.twinklekhj.ros.op;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

/**
 * Unsubscribe topic
 * <p>
 * topic - 구독 취소할 토픽명
 * id - 취소할 특정 구독 ID
 */

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class RosUnsubscription implements RosOperation {
    private final Type op = Type.UNSUBSCRIBE;

    @NonNull
    private final String topic;

    private String id;

    private static RosUnsubscriptionBuilder builder() {
        return new RosUnsubscriptionBuilder();
    }

    public static RosUnsubscriptionBuilder builder(String topic) {
        return builder().topic(topic);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
