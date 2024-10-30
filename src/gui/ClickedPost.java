package gui;

import imageManipulations.ImageMatrix;
import main.Database;
import posts.Comment;
import posts.Post;
import user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ClickedPost extends JFrame {
    private Post post;
    private Database database;
    private User loggedInUser;

    public ClickedPost(Post post, Database database, User loggedInUser) {
        this.post = post;
        this.database = database;
        this.loggedInUser = loggedInUser;

        // Setup the JFrame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(800, 600));
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(new Color(44, 62, 80));  // dark navy color

        // Create back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            new DiscoveryPage(loggedInUser, database).setVisible(true);
            this.dispose();
        });
        backButton.setFont(new Font("Verdana", Font.BOLD, 12));
        backButton.setForeground(new Color(255, 255, 255));  // white
        backButton.setBackground(new Color(52, 152, 219));  // nice blue
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);

        // Fetch the author's profile photo
        User author = database.getUserbynickname(post.getAuthorNickname());
        ImageMatrix profilePhotoMatrix = author.getProfilePhoto();
        BufferedImage profilePhoto = profilePhotoMatrix.getBufferedImage();
        ImageIcon profilePhotoIcon = new ImageIcon(profilePhoto.getScaledInstance(80, 80, Image.SCALE_DEFAULT));

        // Create author profile panel
        JPanel authorPanel = new JPanel(new BorderLayout());
        authorPanel.setBackground(new Color(44, 62, 80));  // dark navy color

        // Author profile photo
        JLabel userProfilePhoto = new JLabel(profilePhotoIcon);
        userProfilePhoto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new SearchedProfile(author, loggedInUser, database).setVisible(true);
                dispose();
            }
        });
        authorPanel.add(userProfilePhoto, BorderLayout.NORTH);

        // Post's image
        ImageMatrix postImageMatrix = post.getImage();
        BufferedImage postImage = postImageMatrix.getBufferedImage();
        ImageIcon postImageIcon = new ImageIcon(postImage.getScaledInstance(this.getWidth() * 3 / 4, this.getHeight() * 3 / 4, Image.SCALE_DEFAULT));

        // Create panel to hold post image and description
        JPanel postPanel = new JPanel(new BorderLayout());
        postPanel.setBackground(new Color(44, 62, 80));  // dark navy color

        // Add post image to the panel
        JLabel postImageLabel = new JLabel(postImageIcon);
        postPanel.add(postImageLabel, BorderLayout.CENTER);

        // Author nickname
        JLabel authorNicknameLabel = new JLabel("Shared by: " + author.getnickname());
        authorNicknameLabel.setForeground(Color.WHITE);
        authorNicknameLabel.setFont(new Font("Verdana", Font.BOLD, 18));

        // Post description
        JPanel postDescriptionPanel = new JPanel(new BorderLayout());
        postDescriptionPanel.setBackground(new Color(44, 62, 80));  // dark navy color

        // Description label
        JLabel descriptionLabel = new JLabel("Post Description: ");
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        postDescriptionPanel.add(descriptionLabel, BorderLayout.NORTH);

        // Post description
        JLabel postDescription = new JLabel(post.getDescription());
        postDescription.setForeground(Color.WHITE);
        postDescription.setFont(new Font("Verdana", Font.PLAIN, 16));
        postDescriptionPanel.add(postDescription, BorderLayout.SOUTH);

        postPanel.add(authorNicknameLabel, BorderLayout.NORTH);
        postPanel.add(postDescriptionPanel, BorderLayout.SOUTH);

        // Post details: comments, likes, dislikes
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(44, 62, 80));  // dark navy color

        for (Comment comment : post.getComments()) {
            JLabel commentLabel = new JLabel(comment.getAuthor().getnickname() + ": " + comment.getText());
            commentLabel.setForeground(Color.WHITE);
            detailsPanel.add(commentLabel);
        }

        JLabel likesLabel = new JLabel("Likes: " + post.getLikes());
        likesLabel.setForeground(Color.WHITE);
        detailsPanel.add(likesLabel);

        JLabel dislikesLabel = new JLabel("Dislikes: " + post.getDislikes());
        dislikesLabel.setForeground(Color.WHITE);
        detailsPanel.add(dislikesLabel);

        // Adds components to the frame
        this.add(authorPanel, BorderLayout.WEST);
        this.add(postPanel, BorderLayout.CENTER);
        this.add(detailsPanel, BorderLayout.EAST);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.setBackground(new Color(44, 62, 80));  // dark navy color

        this.add(topPanel, BorderLayout.NORTH);

        this.setVisible(true);
    }
}