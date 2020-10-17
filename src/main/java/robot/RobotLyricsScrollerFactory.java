package robot;

import lyrics.LyricsWebPage;

import java.awt.*;
import java.util.Optional;

class RobotLyricsScrollerFactory {
    private final Robot myRobot;

    RobotLyricsScrollerFactory() throws AWTException {
        myRobot = new Robot();
    }

    Optional<RobotLyricsScroller> getForType(LyricsWebPage type) {
        switch (type) {
            case AZ:
                return Optional.of(new AZRobotLyricsScroller(myRobot));
            case GENIUS:
                return Optional.of(new GeniusRobotLyricsSroller(myRobot));
            case GOOGLE:
                return Optional.empty();
            default:
                throw new IllegalArgumentException("Unknown lyrics web page type enum: " + type);
        }
    }
}
