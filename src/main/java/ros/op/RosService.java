package ros.op;


import lombok.Builder;
import lombok.NonNull;
import org.json.JSONObject;

import java.util.List;

@Builder
public class RosService implements RosOperation {
    private final Type op = Type.CALL_SERVICE;

    @Builder.Default
    private final String id = String.format("call_service_%s", RosOperation.current());
    @NonNull
    private final String service;

    private final CompressionType compression = CompressionType.NONE;
    private List<?> args;
    private int fragmentSize;

    private static RosServiceBuilder builder() {
        return new RosServiceBuilder();
    }

    public static RosServiceBuilder builder(String service) {
        return builder().service(service);
    }

    public static RosServiceBuilder builder(String service, List<?> args) {
        return builder().service(service).args(args);
    }

    public String getId() {
        return id;
    }

    public String getService() {
        return service;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject().put("op", this.op.code).put("service", this.service).put("id", id);

        if (!this.args.isEmpty()) {
            json.put("args", this.args);
        }
        if (this.compression != CompressionType.NONE) {
            json.put("compression", this.compression.code(true));
        }
        if (this.fragmentSize != 0) {
            json.put("fragmentSize", this.fragmentSize);
        }

        return json.toString();
    }

    @Override
    public Type getOperation() {
        return this.op;
    }
}
