package io.github.twinklekhj.ros.type.geometry;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

@ToString
public class WrenchStamped extends RosMessage {
    public static final String TYPE = "geometry_msgs/WrenchStamped";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_WRENCH = "wrench";

    private Header header;
    private Wrench wrench;

    public WrenchStamped() {
        this(new Header(), new Wrench());
    }

    public WrenchStamped(Wrench wrench) {
        this(new Header(), wrench);
    }

    public WrenchStamped(Header header, Wrench wrench) {
        this.header = header;
        this.wrench = wrench;

        super.setJsonObject(jsonBuilder().put(FIELD_HEADER, header.getJsonObject()).put(FIELD_WRENCH, wrench.getJsonObject()));
        super.setType(TYPE);
    }

    public static WrenchStamped fromJsonString(String jsonString) {
        return WrenchStamped.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static WrenchStamped fromMessage(RosMessage m) {
        return WrenchStamped.fromJsonObject(m.getJsonObject());
    }

    public static WrenchStamped fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        Wrench wrench = jsonObject.containsKey(FIELD_WRENCH) ? Wrench.fromJsonObject(jsonObject.getJsonObject(FIELD_WRENCH)) : new Wrench();
        return new WrenchStamped(header, wrench);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    public Wrench getWrench() {
        return this.wrench;
    }

    public void setWrench(Wrench wrench) {
        this.wrench = wrench;
        this.jsonObject.put(FIELD_WRENCH, wrench.getJsonObject());
    }

    @Override
    public WrenchStamped clone() {
        return new WrenchStamped(this.header, this.wrench);
    }
}
