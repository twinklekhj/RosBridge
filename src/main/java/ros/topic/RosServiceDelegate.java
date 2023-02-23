package ros.topic;

import ros.op.RosResponse;


public interface RosServiceDelegate {
    void receive(RosResponse response);
}