package robot;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GeniusRobotLyricsScroller implements RobotLyricsScroller {
    private final Robot myRobot;

    GeniusRobotLyricsScroller(Robot robot) {
        myRobot = robot;
    }

    @Override
    public void adjustStartingPosition() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 11; i++) {
            myRobot.keyPress(KeyEvent.VK_DOWN);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
