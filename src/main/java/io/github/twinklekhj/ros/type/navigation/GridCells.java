package io.github.twinklekhj.ros.type.navigation;

import io.github.twinklekhj.ros.type.RosMessage;
import io.github.twinklekhj.ros.type.geometry.Point32;
import io.github.twinklekhj.ros.type.std.Header;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.ToString;

import java.util.Arrays;

@ToString
public class GridCells extends RosMessage {
    public static final String TYPE = "nav_msgs/GridCells";

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_WIDTH = "cell_width";
    public static final String FIELD_HEIGHT = "cell_height";
    public static final String FIELD_CELLS = "cells";

    private Header header;
    private float width;
    private float height;
    private Point32[] cells;

    public GridCells() {
        this(new Header(), 0, 0, new Point32[]{});
    }

    public GridCells(float width, float height, Point32[] cells) {
        this(new Header(), width, height, cells);
    }

    public GridCells(Header header, float width, float height, Point32[] cells) {
        this.header = header;
        this.width = width;
        this.height = height;
        this.cells = new Point32[cells.length];
        System.arraycopy(cells, 0, this.cells, 0, cells.length);

        super.setJsonObject(jsonBuilder().put(FIELD_CELLS, jsonBuilder(Arrays.deepToString(cells))));
        super.setType(TYPE);
    }

    public static GridCells fromJsonString(String jsonString) {
        return GridCells.fromMessage(new RosMessage(jsonString, TYPE));
    }

    public static GridCells fromMessage(RosMessage m) {
        return GridCells.fromJsonObject(m.getJsonObject());
    }

    public static GridCells fromJsonObject(JsonObject jsonObject) {
        Header header = jsonObject.containsKey(FIELD_HEADER) ? Header.fromJsonObject(jsonObject.getJsonObject(FIELD_HEADER)) : new Header();
        float width = jsonObject.containsKey(FIELD_WIDTH) ? jsonObject.getFloat(FIELD_WIDTH) : 0.0f;
        float height = jsonObject.containsKey(FIELD_HEIGHT) ? jsonObject.getFloat(FIELD_HEIGHT) : 0.0f;

        JsonArray jsonCells = jsonObject.getJsonArray(FIELD_CELLS);
        Point32[] cells = {};
        if (jsonCells != null) {
            cells = new Point32[jsonCells.size()];
            for (int i = 0; i < cells.length; i++) {
                cells[i] = Point32.fromJsonObject(jsonCells.getJsonObject(i));
            }
        }

        return new GridCells(header, width, height, cells);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
        this.jsonObject.put(FIELD_HEADER, header.getJsonObject());
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
        this.jsonObject.put(FIELD_WIDTH, width);
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
        this.jsonObject.put(FIELD_HEIGHT, height);
    }

    public Point32[] getCells() {
        return cells;
    }

    public void setCells(Point32... cells) {
        this.cells = new Point32[cells.length];
        System.arraycopy(cells, 0, this.cells, 0, cells.length);

        this.jsonObject.put(FIELD_CELLS, jsonBuilder(Arrays.deepToString(cells)));
    }

    @Override
    public GridCells clone() {
        return new GridCells(this.header, this.width, this.height, this.cells);
    }
}
