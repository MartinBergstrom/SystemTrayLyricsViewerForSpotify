package robot;

import java.awt.*;
import java.awt.event.KeyEvent;


public class AZRobotLyricsScroller implements RobotLyricsScroller {
    private final Robot myRobot;

    AZRobotLyricsScroller(Robot robot) {
        myRobot = robot;
    }

    @Override
    public void adjustStartingPosition() {
        for (int i = 0; i < 5; i++) {
            myRobot.keyPress(KeyEvent.VK_DOWN);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
