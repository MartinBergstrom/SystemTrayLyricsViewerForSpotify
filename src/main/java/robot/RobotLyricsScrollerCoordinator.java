package robot;

import lyrics.LyricsWebPage;
import api.spotifyApi.CurrentlyPlaying;
import api.spotifyApi.SpotifyApi;

import java.awt.*;
import java.net.URL;
import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RobotLyricsScrollerCoordinator {
    private final RobotLyricsScrollerFactory myFactory;
    private final SpotifyApi mySpotifyApi;
    private final ExecutorService myExecutorService = Executors.newSingleThreadExecutor();
    private Future<?> myFuture;

    public RobotLyricsScrollerCoordinator(SpotifyApi spotifyApi) throws AWTException {
        myFactory = new RobotLyricsScrollerFactory();
        mySpotifyApi = spotifyApi;
    }

    public void launchRobotScrollerForType(LyricsWebPage type, URL url) {
        System.out.println("Stopping future at :" + new Timestamp(System.currentTimeMillis()));
        if (myFuture != null) {
            myFuture.cancel(true);
        }

        myFactory.getForType(type).ifPresent(robotLyricsScroller -> {
            System.out.println("Launching new robot task at :" + new Timestamp(System.currentTimeMillis()));
            myFuture = myExecutorService.submit(new RobotScrollerTask(robotLyricsScroller, mySpotifyApi, url));
        });
    }

    private class RobotScrollerTask implements Runnable {
        private RobotLyricsScroller myRobotScroller;
        private SpotifyApi mySpotifyApi;
        private CurrentlyPlaying myInitalCurrentlyPlaying;
        private URL myUrl;

        private RobotScrollerTask(RobotLyricsScroller robotScroller, SpotifyApi spotifyApi, URL url) {
            myRobotScroller = robotScroller;
            mySpotifyApi = spotifyApi;
            myUrl = url;
            myInitalCurrentlyPlaying = spotifyApi.requestCurrentlyPlaying();
        }


        @Override
        public void run() {
            CurrentlyPlaying currentlyPlaying;

            do {
                sleep();

                currentlyPlaying = mySpotifyApi.requestCurrentlyPlaying();
                if (!currentlyPlaying.isCurrentlyPlaying()) {
                    //myRobotScroller.pause();
                } else {
                    myRobotScroller.run(currentlyPlaying, myUrl);
                    //         myRobot.keyPress(KeyEvent.VK_DOWN);
                }
            } while (!Thread.currentThread().isInterrupted() && isSameSong(currentlyPlaying));

            System.out.println("Out of loop in task at :" + new Timestamp(System.currentTimeMillis()));
        }

        private void sleep() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private boolean isSameSong(CurrentlyPlaying currentlyPlaying) {
            return myInitalCurrentlyPlaying.getSong().equals(currentlyPlaying.getSong());
        }

    }

}
