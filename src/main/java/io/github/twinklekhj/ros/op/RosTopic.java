package io.github.twinklekhj.ros.op;


import io.github.twinklekhj.ros.type.MessageType;
import io.github.twinklekhj.ros.type.RosMessage;
import lombok.Builder;
import lombok.NonNull;
import org.json.JSONObject;

/**
 * Publish Topic
 * topic - 게시할 토픽명
 * type - 게시할 토픽유형
 * msg - 게시할 메시지
 */
@Builder
public class RosTopic implements RosOperation {
    private final Type op = Type.PUBLISH;
    @Builder.Default
    private final String id = String.format("publish_%s", current());
    @NonNull
    private final String name;
    @NonNull
    private final String type;

    private Object msg;

    private static RosTopicBuilder builder() {
        return new RosTopicBuilder();
    }

    public static RosTopicBuilder builder(String name, String type) {
        return builder().name(name).type(type);
    }

    public static RosTopicBuilder builder(String topic, MessageType type) {
        return builder(topic, type.getName());
    }

    public static RosTopicBuilder builder(String topic, String type, RosMessage msg) {
        return builder(topic, type).msg(msg.toJSONObject());
    }

    public static RosTopicBuilder builder(String topic, MessageType type, RosMessage msg) {
        return builder(topic, type.getName()).msg(msg.toJSONObject());
    }

    public String getId() {
        return id;
    }


    @Override
    public String toString() {
        JSONObject json = new JSONObject().put("op", this.op.code)
                .put("topic", this.name)
                .put("type", this.type)
                .put("id", this.id);

        if (this.msg != null) {
            json.put("msg", this.msg);
        }

        return json.toString();
    }

    @Override
    public Type getOperation() {
        return this.op;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
