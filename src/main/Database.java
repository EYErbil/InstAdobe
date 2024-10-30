package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import exceptions.EmailExistsException;
import exceptions.NickNameExistsException;
import posts.Post;
import user.User;
import user.UserValidator;
/**
 * The Database class stores and manages all the users and posts for an application. 
 * It provides methods for saving, loading, adding, and deleting posts and users. 
 * It also provides methods to get a user by their nickname or email. 
 * Serialization is the process of converting an object's state to a byte stream,
 *  which can then be persisted to a disk or sent over a network.
 *  The opposite of this process, deserialization, 
 *  converts a byte stream back to its original object form.
 *  Throughout the project, that's how we save -load the data used. 
 */
public class Database {
	UserValidator uservalidator= new UserValidator();
	private ArrayList<User> users= new ArrayList<>();
	private ArrayList<Post> posts= new ArrayList<> ();
    private static final String Data = "users.txt";
    private static final String PostData = "Discoveryposts.txt";
    /**
     * Constructor for the Database class.
     * It loads users and posts from the file system.
     */
    public Database() {
        loadUsers();
        loadPosts();

    }
    /**
     * Loads users from a data file into the application.
     * In case the file does not exist, it creates a new one.
     */
    private void loadUsers() {
        try (ObjectInputStream data = new ObjectInputStream(new FileInputStream(Data))) {
            users = (ArrayList<User>) data.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("User data file not found. Creating a new file.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * Loads posts from a data file into the application.
     * In case the file does not exist, it creates a new one.
     */
    private void loadPosts() {
        try (ObjectInputStream data = new ObjectInputStream(new FileInputStream(PostData))) {
            posts = (ArrayList<Post>) data.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Post data file not found. Creating a new file.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * Writes the current state of posts to a file.
     */
    public void savePosts() {
        try (ObjectOutputStream dataout = new ObjectOutputStream(new FileOutputStream(PostData))) {
            dataout.writeObject(posts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Writes the current state of users to a file.
     */
    public void saveUsers() {
        try (ObjectOutputStream dataout = new ObjectOutputStream(new FileOutputStream(Data))) {
            dataout.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Creates a new user after validation.
     * If the user is valid, it's added to the list of users and saved to a file.
     *
     * @param user User object to be created
     * @return true if the user was successfully created, false otherwise
     */
	public boolean createuser(User user) {
    	try {
			uservalidator.valideaccount(user,this.getusers());
			users.add(user);
			saveUsers();
			return true;
			
			
		} catch (NickNameExistsException | EmailExistsException e) {
			// TODO Auto-generated catch block
			return false;
		}
    	
    	
		
	
		
	}
	/**
     * Adds a post to the list of posts and saves it to a file.
     *
     * @param post Post object to be added
     */
	public void addPost(Post post) {
        posts.add(post);
        savePosts();
    }
	/**
     * Removes a post from the list of posts and saves the updated list to a file.
     *
     * @param post Post object to be removed
     */
	public void deletePost(Post post) {
        posts.remove(post);
        savePosts();
    }
	/**
     * Returns the list of posts.
     *
     * @return An ArrayList of posts
     */
	public ArrayList<Post> getPosts() {
        return posts;
    }

    /**
     * Removes a user from the list of users and saves the updated list to a file.
     *
     * @param user User object to be removed
     */
	public void deleteuser(User user) {
		this.users.remove(user);
		saveUsers();
		
		
		
	}
	 
	/**
     * Returns the list of users.
     *
     * @return An ArrayList of users
     */
	public ArrayList<User> getusers() {
		return users;
	}
	/**
     * Returns a User object based on the nickname provided.
     *
     * @param nickname Nickname of the user
     * @return User object if a match is found, null otherwise
     */
	public User getUserbynickname(String nickname) {
		for (User i: users) {
			if (i.getnickname().equals(nickname)) {
				return i;
			}
			
		}
		return null;
		
	}
	/**
     * Returns a User object based on the email provided.
     *
     * @param email Email of the user
     * @return User object if a match is found, null otherwise
     */
	public User getUserbyemail(String email) {
		for (User i: users) {
			if (i.getemail().equals(email)) {
				return i;
			}
			
		}
		return null;
		
	}
	


}
