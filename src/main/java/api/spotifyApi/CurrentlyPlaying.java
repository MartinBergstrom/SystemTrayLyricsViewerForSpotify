package api.spotifyApi;

import java.time.Duration;

public class CurrentlyPlaying {
    private final String myArtist;
    private final String mySong;
    private final Duration mySongProgress;
    private final Duration mySongLength;
    private final boolean myIsCurrentlyPlaying;

    private CurrentlyPlaying(Builder builder) {
        myArtist = builder.myArtist;
        mySong = builder.mySong;
        mySongProgress = builder.mySongProgress;
        mySongLength = builder.mySongLength;
        myIsCurrentlyPlaying = builder.myIsCurrentlyPlaying;
    }

    public String getMyArtist() {
        return myArtist;
    }

    public String getSong() {
        return mySong;
    }

    public Duration getSongProgress() {
        return mySongProgress;
    }

    public Duration getSongLength() {
        return mySongLength;
    }

    public boolean isCurrentlyPlaying() {
        return myIsCurrentlyPlaying;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    public static class Builder {
        private String myArtist;
        private String mySong;
        private Duration mySongProgress;
        private Duration mySongLength;
        private boolean myIsCurrentlyPlaying;

        Builder() {
            // Intentionally left empty
        }

        public Builder withArtist(String artist) {
            myArtist = artist;
            return this;
        }

        public Builder withSong(String song) {
            mySong = song;
            return this;
        }

        public Builder withSongProgress(Duration songProgress) {
            mySongProgress = songProgress;
            return this;
        }

        public Builder withSongLength(Duration songLength) {
            mySongLength = songLength;
            return this;
        }

        public Builder withIsCurrentlyPlaying(boolean currentlyPlaying) {
            myIsCurrentlyPlaying = currentlyPlaying;
            return this;
        }

        public CurrentlyPlaying build() {
            if (myArtist == null || myArtist.length() == 0 || mySong == null || mySong.length() == 0) {
                throw new IllegalArgumentException("Artist and/or song can not be null/empty");
            }

            return new CurrentlyPlaying(this);
        }
    }
}
