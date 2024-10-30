package user;
import main.Database;

import java.util.ArrayList;

import exceptions.EmailExistsException;
import exceptions.NickNameExistsException;

public class UserValidator {
	public void valideaccount(User user,ArrayList<User> arraylist) throws NickNameExistsException, EmailExistsException {
		for (User i:arraylist) {
			if (i.getnickname().equals(user.getnickname())) {
				throw new NickNameExistsException();
			}
		}
		for (User i:arraylist) {
			if (i.getemail().equals(user.getemail())) {
				throw new EmailExistsException();
			}
		}
		
		
		
	}
	

}
