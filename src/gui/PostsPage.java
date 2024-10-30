package gui;

import main.Database;
import user.User;
import posts.Comment;
import posts.Post;
import posts.Privacy;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import logger.BaseLogger;

import java.awt.*;
import java.awt.image.BufferedImage;
/**
 * This class represents a page displaying all posts from a specific user.
 * It allows for resizing of post images and interaction with posts (like, dislike)
 * Additionally, a user can move their posts to the Discovery Page.
 */
public class PostsPage extends JFrame {
	/**
     * Database that stores all user data.
     */
    private Database database;
    /**
     * User whose posts are displayed on this page.
     */
    private User user;
    /**
     * Currently logged-in user.
     */
    private User loggedinuser;
    /**
     * Resizes a given BufferedImage to a target width and height.
     *
     * @param originalImage The image to be resized.
     * @param targetWidth   The desired width of the resized image.
     * @param targetHeight  The desired height of the resized image.
     * @return              A BufferedImage representing the resized image.
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }
    /**
     * Constructs a new PostsPage that displays the posts of the given user.
     *
     * @param user          The user whose posts are to be displayed.
     * @param loggedinuser  The currently logged-in user.
     * @param database      The database containing all users' data.
     */

    public PostsPage(User user,User loggedinuser, Database database) {
        this.user = user;
        this.database = database;
        this.loggedinuser=loggedinuser;

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLayout(new BorderLayout());
        
        // Set the frame's background color
        this.getContentPane().setBackground(new Color(173, 216, 230));

        JPanel postsPanel = new JPanel(new GridBagLayout());
        postsPanel.setBackground(new Color(173, 216, 230));

        GridBagConstraints postsGbc = new GridBagConstraints();
        postsGbc.anchor = GridBagConstraints.CENTER;
        postsGbc.insets = new Insets(0, 0, 10, 10);

        JLabel postsLabel = new JLabel(user.getnickname() + "'s Posts");
        postsLabel.setFont(new Font("Sans Serif", Font.BOLD, 30));
        postsLabel.setHorizontalAlignment(JLabel.CENTER);
        postsLabel.setForeground(Color.WHITE);
        postsLabel.setOpaque(true);
        postsLabel.setBackground(new Color(0, 128, 128));
        postsLabel.setBorder(new EmptyBorder(20, 20, 20, 20));

        this.add(postsLabel, BorderLayout.NORTH);
        if(user.getnickname().equals(loggedinuser.getnickname())) {
        	for(Post post : user.getPosts()) {
            	postsGbc.gridy += 1;

                JPanel postPanel = new JPanel(new BorderLayout());
                postPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                BufferedImage resizedImage = resizeImage(post.getImage().getBufferedImage(), 400, 400);
                ImageIcon imageIcon = new ImageIcon(resizedImage);
                JLabel postImage = new JLabel(imageIcon);
                postImage.setBackground(Color.WHITE);
                postImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                postImage.setPreferredSize(new Dimension(400, 400));
                postPanel.add(postImage, BorderLayout.WEST);

                JPanel infoPanel = new JPanel(new GridBagLayout());
                infoPanel.setBackground(new Color(173, 216, 230));
                GridBagConstraints infoGbc = new GridBagConstraints();
                infoGbc.gridx = 0;
                infoGbc.gridy = 0;
                infoGbc.anchor = GridBagConstraints.WEST;
                infoGbc.insets = new Insets(10, 10, 10, 10);

                JLabel postAuthor = new JLabel("Author: " + post.getAuthorNickname());
                postAuthor.setFont(new Font("Sans Serif", Font.BOLD, 16));
                postAuthor.setForeground(Color.WHITE);
                infoPanel.add(postAuthor, infoGbc);

                infoGbc.gridy += 1;
                JLabel postDescription = new JLabel("Description: " + post.getDescription());
                postDescription.setFont(new Font("Sans Serif", Font.BOLD, 16));
                postDescription.setForeground(Color.WHITE);
                infoPanel.add(postDescription, infoGbc);

                infoGbc.gridy += 1;
                JButton likeButton = new JButton("Like");
                likeButton.setFont(new Font("Sans Serif", Font.BOLD, 16));
                likeButton.setForeground(Color.WHITE);
                likeButton.setBackground(new Color(0, 128, 128));
                JLabel likesLabel = new JLabel("Likes: " + post.getLikes());
                likesLabel.setFont(new Font("Sans Serif", Font.BOLD, 16));
                likesLabel.setForeground(Color.WHITE);
                likeButton.addActionListener(e -> {
                    post.like();
                    likesLabel.setText("Likes: " + post.getLikes());
                    database.saveUsers();
                    
                    infoPanel.invalidate();
                    infoPanel.validate();
                    infoPanel.repaint();
                });
                infoGbc.gridx = 0;
                infoPanel.add(likesLabel, infoGbc);
                infoGbc.gridx = 1;
                infoPanel.add(likeButton, infoGbc);

                infoGbc.gridx = 0;
                infoGbc.gridy += 1;
                JButton dislikeButton = new JButton("Dislike");
                dislikeButton.setFont(new Font("Sans Serif", Font.BOLD, 16));
                dislikeButton.setForeground(Color.WHITE);
                dislikeButton.setBackground(new Color(0, 128, 128));
                JLabel dislikesLabel = new JLabel("Dislikes: " + post.getDislikes());
                dislikesLabel.setFont(new Font("Sans Serif", Font.BOLD, 16));
                dislikesLabel.setForeground(Color.WHITE);
                dislikeButton.addActionListener(e -> {
                    post.dislike();
                    dislikesLabel.setText("Dislikes: " + post.getDislikes());
                    database.saveUsers();

                    infoPanel.invalidate();
                    infoPanel.validate();
                    infoPanel.repaint();
                    
                });
                infoPanel.add(dislikesLabel, infoGbc);
                infoGbc.gridx = 1;
                infoPanel.add(dislikeButton, infoGbc);

                infoGbc.gridx = 0;
                infoGbc.gridy += 1;
                

                JButton movetodiscoverypage = new JButton("Move to Discovery Page");
                movetodiscoverypage.setFont(new Font("Sans Serif", Font.BOLD, 16));
                movetodiscoverypage.setForeground(Color.WHITE);
                movetodiscoverypage.setBackground(new Color(0, 128, 128));
                movetodiscoverypage.addActionListener(e -> {
                	if(!database.getPosts().contains(post)) {
                        database.addPost(post);
                        database.saveUsers();
                        BaseLogger.info().log("User " + loggedinuser.getnickname() + " Just moved an image to discovery page!");


                        JOptionPane.showMessageDialog(
                            null,
                            "Your post has been successfully added to the Discovery Page!",
                            "Post Added to Discovery",
                            JOptionPane.PLAIN_MESSAGE
                        );
                    } else {
                        BaseLogger.error().log("User " + loggedinuser.getnickname() + "Tried to move an image to discovery, but already in!");

                        JOptionPane.showMessageDialog(
                            null,
                            "Your post is already in the Discovery Page!",
                            "Already Added",
                            JOptionPane.PLAIN_MESSAGE
                        );
                    }
                });
                infoPanel.add(movetodiscoverypage, infoGbc);
                /**
                 * This method sets a JLabel with the text "Comments:" and adds it to the infoPanel.
                 * It also creates a JTextArea for displaying the comments of the post. The JTextArea
                 * is not editable, meaning users cannot modify the contents directly in the JTextArea.
                 * The comments from the post are retrieved and appended to the JTextArea, each on a new line.
                 * The JTextArea is then added to a JScrollPane to allow scrolling when the comments exceed 
                 * the display area. The JScrollPane is then added to the infoPanel.
                 */
                JLabel commentsLabel = new JLabel("Comments:");
                commentsLabel.setFont(new Font("Sans Serif", Font.BOLD, 16));
                commentsLabel.setForeground(Color.WHITE);
                infoPanel.add(commentsLabel, infoGbc);

                infoGbc.gridy += 1;
                JTextArea commentsArea = new JTextArea();
                commentsArea.setEditable(false);
                for (Comment comment : post.getComments()) {
                    commentsArea.append(comment.getAuthor().getnickname() + ": " + comment.getText() + "\n");
                }
                JScrollPane commentsScrollPane = new JScrollPane(commentsArea);
                commentsScrollPane.setPreferredSize(new Dimension(200, 200));
                infoPanel.add(commentsScrollPane, infoGbc);

                infoGbc.gridy += 1;
                /**
                 * This method creates a JTextField for users to input their comments and a JButton for users to submit their comments.
                 * When the JButton is pressed, the text in the JTextField is retrieved and added as a comment to the post, and the 
                 * JTextField is cleared. The database is also updated with the new comment. The JTextField and JButton are then added
                 * to the infoPanel.
                 */
                JTextField commentInput = new JTextField(20);
                JButton submitComment = new JButton("Add Comment");
                submitComment.setFont(new Font("Sans Serif", Font.BOLD, 16));
                submitComment.setForeground(Color.WHITE);
                submitComment.setBackground(new Color(0, 128, 128));
                submitComment.addActionListener(e -> {
                    BaseLogger.info().log("User " + loggedinuser.getnickname() + " Just commented on an image!");

                    String commentText = commentInput.getText().trim();
                    if (!commentText.isEmpty()) {
                        Comment comment = new Comment(commentText, loggedinuser);
                        post.addComment(comment);
                        commentsArea.append(this.loggedinuser.getnickname() + ": " + commentText + "\n");
                        commentInput.setText("");
                        database.saveUsers();
                    }
                });
                infoPanel.add(commentInput, infoGbc);
                infoGbc.gridx = 1;
                
                infoPanel.add(submitComment, infoGbc);
                infoGbc.gridx = 2;
                infoGbc.gridy = 0;
                JButton deleteButton = new JButton("Delete");
                deleteButton.setFont(new Font("Sans Serif", Font.BOLD, 16));
                deleteButton.setForeground(Color.WHITE);
                deleteButton.setBackground(new Color(0, 128, 128));

                deleteButton.addActionListener(e -> {
                    // Create a JOptionPane for the deletion confirmation
                    int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to delete this post?",
                        "Delete Confirmation",
                        JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        user.deletepost(post);
                        database.saveUsers();
                        database.deletePost(post);

                        // Refresh the PostsPage
                        this.getContentPane().removeAll();
                        this.dispose();
                        new PostsPage(user, loggedinuser, database).setVisible(true);
                    }
                });

                infoPanel.add(deleteButton, infoGbc);

                postPanel.add(infoPanel, BorderLayout.CENTER);
                postsPanel.add(postPanel, postsGbc);
            }
        	
        }
        else {
        	for(Post post : user.getPosts()) {
        		if (post.getPrivacy()==Privacy.PRIVATE) {
        			continue;
        		}
        		else {
        			postsGbc.gridy += 1;

                    JPanel postPanel = new JPanel(new BorderLayout());
                    postPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    BufferedImage resizedImage = resizeImage(post.getImage().getBufferedImage(), 400, 400);
                    ImageIcon imageIcon = new ImageIcon(resizedImage);
                    JLabel postImage = new JLabel(imageIcon);
                    postImage.setBackground(Color.WHITE);
                    postImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    postImage.setPreferredSize(new Dimension(400, 400));
                    postPanel.add(postImage, BorderLayout.WEST);

                    JPanel infoPanel = new JPanel(new GridBagLayout());
                    infoPanel.setBackground(new Color(173, 216, 230));
                    GridBagConstraints infoGbc = new GridBagConstraints();
                    infoGbc.gridx = 0;
                    infoGbc.gridy = 0;
                    infoGbc.anchor = GridBagConstraints.WEST;
                    infoGbc.insets = new Insets(10, 10, 10, 10);

                    JLabel postAuthor = new JLabel("Author: " + post.getAuthorNickname());
                    postAuthor.setFont(new Font("Sans Serif", Font.BOLD, 16));
                    postAuthor.setForeground(Color.WHITE);
                    infoPanel.add(postAuthor, infoGbc);

                    infoGbc.gridy += 1;
                    JLabel postDescription = new JLabel("Description: " + post.getDescription());
                    postDescription.setFont(new Font("Sans Serif", Font.BOLD, 16));
                    postDescription.setForeground(Color.WHITE);
                    infoPanel.add(postDescription, infoGbc);

                    infoGbc.gridy += 1;
                    JButton likeButton = new JButton("Like");
                    likeButton.setFont(new Font("Sans Serif", Font.BOLD, 16));
                    likeButton.setForeground(Color.WHITE);
                    likeButton.setBackground(new Color(0, 128, 128));
                    JLabel likesLabel = new JLabel("Likes: " + post.getLikes());
                    likesLabel.setFont(new Font("Sans Serif", Font.BOLD, 16));
                    likesLabel.setForeground(Color.WHITE);
                    likeButton.addActionListener(e -> {
                        post.like();
                        likesLabel.setText("Likes: " + post.getLikes());
                        database.saveUsers();
                        
                        infoPanel.invalidate();
                        infoPanel.validate();
                        infoPanel.repaint();
                    });
                    infoGbc.gridx = 0;
                    infoPanel.add(likesLabel, infoGbc);
                    infoGbc.gridx = 1;
                    infoPanel.add(likeButton, infoGbc);

                    infoGbc.gridx = 0;
                    infoGbc.gridy += 1;
                    JButton dislikeButton = new JButton("Dislike");
                    dislikeButton.setFont(new Font("Sans Serif", Font.BOLD, 16));
                    dislikeButton.setForeground(Color.WHITE);
                    dislikeButton.setBackground(new Color(0, 128, 128));
                    JLabel dislikesLabel = new JLabel("Dislikes: " + post.getDislikes());
                    dislikesLabel.setFont(new Font("Sans Serif", Font.BOLD, 16));
                    dislikesLabel.setForeground(Color.WHITE);
                    dislikeButton.addActionListener(e -> {
                        BaseLogger.info().log("User " + loggedinuser.getnickname() + " Disliked a photo");

                        post.dislike();
                        dislikesLabel.setText("Dislikes: " + post.getDislikes());
                        database.saveUsers();

                        infoPanel.invalidate();
                        infoPanel.validate();
                        infoPanel.repaint();
                        
                    });
                    infoPanel.add(dislikesLabel, infoGbc);
                    infoGbc.gridx = 1;
                    infoPanel.add(dislikeButton, infoGbc);

                    infoGbc.gridx = 0;
                    infoGbc.gridy += 1;
                    JLabel commentsLabel = new JLabel("Comments:");
                    commentsLabel.setFont(new Font("Sans Serif", Font.BOLD, 16));
                    commentsLabel.setForeground(Color.WHITE);
                    infoPanel.add(commentsLabel, infoGbc);

                    infoGbc.gridy += 1;
                    JTextArea commentsArea = new JTextArea();
                    commentsArea.setEditable(false);
                    for (Comment comment : post.getComments()) {
                        commentsArea.append(comment.getAuthor().getnickname() + ": " + comment.getText() + "\n");
                    }
                    JScrollPane commentsScrollPane = new JScrollPane(commentsArea);
                    commentsScrollPane.setPreferredSize(new Dimension(200, 200));
                    infoPanel.add(commentsScrollPane, infoGbc);

                    infoGbc.gridy += 1;
                    JTextField commentInput = new JTextField(20);
                    JButton submitComment = new JButton("Add Comment");
                    submitComment.setFont(new Font("Sans Serif", Font.BOLD, 16));
                    submitComment.setForeground(Color.WHITE);
                    submitComment.setBackground(new Color(0, 128, 128));
                    submitComment.addActionListener(e -> {
                        String commentText = commentInput.getText().trim();
                        if (!commentText.isEmpty()) {
                            Comment comment = new Comment(commentText, user);
                            post.addComment(comment);
                            commentsArea.append(user.getnickname() + ": " + commentText + "\n");
                            commentInput.setText("");
                            database.saveUsers();
                        }
                    });
                    infoPanel.add(commentInput, infoGbc);
                    infoGbc.gridx = 1;
                    infoPanel.add(submitComment, infoGbc);
                    /**
                     * This section of the code attaches the infoPanel to the postPanel (created earlier to hold the image and post information).
                     * The postPanel is then added to the postsPanel with appropriate grid bag constraints.
                     */

                    postPanel.add(infoPanel, BorderLayout.CENTER);
                    postsPanel.add(postPanel, postsGbc);
        			
        		}
            	
            }
        	
        }

        
        	
        /**
         * A JScrollPane is created for the postsPanel to allow for scrolling when the number of posts exceeds the display area.
         * The JScrollPane is then added to the frame's content pane.
         */
        JScrollPane postsScrollPane = new JScrollPane(postsPanel);
        this.add(postsScrollPane, BorderLayout.CENTER);
    }
}