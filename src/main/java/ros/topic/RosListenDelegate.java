package ros.topic;

import com.fasterxml.jackson.databind.JsonNode;


public interface RosListenDelegate {
    void receive(JsonNode paramJsonNode, String paramString);
}