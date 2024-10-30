package exceptions;

public class EmailExistsException extends Exception {
	public EmailExistsException() {
		super("this email or nickname is already used, sign up failed!");
		
	}
	

}


