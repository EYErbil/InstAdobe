package gui;

import javax.swing.*;
import main.Database;
import user.User;

import java.awt.*;
import java.awt.event.*;
import logger.BaseLogger;
/**
 * This class represents the Login Page of the application.
 * It provides text fields for nickname and password input, a login button,
 * and a link to the signup page.
 */
public class LoginPage extends JFrame {
	/**
     * JTextField for entering the user's nickname
     */
    private JTextField nicknameField;
    /**
     * JPasswordField for entering the user's password
     */
    private JPasswordField passwordField;
    /**
     * JButton for logging in
     */
    private JButton loginButton;

    /**
     * JLabel link to the signup page
     */
    private JLabel signupLink;
    /**
     * The Database object that holds all the users data
     */
    private Database database;
    /**
     * Constructs a LoginPage object
     * @param database the database containing users data
     */
    public LoginPage(Database database) {
        this.database = database;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        getContentPane().setBackground(new Color(44, 62, 80));  // dark navy color
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Welcome to PhotoCloud");
        titleLabel.setForeground(new Color(236, 240, 241));  // white smoke color
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel nicknameLabel = new JLabel("Nickname:");
        nicknameLabel.setForeground(new Color(236, 240, 241));  // white smoke color
        nicknameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        nicknameField = new JTextField(20);
        nicknameField.setMaximumSize(nicknameField.getPreferredSize());
        nicknameField.setForeground(new Color(52, 73, 94));  // wet asphalt color
        nicknameField.setBackground(new Color(236, 240, 241));  // white smoke color
        nicknameField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(new Color(236, 240, 241));  // white smoke color
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(passwordField.getPreferredSize());
        passwordField.setForeground(new Color(52, 73, 94));  // wet asphalt color
        passwordField.setBackground(new Color(236, 240, 241));  // white smoke color
        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Verdana", Font.BOLD, 12));
        loginButton.setForeground(new Color(255, 255, 255));  // white
        loginButton.setBackground(new Color(52, 152, 219));  // nice blue
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        signupLink = new JLabel("<HTML><U>Don't have an account? Sign up.</U></HTML>");
        signupLink.setForeground(new Color(52, 152, 219));  // nice blue
        signupLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nickname = nicknameField.getText();
                String password = new String(passwordField.getPassword());

                User user = database.getUserbynickname(nickname);
                if(user != null && user.getpassword().equals(password)) {
                    // login successful, navigate to profile or discover page
                    new UserProfilePage(user,user,database).setVisible(true);
                    dispose();
                    BaseLogger.info().log("User " + nickname + " logged in successfully.");

                } else {
                    // show error message
                    JOptionPane.showMessageDialog(LoginPage.this, "Invalid nickname or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    BaseLogger.error().log("Failed login attempt for user: " + nickname);

                }
            }
        });

        signupLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // navigate to signup page
                new Signuppage(database).setVisible(true);
                dispose();
            }
        });

        add(Box.createRigidArea(new Dimension(0, 20)));
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(nicknameLabel);
        add(nicknameField);
        add(passwordLabel);
        add(passwordField);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(loginButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(signupLink);
    }
}