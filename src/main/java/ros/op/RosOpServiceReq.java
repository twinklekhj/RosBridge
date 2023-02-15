package ros.op;


import lombok.AllArgsConstructor;
import org.json.JSONObject;
import ros.topic.CompressionType;

import java.util.List;

@AllArgsConstructor
public class RosOpServiceReq implements RosOpRoot {
    private final RosOp op = RosOp.CALL_SERVICE;
    private final String service;
    private final String id;
    private final List<?> args;
    private final int fragmentSize;
    private final CompressionType compression;

    public static class Builder {
        private final String service;
        private final String id;
        private List<?> args;
        private int fragmentSize;
        private CompressionType compression = CompressionType.NONE;

        private Builder(String service){
            this.service = service;
            this.id = "call_service_"+ RosOpRoot.current();
        }

        public static Builder builder(String service) {
            return new Builder(service);
        }

        public Builder args(List<?> args){
            this.args = args;
            return this;
        }

        public Builder fragmentSize(int fragmentSize){
            this.fragmentSize = fragmentSize;
            return this;
        }

        public Builder compression(CompressionType compression){
            this.compression = compression;
            return this;
        }

        public RosOpServiceReq build(){
            return new RosOpServiceReq(service, id, args, fragmentSize, compression);
        }
    }
    public String getId() {
        return id;
    }

    public String getService() {
        return service;
    }

    @Override
    public String toString() {
        return new JSONObject().put("op", this.op.code)
                .put("service", this.service)
                .put("args", this.args)
                .put("fragmentSize", this.fragmentSize)
                .put("compression", this.compression.code("service"))
                .put("id", id)
                .toString();
    }

    @Override
    public RosOp getOperation() {
        return this.op;
    }
}
