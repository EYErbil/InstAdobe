package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import imageManipulations.ImageMatrix;
import main.Database;
import posts.Post;
import posts.Privacy;
import user.User;
import user.UserType;

public class AdminProfilePage extends JFrame {
    private Database database;
    private User user;
    private User loggedInUser;

    public AdminProfilePage(User user, User loggedInUser, Database database) {
        Font labelFont = new Font("Verdana", Font.PLAIN, 20); 
        JPanel widget = new JPanel(new BorderLayout());
        widget.setBackground(new Color(230, 236, 240));

        this.user = user;
        this.loggedInUser = loggedInUser;
        this.database = database;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        JButton deleteProfileButton = new JButton("Delete Profile");
        deleteProfileButton.setBackground(new Color(235, 87, 87));
        deleteProfileButton.setForeground(Color.WHITE);
        deleteProfileButton.setFont(new Font("Verdana", Font.BOLD, 14));
        deleteProfileButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(
                    AdminProfilePage.this,
                    "Are you sure you want to delete your profile?",
                    "Delete Profile",
                    JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {
                database.deleteuser(user);
                AdminProfilePage.this.dispose();
                new LoginPage(database).setVisible(true);
            }
        });

        JButton editProfileButton = new JButton("Edit Profile");
        editProfileButton.setBackground(new Color(87, 184, 70));
        editProfileButton.setForeground(Color.WHITE);
        editProfileButton.setFont(new Font("Verdana", Font.BOLD, 14));
        editProfileButton.addActionListener(e -> {
        	this.dispose();
        	new EditPage(user, database).setVisible(true);
        });
        JButton changeUserTypeButton = new JButton("Change User Type");
        changeUserTypeButton.setBackground(new Color(87, 184, 70));
        changeUserTypeButton.setForeground(Color.WHITE);
        changeUserTypeButton.setFont(new Font("Verdana", Font.BOLD, 14));
        changeUserTypeButton.addActionListener(e -> {
            UserType[] userTypes = UserType.values();
            JComboBox<UserType> userTypeComboBox = new JComboBox<>(userTypes);
            userTypeComboBox.setSelectedItem(user.getUserType());

            int response = JOptionPane.showConfirmDialog(
                    null,
                    userTypeComboBox,
                    "Change User Type",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (response == JOptionPane.OK_OPTION) {
                UserType selectedUserType = (UserType) userTypeComboBox.getSelectedItem();
                user.setUserType(selectedUserType);
                database.saveUsers();
                AdminProfilePage.this.dispose();
                new UserProfilePage(user, loggedInUser, database).setVisible(true);
            }
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(230, 236, 240));
        JButton discoveryPageButton = createDiscoveryPageButton();
        buttonsPanel.add(deleteProfileButton);
        buttonsPanel.add(editProfileButton);
        buttonsPanel.add(changeUserTypeButton);
        buttonsPanel.add(discoveryPageButton);


        westPanel.add(buttonsPanel, BorderLayout.SOUTH);

        this.add(westPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(230, 236, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 10, 10);

        addUserInfoToPanel(centerPanel, gbc, labelFont);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(new Color(230, 236, 240));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            String nickname = searchField.getText();
            User searchedUser = database.getUserbynickname(nickname);
            if (searchedUser != null) {
                new SearchedProfile(searchedUser,user, database).setVisible(true);
                AdminProfilePage.this.dispose();
            } else {
                JOptionPane.showMessageDialog(AdminProfilePage.this, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton uploadPostButton = createUploadPostButton();
        buttonsPanel.add(uploadPostButton);
        JButton viewPostsButton = createViewPostsButton();
        buttonsPanel.add(viewPostsButton);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        widget.add(searchPanel, BorderLayout.NORTH);
        widget.add(centerPanel, BorderLayout.CENTER);
        this.add(widget, BorderLayout.CENTER);
        JButton updateProfilePhotoButton = new JButton("Update Profile Photo");
        updateProfilePhotoButton.setBackground(new Color(87, 184, 70));
        updateProfilePhotoButton.setForeground(Color.WHITE);
        updateProfilePhotoButton.setFont(new Font("Verdana", Font.BOLD, 14));
        updateProfilePhotoButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    BufferedImage img = ImageIO.read(selectedFile);
                    ImageMatrix imageMatrix = new ImageMatrix(img);

                    user.setpp(imageMatrix);
                    database.saveUsers();
                    AdminProfilePage.this.dispose();
                    new UserProfilePage(user, loggedInUser, database).setVisible(true);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    JOptionPane.showMessageDialog(AdminProfilePage.this, "Failed to load image. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonsPanel.add(updateProfilePhotoButton);

    }

    private void addUserInfoToPanel(JPanel centerPanel, GridBagConstraints gbc, Font labelFont) {
        JLabel[] labels = {
                new JLabel("Nickname:"),
                new JLabel("Name:"),
                new JLabel("Surname:"),
                new JLabel("Age:"),
                new JLabel("UserType:"),
                new JLabel("Email:")
        };
        JLabel[] userInfos = {
                new JLabel(user.getnickname()),
                new JLabel(user.getfirstname()),
                new JLabel(user.getlastname()),
                new JLabel(Integer.toString(user.getage())),
                new JLabel(user.getUserType()),
                new JLabel(user.getemail())
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
    

    private JButton createUploadPostButton() {
        JButton uploadPostButton = new JButton("Upload Post");
        uploadPostButton.setBackground(new Color(87, 184, 70));
        uploadPostButton.setForeground(Color.WHITE);
        uploadPostButton.setFont(new Font("Verdana", Font.BOLD, 14));
        uploadPostButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    BufferedImage img = ImageIO.read(selectedFile);
                    ImageMatrix imageMatrix = new ImageMatrix(img);

                    JTextField descriptionField = new JTextField();
                    JCheckBox isPublicCheckBox = new JCheckBox("Public? Check if Yes");
                    JCheckBox addToDiscoveryCheckBox = new JCheckBox("Add to Discovery Page? Check if Yes");
                    Object[] message = {
                            "Enter a description for your post:", 
                            descriptionField,
                            isPublicCheckBox,
                            addToDiscoveryCheckBox
                    };
                    int option = JOptionPane.showConfirmDialog(null, message, "Upload post", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        String description = descriptionField.getText();
                        boolean isPublic = isPublicCheckBox.isSelected();
                        boolean addToDiscovery = addToDiscoveryCheckBox.isSelected();

                        if (addToDiscovery && !isPublic) {
                            JOptionPane.showMessageDialog(AdminProfilePage.this, "Post must be public to be added to Discovery Page!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        Privacy privacy = isPublic ? Privacy.PUBLIC :Privacy.PRIVATE;
                        
                        if (description != null && !description.trim().isEmpty()) {
                            Post newPost = new Post(imageMatrix, user.getnickname(), description, privacy);
                            user.getPosts().add(newPost);
                            if (addToDiscovery) {
                                database.getPosts().add(newPost);
                                database.savePosts();
                            }
                            database.saveUsers();
                            JOptionPane.showMessageDialog(AdminProfilePage.this, "Your post has been uploaded successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(AdminProfilePage.this, "Post cancelled. You must enter a valid description.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    JOptionPane.showMessageDialog(AdminProfilePage.this, "Failed to load image. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return uploadPostButton;
    }

    private JButton createViewPostsButton() {
        JButton viewPostsButton = new JButton("View Posts");
        viewPostsButton.setBackground(new Color(87, 184, 70));
        viewPostsButton.setForeground(Color.WHITE);
        viewPostsButton.setFont(new Font("Verdana", Font.BOLD, 14));
        viewPostsButton.addActionListener(e -> new PostsPage(user,loggedInUser, database).setVisible(true));
        return viewPostsButton;
    }

    private JButton createProfileButton(String buttonText, Color backgroundColor, ActionListener actionListener) {
        JButton profileButton = new JButton(buttonText);
        profileButton.setBackground(backgroundColor);
        profileButton.setForeground(Color.WHITE);
        profileButton.setFont(new Font("Verdana", Font.BOLD, 14));
        profileButton.addActionListener(actionListener);
        return profileButton;
    }
    private JButton createDiscoveryPageButton() {
        JButton discoveryPageButton = new JButton("Discover");
        discoveryPageButton.setBackground(new Color(87, 184, 70));
        discoveryPageButton.setForeground(Color.WHITE);
        discoveryPageButton.setFont(new Font("Verdana", Font.BOLD, 14));
        discoveryPageButton.addActionListener(e -> {
            new DiscoveryPage(loggedInUser, database).setVisible(true);
            AdminProfilePage.this.dispose();
        });
        return discoveryPageButton;
    }
}


