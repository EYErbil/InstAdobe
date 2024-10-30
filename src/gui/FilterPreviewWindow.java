package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import imageManipulations.ImageMatrix;
import imageManipulations.ImageSecretary;
import logger.BaseLogger;
import main.Database;
import posts.Post;
import posts.Privacy;
import user.User;
/**
 * This class represents the Filter Preview Window of the application.
 * It is used to preview the filtered image alongside the original image.
 * It also provides the option to save the filtered image as a post or to the local machine.
 */
public class FilterPreviewWindow extends JFrame {
	 /**
     * The original ImageMatrix object before any filtering was applied
     */
    private ImageMatrix originalImage;
    /**
     * The ImageMatrix object after the filter has been applied
     */
    private ImageMatrix filteredImage;
    /**
     * The logged-in User object
     */
    private User user;
    /**
     * The Database object that holds all the users data
     */
    private Database database;
    /**
     * Constructs a FilterPreviewWindow that allows the given User to preview and save the filtered image.
     * @param user the currently logged-in user
     * @param database the database containing users data
     * @param originalImage the original image before any filters were applied
     * @param filteredImage the image after the filter has been applied
     */
    public FilterPreviewWindow(User user, Database database, ImageMatrix originalImage, ImageMatrix filteredImage) {
        this.originalImage = originalImage;
        this.filteredImage = filteredImage;
        this.database = database;
        this.user = user;

        setTitle("Filter Preview");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Open the JFrame in full screen
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel labelOriginal = new JLabel("Original Image");
        JLabel labelFiltered = new JLabel("Filtered Image");

        // Getting the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Change the image size to fit the screen
        ImageIcon iconOriginal = new ImageIcon(originalImage.getBufferedImage().getScaledInstance(screenSize.width / 3, screenSize.height / 3, Image.SCALE_DEFAULT));
        ImageIcon iconFiltered = new ImageIcon(filteredImage.getBufferedImage().getScaledInstance(screenSize.width / 3, screenSize.height / 3, Image.SCALE_DEFAULT));

        JLabel originalImageLabel = new JLabel(iconOriginal);
        JLabel filteredImageLabel = new JLabel(iconFiltered);

        JCheckBox saveAsPostCheckBox = new JCheckBox("Save as post?");
        JCheckBox saveToComputerCheckBox = new JCheckBox("Save to Computer?");

        JButton confirmButton = new JButton("Confirm");
        /**
         * This ActionListener is for the confirmButton.
         * When the confirm button is clicked, it allows the user to save the filtered image as a post or to the local machine.
         * The ActionListener also takes care of uploading the post and adding it to the discovery page if the user so chooses.
         */
        confirmButton.addActionListener(e -> {
        	if (saveAsPostCheckBox.isSelected()) {
                BaseLogger.info().log("User " + user.getnickname() + " Just shared a photo!");

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
        	            JOptionPane.showMessageDialog(null, "Post must be public to be added to Discovery Page!", "Error", JOptionPane.ERROR_MESSAGE);
        	            return;
        	        }
        	        Privacy privacy = isPublic ? Privacy.PUBLIC : Privacy.PRIVATE;

        	        if (description != null && !description.trim().isEmpty()) {
        	            Post newPost = new Post(filteredImage, user.getnickname(), description, privacy);
        	            user.getPosts().add(newPost);
        	            if (addToDiscovery) {
        	                database.getPosts().add(newPost);
        	                database.savePosts();
        	            }
        	            database.saveUsers();
        	            JOptionPane.showMessageDialog(null, "Your post has been uploaded successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        	        } else {
        	            JOptionPane.showMessageDialog(null, "Post cancelled. You must enter a valid description.", "Error", JOptionPane.ERROR_MESSAGE);
        	        }
        	    }
        	}
            if(saveToComputerCheckBox.isSelected()) {
                BaseLogger.info().log("User " + user.getnickname() + " Just downloaded an Image!");

                String imageName = "filtered_image";
                String extension = ".jpg";
                boolean result = ImageSecretary.writeFilteredImageToResources(filteredImage, imageName, extension);
                if (result) {
                    JOptionPane.showMessageDialog(null, "Your image has been saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to save image. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JButton backButton = new JButton("Back");
        GridBagConstraints gbcBack = new GridBagConstraints();
        gbcBack.gridx = -3;
        gbcBack.gridy = 0;
        gbcBack.anchor = GridBagConstraints.NORTHWEST;
        /**
         * This ActionListener is for the backButton.
         * When the back button is clicked, it navigates back to the UserProfilePage of the logged-in user.
         */
        backButton.addActionListener(e -> {
            this.dispose();  
            new UserProfilePage(user,user, database).setVisible(true);

        });

        add(labelOriginal, gbc);
        add(originalImageLabel, gbc);
        add(labelFiltered, gbc);
        add(filteredImageLabel, gbc);
        add(saveAsPostCheckBox, gbc);
        add(saveToComputerCheckBox, gbc);
        add(confirmButton, gbc);
        add(backButton, gbcBack);


        setVisible(true);
    }
}