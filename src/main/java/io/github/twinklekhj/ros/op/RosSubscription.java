package io.github.twinklekhj.ros.op;


import io.github.twinklekhj.ros.type.MessageType;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


/**
 * Subscribe topic
 * <p>
 * topic - 구독할 토픽명
 * type - 구독할 토픽의 추정 유형
 * throttle_rate - 전송되는 메시지 사이에 경과해야 하는 최소 시간(miliseconds). 기본 값은 0
 * queue_length - 메시지를 버퍼링할 대기열의 크기. 메시지는 throttle_rate의 결과로 버퍼링됨. 기본값은 0 (대기열 없음)
 * id - id가 지정된 경우 이 특정 구독은 ID를 참조하여 구독을 취소할 수 있다.
 * fragment_size: 메시지가 조각화되기 전에 걸릴 수 있는 최대 크키
 * compression - 메시지에 사용할 압축 체계를 지정하는 선택적 문자열. 유효한 값은 "none", "png", "cbor", "cbor-raw"
 */
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class RosSubscription implements RosOperation {
    private final Type op = Type.SUBSCRIBE;
    @Builder.Default
    private final String id = String.format("subscribe_%s", RosOperation.current());
    @NonNull
    private final String topic;
    @NonNull
    private final String type;

    private int throttleRate = 0;
    private int queueLength = 0;
    private int fragmentSize = 0;

    private CompressionType compression;

    private static RosSubscriptionBuilder builder() {
        return new RosSubscriptionBuilder();
    }

    public static RosSubscriptionBuilder builder(String topic, String type) {
        return builder().topic(topic).type(type);
    }

    public static RosSubscriptionBuilder builder(String topic, MessageType type) {
        return builder().topic(topic).type(type.getName());
    }


    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public int getThrottleRate() {
        return throttleRate;
    }

    public void setThrottleRate(int throttleRate) {
        this.throttleRate = throttleRate;
    }

    public int getQueueLength() {
        return queueLength;
    }

    public void setQueueLength(int queueLength) {
        this.queueLength = queueLength;
    }

    public int getFragmentSize() {
        return fragmentSize;
    }

    public void setFragmentSize(int fragmentSize) {
        this.fragmentSize = fragmentSize;
    }

    @Override
    public String toString() {
        JsonObject json = new JsonObject()
                .put("op", this.op.code)
                .put("topic", this.topic)
                .put("id", this.id)
                .put("type", this.type);

        if (this.throttleRate != 0) {
            json.put("throttle_rate", this.throttleRate);
        }
        if (this.queueLength != 0) {
            json.put("queue_length", this.queueLength);
        }
        if (this.fragmentSize != 0) {
            json.put("fragment_size", this.fragmentSize);
        }
        if (this.compression != null && !this.compression.equals(CompressionType.NONE)) {
            json.put("compression", this.compression.code());
        }

        return json.toString();
    }

    @Override
    public Type getOperation() {
        return this.op;
    }
}
