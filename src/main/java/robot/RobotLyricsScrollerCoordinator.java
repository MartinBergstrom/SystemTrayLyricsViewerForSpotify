package robot;

import lyrics.LyricsWebPage;
import spotifyApi.CurrentlyPlaying;
import spotifyApi.SpotifyApiHandler;

import java.awt.*;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RobotLyricsScrollerCoordinator {
    private final RobotLyricsScrollerFactory myFactory;
    private final SpotifyApiHandler mySpotifyApi;
    private final ExecutorService myExecutorService = Executors.newSingleThreadExecutor();
    private Future<?> myFuture;

    public RobotLyricsScrollerCoordinator(SpotifyApiHandler spotifyApiHandler) throws AWTException {
        myFactory = new RobotLyricsScrollerFactory();
        mySpotifyApi = spotifyApiHandler;
    }

    public void launchRobotScrollerForType(LyricsWebPage type) {
        if (myFuture != null) {
            myFuture.cancel(true);
        }

        myFactory.getForType(type).ifPresent(robotLyricsScroller ->
                myFuture = myExecutorService.submit(new RobotScrollerTask(robotLyricsScroller)));
    }

    private class RobotScrollerTask implements Runnable {
        private RobotLyricsScroller myRobotScroller;

        private RobotScrollerTask(RobotLyricsScroller robotScroller) {
            myRobotScroller = robotScroller;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                CurrentlyPlaying currentlyPlaying = mySpotifyApi.requestCurrentlyPlaying();

                if (!currentlyPlaying.isCurrentlyPlaying()) {
                    myRobotScroller.pause();
                } else {
                    myRobotScroller.run(currentlyPlaying.getSongProgress(), Duration.ZERO);
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
