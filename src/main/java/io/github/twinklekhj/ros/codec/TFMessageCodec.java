package io.github.twinklekhj.ros.codec;

import io.github.twinklekhj.ros.type.tf.TFArray;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

public class TFMessageCodec implements MessageCodec<TFArray, TFArray> {
    @Override
    public void encodeToWire(Buffer buffer, TFArray res) {
        String json = res.getJsonObject().toString();
        int length = json.getBytes().length;

        buffer.appendInt(length);
        buffer.appendString(json);
    }

    @Override
    public TFArray decodeFromWire(int position, Buffer buffer) {
        // My custom message starting from this *position* of buffer
        int _pos = position;

        // Length of JSON
        int length = buffer.getInt(_pos);

        // Get JSON string by it`s length
        // Jump 4 because getInt() == 4 bytes
        String jsonStr = buffer.getString(_pos += 4, _pos += length);
        JsonObject jsonObject = new JsonObject(jsonStr);

        return TFArray.fromJsonObject(jsonObject);
    }

    @Override
    public TFArray transform(TFArray tfArray) {
        return tfArray;
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
