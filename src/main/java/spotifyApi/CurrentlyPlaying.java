package spotifyApi;

import java.time.Duration;
import java.util.Optional;

public class CurrentlyPlaying {
    private String artist;
    private String song;
    private Duration mySongProgress;
    private boolean isCurrentlyPlaying;

    CurrentlyPlaying(String artist, String song, Duration songProgress, boolean isCurrentlyPlaying) {
        if (artist == null || artist.length() == 0 || song == null || song.length() == 0) {
            throw new IllegalArgumentException("Artist and/or song can not be null/empty");
        }
        this.artist = artist;
        this.song = song;
        this.mySongProgress = songProgress;
        this.isCurrentlyPlaying = isCurrentlyPlaying;
    }

    public String getArtist() {
        return artist;
    }

    public String getSong() {
        return song;
    }

    public Optional<Duration> getSongProgress() {
        return Optional.ofNullable(mySongProgress);
    }

    public boolean isCurrentlyPlaying() {
        return isCurrentlyPlaying;
    }
}
