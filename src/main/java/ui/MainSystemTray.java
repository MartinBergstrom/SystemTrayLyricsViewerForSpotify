package ui;

import http.MyHttpClient;
import lyrics.LyricsFinder;
import lyrics.LyricsFinderProvider;
import lyrics.LyricsFinderProviderImpl;
import spotifyApi.CurrentlyPlaying;
import spotifyApi.SpotifyApiHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;

/**
 */
public class MainSystemTray {
    private final TrayIcon trayIcon = new TrayIcon(createImage("images/rsz_1lyrics-text.png", "tray icon"));
    private SpotifyApiHandler spotifyApiHandler;
    private List<LyricsFinder> lyricFinders;

    public MainSystemTray(SpotifyApiHandler spotifyApiHandler, LyricsFinderProvider lyricsFinderProvider) {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray is not supported");
            return;
        }
        this.spotifyApiHandler = spotifyApiHandler;
        lyricFinders = lyricsFinderProvider.getAllLyricsFinders();

        final SystemTray tray = SystemTray.getSystemTray();
        trayIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    CurrentlyPlaying currentlyPlaying = spotifyApiHandler.getMyCurrentlyPlaying();
                    System.out.format("Currently playing song %s by %s\n", currentlyPlaying.getSong(), currentlyPlaying.getArtist());
                }
            }
        });
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }

    private Image createImage(String imagePath,  String description) {
        URL imageURL = this.getClass().getClassLoader().getResource(imagePath);
        if (imageURL == null) {
            System.err.println("Resource not found: " + imagePath);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
}
