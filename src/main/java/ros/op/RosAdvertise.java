package ros.op;


import lombok.Builder;
import lombok.NonNull;
import org.json.JSONObject;

/**
 * @author khj
 * @apiNote topic - 게시를 알릴 토픽명
 * type - 토픽 유형
 * id - 공고할 ID
 */

@Builder
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

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return new JSONObject().put("op", this.op.code).put("topic", this.topic).put("type", this.type).put("id", this.id).toString();
    }

    @Override
    public Type getOperation() {
        return this.op;
    }
}
