package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import logger.BaseLogger;
import main.Database;
import user.User;
import user.UserType;

/**
 * This class provides a GUI for administrator login functionality. 
 * It extends the JFrame class to create a form where an administrator can enter their credentials (nickname and password) 
 * to log into the PhotoCloud application.
 */
public class AdminLogIn extends JFrame {

    /**
     * TextField to input administrator nickname
     */
    private JTextField nicknameField;

    /**
     * PasswordField to input administrator password
     */
    private JPasswordField passwordField;

    /**
     * Button to initiate the login process
     */
    private JButton loginButton;

    /**
     * Label that serves as a link to the signup page
     */
    private JLabel signupLink;

    /**
     * The database object containing user data
     */
    private Database database;

    /**
     * The constructor of the class.
     * Initializes the form and its components.
     * @param database a Database object containing user data.
     */

    public AdminLogIn(Database database) {
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
        
        /**
         * ActionListener for loginButton.
         * Checks the entered credentials against the user data in the database, and if the credentials are valid and belong 
         * to an administrator, redirects to the AdminProfilePage. 
         * Otherwise, it displays an error message.
         */

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nickname = nicknameField.getText();
                String password = new String(passwordField.getPassword());

                User user = database.getUserbynickname(nickname);
                if(user != null && user.getpassword().equals(password)) {
                    if (user.getUserTypeEnum() == UserType.ADMIN) {
                        new AdminProfilePage(user,user, database).setVisible(true);
                        dispose();
                        BaseLogger.info().log("User " + nickname + " admin logged in successfully.");

                    } else {
                        JOptionPane.showMessageDialog(AdminLogIn.this, "Admin does not exist.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                        BaseLogger.error().log("Failed adminlogin attempt for user: " + nickname);

                    }
                } else {
                    JOptionPane.showMessageDialog(AdminLogIn.this, "Invalid nickname or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        /**
         * MouseAdapter for signupLink label.
         * Redirects to the Signup page on mouse click.
         */

        signupLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
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
 


