package ros.op;


import org.json.JSONObject;

/**
 * Advertise Service
 *
 * topic - 게시를 알릴 service
 * type - service 유형
 * id - 공고할 ID
 */
public class RosOpAdvertiseService implements RosOpRoot {
    private final RosOp op = RosOp.ADVERTISE_SERVICE;
    private final String service;
    private final String type;
    private final String id;

    public RosOpAdvertiseService(String service, String type){
        this.service = service;
        this.type = type;
        this.id = "advertise_service_"+ RosOpRoot.current();
    }

    public String getId() {
        return id;
    }
    @Override
    public String toString() {
        return new JSONObject().put("op", this.op.code)
                .put("service", this.service)
                .put("type", this.type)
                .put("id", this.id)
                .toString();
    }

    @Override
    public RosOp getOperation() {
        return this.op;
    }
}
