package ros.topic;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RosSubscriber {
    protected List<RosListenDelegate> delegates = new ArrayList<>();
    public RosSubscriber() {
    }
    public RosSubscriber(RosListenDelegate... delegates) {
        Collections.addAll(this.delegates, delegates);
    }
    public void addDelegate(RosListenDelegate delegate) {
        this.delegates.add(delegate);
    }
    public void removeDelegate(RosListenDelegate delegate) {
        this.delegates.remove(delegate);
    }
    public void receive(JsonNode data, String stringRep) {
        for (RosListenDelegate delegate : this.delegates) {
            delegate.receive(data, stringRep);
        }
    }
    public int numDelegates() {
        return this.delegates.size();
    }
}
