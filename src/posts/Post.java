package posts;

import imageManipulations.ImageMatrix;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * The Post class represents a post in a social network context.
 * Each Post is characterized by an image, author's nickname, a description, 
 * a list of comments, a number of likes and dislikes and a privacy setting.
 * This class implements the Serializable interface, allowing Post instances to be serialized.
 */
public class Post implements Serializable {

    private ImageMatrix image;
    private String authorNickname;
    private String description;
    private ArrayList<Comment> comments;
    private int likes;
    private int dislikes;
    private Privacy privacy;

    /**
     * Creates a new Post instance with the given image, authorNickname, description, and privacy setting.
     *
     * @param image the image associated with the post
     * @param authorNickname the nickname of the author of the post
     * @param description the description text for the post
     * @param privacy the privacy setting for the post (PUBLIC or PRIVATE)
     */
    public Post(ImageMatrix image, String authorNickname, String description,Privacy privacy) {
        this.image = image;
        this.authorNickname = authorNickname;
        this.description = description;
        this.comments = new ArrayList<Comment>();
        this.likes = 0;
        this.dislikes = 0;
        this.privacy=privacy;
    }

    public ImageMatrix getImage() {
        return image;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public String getDescription() {
        return description;
    }
    /**
     * Adds a new Comment to this Post.
     *
     * @param comment the Comment to be added to the post
     */
    public ArrayList<Comment> getComments() {
        return comments;  
    }
    /**
     * Increases the like count of this Post by one.
     */
    public int getLikes() {
        return likes;
    }
    /**
     * Increases the dislike count of this Post by one.
     */
    public int getDislikes() {
        return dislikes;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void like() {
        likes++;
    }

    public void dislike() {
        dislikes++;
    }
    public Privacy getPrivacy() {
    	return privacy;
    }
    public void setPrivacyprivate() {
    	privacy=privacy.PRIVATE;
    }
    public void setPrivacyPublic() {
    	privacy=privacy.PUBLIC;
    }
    /**
     * Checks if this Post is equal to another Object.
     * They are equal if the other Object is a Post and has the same description.
     *
     * @param obj the Object to compare this Post to
     * @return true if the given Object represents a Post equivalent to this Post, false otherwise
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Post other = (Post) obj;
        return description != null ? description.equals(other.description) : other.description == null;
    }
    /**
     * Returns a hash code value for the Post. This method is supported for the benefit of hash tables such as those provided by HashMap.
     *
     * @return a hash code value for this Post
     */
    @Override
    public int hashCode() {
        return description != null ? description.hashCode() : 0;
    }
}