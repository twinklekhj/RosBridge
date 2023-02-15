package ros.op;


import lombok.AllArgsConstructor;
import org.json.JSONObject;

import java.util.List;

@AllArgsConstructor
public class RosOpServiceRes implements RosOpRoot {
    private final RosOp op = RosOp.CALL_SERVICE;
    private final String service;
    private final String id;
    private final List<?> values;
    private final boolean result;

    public static class Builder {
        private final String service;
        private String id;
        private List<?> values;
        private final boolean result;

        private Builder(String service, boolean result) {
            this.service = service;
            this.result = result;
        }

        public static Builder builder(String service, boolean result){
            return new Builder(service, result);
        }

        public Builder id(String id){
            this.id = id;
            return this;
        }

        public Builder values(List<?> values){
            this.values = values;
            return this;
        }

        public RosOpServiceRes build(){
            return new RosOpServiceRes(service, id, values, result);
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
                .put("values", this.values)
                .put("result", this.result)
                .put("id", this.id)
                .toString();
    }

    @Override
    public RosOp getOperation() {
        return this.op;
    }
}
