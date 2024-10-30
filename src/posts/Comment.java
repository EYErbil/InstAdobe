package posts;

import java.io.Serializable;

import user.User;
/**
 * This class represents a Comment made by a User.
 * Each Comment is characterized by the text of the comment and the User who authored the comment.
 * This class implements the Serializable interface, which allows instances of this class to be converted into a byte stream for various purposes, such as saving to disk or transmission over a network.
 */
public class Comment implements Serializable {
    private String text;
    private User author;
    /**
     * Creates a new Comment instance with the specified text and author.
     *
     * @param text the text of the comment. This is the content of the comment itself.
     * @param author the User who authored the comment. This user is the creator of the comment.
     */
    public Comment(String text, User author) {
        this.text = text;
        this.author = author;
    }
    /**
     * Returns the text of this Comment.
     *
     * @return a String representing the text of the comment.
     */
    public String getText() {
        return text;
    }
    /**
     * Returns the User who authored this Comment.
     *
     * @return a User object representing the author of the comment.
     */
    public User getAuthor() {
        return author;
    }
}
