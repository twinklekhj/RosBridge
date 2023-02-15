package ros.topic;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public interface RosListenDelegate {
    void receive(JsonNode paramJsonNode, String paramString);

    class LegacyFormat {
        public static Map<String, Object> legacyFormat(String jsonString) {
            JsonFactory jsonFactory = new JsonFactory();
            Map<String, Object> messageData = new HashMap<>();
            try {
                ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
                TypeReference<Map<String, Object>> listTypeRef = new TypeReference<Map<String, Object>>() {
                };
                messageData = objectMapper.readValue(jsonString, listTypeRef);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return messageData;
        }
    }
}