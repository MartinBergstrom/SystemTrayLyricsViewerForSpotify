package ui;

import spotifyApi.CurrentlyPlaying;
import spotifyApi.SpotifyApiHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
 */
public class MainSystemTray {
    private final TrayIcon trayIcon = new TrayIcon(createImage("images/rsz_1lyrics-text.png", "tray icon"));
    private SpotifyApiHandler spotifyApiHandler;

    public MainSystemTray(SpotifyApiHandler spotifyApiHandler) {
        if (!SystemTray.isSupported()) {
            System.out.println("RecorderSystemTray is not supported");
            return;
        }
        this.spotifyApiHandler = spotifyApiHandler;

        final SystemTray tray = SystemTray.getSystemTray();
        trayIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    CurrentlyPlaying currentlyPlaying = spotifyApiHandler.getMyCurrentlyPlaying();
                    System.out.format("Currently playing song %s by %s", currentlyPlaying.getSong(), currentlyPlaying.getArtist());
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
