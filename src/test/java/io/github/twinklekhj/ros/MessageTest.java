package io.github.twinklekhj.ros;

import io.github.twinklekhj.ros.type.geometry.Point;
import io.github.twinklekhj.ros.type.geometry.Point32;
import io.github.twinklekhj.ros.type.geometry.Polygon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageTest {
    private static final Logger logger = LoggerFactory.getLogger(MessageTest.class);

    @Test
    @DisplayName("message 생성")
    public void createMessage() {
        Point p = new Point(1, 0, 0);
        logger.info("point: {}", p);
    }

    @Test
    @DisplayName("polygon 생성")
    public void createPolygon() {
        Polygon polygon = new Polygon();
        polygon.setPoints(new Point32(0.0F, 0.0F, 0.0F), new Point32(1.0F, 1.0F, 1.0F));
        logger.info("polygon: {}", polygon);
    }
}
