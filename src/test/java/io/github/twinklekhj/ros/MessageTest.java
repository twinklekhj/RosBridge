package io.github.twinklekhj.ros;

import io.github.twinklekhj.ros.type.geometry.Point;
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
}
