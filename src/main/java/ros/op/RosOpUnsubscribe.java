package ros.op;


import lombok.AllArgsConstructor;
import org.json.JSONObject;

/**
 * Unsubscribe topic
 *
 * topic - 구독 취소할 토픽명
 * id - 취소할 특정 구독 ID
 */

@AllArgsConstructor
public class RosOpUnsubscribe implements RosOpRoot {
    private final RosOp op = RosOp.UNSUBSCRIBE;
    private final String topic;
    private String id;

    public RosOpUnsubscribe(String topic){
        this.topic = topic;
        id = "unsubscribe_"+ RosOpRoot.current();
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return new JSONObject().put("op", this.op.code)
                .put("topic", this.topic)
                .put("id", this.id)
                .toString();
    }

    @Override
    public RosOp getOperation() {
        return this.op;
    }
}
