package ros.op;


import lombok.Builder;
import lombok.NonNull;
import org.json.JSONObject;

/**
 * Unsubscribe topic
 * <p>
 * topic - 구독 취소할 토픽명
 * id - 취소할 특정 구독 ID
 */

@Builder
public class RosUnsubscribe implements RosOperation {
    private final Type op = Type.UNSUBSCRIBE;
    @Builder.Default
    private final String id = String.format("unsubscribe_%s", RosOperation.current());
    @NonNull
    private final String topic;

    private static RosUnsubscribeBuilder builder() {
        return new RosUnsubscribeBuilder();
    }

    public static RosUnsubscribeBuilder builder(String topic) {
        return builder().topic(topic);
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return new JSONObject().put("op", this.op.code).put("topic", this.topic).put("id", this.id).toString();
    }

    @Override
    public Type getOperation() {
        return this.op;
    }
}
