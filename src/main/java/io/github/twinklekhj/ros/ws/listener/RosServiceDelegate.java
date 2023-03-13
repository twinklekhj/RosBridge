package io.github.twinklekhj.ros.ws.listener;

import io.github.twinklekhj.ros.op.RosResponse;

@Deprecated
public interface RosServiceDelegate {
    void receive(RosResponse response);
}