package io.github.twinklekhj.ros.op;


import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.*;


/**
 * Publish Topic
 * topic - 게시할 토픽명
 * type - 게시할 토픽유형
 * msg - 게시할 메시지
 */
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class RosTopic implements RosOperation {
    private final Type op = Type.PUBLISH;
    @Builder.Default
    private final String id = String.format("publish_%s", RosOperation.current());
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

    public static RosTopicBuilder builder(String topic, RosMessage.Type type) {
        return builder(topic, type.getName());
    }

    public static RosTopicBuilder builder(RosCommand command) {
        return builder(command.getName(), command.getType());
    }

    public static RosTopicBuilder builder(String topic, RosMessage msg) {
        return builder(topic, msg.getType()).msg(msg);
    }

    @Override
    public JsonObject getJsonObject() {
        JsonObject json = new JsonObject()
                .put("op", this.op.code)
                .put("topic", this.name)
                .put("type", this.type)
                .put("id", this.id);

        if (this.msg != null) {
            json.put("msg", this.msg);
        }

        return json;
    }

    public String getId() {
        return id;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public static class RosTopicBuilder {
        public RosTopicBuilder msg(Object msg) {
            if (msg instanceof RosMessage) {
                this.msg = ((RosMessage) msg).getJsonObject();
            } else {
                this.msg = msg;
            }
            return this;
        }
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
