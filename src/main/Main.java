package main;
import java.awt.Color;
import java.awt.FlowLayout; // specifies how components are arranged
import java.awt.GridLayout;
import java.awt.desktop.UserSessionEvent;
import java.util.ArrayList;

import javax.swing.JFrame; // provides basic window features
import javax.swing.JLabel; // displays text and images
import javax.swing.JOptionPane;
import javax.swing.SwingConstants; // common constants used with Swing

import gui.LandingPage;
import gui.LoginPage;
import gui.Signuppage;
import gui.TimeoutWindow;
import user.User;
import javax.swing.Icon; // interface used to manipulate images
import javax.swing.ImageIcon; // loads images
import javax.swing.JButton;
import gui.UserProfilePage;
/* *********** Pledge of Honor ************************************************ *

I hereby certify that I have completed this lab assignment on my own
without any help from anyone else. I understand that the only sources of authorized
information in this lab assignment are (1) the course textbook, (2) the
materials posted at the course website and (3) any study notes handwritten by myself.

I have not used, accessed or received any information from any other unauthorized
source in taking this lab assignment. The effort in the assignment thus belongs
completely to me.

READ AND SIGN BY WRITING YOUR NAME SURNAME AND STUDENT ID
SIGNATURE: <Ege Yiğit Erbil, 75764>

********************************************************************************/
public class Main {
	
	
	
	
	public static void main(String[] args) {
		Database database=new Database();
		new TimeoutWindow().setVisible(true);
	   


		//If I forget the user-nicknames :D
		for (User i:(database.getusers())) {
			
			
			System.out.println(i.getnickname());

			System.out.println(i.getpassword());
			
		}
	    
		
		
		
		
	}
	

}
