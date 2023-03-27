package io.github.twinklekhj.ros.type.navigation;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.geometry.PoseStamped;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;

@ToString
public class Path extends RosMessage {
    public static final String TYPE = "nav_msgs/Path";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_POSES = "poses";

    private Header header;
    private PoseStamped[] poses;

    public Path() {
        this(new Header(), new PoseStamped[]{});
    }

    public Path(PoseStamped[] poses) {
        this(new Header(), poses);
    }

    public Path(Header header, PoseStamped[] poses) {
        this.header = header;
        this.poses = new PoseStamped[poses.length];
        System.arraycopy(poses, 0, this.poses, 0, poses.length);

        JsonObject json = jsonBuilder()
                .put(FIELD_HEADER, header.getJsonObject())
                .put(FIELD_POSES, jsonBuilder(Arrays.deepToString(poses)));

        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static Path fromJsonString(String jsonString) {
        return Path.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static Path fromMessage(RosMessage m) {
        return Path.fromJsonObject(m.getJsonObject());
    }

    public static Path fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        JsonArray jsonCells = jsonObject.getJsonArray(FIELD_POSES);
        PoseStamped[] poses = {};
        if (jsonCells != null) {
            poses = new PoseStamped[jsonCells.size()];
            for (int i = 0; i < poses.length; i++) {
                poses[i] = PoseStamped.fromJsonObject(jsonCells.getJsonObject(i));
            }
        }

        return new Path(header, poses);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    public PoseStamped[] getPoses() {
        return poses;
    }

    public void setPoses(PoseStamped... poses) {
        this.poses = new PoseStamped[poses.length];
        System.arraycopy(poses, 0, this.poses, 0, poses.length);

        this.jsonObject.put(FIELD_POSES, jsonBuilder(Arrays.deepToString(poses)));
    }

    @Override
    public Path clone() {
        return new Path(this.header, this.poses);
    }
}
