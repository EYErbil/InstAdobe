package gui;

import javax.swing.*;

import main.Database;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimeoutWindow extends JFrame {
    private Timer timer;
    private JLabel welcomeLabel;
    private JPanel contentPanel;

    public TimeoutWindow() {
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.BLACK);
        setContentPane(contentPanel);

        welcomeLabel = new JLabel("Welcome to PhotoCloud, your journey starts now!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 48));
        welcomeLabel.setForeground(Color.WHITE);
        contentPanel.add(welcomeLabel, BorderLayout.CENTER);

        int timeoutDuration = 3000; // 3 seconds
        timer = new Timer(timeoutDuration, e -> fadeOutAndOpenLandingPage());
        timer.setRepeats(false);

        setOpacity(0f);
        setVisible(true);
        fadeIn();
        timer.start();
    }

    private void fadeIn() {
        final long animationDuration = 1000;
        final float targetOpacity = 1f;
        Timer fadeInTimer = new Timer(10, new ActionListener() {
            private final long startTime = System.currentTimeMillis();

            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - startTime;
                float opacity = (float) elapsed / animationDuration;
                if (opacity >= targetOpacity) {
                    setOpacity(targetOpacity);
                    ((Timer) e.getSource()).stop();
                } else {
                    setOpacity(opacity);
                }
                repaint();
            }
        });
        fadeInTimer.start();
    }

    private void fadeOutAndOpenLandingPage() {
        final long animationDuration = 1000;
        final float initialOpacity = getOpacity();
        Timer fadeOutTimer = new Timer(10, new ActionListener() {
            private final long startTime = System.currentTimeMillis();

            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - startTime;
                float opacity = 1f - ((float) elapsed / animationDuration);
                if (opacity <= 0f) {
                    setOpacity(0f);
                    ((Timer) e.getSource()).stop();
                    openLandingPage();
                } else {
                    setOpacity(initialOpacity * opacity);
                }
                repaint();
            }
        });
        fadeOutTimer.start();
    }

    private void openLandingPage() {
        Database database = new Database();
        LandingPage landingPage = new LandingPage(database);
        landingPage.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException |
                    IllegalAccessException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            TimeoutWindow timeoutWindow = new TimeoutWindow();
        });
    }
}