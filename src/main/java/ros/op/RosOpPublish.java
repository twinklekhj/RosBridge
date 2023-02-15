package ros.op;


import lombok.AllArgsConstructor;
import org.json.JSONObject;

/**
 * Publish Topic
 * topic - 게시할 토픽명
 * type - 게시할 토픽유형
 * msg - 게시할 메시지
 */
@AllArgsConstructor
public class RosOpPublish implements RosOpRoot {
    private final RosOp op = RosOp.PUBLISH;
    private final String topic;
    private final String type;
    private final String id;
    private Object msg;

    public RosOpPublish(String topic, String type){
        this.id = "publish_"+ RosOpRoot.current();
        this.topic = topic;
        this.type = type;
    }
    public RosOpPublish(String topic, String type, Object msg){
        this(topic, type);
        this.msg = msg;
    }
    public String getId() {
        return id;
    }

    public void msg(Object msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return new JSONObject().put("op", this.op.code)
                .put("topic", this.topic)
                .put("type", this.type)
                .put("id", this.id)
                .put("msg", this.msg)
                .toString();
    }

    @Override
    public RosOp getOperation() {
        return this.op;
    }
}
