package ros.op;

public enum RosOp {
    ADVERTISE_TOPIC("advertise"), UNADVERTISE_TOPIC("unadvertise"),
    PUBLISH("publish"), SUBSCRIBE("subscribe"), UNSUBSCRIBE("unsubscribe"),
    CALL_SERVICE("call_service"), SERVICE_RESPONSE("service_response"),
    ADVERTISE_SERVICE("advertise_service"), UNADVERTISE_SERVICE("unadvertise_service")
    ;

    final String code;

    RosOp(String code){
        this.code = code;
    }
}
