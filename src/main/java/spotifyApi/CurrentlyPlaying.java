package spotifyApi;

public class CurrentlyPlaying {
    private String artist;
    private String song;

    public CurrentlyPlaying(String artist, String song) {
        if (artist == null || artist.length() == 0 || song == null || song.length() == 0) {
            throw new IllegalArgumentException("Artist and/or song can not be null/empty");
        }
        this.artist = artist;
        this.song = song;
    }

    public String getArtist() {
        return artist;
    }

    public String getSong() {
        return song;
    }
}
