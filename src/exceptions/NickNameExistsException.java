package exceptions;

public class NickNameExistsException extends Exception {
	public NickNameExistsException() {
		super("NickNameExistsException: this Nickname is already used,sign up failed!");
		
	}
	

}
