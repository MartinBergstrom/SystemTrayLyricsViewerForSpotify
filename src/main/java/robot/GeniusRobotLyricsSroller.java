package robot;

import api.spotifyApi.CurrentlyPlaying;

import java.awt.*;
import java.net.URL;

public class GeniusRobotLyricsSroller implements RobotLyricsScroller {
    // Authenticate and use API ? static initalized once
    private final Robot myRobot;
    private static final GeniusApi GENIUS_API;

    static {
        GENIUS_API = new GeniusApi();
    }

    GeniusRobotLyricsSroller(Robot myRobot) {
        this.myRobot = myRobot;
    }

    @Override
    public void run(CurrentlyPlaying current, URL url) {

    }
}
