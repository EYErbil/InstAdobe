package gui;

import javax.swing.*;
import imageManipulations.ImageMatrix;
import logger.BaseLogger;
import main.Database;
import posts.Post;
import user.User;
import user.UserType;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import logger.BaseLogger;
/**
 * This class represents the Discovery Page of the PhotoCloud application. 
 * The DiscoveryPage shows a selection of posts from the application's database and allows the logged-in user to view any post in detail 
 * and visit the author's profile. If the logged-in user is an administrator, they also have the option to remove posts.
 */
public class DiscoveryPage extends JFrame {
	/**
     * The logged-in User object
     */
    private User loggedInUser;
    /**
     * The Database object that holds all the users and posts data
     */
    private Database database;
    
    /**
     * Constructs a DiscoveryPage that displays posts from the given Database for the logged-in user.
     * @param loggedInUser the currently logged-in user
     * @param database the database containing users and posts data
     */

    public DiscoveryPage(User loggedInUser, Database database) {
        BaseLogger.info().log("User " + loggedInUser.getnickname() + " Navigated to discover Page.");

        this.loggedInUser = loggedInUser;
        this.database = database;

        // set up the JFrame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(new Color(44, 62, 80));  // dark navy color

        // create back button
        JButton backButton = new JButton("Back");
        /**
         * This ActionListener is for the backButton.
         * When the back button is clicked, it navigates back to the UserProfilePage of the logged-in user.
         */
        backButton.addActionListener(e -> {
            new UserProfilePage(loggedInUser, loggedInUser, database).setVisible(true);
            this.dispose();
        });
        backButton.setFont(new Font("Verdana", Font.BOLD, 12));
        backButton.setForeground(new Color(255, 255, 255));  // white
        backButton.setBackground(new Color(52, 152, 219));  // nice blue
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);

        // create a panel for the grid layout
        int postsSize = Math.min(database.getPosts().size(), 30);
        int rows = (postsSize / 4) + (postsSize % 4 == 0 ? 0 : 1);
        JPanel gridPanel = new JPanel(new GridLayout(rows, 4, 10, 10));
        gridPanel.setBackground(new Color(44, 62, 80));  // dark navy color

        // populate the grid with posts
        List<Post> posts = database.getPosts();
        for (int i = 0; i < postsSize; i++) {
            Post post = posts.get(i);
            JButton postButton = new JButton(new ImageIcon(post.getImage().getBufferedImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
            /**
             * This ActionListener is for each postButton.
             * When a post is clicked, it opens the ClickedPost page showing the details of the clicked post.
             */
            postButton.addActionListener(e -> {
            	new ClickedPost(post, database, loggedInUser).setVisible(true);
                this.dispose();
            });

            JLabel nicknameLabel = new JLabel(post.getAuthorNickname());
            nicknameLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
            nicknameLabel.setForeground(new Color(236, 240, 241));  // white smoke color
            /**
             * This MouseAdapter is for the nicknameLabel and userProfilePhoto.
             * When the nickname or photo of a user is clicked, it navigates to the SearchedProfile page of the clicked user.
             */
            nicknameLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    User clickedUser = database.getUserbynickname(post.getAuthorNickname());
                    if (clickedUser != null) {
                        new SearchedProfile(clickedUser, loggedInUser, database).setVisible(true);
                        DiscoveryPage.this.dispose();
                    }
                }
            });
            ImageMatrix profilePhotoMatrix = database.getUserbynickname(post.getAuthorNickname()).getProfilePhoto();
            BufferedImage profilePhoto = profilePhotoMatrix.getBufferedImage();
            ImageIcon profilePhotoIcon = new ImageIcon(profilePhoto.getScaledInstance(20, 20, Image.SCALE_DEFAULT));
            JLabel userProfilePhoto = new JLabel(profilePhotoIcon);

            userProfilePhoto.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    User clickedUser = database.getUserbynickname(post.getAuthorNickname());
                    if (clickedUser != null) {
                        new SearchedProfile(clickedUser, loggedInUser, database).setVisible(true);
                        DiscoveryPage.this.dispose();
                    }
                }
            });

            JPanel postPanel = new JPanel();
            postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
            postPanel.setBackground(new Color(52, 73, 94));  // wet asphalt color
            postPanel.add(nicknameLabel);
            postPanel.add(userProfilePhoto);
            postPanel.add(postButton);
            if (loggedInUser.getUserTypeEnum()==UserType.ADMIN) {
                JButton removeButton = new JButton("Remove");
                /**
                 * This ActionListener is for the removeButton which is only visible to admin users.
                 * When the remove button is clicked, it removes the associated post from the database and the author's list of posts, then refreshes the DiscoveryPage.
                 */
                removeButton.addActionListener(e -> {
                    User postAuthor = database.getUserbynickname(post.getAuthorNickname());
                    postAuthor.removepost(post);
                    database.saveUsers();

                    database.deletePost(post);
                    
                    // refresh the page
                    this.dispose();
                    new DiscoveryPage(loggedInUser, database).setVisible(true);
                });
                removeButton.setFont(new Font("Verdana", Font.BOLD, 12));
                removeButton.setForeground(new Color(255, 255, 255));  // white
                removeButton.setBackground(new Color(192, 57, 43));  // red color
                removeButton.setBorderPainted(false);
                removeButton.setFocusPainted(false);
                postPanel.add(removeButton); // add remove button to the post panel
            }

            gridPanel.add(postPanel);
        }

        // add the components to the frame
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.setBackground(new Color(44, 62, 80));  // dark navy color
        this.add(topPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        this.add(scrollPane, BorderLayout.CENTER);
    }
}