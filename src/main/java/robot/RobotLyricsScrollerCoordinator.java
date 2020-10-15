package robot;

import lyrics.LyricsWebPage;
import spotifyApi.CurrentlyPlaying;
import spotifyApi.SpotifyApiHandler;

import java.awt.*;
import java.sql.Timestamp;
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
        System.out.println("stopping future at :" + new Timestamp(System.currentTimeMillis()));
        if (myFuture != null) {
            myFuture.cancel(true);
        }

        myFactory.getForType(type).ifPresent(robotLyricsScroller -> {
            System.out.println("launching new robot task at :" + new Timestamp(System.currentTimeMillis()));
            myFuture = myExecutorService.submit(new RobotScrollerTask(robotLyricsScroller, mySpotifyApi));
        });
    }

    private class RobotScrollerTask implements Runnable {
        private RobotLyricsScroller myRobotScroller;
        private SpotifyApiHandler mySpotifyApi;
        private CurrentlyPlaying myInitalCurrentlyPlaying;

        private RobotScrollerTask(RobotLyricsScroller robotScroller, SpotifyApiHandler spotifyApi) {
            myRobotScroller = robotScroller;
            mySpotifyApi = spotifyApi;
            myInitalCurrentlyPlaying = spotifyApi.requestCurrentlyPlaying();
        }

        @Override
        public void run() {
            CurrentlyPlaying currentlyPlaying;

            do {
                sleep();

                currentlyPlaying = mySpotifyApi.requestCurrentlyPlaying();
                if (!currentlyPlaying.isCurrentlyPlaying()) {
                    myRobotScroller.pause();
                } else {
                    myRobotScroller.run(currentlyPlaying.getSongProgress(), currentlyPlaying.getSongLength());
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
