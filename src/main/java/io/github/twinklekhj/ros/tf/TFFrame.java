package io.github.twinklekhj.ros.tf;

import io.github.twinklekhj.ros.op.RosOperation;
import io.github.twinklekhj.ros.type.geometry.TransformStamped;
import io.vertx.core.Handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TFFrame {
    private final String frame;
    TransformStamped transform;
    Map<String, Handler<TransformStamped>> handlers;

    public TFFrame(String frame) {
        this.frame = frame;
        this.transform = null;
        this.handlers = new HashMap<>();
    }

    public boolean hasTransform() {
        return this.transform != null;
    }

    public TransformStamped getTransform() {
        return transform;
    }

    public void setTransform(TransformStamped transform) {
        this.transform = transform;
    }

    public boolean hasHandler() {
        return this.handlers.size() > 0;
    }

    public Collection<Handler<TransformStamped>> getHandlers() {
        return handlers.values();
    }

    public String addHandler(Handler<TransformStamped> handler) {
        String handlerID = String.format("%s_%s", frame, RosOperation.current());
        this.handlers.put(handlerID, handler);
        return handlerID;
    }

    public Handler<TransformStamped> removeHandler(String handlerID) {
        return this.handlers.remove(handlerID);
    }
}
