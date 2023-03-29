package io.github.twinklekhj.ros.ws.codec;

import io.github.twinklekhj.ros.type.tf.TFMessage;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

public class TFMessageCodec implements MessageCodec<TFMessage, TFMessage> {
    @Override
    public void encodeToWire(Buffer buffer, TFMessage res) {
        String json = res.getJsonObject().toString();
        int length = json.getBytes().length;

        buffer.appendInt(length);
        buffer.appendString(json);
    }

    @Override
    public TFMessage decodeFromWire(int position, Buffer buffer) {
        // My custom message starting from this *position* of buffer
        int _pos = position;

        // Length of JSON
        int length = buffer.getInt(_pos);

        // Get JSON string by it`s length
        // Jump 4 because getInt() == 4 bytes
        String jsonStr = buffer.getString(_pos += 4, _pos += length);
        JsonObject jsonObject = new JsonObject(jsonStr);

        return TFMessage.fromJsonObject(jsonObject);
    }

    @Override
    public TFMessage transform(TFMessage tfMessage) {
        return tfMessage;
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
