package gui;

import main.Database;
import user.User;

import javax.swing.*;

import imageManipulations.ImageMatrix;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
/**
 * This class represents the GUI for a SearchedProfile. It extends JFrame to create a window for the profile.
 */
public class SearchedProfile extends JFrame {
    private Database database;
    private User user;
    private User loggedInUser;
    /**
     * SearchedProfile constructor initializes the UI elements and interactions within the window.
     * 
     * @param user - The User object whose profile is being searched for
     * @param loggedInUser - The User object who is currently logged into the application
     * @param database - The Database object where all users and their related information is stored
     */
    public SearchedProfile(User user, User loggedInUser, Database database) {
        Font labelFont = new Font("Verdana", Font.PLAIN, 20);
        JPanel widget = new JPanel(new BorderLayout());
        widget.setBackground(new Color(230, 236, 240));

        this.user = user;
        this.loggedInUser = loggedInUser;
        this.database = database;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLayout(new BorderLayout());

        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setBackground(new Color(230, 236, 240));

        JLabel imageLabel = new JLabel(); // Initialize here
        ImageMatrix profileImageMatrix = user.getProfilePhoto();
        BufferedImage profileImage = null;
        if (profileImageMatrix != null) {
            profileImage = profileImageMatrix.getBufferedImage();
        }
        if (profileImage == null) {
            try {
                profileImage = ImageIO.read(new File("Default.jpg"));
            } catch (IOException e) {
                System.err.println("Unable to read default image file.");
                e.printStackTrace();
            }
        }
        if (profileImage != null) {
            Image scaledImage = profileImage.getScaledInstance(250, 250, Image.SCALE_DEFAULT);
            ImageIcon profileIcon = new ImageIcon(scaledImage);
            imageLabel.setIcon(profileIcon);
        }
        westPanel.add(imageLabel, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.setBackground(new Color(230, 236, 240));

        JButton returnButton = new JButton("Return");
        returnButton.setBackground(new Color(87, 184, 70));
        returnButton.setForeground(Color.WHITE);
        returnButton.setFont(new Font("Verdana", Font.BOLD, 14));
        returnButton.addActionListener(e -> {
            new UserProfilePage(loggedInUser, loggedInUser, database).setVisible(true);
            SearchedProfile.this.dispose();
        });
        buttonPanel.add(returnButton);

        JButton viewPostsButton = createViewPostsButton();
        viewPostsButton.setBackground(new Color(87, 184, 70));
        viewPostsButton.setForeground(Color.WHITE);
        viewPostsButton.setFont(new Font("Verdana", Font.BOLD, 14));
        buttonPanel.add(viewPostsButton);

        westPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(westPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(230, 236, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 10, 10);

        addUserInfoToPanel(centerPanel, gbc, labelFont);

        widget.add(centerPanel, BorderLayout.CENTER);
        this.add(widget, BorderLayout.CENTER);
    }
    /**
     * This method adds the user's information to the given panel.
     * 
     * @param centerPanel - The panel where the information is to be added
     * @param gbc - The GridBagConstraints object to set constraints for each component in the layout
     * @param labelFont - The font to be used for the labels
     */
    private void addUserInfoToPanel(JPanel centerPanel, GridBagConstraints gbc, Font labelFont) {
    	JLabel[] labels = {
                new JLabel("Name:"),
                new JLabel("Surname:"),
                new JLabel("User Type:")
        };
        JLabel[] userInfos = {
                new JLabel(user.getfirstname()),
                new JLabel(user.getlastname()),
                new JLabel(user.getUserType())
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            labels[i].setFont(labelFont);
            labels[i].setHorizontalAlignment(JLabel.CENTER);
            labels[i].setForeground(Color.DARK_GRAY);
            centerPanel.add(labels[i], gbc);
            gbc.gridx = 1;
            userInfos[i].setFont(labelFont);
            userInfos[i].setHorizontalAlignment(JLabel.CENTER);
            centerPanel.add(userInfos[i], gbc);
        }
    }
    
    /**
     * This method creates the "View Posts" button and adds a listener that triggers 
     * a new PostsPage GUI window when clicked.
     * 
     * @return JButton - The "View Posts" button
     */
    private JButton createViewPostsButton() {
        JButton viewPostsButton = new JButton("View Posts");
        viewPostsButton.addActionListener(e -> new PostsPage(user,loggedInUser, database).setVisible(true));
        return viewPostsButton;
    }
}