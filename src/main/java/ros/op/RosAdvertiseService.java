package ros.op;


import lombok.Builder;
import lombok.NonNull;
import org.json.JSONObject;

/**
 * Advertise Service
 * <p>
 * topic - 게시를 알릴 service
 * type - service 유형
 * id - 공고할 ID
 */
@Builder
public class RosAdvertiseService implements RosOperation {
    private final Type op = Type.ADVERTISE_SERVICE;
    @Builder.Default
    private final String id = String.format("advertise_service_%s", RosOperation.current());
    @NonNull
    private final String service;
    @NonNull
    private final String type;

    private static RosAdvertiseServiceBuilder builder() {
        return new RosAdvertiseServiceBuilder();
    }

    public static RosAdvertiseServiceBuilder builder(String service, String type) {
        return builder().service(service).type(type);
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return new JSONObject().put("op", this.op.code).put("service", this.service).put("type", this.type).put("id", this.id).toString();
    }

    @Override
    public Type getOperation() {
        return this.op;
    }
}
