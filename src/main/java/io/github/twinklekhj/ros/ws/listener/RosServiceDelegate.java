package io.github.twinklekhj.ros.ws.listener;

import io.github.twinklekhj.ros.op.RosResponse;


public interface RosServiceDelegate {
    void receive(RosResponse response);
}