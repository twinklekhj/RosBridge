package ros.listener;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RosSubscribers {
    protected List<RosSubscribeDelegate> delegates = new ArrayList<>();

    public RosSubscribers() {
    }

    public RosSubscribers(RosSubscribeDelegate... delegates) {
        Collections.addAll(this.delegates, delegates);
    }

    public void addDelegate(RosSubscribeDelegate delegate) {
        this.delegates.add(delegate);
    }

    public void removeDelegate(RosSubscribeDelegate delegate) {
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
