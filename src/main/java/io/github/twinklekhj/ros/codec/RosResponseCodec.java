package io.github.twinklekhj.ros.codec;

import io.github.twinklekhj.ros.op.RosResponse;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

public class RosResponseCodec implements MessageCodec<RosResponse, RosResponse> {
    @Override
    public void encodeToWire(Buffer buffer, RosResponse res) {
        String json = res.getJsonObject().toString();
        int length = json.getBytes().length;

        buffer.appendInt(length);
        buffer.appendString(json);
    }

    @Override
    public RosResponse decodeFromWire(int position, Buffer buffer) {
        // My custom message starting from this *position* of buffer
        int _pos = position;

        // Length of JSON
        int length = buffer.getInt(_pos);

        // Get JSON string by it`s length
        // Jump 4 because getInt() == 4 bytes
        String jsonStr = buffer.getString(_pos+=4, _pos+=length);
        JsonObject jsonObject = new JsonObject(jsonStr);

        return RosResponse.fromJsonObject(jsonObject);
    }

    @Override
    public RosResponse transform(RosResponse rosResponse) {
        return rosResponse;
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
