package def_pkg;

import javax.swing.*;

public class BM_System extends JFrame {
	BM_System(){
		setTitle("Bank Managment System");
		setIconImage(new ImageIcon(BM_System.class.getResource("ddot.png")).getImage());
		GUI interf = new GUI();
		Login_Account user = new Login_Account();
	    interf.openSignInForm(this, user);
		setSize(800,500);  
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(this.DISPOSE_ON_CLOSE); 
	}
	public static void main(String[] args) {
		new BM_System();
	}
}
