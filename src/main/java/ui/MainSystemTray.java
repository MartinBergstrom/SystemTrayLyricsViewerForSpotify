package ui;

import lyrics.LyricsFinder;
import lyrics.LyricsFinderProvider;
import robot.RobotLyricsScrollerCoordinator;
import spotifyApi.CurrentlyPlaying;
import spotifyApi.SpotifyApiHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

/**
 */
public class MainSystemTray {
    private final TrayIcon trayIcon = new TrayIcon(createImage("images/rsz_1lyrics-text.png", "tray icon"));
    private List<LyricsFinder> lyricFinders;
    private RobotLyricsScrollerCoordinator myRobotCoordinator;

    public MainSystemTray(SpotifyApiHandler spotifyApiHandler, LyricsFinderProvider lyricsFinderProvider) {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray is not supported");
            return;
        }
        lyricFinders = lyricsFinderProvider.getAllLyricsFinders();
        try {
            myRobotCoordinator = new RobotLyricsScrollerCoordinator(spotifyApiHandler);
        } catch (AWTException e) {
            System.out.println("Robot not supported");
            e.printStackTrace();
        }

        PopupMenu popup = new PopupMenu();
        MenuItem aboutItem = new MenuItem("Exit");
        aboutItem.addActionListener(e -> System.exit(0));
        popup.add(aboutItem);

        setUpTray(spotifyApiHandler, popup);
    }

    private void setUpTray(SpotifyApiHandler spotifyApiHandler, PopupMenu popup) {
        SystemTray tray = SystemTray.getSystemTray();
        trayIcon.setPopupMenu(popup);

        trayIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                    CurrentlyPlaying currentlyPlaying = spotifyApiHandler.requestCurrentlyPlaying();
                    for (LyricsFinder lyricsFinder : lyricFinders) {
                        Optional<URL> url = lyricsFinder.findLyricsFor(currentlyPlaying);
                        if (url.isPresent()) {
                            openBrowserWithUrl(url.get());
                            launchRobotScroller(lyricsFinder);
                            break;
                        }
                    }
                    System.out.format("Currently playing song %s by %s\n", currentlyPlaying.getSong(), currentlyPlaying.getMyArtist());
                }
            }
        });
        trayIcon.setToolTip("Find Lyrics for currently played song");
        try {
            tray.add(trayIcon);
        } catch (
                AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }

    private void launchRobotScroller(LyricsFinder lyricsFinder) {
            myRobotCoordinator.launchRobotScrollerForType(lyricsFinder.getLyricsWebPage());
    }

    private static void openBrowserWithUrl(URL url) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(url.toURI());
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        }
    }

    private Image createImage(String imagePath, String description) {
        URL imageURL = this.getClass().getClassLoader().getResource(imagePath);
        if (imageURL == null) {
            throw new RuntimeException("Resource not found: " + imagePath);
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
}
