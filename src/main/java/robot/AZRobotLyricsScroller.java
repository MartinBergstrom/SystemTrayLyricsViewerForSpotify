package robot;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;

public class AZRobotLyricsScroller implements RobotLyricsScroller {
    private final Robot myRobot;

    AZRobotLyricsScroller(Robot robot) {
        myRobot = robot;
    }

    @Override
    public void run(Duration progress, Duration songLength) {
        //sleep(2000);

        for (int i = 0; i < 20; i++) {
            //myRobot.keyPress(KeyEvent.VK_DOWN);
            //sleep(1000);
        }

    }

    @Override
    public void pause() {

    }


    private void sleep(int mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            // Intentionally left empty
        }
    }
}
