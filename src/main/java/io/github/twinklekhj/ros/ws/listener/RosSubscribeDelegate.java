package io.github.twinklekhj.ros.ws.listener;

import io.vertx.core.json.JsonObject;

@Deprecated
public interface RosSubscribeDelegate {
    void receive(JsonObject paramJsonNode, String paramString);
}