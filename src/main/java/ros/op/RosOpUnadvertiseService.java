package ros.op;


import lombok.AllArgsConstructor;
import org.json.JSONObject;

/**
 * Unadvertise Service
 *
 * topic - 게시 알림을 취소할 토픽명
 */
@AllArgsConstructor
public class RosOpUnadvertiseService implements RosOpRoot {
    private final RosOp op = RosOp.UNADVERTISE_TOPIC;
    private final String topic;
    private final String id;

    public RosOpUnadvertiseService(String topic){
        this.topic = topic;
        this.id = "unadvertised_service_"+ RosOpRoot.current();
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
