package gui;

import javax.swing.*;

import logger.BaseLogger;
import main.Database;
import user.User;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
/**
 * This class represents the Edit Page of the PhotoCloud application. 
 * The EditPage allows the logged-in user to update their profile information.
 */
public class EditPage extends JFrame {
	/**
     * The logged-in User object
     */
    private User user;
    /**
     * The Database object that holds all the users data
     */
    private Database database;
    /**
     * Constructs an EditPage that allows the given User to edit their profile.
     * @param user the currently logged-in user
     * @param database the database containing users data
     */
    public EditPage(User user, Database database) {
    	
        this.user = user;
        
        this.database = database;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(6, 2));
        this.setSize(new Dimension(500, 300));

        JTextField firstNameField = new JTextField(user.getfirstname());
        JTextField lastNameField = new JTextField(user.getlastname());
        JTextField ageField = new JTextField(String.valueOf(user.getage()));
        JTextField emailField = new JTextField(user.getemail());
        JPasswordField passwordField = new JPasswordField(user.getpassword());

        this.add(new JLabel("First Name: "));
        this.add(firstNameField);
        this.add(new JLabel("Last Name: "));
        this.add(lastNameField);
        this.add(new JLabel("Age: "));
        this.add(ageField);
        this.add(new JLabel("Email: "));
        this.add(emailField);
        this.add(new JLabel("Password: "));
        this.add(passwordField);
        /**
         * This WindowAdapter is for the EditPage JFrame.
         * When the window is closing, it navigates back to the UserProfilePage of the logged-in user.
         */
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                new UserProfilePage(user, user, database).setVisible(true);
                dispose();
            }
        });
    

        JButton saveButton = new JButton("Save Changes");
        /**
         * This ActionListener is for the saveButton.
         * When the save button is clicked, it updates the user's information based on the provided inputs, validates the new values, 
         * saves the changes to the database, and navigates back to the UserProfilePage of the logged-in user.
         */
        saveButton.addActionListener(e -> {
            BaseLogger.info().log("User " + user.getnickname() + " Changed their Info.");

            user.setfirstname(firstNameField.getText());
            user.setlastname(lastNameField.getText());
            try {
                int age = Integer.parseInt(ageField.getText());
                if (age < 15) {
                    JOptionPane.showMessageDialog(this, "Age must be 15 or older.");
                    return;
                }
                user.setage(age);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age.");
                return;
            }

            // Check email.
            String email = emailField.getText();
            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
                return;
            }
            
            User existingUser = database.getUserbyemail(email);
            if (existingUser != null && !existingUser.equals(user)) {
                JOptionPane.showMessageDialog(this, "This email address already exists!");
                return;
            }
            user.setemail(email);

            
            user.setpassword(new String(passwordField.getPassword()));

            database.saveUsers();

            JOptionPane.showMessageDialog(this, "Changes saved.");
            this.dispose();
            new UserProfilePage(user, user, database).setVisible(true);
        });
        

        this.add(saveButton);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    }
}