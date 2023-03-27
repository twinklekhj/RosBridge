package io.github.twinklekhj.ros;

import io.github.twinklekhj.ros.op.RosCommand;
import io.github.twinklekhj.ros.type.std.Int32;

public enum Topics implements RosCommand {
    TEST("/test", Int32.TYPE);

    String name;
    String type;

    Topics(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }
}
