package ros.op;


import lombok.AllArgsConstructor;
import org.json.JSONObject;
import ros.topic.CompressionType;

/**
 * Subscribe topic
 *
 * topic - 구독할 토픽명
 * type - 구독할 토픽의 추정 유형
 * throttle_rate - 전송되는 메시지 사이에 경과해야 하는 최소 시간(miliseconds). 기본 값은 0
 * queue_length - 메시지를 버퍼링할 대기열의 크기. 메시지는 throttle_rate의 결과로 버퍼링됨. 기본값은 0 (대기열 없음)
 * id - id가 지정된 경우 이 특정 구독은 ID를 참조하여 구독을 취소할 수 있다.
 * fragment_size: 메시지가 조각화되기 전에 걸릴 수 있는 최대 크키
 * compression - 메시지에 사용할 압축 체계를 지정하는 선택적 문자열. 유효한 값은 "none", "png", "cbor", "cbor-raw"
 */
@AllArgsConstructor
public class RosOpSubscribe implements RosOpRoot {
    private final RosOp op = RosOp.SUBSCRIBE;
    private final String topic;
    private final String type;
    private final String id;
    private final int throttleRate;
    private final int queueLength;
    private final int fragmentSize;
    private final CompressionType compression;

    public static class Builder {
        private final String topic;
        private final String type;
        private final String id;
        private int throttleRate = 0;
        private int queueLength = 0;
        private int fragmentSize;
        private CompressionType compression;

        public Builder(String topic, String type){
            this.id = "subscribe_"+ RosOpRoot.current();
            this.topic = topic;
            this.type = type;
        }

        public Builder compression(CompressionType compression){
            this.compression = compression;
            return this;
        }

        public Builder throttleRate(int throttleRate){
            this.throttleRate = throttleRate;
            return this;
        }

        public Builder queueLength(int queueLength){
            this.queueLength = queueLength;
            return this;
        }
        public Builder fragmentSize(int fragmentSize){
            this.fragmentSize = fragmentSize;
            return this;
        }

        public RosOpSubscribe build(){
            return new RosOpSubscribe(this.topic, this.type, this.id, this.throttleRate, this.queueLength, this.fragmentSize, this.compression);
        }
    }

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return new JSONObject().put("op", this.op.code)
                .put("topic", this.topic)
                .put("id", this.id)
                .put("type", this.type)
                .put("throttle_rate", this.throttleRate)
                .put("queue_length", this.queueLength)
                .put("fragment_size", this.fragmentSize)
                .put("compression", this.compression.code("topic"))
                .toString();
    }

    @Override
    public RosOp getOperation() {
        return this.op;
    }
}
