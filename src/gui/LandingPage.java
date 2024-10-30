package gui;

import javax.swing.*;

import logger.BaseLogger;
import main.Database;

import java.awt.*;
import java.awt.event.*;
/**
 * This class represents the Landing Page of the application.
 * It provides buttons for logging in, signing up and admin login.
 */
public class LandingPage extends JFrame {
	/**
     * JButton for logging in,signingup, adminloggingin
     */
    private JButton loginButton, signupButton, adminLoginButton;
    /**
     * The Database object that holds all the users data
     */
    private Database database;
    /**
     * Constructs a LandingPage object
     * @param database the database containing users data
     */
    public LandingPage(Database database) {
        this.database = database;
        BaseLogger.info().log("Program Starting... \n\n\n");


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        getContentPane().setBackground(new Color(44, 62, 80)); // dark navy color
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Welcome to PhotoShare");
        titleLabel.setForeground(new Color(236, 240, 241)); // white smoke color
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton = new JButton("Log In");
        loginButton.setFont(new Font("Verdana", Font.BOLD, 12));
        loginButton.setForeground(new Color(255, 255, 255)); // white
        loginButton.setBackground(new Color(52, 152, 219)); // nice blue
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        signupButton = new JButton("Sign Up");
        signupButton.setFont(new Font("Verdana", Font.BOLD, 12));
        signupButton.setForeground(new Color(255, 255, 255)); // white
        signupButton.setBackground(new Color(52, 152, 219)); // nice blue
        signupButton.setBorderPainted(false);
        signupButton.setFocusPainted(false);
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        adminLoginButton = new JButton("Admin Log In");
        adminLoginButton.setFont(new Font("Verdana", Font.BOLD, 12));
        adminLoginButton.setForeground(new Color(255, 255, 255)); // white
        adminLoginButton.setBackground(new Color(52, 152, 219)); // nice blue
        adminLoginButton.setBorderPainted(false);
        adminLoginButton.setFocusPainted(false);
        adminLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginPage(database).setVisible(true);
                dispose();
            }
        });

        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Signuppage(database).setVisible(true);
                dispose();
            }
        });

        adminLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	new AdminLogIn(database).setVisible(true);
            	dispose();
                
            }
        });

        add(Box.createRigidArea(new Dimension(0, 20)));
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(loginButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(signupButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(adminLoginButton);
    }
}