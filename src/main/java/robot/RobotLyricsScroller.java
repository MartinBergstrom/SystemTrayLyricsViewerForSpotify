package robot;

import java.time.Duration;

public interface RobotLyricsScroller {

    void run(Duration progress, Duration songLength);

    void pause();

}
