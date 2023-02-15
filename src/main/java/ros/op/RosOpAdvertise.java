package ros.op;


import org.json.JSONObject;

/**
 * @author khj
 * @apiNote topic - 게시를 알릴 토픽명
 *  type - 토픽 유형
 *  id - 공고할 ID
 */
public class RosOpAdvertise implements RosOpRoot {
    private final RosOp op = RosOp.ADVERTISE_TOPIC;
    private final String topic;
    private final String type;
    private final String id;

    public RosOpAdvertise(String topic, String type){
        this.topic = topic;
        this.type = type;
        this.id = "advertise_topic_"+ RosOpRoot.current();
    }

    public String getId() {
        return id;
    }
    @Override
    public String toString() {
        return new JSONObject().put("op", this.op.code)
                .put("topic", this.topic)
                .put("type", this.type)
                .put("id", this.id)
                .toString();
    }

    @Override
    public RosOp getOperation() {
        return this.op;
    }
}
