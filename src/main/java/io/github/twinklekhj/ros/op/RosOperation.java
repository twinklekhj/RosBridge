package io.github.twinklekhj.ros.op;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public interface RosOperation {
    static String current() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        return now.format(dtf);
    }

    Type getOperation();

    /**
     * Operation 유형
     */
    enum Type {
        ADVERTISE_TOPIC("advertise"), UNADVERTISE_TOPIC("unadvertise"), PUBLISH("publish"), SUBSCRIBE("subscribe"), UNSUBSCRIBE("unsubscribe"), CALL_SERVICE("call_service"), SERVICE_RESPONSE("service_response"), ADVERTISE_SERVICE("advertise_service"), UNADVERTISE_SERVICE("unadvertise_service");

        final String code;

        Type(String code) {
            this.code = code;
        }
    }

    /**
     * 압축 유형
     */
    enum CompressionType {
        NONE("none"), PNG("png"), CBOR("cbor"), CBOR_RAW("cbor-raw");
        public final String code;

        CompressionType(String code) {
            this.code = code;
        }

        public String code(boolean flag) {
            if (flag) {
                if (this.equals(CBOR) || this.equals(CBOR_RAW)) {
                    return NONE.code;
                }
            }
            return code;
        }

        public String code() {
            return code(false);
        }
    }
}
