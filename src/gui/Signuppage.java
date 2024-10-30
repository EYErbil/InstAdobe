package gui;
import main.Database;
import main.Main;
import user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*public class Signuppage extends JFrame {
	private Database database;
    private JTextField nameField;
    private JTextField lastNameField;
    private JTextField nickNameField;
    private JPasswordField passwordField;
    private JButton signUpButton;
    private JTextField emailField;
    private JTextField ageField;
    private JTextField photoPathField;

    public Signuppage(Database database) {
    	this.database=database;
    	
    	setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Initialize fields
        nameField = new JTextField(20);
        lastNameField = new JTextField(20);
        nickNameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        emailField = new JTextField(20);
        ageField = new JTextField(20);
        photoPathField = new JTextField(20);
        signUpButton = new JButton("Sign Up");

        // Add fields to frame
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Name: "), gbc);

        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Last Name: "), gbc);

        gbc.gridx = 1;
        add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Nickname: "), gbc);

        gbc.gridx = 1;
        add(nickNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Password: "), gbc);

        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Email: "), gbc);

        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Age: "), gbc);

        gbc.gridx = 1;
        add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(signUpButton, gbc);
        

        signUpButton.addActionListener(new ActionListener() {
            @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String lastName = lastNameField.getText();
            String nickName = nickNameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            int age = Integer.parseInt(ageField.getText()); // assuming the age is a valid integer

            User user = new User(nickName, password, name, lastName, age, email);
            if(database.createuser(user)==true) {
            	Signuppage.this.dispose();
            	
            }
            
            
            
            

            
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
    
}
*/
import javax.swing.*;
import java.awt.event.*;
/**
 * This class represents the GUI for a Signuppage. It extends JFrame to create a window for the sign up process.
 */
public class Signuppage extends JFrame {
    private JTextField nicknameField, realNameField, surnameField, ageField, emailField;
    private JPasswordField passwordField;
    private JButton signupButton;
    private JLabel loginLink;
    private Database database;
    /**
     * The Signuppage constructor initializes the UI elements and interactions within the window.
     * 
     * @param database - The Database object where all users and their related information is stored
     */
    public Signuppage(Database database) {
        this.database = database;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        nicknameField = new JTextField();
        realNameField = new JTextField();
        surnameField = new JTextField();
        ageField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        signupButton = new JButton("Sign Up");
        loginLink = new JLabel("<HTML><U>Already have an account? Login.</U></HTML>"); //Stackoverflow (reference 1)
        /**
         * This function handles the sign up process, validating the user input and creating a new User object if the input is valid.
         *
         * @param e - The event source
         */
        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nickname = nicknameField.getText();
                String realName = realNameField.getText();
                String surname = surnameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();
                int age = 0;  // assuming the age is a valid integer
                try {
                    age = Integer.parseInt(ageField.getText()); // assuming the age is a valid integer
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Signuppage.this, "Age must be a number.", "Signup Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check for essential fields
                if (nickname.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(Signuppage.this, "Nickname, Password and Email are essential.", "Signup Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check for password length
                if (password.length() < 8) {
                    JOptionPane.showMessageDialog(Signuppage.this, "Password must be longer than 7 characters.", "Signup Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check for age
                if (age < 15) {
                    JOptionPane.showMessageDialog(Signuppage.this, "You have to be 15 or older to use PhotoCloud.", "Signup Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check for valid email
                if (!email.contains("@")) {
                    JOptionPane.showMessageDialog(Signuppage.this, "Email must contain '@'.", "Signup Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User user = new User(nickname, password, realName, surname, age, email);
                if(database.createuser(user)) {
                    JOptionPane.showMessageDialog(Signuppage.this, "User has been successfully registered!", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                    LoginPage loginPage = new LoginPage(database); 
                    loginPage.setVisible(true);
                    dispose();

                    // account creation successful, navigate to profile or discover page
                    // new ProfilePage(user).setVisible(true);
                    // dispose();
                	//youtube videosu buraya kadar, burayı kendimiz dolduracaz mecbur, bi page açalım
                } else {
                    // show error message
                    JOptionPane.showMessageDialog(Signuppage.this, "this email or nickname is already used, sign up failed!", "Signup Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        /**
         * This function handles the event when the user wants to navigate to the login page.
         *
         * @param e - The event source
         */
        loginLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // navigate to login page
                new LoginPage(database).setVisible(true);
                dispose();
            }
        });

        add(new JLabel("Nickname:"));
        add(nicknameField);
        add(new JLabel("Real Name:"));
        add(realNameField);

        add(new JLabel("Surname:"));
        add(surnameField);

        add(new JLabel("Age:"));
        add(ageField);

        add(new JLabel("Email:"));
        add(emailField);

        add(new JLabel("Password:"));
        add(passwordField);

        add(signupButton);
        add(loginLink);

        pack();
        setLocationRelativeTo(null);  // Centers the window on the screen
    }
}