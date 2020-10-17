package robot;

import api.spotifyApi.CurrentlyPlaying;

import java.net.URL;

public interface RobotLyricsScroller {

    void run(CurrentlyPlaying current, URL url);

}
