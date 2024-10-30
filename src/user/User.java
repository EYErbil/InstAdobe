package user;
import java.awt.Color;
import java.awt.FlowLayout; // specifies how components are arranged
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JFrame; // provides basic window features
import javax.swing.JLabel; // displays text and images
import javax.swing.JOptionPane;
import javax.swing.SwingConstants; // common constants used with Swing

import posts.Post;

import javax.imageio.ImageIO;
import javax.swing.Icon; // interface used to manipulate images
import javax.swing.ImageIcon; // loads images
import imageManipulations.ImageMatrix;
/**
 * The User class represents a user in a social network context.
 * Each User is characterized by a nickname, password, firstname, lastname, age, email,
 * a profile photo, a UserType and a list of posts.
 * This class implements the Serializable interface, allowing User instances to be serialized.
 */
public class User implements Serializable{
	public String nickname; //Unique, check if it exists, add to a map, if present, reenter
	private String password;
	public String firstname;
	public String lastname;
	private int age;
	private String email; //unique
	public ImageMatrix profilephoto;
	private UserType usertype;
	public ArrayList<Post> posts;
	
	/**
     * Creates a new User instance with the given nickname, password, firstname, lastname, age and email.
     * It initializes the usertype as UserType.FREE and tries to read a default profile photo.
     *
     * @param nickname the nickname of the user
     * @param password the password of the user
     * @param firstname the firstname of the user
     * @param lastname the lastname of the user
     * @param age the age of the user
     * @param email the email of the user
     */
	public User(String nickname, String password, String firstname, String lastname, int age, String email) {
		this.posts=new ArrayList<Post>();
		this.nickname=nickname;
		this.password=password;
		this.firstname=firstname;
		this.lastname=lastname;
		this.age=age;
		this.email=email;
		this.usertype=UserType.FREE; //default
		
		

		try {
            this.profilephoto = new ImageMatrix(ImageIO.read(new File("Default.jpg")));
        } catch (IOException e) {
            System.err.println("Unable to read image file.");
            e.printStackTrace();
        }
    }

	
	public void setpassword(String password) {
		this.password=password;
	}
	public void setfirstname(String firstname) {
		this.firstname=firstname;
	}
	public void setlastname(String lastname) {
		this.lastname=lastname;
	}
	public void setage(int age) {
		this.age=age;
	}
	public void setemail(String email) {
		this.email=email;
	}
	public void setpp(ImageMatrix image) {
	    this.profilephoto = image;
	}
	public String getnickname() {
		return nickname;
	}
	public String getemail() {
		return email;
	}
	public String getpassword() {
		return password;
	}
		public void setUserType(UserType userType) {
		    this.usertype = userType;
		}
	public String getUserType() {
		return usertype.name();
	}

	public String getlastname() {
		return lastname;
	}

	public int getage() {
		
		return age;
	}
	public String getfirstname() {
		return firstname;
	}

	public ArrayList<Post> getPosts() {
		return posts;
	}
	public void deletepost(Post post) {
		posts.remove(post);
	}
	/**
     * Returns the profile photo of this User.
     *
     * @return the ImageMatrix representing the profile photo of the User.
     */
	public ImageMatrix getProfilePhoto() {
        return profilephoto;
    }
	public UserType getUserTypeEnum() {
		return usertype;
	}
	public void removepost(Post post) {
		posts.remove(post);
		
	
	}
	
	
	
	

}
