package ros.topic;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RosListeners {
    protected List<RosListenDelegate> delegates = new ArrayList<>();

    public RosListeners() {
    }

    public RosListeners(RosListenDelegate... delegates) {
        Collections.addAll(this.delegates, delegates);
    }

    public void addDelegate(RosListenDelegate delegate) {
        this.delegates.add(delegate);
    }

    public void removeDelegate(RosListenDelegate delegate) {
        this.delegates.remove(delegate);
    }

    public void receive(JsonNode data, String stringRep) {
        this.delegates.forEach(delegate -> {
            delegate.receive(data, stringRep);
        });
    }

    public int numDelegates() {
        return this.delegates.size();
    }
}
