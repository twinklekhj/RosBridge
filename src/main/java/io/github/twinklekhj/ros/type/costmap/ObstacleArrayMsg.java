package io.github.twinklekhj.ros.type.costmap;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;

@ToString
public class ObstacleArrayMsg extends RosMessage {
    public static final String TYPE = "costmap_converter/ObstacleArrayMsg";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_OBSTACLES = "obstacles";

    private Header header;
    private ObstacleMsg[] obstacles;

    public ObstacleArrayMsg() {
        this(new Header());
    }

    public ObstacleArrayMsg(ObstacleMsg... obstacles) {
        this(new Header(), obstacles);
    }

    public ObstacleArrayMsg(Header header, ObstacleMsg... obstacles) {
        this.header = header;
        this.obstacles = new ObstacleMsg[obstacles.length];
        System.arraycopy(obstacles, 0, this.obstacles, 0, obstacles.length);

        JsonObject json = jsonBuilder().put(FIELD_HEADER, header.getJsonObject()).put(FIELD_OBSTACLES, jsonBuilder(Arrays.deepToString(obstacles)));

        super.setJsonObject(json);
        super.setType(TYPE);
    }

    public static ObstacleArrayMsg fromJsonString(String jsonString) {
        return ObstacleArrayMsg.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static ObstacleArrayMsg fromMessage(RosMessage m) {
        return ObstacleArrayMsg.fromJsonObject(m.getJsonObject());
    }

    public static ObstacleArrayMsg fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        JsonArray jsonCells = jsonObject.getJsonArray(FIELD_OBSTACLES);
        ObstacleMsg[] obstacles = {};
        if (jsonCells != null) {
            obstacles = new ObstacleMsg[jsonCells.size()];
            for (int i = 0; i < obstacles.length; i++) {
                obstacles[i] = ObstacleMsg.fromJsonObject(jsonCells.getJsonObject(i));
            }
        }

        return new ObstacleArrayMsg(header, obstacles);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    public ObstacleMsg[] getObstacles() {
        return obstacles;
    }

    public void setObstacles(ObstacleMsg... obstacles) {
        this.obstacles = new ObstacleMsg[obstacles.length];
        System.arraycopy(obstacles, 0, this.obstacles, 0, obstacles.length);

        this.jsonObject.put(FIELD_OBSTACLES, jsonBuilder(Arrays.deepToString(obstacles)));
    }

    @Override
    public ObstacleArrayMsg clone() {
        return new ObstacleArrayMsg(this.header, this.obstacles);
    }
}
