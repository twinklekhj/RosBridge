package ros.listener;

import com.fasterxml.jackson.databind.JsonNode;


public interface RosSubscribeDelegate {
    void receive(JsonNode paramJsonNode, String paramString);
}