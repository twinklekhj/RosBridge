package io.github.twinklekhj.ros.ws.listener;

import io.vertx.core.json.JsonObject;


public interface RosSubscribeDelegate {
    void receive(JsonObject paramJsonNode, String paramString);
}