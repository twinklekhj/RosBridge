package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;


public class WrenchStamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_WRENCH = "wrench";

    public static final String TYPE = "geometry_msgs/WrenchStamped";

    private final Header header;
    private final Wrench wrench;

    public WrenchStamped() {
        this(new Header(), new Wrench());
    }

    public WrenchStamped(Header header, Wrench wrench) {
        // build the JSON object
        super(jsonBuilder().put(WrenchStamped.FIELD_HEADER, header.getJsonObject()).put(WrenchStamped.FIELD_WRENCH, wrench.getJsonObject()), WrenchStamped.TYPE);
        this.header = header;
        this.wrench = wrench;
    }

    public static WrenchStamped fromJsonString(String jsonString) {
        return WrenchStamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static WrenchStamped fromMessage(RosMessage m) {
        return WrenchStamped.fromJsonObject(m.getJsonObject());
    }

    public static WrenchStamped fromJsonObject(JsonObject jsonObject) {
        // check the fields
        Header header = jsonObject.containsKey(WrenchStamped.FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(WrenchStamped.FIELD_HEADER)) : new Header();
        Wrench wrench = jsonObject.containsKey(WrenchStamped.FIELD_WRENCH) ? Wrench.fromJsonObject(jsonObject.getJsonObject(WrenchStamped.FIELD_WRENCH)) : new Wrench();
        return new WrenchStamped(header, wrench);
    }

    public Header getHeader() {
        return this.header;
    }

    public Wrench getWrench() {
        return this.wrench;
    }

    @Override
    public WrenchStamped clone() {
        return new WrenchStamped(this.header, this.wrench);
    }
}
