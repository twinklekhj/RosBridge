package io.github.twinklekhj.ros.op;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class RosService implements RosOperation {
    private final Type op = Type.CALL_SERVICE;

    @Builder.Default
    private final String id = String.format("call_service_%s", RosOperation.current());
    @NonNull
    private final String name;
    private final String type;

    private CompressionType compression = CompressionType.NONE;
    private List<?> args;
    private int fragmentSize;

    private static RosServiceBuilder builder() {
        return new RosServiceBuilder();
    }

    public static RosServiceBuilder builder(String service) {
        return builder().name(service);
    }

    public static RosServiceBuilder builder(String service, String type) {
        return builder().name(service).type(type);
    }

    public static RosServiceBuilder builder(String service, List<?> args) {
        return builder().name(service).args(args);
    }

    public static RosServiceBuilder builder(String service, String type, List<?> args) {
        return builder().name(service).type(type).args(args);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public String getType() {
        return type;
    }

    public int getFragmentSize() {
        return fragmentSize;
    }

    public void setFragmentSize(int fragmentSize) {
        this.fragmentSize = fragmentSize;
    }

    public CompressionType getCompression() {
        return compression;
    }

    public void setCompression(CompressionType compression) {
        this.compression = compression;
    }

    public List<?> getArgs() {
        return args;
    }

    public void setArgs(List<?> args) {
        this.args = args;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject()
                .put("op", this.op.code)
                .put("service", this.name)
                .put("id", id);

        if (this.args != null && !this.args.isEmpty()) {
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

    public static class RosServiceBuilder {
        public RosServiceBuilder args(List<?> args) {
            this.args = args;
            return this;
        }

        public RosServiceBuilder args(String... arg) {
            List<String> args = new ArrayList<>();
            Collections.addAll(args, arg);
            this.args(args);

            return this;
        }
    }
}
