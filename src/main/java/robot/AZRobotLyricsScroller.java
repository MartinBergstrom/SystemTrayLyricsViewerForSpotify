package robot;

import http.MyHttpClient;
import api.spotifyApi.CurrentlyPlaying;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.URL;


public class AZRobotLyricsScroller implements RobotLyricsScroller {
    private final Robot myRobot;
    private final MyHttpClient httpClient;
    private int myPreviousProgress;

    AZRobotLyricsScroller(Robot robot) {
        myRobot = robot;
        httpClient = new MyHttpClient();
    }

    // length on master of puppets is 18621
    // length of avicci levels is 15007, whereas length of lyrics are 138, so 14869 standard+
    //
    @Override
    public void run(CurrentlyPlaying current, URL url) {
        String response = httpClient.getRequest(url.toString());

        myRobot.keyPress(KeyEvent.VK_DOWN);

    }

    private int getNumberOfLineBreaks(String response)
    {
        //
        return 0;
    }
}
