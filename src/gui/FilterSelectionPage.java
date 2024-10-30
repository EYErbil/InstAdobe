package gui;

import javax.swing.JFrame;

import main.Database;
import user.User;

import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import imageManipulations.ImageMatrix;
import logger.BaseLogger;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
/**
 * This class represents the Filter Selection Page of the application.
 * It allows the user to choose a filter to apply on an image.
 */
public class FilterSelectionPage extends JFrame {
	/**
     * The logged-in User object
     */
    private User user;
    /**
     * The Database object that holds all the users data
     */
    private Database database;
    /**
     * Constructs a FilterSelectionPage that allows the given User to select a filter.
     * @param user the currently logged-in user
     * @param database the database containing users data
     */
    public FilterSelectionPage(User user, Database database) {
        this.user = user;
        this.database = database;

        setTitle("Filter Photos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        getContentPane().setBackground(new Color(230, 236, 240));
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        northPanel.setBackground(new Color(135, 206, 235)); 
        add(northPanel, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel("Select a Filter");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        northPanel.add(titleLabel);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(230, 236, 240)); 
        add(centerPanel, BorderLayout.CENTER);
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Adding filter buttons
        addButtonIfAllowed("Blur", e -> applyFilter("Blur"), centerPanel, gbc);
        addButtonIfAllowed("Sharpen", e -> applyFilter("Sharpen"), centerPanel, gbc);
        addButtonIfAllowed("Brightness", e -> applyFilter("Brightness"), centerPanel, gbc);
        addButtonIfAllowed("Contrast", e -> applyFilter("Contrast"), centerPanel, gbc);
        addButtonIfAllowed("Grayscale", e -> applyFilter("Grayscale"), centerPanel, gbc);
        addButtonIfAllowed("Edge Detection", e -> applyFilter("Edge Detection"), centerPanel, gbc);

        JPanel southPanel = new JPanel();
        southPanel.setBackground(new Color(135, 206, 235)); 
        add(southPanel, BorderLayout.SOUTH);

        JButton returnButton = new JButton("Return");
        returnButton.setFont(new Font("Verdana", Font.BOLD, 14));
        returnButton.addActionListener(e -> {
            new UserProfilePage(user, user, database).setVisible(true);
            FilterSelectionPage.this.dispose();
        });
        southPanel.add(returnButton);
    }
    /**
     * Creates a JButton with given filter name and action listener.
     * @param filterName name of the filter button
     * @param actionListener the action to be performed when the button is clicked
     * @return a JButton with specified name and action listener
     */
    private JButton createFilterButton(String filterName, ActionListener actionListener) {
        JButton button = new JButton(filterName);
        button.setFont(new Font("Verdana", Font.BOLD, 14));
        button.setBackground(new Color(87, 184, 70));
        button.setForeground(Color.WHITE);
        button.addActionListener(actionListener);
        return button;
    }
    /**
     * Applies the selected filter to the image selected by the user.
     * @param filterName name of the filter to be applied
     */
    private void applyFilter(String filterName) {

        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedImage img = ImageIO.read(selectedFile);
                ImageMatrix imageMatrix = new ImageMatrix(img);
                ImageMatrix filteredMatrix;
                if (img == null) {
                    JOptionPane.showMessageDialog(null, "The selected file is not an image. Please select a valid image file.", "Invalid File Type", JOptionPane.ERROR_MESSAGE);
                    BaseLogger.error().log(user.getnickname() + " tried to filter a file that is not an image!");
                    return;
                }
                else {
                    BaseLogger.info().log("User " + user.getnickname() + " Just applied "+ filterName+" Filter");

                }


                switch (filterName) {
                    case "Blur":
                    	JSlider blurSlider = new JSlider(0, 10, 1);
                        blurSlider.setMajorTickSpacing(1);
                        blurSlider.setPaintTicks(true);
                        blurSlider.setPaintLabels(true);
                        JOptionPane.showMessageDialog(null, blurSlider, "Choose Blur Level", JOptionPane.PLAIN_MESSAGE);
                        int blurLevel = blurSlider.getValue();
                        filteredMatrix = imageMatrix.applyBlurFilter(blurLevel);
                        break;
                    case "Sharpen":
                    	JSlider sharpenSlider = new JSlider(0, 10, 1);
                        sharpenSlider.setMajorTickSpacing(1);
                        sharpenSlider.setPaintTicks(true);
                        sharpenSlider.setPaintLabels(true);
                        JOptionPane.showMessageDialog(null, sharpenSlider, "Choose Sharpen Level", JOptionPane.PLAIN_MESSAGE);
                        int sharpenLevel = sharpenSlider.getValue();
                        filteredMatrix = imageMatrix.applySharpenFilter(sharpenLevel);
                        break;
                    case "Grayscale":
                        JSlider grayscaleSlider = new JSlider(0, 100, 100);
                        grayscaleSlider.setMajorTickSpacing(10);
                        grayscaleSlider.setPaintTicks(true);
                        grayscaleSlider.setPaintLabels(false);
                        JOptionPane.showMessageDialog(null, grayscaleSlider, "Choose Grayscale Level", JOptionPane.PLAIN_MESSAGE);
                        double grayscaleModifier = grayscaleSlider.getValue() / 100.0;
                        filteredMatrix = imageMatrix.applyGrayscaleFilter(grayscaleModifier);
                        break;
                    case "Edge Detection":
                        filteredMatrix = imageMatrix.applyEdgeDetectionFilter();
                        break;
                    case "Contrast":
                    	JSlider contrastSlider = new JSlider(-100, 100, 0);
                    	contrastSlider.setMajorTickSpacing(10);
                    	contrastSlider.setPaintTicks(true);
                    	contrastSlider.setPaintLabels(false);
                    	JOptionPane.showMessageDialog(null, contrastSlider, "Choose Contrast Level", JOptionPane.PLAIN_MESSAGE);
                    	double contrastModifier = contrastSlider.getValue();
                        filteredMatrix = imageMatrix.applyContrastFilter(contrastModifier);

                        break;
                    case "Brightness":
                    	JSlider brightnessSlider = new JSlider(0, 100, 0);
                    	brightnessSlider.setMajorTickSpacing(10);
                    	brightnessSlider.setPaintTicks(true);
                    	brightnessSlider.setPaintLabels(false);
                    	JOptionPane.showMessageDialog(null, brightnessSlider, "Choose Brightness Level", JOptionPane.PLAIN_MESSAGE);
                    	double brightnessModifier = brightnessSlider.getValue() / 100.0;
                    	filteredMatrix = imageMatrix.applyBrightnessFilter(brightnessModifier);
                    	break;
                    default:
                        throw new IllegalArgumentException("Invalid filter name");
                }

                new FilterPreviewWindow(user, database,imageMatrix, filteredMatrix).setVisible(true);
                FilterSelectionPage.this.dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "The selected file is not an image. Please select a valid image file.", "Invalid File Type", JOptionPane.ERROR_MESSAGE);

                BaseLogger.error().log(user.getnickname() + " tried to filter a file that is not an image!");

            }
            catch (NullPointerException ex) {
            	ex.printStackTrace();
            	JOptionPane.showMessageDialog(null, "The selected file is not an image. Please select a valid image file.", "Invalid File Type", JOptionPane.ERROR_MESSAGE);
                BaseLogger.error().log(user.getnickname() + " tried to filter a file that is not an image!");
            	
            }
        }
    }
    /**
     * Adds the button to the panel if the user is allowed to use the filter.
     * @param filterName name of the filter
     * @param actionListener the action to be performed when the button is clicked
     * @param panel the panel where the button will be added
     * @param gbc the constraints for the gridbag layout
     */
    private void addButtonIfAllowed(String filterName, ActionListener actionListener, JPanel panel, GridBagConstraints gbc) {
        if (isAllowed(filterName)) {
            JButton button = createFilterButton(filterName, actionListener);
            panel.add(button, gbc);
        }
    }
    /**
     * Checks if the user is allowed to use the filter based on their user type.
     * @param filterName name of the filter
     * @return true if the user is allowed to use the filter, false otherwise
     */
    private boolean isAllowed(String filterName) {
        switch (user.getUserType()) {
            case "FREE":
                return filterName.equals("Blur") || filterName.equals("Sharpen");
            case "HOBBYIST":
                return filterName.equals("Blur") || filterName.equals("Sharpen") || filterName.equals("Brightness") || filterName.equals("Contrast");
            case "PROFESSIONAL":
                return true;
            case "ADMIN":
                return true;
            default:
                return false;
        }
    }
}