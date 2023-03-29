package io.github.twinklekhj.ros.type.tf;

import io.github.twinklekhj.ros.type.RosMessage;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;
import java.util.Optional;

@ToString
public class TF2Error extends RosMessage {
    public static final String TYPE = "tf2_msgs/TF2Error";

    public static final String FIELD_ERROR = "error";
    public static final String FIELD_MESSAGE = "error_string";

    private final Status error;
    private final String message;

    public TF2Error() {
        this(Status.NO_ERROR, "");
    }

    public TF2Error(Status error, String message) {
        this.error = error;
        this.message = message;

        JsonObject json = jsonBuilder().put(FIELD_ERROR, error.value).put(FIELD_MESSAGE, message);

        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static TF2Error fromJsonString(String jsonString) {
        return TF2Error.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static TF2Error fromMessage(RosMessage m) {
        return TF2Error.fromJsonObject(m.getJsonObject());
    }

    public static TF2Error fromJsonObject(JsonObject jsonObject) {
        Status error = Status.findByValue(jsonObject.containsKey(FIELD_ERROR) ? jsonObject.getInteger(FIELD_ERROR) : 0).orElse(Status.NO_ERROR);
        String message = jsonObject.containsKey(FIELD_MESSAGE) ? jsonObject.getString(FIELD_MESSAGE) : "";

        return new TF2Error(error, message);
    }

    public Status getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public TF2Error clone() {
        return new TF2Error(this.error, this.message);
    }

    private enum Status {
        NO_ERROR(0),
        LOOKUP_ERROR(1),
        CONNECTIVITY_ERROR(2),
        EXTRAPOLATION_ERROR(3),
        INVALID_ARGUMENT_ERROR(4),
        TIMEOUT_ERROR(5),
        TRANSFORM_ERROR(6),
        ;

        final int value;

        Status(int value) {
            this.value = value;
        }

        private static Optional<Status> findByValue(int value) {
            return Arrays.stream(Status.values()).filter(status -> status.value == value).findFirst();
        }
    }
}
