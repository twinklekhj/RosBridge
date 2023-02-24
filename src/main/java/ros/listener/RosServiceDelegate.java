package ros.listener;

import ros.op.RosResponse;


public interface RosServiceDelegate {
    void receive(RosResponse response);
}