package def_pkg;

import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GUI {
	
	// remake the screen
	public void remakeScreen(JFrame frame, JPanel f) {
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}
	
	
	
	//------------------------------------------------------------
	//               Các chức năng hiển thị liên quan đến đăng nhập               |
	//------------------------------------------------------------
	
	
	// Form đăng nhập được hiển thị khi bắt đầu chương trình
	public void openSignInForm(JFrame frame, Login_Account user) {
		System.out.println( System.getProperty("user.dir") + "\\BM_System\\src\\def_pkg\\anchor.png");
		JPanel f = new JPanel();		
		f.setBackground(Color.white);
		
		// main screen picture
		JLabel imgLabel = new JLabel(new ImageIcon( getClass().getResource("anchor.png")));
		imgLabel.setBounds(20,30,450, 400);
		f.add(imgLabel);
		
		//---------------------------------Creating Labels------------------------------------------		
		JLabel l_hSignIn = new JLabel("Sign In");
		l_hSignIn.setFont( l_hSignIn.getFont().deriveFont(30f) );
		l_hSignIn.setBounds(550,50,100, 40);
		f.add(l_hSignIn);
		
		JLabel l_UserName = new JLabel("User Name");
		l_UserName.setBounds(500,120,100, 40);
		f.add(l_UserName);
		
		JLabel l_Password = new JLabel("Password");
		l_Password.setBounds(500,200,100, 40);
		f.add(l_Password);
		
		JLabel l_Signup = new JLabel("Dont have an account?");
		l_Signup.setBounds(500,380,150, 40);
		f.add(l_Signup);
	
		//-----------------------------------Creating TextFields--------------------------------------------------		
		JTextField tf_UserName = new JTextField();
		tf_UserName.setBounds(500,170,180, 25);
		f.add(tf_UserName);
		
		JPasswordField tf_Password = new JPasswordField();
		tf_Password.setBounds(500,250,180, 25);
		f.add(tf_Password);
			
		//-------------------------------------Creating Buttons--------------------------------------------------
		JButton btn_SignIn = new JButton("Sign In");
		btn_SignIn.setFont( btn_SignIn.getFont().deriveFont(18f) );
		btn_SignIn.setBackground( new Color(0, 204, 153) );
		btn_SignIn.setForeground(Color.white);
		btn_SignIn.setBounds(550,300,110, 40);
		f.add(btn_SignIn); 
		
		JButton btn_SignUp = new JButton("Sign Up");
		btn_SignUp.setFont( btn_SignUp.getFont().deriveFont(18f) );
		btn_SignUp.setBackground( new Color(0, 204, 153) );
		btn_SignUp.setForeground(Color.white);
		btn_SignUp.setBounds(650,380,110, 40);
		f.add(btn_SignUp);
			
		//---------------------------------------SignUp functionality----------------------------------------------
		
		btn_SignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			open_Signup_form_1(frame, user);
				
			}
		});
		
		//----------------------------------Sign in Functionality-----------------------------------------------		
		btn_SignIn.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				
				DB_Handler db = new DB_Handler();
				Login_Account user = db.signIn( tf_UserName.getText(), tf_Password.getText() );
				if( user.getLoginId().compareTo("") == 0 ) {
					JOptionPane.showMessageDialog(f, "Tên người dùng hoặc mật khẩu sai");
				}
				else {
					System.out.println("Login type="+user.getType());
					if( user.getType().compareTo("Client") == 0 ) {
						Client client = db.getClient( user.getLoginId() );
						Bank_Account account = db.getAccount( user.getLoginId() );
						
						if ( Integer.valueOf(account.getStatus()) == 0 ) 
							JOptionPane.showMessageDialog(f, "Tài khoản đã bị đóng vĩnh viễn");
						else if( Integer.valueOf(account.getStatus()) == 1 ) {
							frame.remove(f);
							frame.repaint();
							frame.validate();
							openClientMenu(frame, client, account);
						}
						else if( Integer.valueOf(account.getStatus()) == 2 ) {
							JOptionPane.showMessageDialog(f, "Tài khoản này bị chặn nên bạn không thể đăng nhập");
						}
						else {
							JOptionPane.showMessageDialog(f, "Đăng nhập không thành công");
						}
					}
					else if( user.getType().compareTo("Manager") == 0 ) {
						Manager manager = new Manager( user.getName() );
						frame.remove(f);
						frame.repaint();
						frame.validate();
						openManagerMenu(frame, manager);
					}
					else if( user.getType().compareTo("Accountant") == 0 ) {
						Accountant accountant = new Accountant( user.getName() );
						frame.remove(f);
						frame.repaint();
						frame.validate();
						openAccountantMenu(frame, accountant);
					}
				}		
			}
		});
		remakeScreen(frame, f);
	}
	
	
	
	
	
	// Form đăng kí 1, nhận AccountNum và CIC từ người dùng.
	void open_Signup_form_1(JFrame frame, Login_Account user)
	{
		System.out.println("Chuyển sang trang Signup form_1 page");
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		
		remakeScreen(frame, f);
		
		// Sign Up Label
		JLabel lSignUp = new JLabel("Sign Up");
		lSignUp.setFont( lSignUp.getFont().deriveFont(30f) );
		lSignUp.setBounds(320,40,300, 40);
		f.add(lSignUp);
			
		//-----------------------------------------Creating Labels-------------------------------------		
		JLabel l_AccountNo = new JLabel("Enter Account#:");
		l_AccountNo.setBounds(200,110,100, 90);
		f.add(l_AccountNo);
		
		JLabel l_CIC = new JLabel("Enter the CIC:");
		l_CIC.setBounds(200, 180, 100, 90);
		f.add(l_CIC);
		
		//-----------------------------------------Creating Textfields---------------------------------------		
		JTextField tf_AccountNo = new JTextField();
		tf_AccountNo.setBounds(350, 140 ,200, 25);
		f.add(tf_AccountNo);
		
		JTextField tf_CIC = new JTextField();
		tf_CIC.setBounds(350,210,200, 25);
		f.add(tf_CIC);
		
		//-------------------------------------------Creating Buttons----------------------------------------		
		JButton btn_Verify_Account = new JButton("Verify Account");
		btn_Verify_Account.setBackground( new Color(0, 204, 153) );
		btn_Verify_Account.setForeground(Color.white);
		btn_Verify_Account.setBounds(290,280,150, 40);
		f.add(btn_Verify_Account);
		
		JButton btn_BACK = new JButton("BACK");
		btn_BACK.setBackground( new Color(0, 204, 153) );
		btn_BACK.setForeground(Color.white);
		btn_BACK.setBounds(50,400,100, 40);
		f.add(btn_BACK);
		
		// function executed when Back button is clicked
		btn_BACK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				openSignInForm(frame, user);
			}
		});
		
		// function executed when Verify Account Button is clicked
		btn_Verify_Account.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
					int verify_status = user.verify_account(tf_AccountNo.getText(), tf_CIC.getText());
					System.out.println("Verify Status Value: " + verify_status );
					if (verify_status == -1)
					{
						JOptionPane.showMessageDialog(f, "Không có tài khoản nào có thông tin này");
						frame.remove(f);
						frame.repaint();
						frame.validate();
						openSignInForm(frame, user); 
					}
					else if (verify_status == -2)
					{
						JOptionPane.showMessageDialog(f, "Tài khoản đăng nhập đã được tạo cho tài khoản này");
						System.out.println("Không thể tạo tài khoản đăng nhập vì đã có tài khoản đăng nhập được liên kết với tài khoản này");						
						frame.remove(f);
						frame.repaint();
						frame.validate();
						openSignInForm(frame, user); 
					}
					
					else if (verify_status == -3)
					{
						// System.out.println("The CIC matched with the client having client id=" + temp_client_id);
						open_Signup_form_2(frame, user, tf_AccountNo.getText());
					}
					else
					{
						JOptionPane.showMessageDialog(f, "Không tìm thấy tài khoản với CIC này");
						// **************************************
						System.out.println("CIC không phù hợp với bất kỳ client nào");
						frame.remove(f);
						frame.repaint();
						frame.validate();
						openSignInForm(frame, user);
					}
			}
		});
	}
			
	
	
	
	//Form đăng kí 2
	// open sign up form 2, getting user name and password
	void open_Signup_form_2(JFrame frame, Login_Account user, String acc_num)
	{
			System.out.println("Control Shifted to Signup form_2 page");
			JPanel f = new JPanel();
			f.setBackground(Color.white);
			
			remakeScreen(frame, f);
			
			// Sign Up Label
			JLabel lSignUp = new JLabel("Sign Up");
			lSignUp.setFont( lSignUp.getFont().deriveFont(30f) );
			lSignUp.setBounds(320,40,300, 40);
			f.add(lSignUp);
			
			//-------------------------------------------Creating labels-----------------------------------------------		
			JLabel l_username = new JLabel("Enter Username:");
			l_username.setBounds(200,100,100, 90);
			f.add(l_username);
			
			JLabel l_password = new JLabel("Enter Password:");
			l_password.setBounds(200, 160, 100, 90);
			f.add(l_password);
			
			JLabel l_password_2 = new JLabel("Confirm the password:");
			l_password_2.setBounds(200, 220, 150, 90);
			f.add(l_password_2);
			
			//---------------------------------------------Creating Textfileds------------------------------------------		
			JTextField tf_username = new JTextField();
			tf_username.setBounds(350, 130 ,200, 25);
			f.add(tf_username);
			
			JTextField tf_password = new JPasswordField();;
			tf_password.setBounds(350,190,200, 25);
			f.add(tf_password);
			
			JTextField tf_password_2 = new JPasswordField();
			tf_password_2.setBounds(350,250,200, 25);
			f.add(tf_password_2);
			
			//-----------------------------------------------Creating Buttons----------------------------------------		
			JButton btn_create_login_Account = new JButton("Create Login Account");
			btn_create_login_Account.setBackground( new Color(0, 204, 153) );
			btn_create_login_Account.setForeground(Color.white);
			btn_create_login_Account.setBounds(300,320,200, 40);
			f.add(btn_create_login_Account);
			
			JButton btn_BACK = new JButton("BACK");
			btn_BACK.setBackground( new Color(0, 204, 153) );
			btn_BACK.setForeground(Color.white);
			btn_BACK.setBounds(50,400,100, 40);
			f.add(btn_BACK);
				
			// function executed when back button is clicked
			btn_BACK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frame.remove(f);
					frame.repaint();
					frame.validate();
					open_Signup_form_1(frame, user);
				}
			});
			
			// function executed when Create Login Account Button is clicked
			btn_create_login_Account.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					int signup_status = user.signup(tf_username.getText(), tf_password.getText(), tf_password_2.getText(), acc_num);
					if (signup_status == -1)
					{
						System.out.println("2 mật khẩu không trùng nhau");
						JOptionPane.showMessageDialog(f, "Mật khẩu đã nhập không khớp");
						frame.remove(f);
						frame.repaint();
						frame.validate();
						open_Signup_form_2(frame, user, acc_num);
					}
					else if (signup_status == -2)
					{
						System.out.println("Đã xảy ra lỗi khi tạo tài khoản đăng nhập vì login id được trả về là -1");
						JOptionPane.showMessageDialog(f, "Thật không may, tài khoản đăng nhập không thể được thực hiện");
						frame.remove(f);
						frame.repaint();
						frame.validate();
						openSignInForm(frame, user);
					}
					else 
					{
						JOptionPane.showMessageDialog(f, "Tài khoản đăng nhập của bạn đã được tạo thành công. Làm ơn đăng nhập để tiếp tục");
						frame.remove(f);
						frame.repaint();
						frame.validate();
						openSignInForm(frame, user);
					}
				}
			});
	}
	
	
	
	//------------------------------------------------------
	//            Các chức năng liên quan đến Client        |
	//------------------------------------------------------
	
	
	// this function display the menu screen for client
	void openClientMenu(JFrame frame, Client client, Bank_Account account) {
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		
		// Log Out button
		JButton btnLogOut = new JButton("Sign Out");
		btnLogOut.setFont( btnLogOut.getFont().deriveFont(14f) );
		btnLogOut.setBackground( new Color(0, 204, 153) );
		btnLogOut.setForeground(Color.white);
		//btnLogOut.setBounds(580,58,100, 30);
		btnLogOut.setBounds(650,30,100, 30);
		btnLogOut.setFocusPainted(false);
		f.add(btnLogOut);
		// function to be executed when Log Out Button is clicked
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Login_Account user = new Login_Account ();
				openSignInForm(frame, user);
			}
		});
		
		// User Name label
		JLabel lUser =  new JLabel(client.getFName() + " " + client.getLName());
		lUser.setFont( lUser.getFont().deriveFont(30f) );
		lUser.setBounds(100,50,250, 40);
		f.add(lUser);
		
		// Designation label
		JLabel lDesg = new JLabel("User");
		lDesg.setBounds(100,80,100, 40);
		f.add(lDesg);
		// Account Info Button
		JButton btnAccInfo = new JButton("Account Info");
		btnAccInfo.setBounds(100,180,150, 40);
		btnAccInfo.setBackground( new Color(0, 204, 153) );
		btnAccInfo.setForeground(Color.white);
		f.add(btnAccInfo);
		
		// function to be executed when Account Info Button is clicked
		btnAccInfo.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				open_info_page(frame, client, account);
			}
		});
		// Cardless Cash WithDrawal Button
		JButton btnCCWithdrawal = new JButton("Cardless Cash Withdrawal");
		btnCCWithdrawal.setBounds(300,180,200, 40);
		btnCCWithdrawal.setBackground( new Color(0, 204, 153) );
		btnCCWithdrawal.setForeground(Color.white);
		f.add(btnCCWithdrawal);
		
		// function to be executed when Cardless Cash Withdrawal Button is clicked
		btnCCWithdrawal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				open_cardless_page_1(frame, client, account);
			}
		});
		
		// Transfer Money Button
		JButton btnTransferMoney = new JButton("Transfer Money");
		btnTransferMoney.setBounds(550,180,150, 40);
		btnTransferMoney.setBackground( new Color(0, 204, 153) );
		btnTransferMoney.setForeground(Color.white);
		f.add(btnTransferMoney);
	
		// EStatement Button
		JButton btnEStatement = new JButton("View EStatement");
		btnEStatement.setBounds(100,250,150, 40);
		btnEStatement.setBackground( new Color(0, 204, 153) );
		btnEStatement.setForeground(Color.white);
		f.add(btnEStatement);
	
		// function to be executed when transfer money button is clicked
		btnTransferMoney.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				frame.remove(f);
				frame.repaint();
				frame.validate();
			    openTransferMoneyForm(frame, client, account);
			}
		});
		
		// function to be executed when View EStatement(Báo cáo điện tử) Button is clicked
		btnEStatement.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				frame.remove(f);
				frame.repaint();
				frame.validate();
			    eStatement2(frame, client, account);
			}
		});
		
		//change password 
		JButton btnChangePass = new JButton("Change Password");
		btnChangePass.setBounds(300, 250, 200, 40);
		btnChangePass.setBackground( new Color(0, 204, 153) );
		btnChangePass.setForeground(Color.white);
		f.add(btnChangePass);
		// funtion when change Password Button is click
		btnChangePass.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				frame.remove(f);
				frame.repaint();
				frame.validate();
			    open_change_password_form(frame, client, account);
			}
		});	
		
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}
	
	
	
	
	
	// this function displays the clients account details on screen
	void open_info_page(JFrame frame, Client client, Bank_Account account)
	{
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		remakeScreen(frame, f);

		// Account Info Label
		JLabel lAccInfo = new JLabel("Account Info");
		lAccInfo.setHorizontalAlignment(JLabel.CENTER);
		lAccInfo.setFont( lAccInfo.getFont().deriveFont(30f) );
		lAccInfo.setBounds(300,40,200, 40);
		f.add(lAccInfo);
		
		//							Account Holder Name
		JLabel l_name = new JLabel("Account Holder Name");
		l_name.setHorizontalAlignment(JLabel.CENTER);
		l_name.setBounds(300, 70, 200, 90);
		f.add(l_name);
		JTextField tf_name = new JTextField();
		tf_name.setHorizontalAlignment(JTextField.CENTER);
		tf_name.setBounds(300, 130 ,200, 25);
		tf_name.setText(client.getFName() + " " + client.getLName());
		tf_name.setEditable(false);
		f.add(tf_name);
		
		//							Account Number
		JLabel l_acc_num = new JLabel("Account Number:");
		l_acc_num.setHorizontalAlignment(JLabel.CENTER);
		l_acc_num.setBounds(150, 160, 200, 90);
		f.add(l_acc_num);
		JTextField tf_acc_num = new JTextField();
		tf_acc_num.setHorizontalAlignment(JTextField.CENTER);
		tf_acc_num.setBounds(150, 220 ,200, 25);
		tf_acc_num.setText( account.getAccountNum() );
		tf_acc_num.setEditable(false);
		f.add(tf_acc_num);

		//							Account Type
		JLabel l_acc_type = new JLabel("Account Type");
		l_acc_type.setHorizontalAlignment(JLabel.CENTER);
		l_acc_type.setBounds(400, 160, 200, 90);
		f.add(l_acc_type);
		JTextField tf_acc_type = new JTextField(account.getType() );
		tf_acc_type.setHorizontalAlignment(JTextField.CENTER);
		tf_acc_type.setBounds(400, 220 ,200, 25);
		tf_acc_type.setEditable(false);
		f.add(tf_acc_type);
		
		//							Account Current Balance
		JLabel l_balance = new JLabel("Account Current Balance");
		l_balance.setHorizontalAlignment(JLabel.CENTER);
		l_balance.setBounds(150, 250, 200, 90);
		f.add(l_balance);
		JTextField tf_balance = new JTextField();
		tf_balance.setHorizontalAlignment(JTextField.CENTER);
		tf_balance.setBounds(150, 310 ,200, 25);
		tf_balance.setText( account.getBalance() );
		tf_balance.setEditable(false);;
		f.add(tf_balance);
		
		//							Account Opening Date
		JLabel l_open_date = new JLabel("Account Opening Date");
		l_open_date.setHorizontalAlignment(JLabel.CENTER);
		l_open_date.setBounds(400, 250, 200, 90);
		f.add(l_open_date);
		JTextField tf_open_date = new JTextField();
		tf_open_date.setHorizontalAlignment(JTextField.CENTER);
		tf_open_date.setBounds(400, 310 ,200, 25);
		tf_open_date.setText( account.getOpeningDate() );
		tf_open_date.setEditable(false);
		f.add(tf_open_date);
		
		JButton btn_BACK = new JButton("Main Menu");
		btn_BACK.setBackground( new Color(0, 204, 153) );
		btn_BACK.setForeground(Color.white);
		btn_BACK.setBounds(50,400,100, 30);
		f.add(btn_BACK);
		
		btn_BACK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				openClientMenu(frame, client, account);
			}
		});
		
		JButton btn_sign_out = new JButton("Sign Out");
		btn_sign_out.setBackground( new Color(0, 204, 153) );
		btn_sign_out.setForeground(Color.white);
		btn_sign_out.setBounds(650,30,100, 30);
		f.add(btn_sign_out);
		
		btn_sign_out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Login_Account user = new Login_Account();
				openSignInForm(frame, user);
			}
		});
	}
	
	
	
	
	
	// cardless cash withdrawal screen
	void open_cardless_page_1(JFrame frame, Client client, Bank_Account account)
	{
		System.out.println("Điều khiển được chuyển sang Cardless form_1 page");
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		
		remakeScreen(frame, f);
		
		// Cardless Cash Withdrawal Label
		JLabel lCCWithdrawal = new JLabel("Cardless Cash Withdrawal");
		lCCWithdrawal.setFont( lCCWithdrawal.getFont().deriveFont(30f) );
		lCCWithdrawal.setBounds(200,40,400, 40);
		f.add(lCCWithdrawal);
		
		//-------------------------------------------Creating labels-----------------------------------------------		
		JLabel l_card_no = new JLabel("Enter Your Card No:");
		l_card_no.setBounds(150,100,180, 90);
		f.add(l_card_no);
		
		JLabel l_amount = new JLabel("Enter the amount of withdrawal:");
		l_amount.setBounds(150, 160, 180, 90);
		f.add(l_amount);
		
		JLabel l_temp_pin = new JLabel("Pin Code:");
		l_temp_pin.setBounds(150, 220, 180, 90);
		f.add(l_temp_pin);
		
		//---------------------------------------------Creating Textfileds------------------------------------------		
		JTextField tf_card_no = new JTextField();
		tf_card_no.setBounds(380, 130 ,200, 25);
		f.add(tf_card_no);
		
		JTextField tf_amount = new JTextField();
		tf_amount.setBounds(380,190,200, 25);
		f.add(tf_amount);
		
		JTextField tf_temp_pin = new JTextField();
		tf_temp_pin.setBounds(380,250,200, 25);
		f.add(tf_temp_pin);
		
		//-----------------------------------------------Creating Buttons----------------------------------------		
		JButton btn_create_cardless_entry = new JButton("Generate transaction OTP"); //Tạo OTP giao dịch
		btn_create_cardless_entry.setBackground( new Color(0, 204, 153) );
		btn_create_cardless_entry.setForeground(Color.white);
		btn_create_cardless_entry.setBounds(300,300,200, 40);
		f.add(btn_create_cardless_entry);
		
		JButton btn_sign_out = new JButton("Sign Out");
		btn_sign_out.setBackground( new Color(0, 204, 153) );
		btn_sign_out.setForeground(Color.white);
		btn_sign_out.setBounds(650,30,100, 30);
		f.add(btn_sign_out);
		
		// function to be executed when Sign Out Button is clicked
		btn_sign_out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Login_Account user = new Login_Account();
				openSignInForm(frame, user);
			}
		});
		
		// Main Menu Button
		JButton btn_mm = new JButton("Main Menu");
		btn_mm.setBackground( new Color(0, 204, 153) );
		btn_mm.setForeground(Color.white);
		btn_mm.setBounds(50,400,100, 30);
		f.add(btn_mm);
		
		// function to be executed when Main Menu Button is clicked
		btn_mm.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				openClientMenu(frame, client, account);
			}
		});
		
		// function to be executed when
		btn_create_cardless_entry.addActionListener(new ActionListener () {
			public void actionPerformed (ActionEvent e) {
	
				if ( account.getCardNum().compareTo(tf_card_no.getText()) != 0 )
				{
					System.out.println("Không có thẻ nào được liên kết với tài khoản này vì số thẻ nhận được là 0");					
					JOptionPane.showMessageDialog(f, "Bạn không có bất kỳ thẻ nào được liên kết với tài khoản này");
					frame.remove(f);
					frame.repaint();
					frame.validate();
					open_cardless_page_1(frame, client, account);
				}
				else {
					String cardless_status = client.do_card_cash_withdrawal(account, tf_amount.getText(), tf_temp_pin.getText());
					if ( cardless_status.compareTo("a")==0 )
					{
						JOptionPane.showMessageDialog(f, "Thẻ của bạn hiện đang bị chặn");
						frame.remove(f);
						frame.repaint();
						frame.validate();
						open_cardless_page_1(frame, client, account);
					}
					else if( cardless_status.compareTo("b")==0 )
					{
						
						JOptionPane.showMessageDialog(f, "Bạn không có đủ số dư để hoàn thành giao dịch này");
						frame.remove(f);
						frame.repaint();
						frame.validate();
						open_cardless_page_1(frame, client, account);
					}
					else if (cardless_status.compareTo("c")==0)
					{
						JOptionPane.showMessageDialog(f, "Yêu cầu rút tiền không cần thẻ của bạn không thể hoàn thành");
						frame.remove(f);
						frame.repaint();
						frame.validate();
						open_cardless_page_1(frame, client, account);
					}
					else 
					{
						//					System.out.println("You have enough balance to carry out this transaction");
						System.out.println("Yêu cầu rút tiền không cần thẻ của bạn đã được hoàn tất");
						JOptionPane.showMessageDialog(f, "Yêu cầu rút tiền không cần thẻ của bạn đã được hoàn thành và OTP của bạn là "+String.valueOf(cardless_status));
						account.updateBalance();
						frame.remove(f);
						frame.repaint();
						frame.validate();
						openClientMenu(frame, client, account);
					}
				}
			}
		});
	}
	
	
	
	
	
	// money transfer screen
	void openTransferMoneyForm(JFrame frame, Client client, Bank_Account account) {
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		
		// Transfer Money Label
		JLabel l_hcreate = new JLabel("Transfer Money");
		l_hcreate.setFont( l_hcreate.getFont().deriveFont(30f) );
		l_hcreate.setBounds(300,60,300, 40);
		f.add(l_hcreate);
		
		// Receiver Account Number Label
		JLabel l_rAccNum = new JLabel("Reciever Account Number: ");
		l_rAccNum.setBounds(200,140,200, 40);
		f.add(l_rAccNum);
		
		// Amount Label
		JLabel l_amount = new JLabel("Amount: ");
		l_amount.setBounds(200,190,100, 40);
		f.add(l_amount);
	
		// Receiver Account Number Text Field
		JTextField tf_rAccNum = new JTextField();
		tf_rAccNum.setBounds(400,150,180, 25);
		f.add(tf_rAccNum);
		
		// Amount Text field
		JTextField tf_amount = new JTextField();
		tf_amount.setBounds(400,200,180, 25);
		f.add(tf_amount);
		
		// Transfer Button
		JButton btn_Transfer = new JButton("Transfer");
		btn_Transfer.setBackground( new Color(0, 204, 153) );
		btn_Transfer.setForeground(Color.white);
		btn_Transfer.setBounds(300,270,180, 40);
		f.add(btn_Transfer);
		
		// Main Menu Button
		JButton btn_mm = new JButton("Main Menu");
		btn_mm.setBackground( new Color(0, 204, 153) );
		btn_mm.setForeground(Color.white);
		btn_mm.setBounds(50,400,100, 30);
		f.add(btn_mm);
				
		// function to be executed when Transfer Button is clicked
		btn_Transfer.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				if( tf_rAccNum.getText().compareTo( account.getAccountNum() ) == 0  )
						JOptionPane.showMessageDialog(f, "Bạn không thể nhập số tài khoản của chính mình");
				else {
					int r = client.tranferMoney( tf_rAccNum.getText(), Integer.parseInt( tf_amount.getText() ));
					if( r == 1 )
						JOptionPane.showMessageDialog(f, "Không tìm thấy tài khoản người nhận, bị chặn hoặc đóng");
					else if( r == 2)
						JOptionPane.showMessageDialog(f, "Bạn có số dư thấp");
					else if( r == 3 ) {
						JOptionPane.showMessageDialog(f, "Chuyển tiền thành công");
						account.updateBalance();
						frame.remove(f);
						frame.repaint();
						frame.validate();
					    openClientMenu(frame, client, account);
					}
					else { 
						
					}
				}
			}
		});
		
		// function to be executed when Main Menu Button is clicked
		btn_mm.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				openClientMenu(frame, client, account);
			}
		});
		
		// Sign Out Button
		JButton btn_sign_out = new JButton("Sign Out");
		btn_sign_out.setBackground( new Color(0, 204, 153) );
		btn_sign_out.setForeground(Color.white);
		btn_sign_out.setBounds(650,30,100, 30);
		f.add(btn_sign_out);
		
		btn_sign_out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Login_Account user = new Login_Account();
				openSignInForm(frame, user);
			}
		});
		
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);	
	}
	
	
	
	
	
	// view e statement screen for client
	void eStatement2(JFrame frame, Client client, Bank_Account account) {
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		
		// E-Statement Label
		JLabel lEStatement = new JLabel("E-Statement");
		lEStatement.setFont( lEStatement.getFont().deriveFont(30f) );
		lEStatement.setBounds(50,50,250, 40);
		f.add(lEStatement);
		
		// From Date Label
		JLabel lFrom = new JLabel("From   ( DD : MM : YY )");
		lFrom.setBounds(50,100,150, 40);
		f.add(lFrom);
		
		// To Date Label
		JLabel lTo = new JLabel("To   ( DD : MM : YY )");
		lTo.setBounds(450,100,150, 40);
		f.add(lTo);
		
		// Day, Month and Year for From Date
		SpinnerModel fromDayValues =  new SpinnerNumberModel(21, 1, 31, 1);  
	    JSpinner fromDaySpinner = new JSpinner(fromDayValues);   
	    fromDaySpinner.setBounds(50,130,40,25);    
	    f.add(fromDaySpinner); 
	    SpinnerModel fromMonthValues =  new SpinnerNumberModel(12, 1, 12, 1);  
	    JSpinner fromMonthSpinner = new JSpinner(fromMonthValues);   
	    fromMonthSpinner.setBounds(100,130,40,25);    
	    f.add(fromMonthSpinner); 
	    SpinnerModel fromYearValues =  new SpinnerNumberModel(2020, 2018, 2023, 1);  
	    JSpinner fromYearSpinner = new JSpinner(fromYearValues);   
	    fromYearSpinner.setBounds(150,130,55,25);    
	    f.add(fromYearSpinner); 
	    
	    // Day, Month and Year for From Date
 		SpinnerModel toDayValues =  new SpinnerNumberModel(30, 1, 31, 1);  
 	    JSpinner toDaySpinner = new JSpinner(toDayValues);   
 	    toDaySpinner.setBounds(450,130,40,25);    
 	    f.add(toDaySpinner); 
 	    SpinnerModel toMonthValues =  new SpinnerNumberModel(12, 1, 12, 1);  
 	    JSpinner toMonthSpinner = new JSpinner( toMonthValues);   
 	    toMonthSpinner.setBounds(500,130,40,25);    
 	    f.add(toMonthSpinner); 
 	    SpinnerModel toYearValues =  new SpinnerNumberModel(2020, 2018, 2023, 1);  
 	    JSpinner toYearSpinner = new JSpinner(toYearValues);   
 	    toYearSpinner.setBounds(550,130,55,25);    
 	    f.add(toYearSpinner); 
 	    
 	    // E-Statement Button
 		JButton btnEStatement = new JButton("Get E-Statement");
		btnEStatement.setBackground( new Color(0, 204, 153) );
		btnEStatement.setForeground(Color.white);
		btnEStatement.setBounds(50,180,150, 30);
		f.add(btnEStatement);
 	    
		// Download Button
		JButton btnDownload = new JButton("Download");
		btnDownload.setBackground( new Color(0, 204, 153) );
		btnDownload.setForeground(Color.white);
		btnDownload.setBounds(450,180,100, 30);
		f.add(btnDownload);
		btnDownload.setEnabled(false);
		   
		// creating table for transaction data
		DefaultTableModel tableModel = new DefaultTableModel();
		JTable table=new JTable(tableModel);   
		tableModel.addColumn("Serial No");
		tableModel.addColumn("Amount");
		tableModel.addColumn("Type");
		tableModel.addColumn("Date");
		tableModel.addColumn("Time");
		tableModel.addColumn("Account Number");
		tableModel.addColumn("Reciever Account Number");
		tableModel.addColumn("Cheque No");
		table.setEnabled(false);
		JScrollPane sp=new JScrollPane(table);
		sp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
		sp.setBounds(50,230,700,150);  
		f.add(sp);
		sp.setVisible(true);
		
		// function to be executed when E Statement Button is clicked
		btnEStatement.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.setRowCount(0);
				//sp.setVisible(false);
				
				String From = String.valueOf(fromYearSpinner.getValue()) +"-"+ String.valueOf(fromMonthSpinner.getValue()) +"-"+ String.valueOf(fromDaySpinner.getValue());
				String To = String.valueOf(toYearSpinner.getValue()) +"-"+ String.valueOf(toMonthSpinner.getValue()) +"-"+ String.valueOf(toDaySpinner.getValue());
			
				List<Transaction_History> list = client.getTransactions( account.getAccountNum(), From, To);
				if( list.size() > 0 ) {
					for( Transaction_History th: list) {
						tableModel.addRow(new Object[] { th.getSerialNo(), th.getAmount(), th.getType(), th.getDate(), th.getTime(), th.getAccountNumber(), th.getRecvAccNum(), th.getChequeNum() });
					}	
					tableModel.fireTableDataChanged();
					sp.setVisible(true);
					//btnEStatement.setEnabled(false);
					btnDownload.setEnabled(true);
				}
				else {
					btnDownload.setEnabled(false);
				}
			}
		});

		// function to be executed when Download Button is clicked
//		btnDownload.addActionListener( new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				String From = String.valueOf(fromYearSpinner.getValue()) +"-"+ String.valueOf(fromMonthSpinner.getValue()) +"-"+ String.valueOf(fromDaySpinner.getValue());
//				String To = String.valueOf(toYearSpinner.getValue()) +"-"+ String.valueOf(toMonthSpinner.getValue()) +"-"+ String.valueOf(toDaySpinner.getValue());
//			
//				DB_Handler db = new DB_Handler();
//				int r = db.createPDF(client, account, From, To);
//				if( r == 1 ) 
//					JOptionPane.showMessageDialog(f, "E-Statement is downloaded");
//				else
//					JOptionPane.showMessageDialog(f, "Error in downloading the e-statement");
//				frame.remove(f);
//				frame.repaint();
//				frame.validate();
//				//openAccountantMenu(frame, accountant);
//			}
//		});
		
		// Main Menu Button
		JButton btnMM = new JButton("Main Menu");
		btnMM.setBackground( new Color(0, 204, 153) );
		btnMM.setForeground(Color.white);
		btnMM.setBounds(50,400,100, 30);
		f.add(btnMM);
		
		// function to be executed when Main Menu Button is clicked
		btnMM.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				openClientMenu(frame, client, account);
			}
		});
		
		// Sign Out Button
		JButton btn_sign_out = new JButton("Sign Out");
		btn_sign_out.setBackground( new Color(0, 204, 153) );
		btn_sign_out.setForeground(Color.white);
		btn_sign_out.setBounds(650,30,100, 30);
		f.add(btn_sign_out);
		
		btn_sign_out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Login_Account user = new Login_Account();
				openSignInForm(frame, user);
			}
		});
		
		// displaying the new panel on frame	
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);		
	}

	
	
	
	
	// client screen to change password of his login id
	void open_change_password_form(JFrame frame, Client client, Bank_Account account)
	{
		System.out.println("Điều khiển được chuyển sang Thay đổi password form");
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		
		remakeScreen(frame, f);
		
		// Change Password
		JLabel lChgPass = new JLabel("Change Password");
		lChgPass.setFont( lChgPass.getFont().deriveFont(30f) );
		lChgPass.setBounds(250,50,300, 40);
		f.add(lChgPass);
		
		//-------------------------------------------Creating labels-----------------------------------------------		
		JLabel l_curr_pass = new JLabel("Enter Current Password:");
		l_curr_pass.setBounds(150,100,200, 90);
		f.add(l_curr_pass);
		
		JLabel l_new_pass_1 = new JLabel("Enter the new Password:");
		l_new_pass_1.setBounds(150, 150, 200, 90);
		f.add(l_new_pass_1);
		
		JLabel l_new_pass_2 = new JLabel("Confirm the new Password:");
		l_new_pass_2.setBounds(150, 200, 200, 90);
		f.add(l_new_pass_2);
		
		//---------------------------------------------Creating Textfileds------------------------------------------		
		JPasswordField tf_curr_pass = new JPasswordField();
		tf_curr_pass.setBounds(350, 130 ,200, 25);
		f.add(tf_curr_pass);
		
		JPasswordField tf_new_pass_1 = new JPasswordField();
		tf_new_pass_1.setBounds(350,180,200, 25);
		f.add(tf_new_pass_1);
		
		JPasswordField tf_new_pass_2 = new JPasswordField();
		tf_new_pass_2.setBounds(350,230,200, 25);
		f.add(tf_new_pass_2);
		
		//-----------------------------------------------Creating Buttons----------------------------------------		
		JButton btn_change_password = new JButton("Change Password");
		btn_change_password.setBackground( new Color(0, 204, 153) );
		btn_change_password.setForeground(Color.white);
		btn_change_password.setBounds(320,280,150, 40);
		f.add(btn_change_password);
		
		// Main Menu Button
		JButton btnMM = new JButton("Main Menu");
		btnMM.setBackground( new Color(0, 204, 153) );
		btnMM.setForeground(Color.white);
		btnMM.setBounds(50,400,100, 30);
		f.add(btnMM);
		
		// function to be executed when Main Menu Button is clicked
		btnMM.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				openClientMenu(frame, client, account);
			}
		});
		
		// function to be executed when Change Password Button is clicked
		btn_change_password.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int change_pwd_status = client.change_password(tf_curr_pass.getText(), tf_new_pass_1.getText(), tf_new_pass_2.getText(), account.getAccountNum());
		
				if (change_pwd_status == -1)
				{
					JOptionPane.showMessageDialog(f, "Hệ thống không tìm thấy tài khoản đăng nhập");
					frame.remove(f);
					frame.repaint();
					frame.validate();
					open_change_password_form(frame, client, account);
				}
				else if ( change_pwd_status == -2 )
				{
					System.out.println("Mật khẩu hiện tại không khớp với mật khẩu trong hệ thống");
					JOptionPane.showMessageDialog(f, "Mật khẩu hiện tại không khớp với mật khẩu trong hệ thống");
					frame.remove(f);
					frame.repaint();
					frame.validate();
					open_change_password_form(frame, client, account);
				}
				else if (change_pwd_status == -3)
				{
					System.out.println("Các mật khẩu mới không khớp với nhau");
					JOptionPane.showMessageDialog(f, "Các mật khẩu mới không khớp với nhau");
					frame.remove(f);
					frame.repaint();
					frame.validate();
					open_change_password_form(frame, client, account);
				}
				else
				{
					JOptionPane.showMessageDialog(f, "Bạn đã thay đổi thành công mật khẩu của bạn. Bạn sẽ phải Đăng nhập lại để tiếp tục");
					frame.remove(f);
					frame.repaint();
					frame.validate();
					Login_Account user = new Login_Account();
					openSignInForm(frame, user);
				}
			}
		});
	}
	
	
	
	//----------------------------------------------------
	//              Màn hình liên quan đến người quản lý             |
	//----------------------------------------------------
	
	
	// menu screen for manager
	void openManagerMenu(JFrame frame, Manager manager) {
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		
		// Sign Out Button
		JButton btn_sign_out = new JButton("Sign Out");
		btn_sign_out.setBackground( new Color(0, 204, 153) );
		btn_sign_out.setForeground(Color.white);
		btn_sign_out.setBounds(650,30,100, 30);
		f.add(btn_sign_out);
		
		btn_sign_out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Login_Account user = new Login_Account();
				openSignInForm(frame, user);
			}
		});
		
		// User Name label
		JLabel lUser = new JLabel(manager.getName());
		lUser.setFont( lUser.getFont().deriveFont(30f) );
		lUser.setBounds(100,50,300, 40);
		f.add(lUser);
		
		// Designation label
		JLabel lDesg = new JLabel("Manager");
		//lDesg.setFont( lDesg.getFont().deriveFont(30f) );
		lDesg.setBounds(100,80,100, 40);
		f.add(lDesg);
		
		// Create Account Button
		JButton btnCreateAcc = new JButton("Create Account");
		btnCreateAcc.setBounds(100,180,150, 40);
		btnCreateAcc.setBackground( new Color(0, 204, 153) );
		btnCreateAcc.setForeground(Color.white);
		f.add(btnCreateAcc);
		
		// function to be executed when Create Account Button is clicked
		btnCreateAcc.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				frame.remove(f);
				frame.repaint();
				frame.validate();
			    openCreateAccountForm(frame, manager);
			}
		});
			
		// Block/Unblock Account Button
		JButton btnBlockAcc = new JButton("Block/Unblock Account");
		btnBlockAcc.setBounds(300,180,200, 40);
		btnBlockAcc.setBackground( new Color(0, 204, 153) );
		btnBlockAcc.setForeground(Color.white);
		f.add(btnBlockAcc);
		
		
		btnBlockAcc.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				frame.remove(f);
				frame.repaint();
				frame.validate();
			    open_block_unblock_account_page(frame, manager);
			}
		});
		
		
		// Close Account Button
		JButton btnCloseAcc = new JButton("Close Account");
		btnCloseAcc.setBounds(550,180,150, 40);
		btnCloseAcc.setBackground( new Color(0, 204, 153) );
		btnCloseAcc.setForeground(Color.white);
		f.add(btnCloseAcc);
		
		btnCloseAcc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				open_close_account_page(frame, manager);
			}
		});
		
		// Search Account Button
		JButton btnSearchAcc = new JButton("Search Account");
		btnSearchAcc.setBounds(100,250,150, 40);
		btnSearchAcc.setBackground( new Color(0, 204, 153) );
		btnSearchAcc.setForeground(Color.white);
		f.add(btnSearchAcc);
		
		// function to be executed when Search Account button is clicked
		btnSearchAcc.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				searchAccount(frame, manager);
			}
		});
		
		
		// Manage Card Button
		JButton btnManageCard = new JButton("Manage Credit/Debit Card");
		btnManageCard.setBounds(300,250,200, 40);
		btnManageCard.setBackground( new Color(0, 204, 153) );
		btnManageCard.setForeground(Color.white);
		f.add(btnManageCard);
		
		btnManageCard.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				open_manage_card_page(frame, manager);
			}
		});
		
		//  Update Client Info Button
		JButton btnUpdateInfo = new JButton("Update Client Info");
		btnUpdateInfo.setBounds(100,320,150, 40);
		btnUpdateInfo.setBackground( new Color(0, 204, 153) );
		btnUpdateInfo.setForeground(Color.white);
		f.add(btnUpdateInfo);
		
		// function to be executed when Update Info Button is clicked
		btnUpdateInfo.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				frame.remove(f);
				frame.repaint();
				frame.validate();
			    updateClientInfo(frame, manager);
			}
		});
		// Add Account From File
		JButton btnAccountFromFile = new JButton("Add Account From File");
		btnAccountFromFile.setBounds(300,320,200, 40);
		btnAccountFromFile.setBackground( new Color(0, 204, 153) );
		btnAccountFromFile.setForeground(Color.white);
		f.add(btnAccountFromFile);
		
		// function to be executed when Add Account From File button is clicked
		btnAccountFromFile.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				AccountsFromFile AccFF = new AccountsFromFile();
				String fileName = chooseFile(); // Đường dẫn tới file chứa dữ liệu cần đọc
				if(!fileName.equals("-1"))    AccFF.importAccountsFromFile(fileName); //nếu đã chọn file thì mới thực hiện tiếp
				}
			
			
		});
	
		
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}
	  public static String chooseFile() {
	        JFileChooser fileChooser = new JFileChooser();
	        int option = fileChooser.showOpenDialog(null);
	        while (option != JFileChooser.APPROVE_OPTION) {
	            int result = JOptionPane.showConfirmDialog(null, "Bạn phải chọn một file nếu muốn thêm dữ liệu. Bạn có muốn tiếp tục không?", "Chọn file", JOptionPane.YES_NO_OPTION);
	            if (result == JOptionPane.YES_OPTION) {
	                option = fileChooser.showOpenDialog(null);
	            } else {
	                return "-1";
	            }
	        }
	        File file = fileChooser.getSelectedFile();
	        String filePath = file.getAbsolutePath();
	        return filePath;
	    }
	
	
	
	
	// screen to create a new account
	public void openCreateAccountForm(JFrame frame, Manager manager) {
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		 
		// Create Account Label
		JLabel l_hcreate = new JLabel("Create Account");
		l_hcreate.setFont( l_hcreate.getFont().deriveFont(30f) );
		l_hcreate.setBounds(300,40,300, 40);
		f.add(l_hcreate);
		
		// Account Fields Labels
		JLabel l_fName = new JLabel("First Name: ");
		l_fName.setBounds(100,100,100, 40);
		f.add(l_fName);
		JLabel l_lName = new JLabel("Last Name: ");
		l_lName.setBounds(400,100,100, 40);
		f.add(l_lName);
		JLabel l_fatherName = new JLabel("Father Name: ");
		l_fatherName.setBounds(100,150,100, 40);
		f.add(l_fatherName);
		JLabel l_motherName = new JLabel("Mother Name: ");
		l_motherName.setBounds(400,150,100, 40);
		f.add(l_motherName);
		JLabel l_cic = new JLabel("CIC: ");
		l_cic.setBounds(100,200,100, 40);
		f.add(l_cic);
		JLabel l_dob = new JLabel("Date of Birth: ");
		l_dob.setBounds(400,200,100, 40);
		f.add(l_dob);
		JLabel l_phone = new JLabel("Phone: ");
		l_phone.setBounds(100,250,100, 40);
		f.add(l_phone);
		JLabel l_email = new JLabel("Email: ");
		l_email.setBounds(400,250,100, 40);
		f.add(l_email);
		JLabel l_address = new JLabel("Address: ");
		l_address.setBounds(100,300,100, 40);
		f.add(l_address);
		JLabel l_acc_type = new JLabel("Account Type: ");
		l_acc_type.setBounds(400,300,100, 40);
		f.add(l_acc_type);
		
		// Account Text Fields
		JTextField tf_fName = new JTextField();
		tf_fName.setBounds(200,100,180, 25);
		f.add(tf_fName);
		JTextField tf_lName = new JTextField();
		tf_lName.setBounds(500,100,180, 25);
		f.add(tf_lName);
		JTextField tf_fatherName = new JTextField();
		tf_fatherName.setBounds(200,150,180, 25);
		f.add(tf_fatherName);
		JTextField tf_motherName = new JTextField();
		tf_motherName.setBounds(500,150,180, 25);
		f.add(tf_motherName);
		JTextField tf_cic = new JTextField();
		tf_cic.setBounds(200,200,180, 25);
		f.add(tf_cic);
		JTextField tf_dob = new JTextField();
		tf_dob.setBounds(500,200,180, 25);
		f.add(tf_dob);
		JTextField tf_phone = new JTextField();
		tf_phone.setBounds(200,250,180, 25);
		f.add(tf_phone);
		JTextField tf_email = new JTextField();
		tf_email.setBounds(500,250,180, 25);
		f.add(tf_email);
		JTextField tf_address = new JTextField();
		tf_address.setBounds(200,300,180, 25);
		f.add(tf_address);
		
		// Account Type
		String[] types = {"Current", "Saving" };
		JComboBox jcb_types = new JComboBox(types);
		jcb_types.setBounds(500,300,180, 25);
		f.add(jcb_types);
			
		// Create Button
		JButton btn_create = new JButton("Create");
		btn_create.setBackground( new Color(0, 204, 153) );
		btn_create.setForeground(Color.white);
		btn_create.setBounds(350,365,100, 40);
		f.add(btn_create);
	
		// function to be executed when Create Button is clicked
		btn_create.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client newClient = new Client( "",
						tf_fName.getText(), tf_lName.getText(), tf_fatherName.getText(), tf_motherName.getText(),
						tf_cic.getText(), tf_dob.getText(), tf_phone.getText(), tf_email.getText(),
						tf_address.getText()
						);
				int res =  manager.createAccount(newClient, jcb_types.getSelectedItem().toString());
				if( res == 1 ) {
					String id_ = manager.getAccNum( tf_cic.getText() );
					String msg = "Tài khoản đã được tạo";
					if( id_.compareTo("") != 0 )
						msg += " với số tài khoản là: "+id_;
					JOptionPane.showMessageDialog(f, msg);
					frame.remove(f);
					frame.repaint();
					frame.validate();
				    openManagerMenu(frame, manager);
				}
				else if( res == 2 )
					JOptionPane.showMessageDialog(f, "Tồn tại tài khoản có số CIC này");
				else
					JOptionPane.showMessageDialog(f, "Tạo tài khoản không thành công");			
			}
		});
		
		// Main Menu Button
		JButton btn_mm = new JButton("Main Menu");
		btn_mm.setBackground( new Color(0, 204, 153) );
		btn_mm.setForeground(Color.white);
		btn_mm.setBounds(50,400,100, 30);
		f.add(btn_mm);
		
		// function to be executed when Main Menu Button is clicked
		btn_mm.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				openManagerMenu(frame, manager);
			}
		});
		
		// Sign Out Button
		JButton btn_sign_out = new JButton("Sign Out");
		btn_sign_out.setBackground( new Color(0, 204, 153) );
		btn_sign_out.setForeground(Color.white);
		btn_sign_out.setBounds(650,30,100, 30);
		f.add(btn_sign_out);
		
		// function to be executed when Sign Out Button is clicked
		btn_sign_out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Login_Account user = new Login_Account();
				openSignInForm(frame, user);
			}
		});
		
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}
	
	
	
	
	
	// manage card page
	void open_manage_card_page(JFrame frame, Manager manager)
	{
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		remakeScreen(frame, f);
		
		// Manage Credit/Debit Card Label
		JLabel lcard = new JLabel("Manage Credit/Debit Card");
		lcard.setFont( lcard.getFont().deriveFont(30f) );
		lcard.setBounds(200,40,400, 40);
		f.add(lcard);
		
		//		Account Holder Name
		JLabel l_acc_num = new JLabel("Enter Account Number");
		l_acc_num.setBounds(400, 90, 150, 90);
		f.add(l_acc_num);
		JTextField tf_acc_num = new JTextField();
		tf_acc_num.setBounds(400, 150 ,200, 25);
		f.add(tf_acc_num);
		
		//		Account Type
		JLabel l_card_no = new JLabel("Enter Card Number");
		l_card_no.setBounds(400, 180, 150, 90);
		f.add(l_card_no);
		JTextField tf_card_no = new JTextField();
		tf_card_no.setBounds(400, 240 ,200, 25);
		f.add(tf_card_no);
		
		//		Account Opening Date
		JLabel l_cic = new JLabel("Enter CIC of account holder");
		l_cic.setBounds(400, 270, 200, 90);
		f.add(l_cic);
		JTextField tf_cic = new JTextField();
		tf_cic.setBounds(400, 330 ,200, 25);
		f.add(tf_cic);

		//-----------------------------------------------Buttons----------------------------------------------------		
		JButton btn_block_card = new JButton("Block Credit/Debit Card");
		btn_block_card.setBackground( new Color(0, 204, 153) );
		btn_block_card.setForeground(Color.white);
		btn_block_card.setBounds(100,120,200, 35);
		f.add(btn_block_card);
		
		JButton btn_unblock_card = new JButton("Unblock Credit/Debit Card");
		btn_unblock_card.setBackground( new Color(0, 204, 153) );
		btn_unblock_card.setForeground(Color.white);
		btn_unblock_card.setBounds(100,170,200,35);
		f.add(btn_unblock_card);
	
		// Sign Out Button
		JButton btn_sign_out = new JButton("Sign Out");
		btn_sign_out.setBackground( new Color(0, 204, 153) );
		btn_sign_out.setForeground(Color.white);
		btn_sign_out.setBounds(650,30,100, 30);
		f.add(btn_sign_out);
		
		// function to be executed when Sign Out Button is clicked
		btn_sign_out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Login_Account user = new Login_Account();
				openSignInForm(frame, user);
			}
		});
		
		// Main Menu Button
		JButton btn_mm = new JButton("Main Menu");
		btn_mm.setBackground( new Color(0, 204, 153) );
		btn_mm.setForeground(Color.white);
		btn_mm.setBounds(50,400,100, 30);
		f.add(btn_mm);
		
		// function to be executed when Main Menu Button is clicked
		btn_mm.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				openManagerMenu(frame, manager);
			}
		});
		
		// function to be executed when Block Card Button is clicked
		btn_block_card.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				
			
				int block_status = manager.block_card(Integer.parseInt(tf_acc_num.getText()), tf_cic.getText(), tf_card_no.getText());
				
				if (block_status == -1)
				{
					JOptionPane.showMessageDialog(f, "Không tìm thấy tài khoản nào có các chi tiết này");
					frame.remove(f);
					frame.repaint();
					frame.validate();
					open_manage_card_page(frame, manager);
				}
				else if (block_status == -2)
				{
					JOptionPane.showMessageDialog(f, "Không tìm thấy thẻ nào có các chi tiết này");
					frame.remove(f);
					frame.repaint();
					frame.validate();
					open_manage_card_page(frame, manager);
				}
				else if (block_status == -3)
				{
					JOptionPane.showMessageDialog(f, "Thẻ này đã bị khóa");
					frame.remove(f);
					frame.repaint();
					frame.validate();
					open_manage_card_page(frame, manager);
				}
				else
				{
					JOptionPane.showMessageDialog(f, "Khóa thẻ thành công");
					frame.remove(f);
					frame.repaint();
					frame.validate();
					openManagerMenu(frame, manager);
				}	
			}
		});
		
		
		// function to be executed when Unblock Card Button is clicked
		btn_unblock_card.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				
				int unblock_status = manager.unlock_card(Integer.parseInt(tf_acc_num.getText()), tf_cic.getText(), tf_card_no.getText());
				if (unblock_status == -1)
				{
					JOptionPane.showMessageDialog(f, "Không tìm thấy tài khoản nào có các chi tiết này");
					frame.remove(f);
					frame.repaint();
					frame.validate();
					open_manage_card_page(frame, manager);
				}
				else if (unblock_status == -2)
				{
					JOptionPane.showMessageDialog(f, "Không tìm thấy thẻ nào có các chi tiết này");
					frame.remove(f);
					frame.repaint();
					frame.validate();
					open_manage_card_page(frame, manager);
				}
				else if (unblock_status == -3)
				{
					JOptionPane.showMessageDialog(f, "Thẻ này đã hoạt động và đang hoạt động");
					frame.remove(f);
					frame.repaint();
					frame.validate();
					open_manage_card_page(frame, manager);
				}
				else
				{
					JOptionPane.showMessageDialog(f, "Thẻ được chọn đã được bỏ chặn thành công");
					frame.remove(f);
					frame.repaint();
					frame.validate();
					openManagerMenu(frame, manager);
				}
			}
		});
	}
	
	
	
	
	
	// block unblock account screen
	void open_block_unblock_account_page(JFrame frame, Manager manager)
	{
		//--------------------------------Clearing screen for new page---------------------------------------		
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		remakeScreen(frame, f);
		
		// Block Unblock Account Label
		JLabel lBUAcc = new JLabel("Block/Unblock Account");
		lBUAcc.setFont( lBUAcc.getFont().deriveFont(30f) );
		lBUAcc.setBounds(200,40,400, 40);
		f.add(lBUAcc);
		
		//----------------------------------Creating Labels-----------------------------------------------------				
		JLabel l_account_no = new JLabel("Enter Account No:");
		l_account_no.setBounds(200,110,150, 90);
		f.add(l_account_no);
				
		JLabel l_cic = new JLabel("Enter CIC:");
		l_cic.setBounds(200, 170, 100, 90);
		f.add(l_cic);

		//-----------------------------------Creating Textfields-----------------------------------------------				
		JTextField tf_account_no = new JTextField();
		tf_account_no.setBounds(400, 140 ,200, 25);
		f.add(tf_account_no);
				
		JTextField tf_cic = new JTextField();
		tf_cic.setBounds(400,200,200, 25);
		f.add(tf_cic);
		
		//------------------------------------Creating buttons---------------------------------------------------				
		JButton btn_block_account = new JButton("Block Account");
		btn_block_account.setBackground( new Color(0, 204, 153) );
		btn_block_account.setForeground(Color.white);
		btn_block_account.setBounds(300,270,150, 40);
		f.add(btn_block_account);
		
		JButton btn_unblock_account = new JButton("UnBlock Account");
		btn_unblock_account.setBackground( new Color(0, 204, 153) );
		btn_unblock_account.setForeground(Color.white);
		btn_unblock_account.setBounds(300,320,150,40);
		f.add(btn_unblock_account);
				
		// function to be executed when Block Account Button is clicked
		btn_block_account.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Request received of blocking account with acc num:"+acc_num+" and cic:"+cic);
				
				int block_status = manager.block_account( Integer.parseInt(tf_account_no.getText()), tf_cic.getText() );

				if ( block_status == -1 )
				{
					JOptionPane.showMessageDialog(f, "Thông tin đăng nhập đã nhập của bạn không khớp với bất kỳ tài khoản nào");
				}
				else if (block_status == -2)
				{
					JOptionPane.showMessageDialog(f, "Không có tài khoản đang hoạt động với các chi tiết này");
				}
				else if (block_status == -3)
				{
					JOptionPane.showMessageDialog(f, "Tài khoản bạn nhập đã bị chặn");
				}
				else
				{
					JOptionPane.showMessageDialog(f, "Tài khoản được chọn đã bị chặn thành công");
					frame.remove(f);
					frame.repaint();
					frame.validate();
					openManagerMenu(frame, manager);
				}
			}
		});
				
		// function to be executed when Unblock Account Button is clicked
		btn_unblock_account.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int unblock_status = manager.unlock_account( Integer.parseInt(tf_account_no.getText()), tf_cic.getText() );
				
				if ( unblock_status == -1)
				{
					JOptionPane.showMessageDialog(f, "Thông tin đăng nhập đã nhập của bạn không khớp với bất kỳ tài khoản nào");
				}
				else if ( unblock_status == -2)
				{
					JOptionPane.showMessageDialog(f, "Không có tài khoản đang hoạt động với các chi tiết này");
				}
				else if (unblock_status == -3)
				{
					JOptionPane.showMessageDialog(f, "Tài khoản bạn đã nhập đã hoạt động và đang hoạt động");
				}
				else
				{
					JOptionPane.showMessageDialog(f, "Tài khoản được chọn đã được bỏ chặn thành công");
					frame.remove(f);
					frame.repaint();
					frame.validate();
					openManagerMenu(frame, manager);
				}
			}
		});
			
		// Main Menu Button
		JButton btn_mm = new JButton("Main Menu");
		btn_mm.setBackground( new Color(0, 204, 153) );
		btn_mm.setForeground(Color.white);
		btn_mm.setBounds(50,400,100, 30);
		f.add(btn_mm);
		
		// function to be executed when Main Menu Button is clicked
		btn_mm.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				openManagerMenu(frame, manager);
			}
		});
				
		// Sign Out Button
		JButton btn_sign_out = new JButton("Sign Out");
		btn_sign_out.setBackground( new Color(0, 204, 153) );
		btn_sign_out.setForeground(Color.white);
		btn_sign_out.setBounds(650,30,100, 30);
		f.add(btn_sign_out);
		
		// function to be executed when Sign Out Button is clicked
		btn_sign_out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Login_Account user = new Login_Account();
				openSignInForm(frame, user);
			}
		});
	}

		
	// close account screen
	void open_close_account_page(JFrame frame, Manager manager)
	{
		//--------------------------------Clearing screen for new page---------------------------------------		
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		remakeScreen(frame, f);
		
		// Close Account Label
		JLabel lCloseAcc = new JLabel("Close Account");
		lCloseAcc.setFont( lCloseAcc.getFont().deriveFont(30f) );
		lCloseAcc.setBounds(300,40,400, 40);
		f.add(lCloseAcc);
		
		//----------------------------------Creating Labels-----------------------------------------------------				
		JLabel l_account_no = new JLabel("Enter Account No:");
		l_account_no.setBounds(220,110,150, 90);
		f.add(l_account_no);
		
		JLabel l_cic = new JLabel("Enter CIC:");
		l_cic.setBounds(220, 170, 100, 90);
		f.add(l_cic);
		
		//-----------------------------------Creating Textfields-----------------------------------------------				
		JTextField tf_account_no = new JTextField();
		tf_account_no.setBounds(400, 140 ,200, 25);
		f.add(tf_account_no);
		
		JTextField tf_cic = new JTextField();
		tf_cic.setBounds(400,200,200, 25);
		f.add(tf_cic);

		//------------------------------------Creating buttons---------------------------------------------------				
		JButton btn_close_account = new JButton("Close Account");
		btn_close_account.setBackground( new Color(0, 204, 153) );
		btn_close_account.setForeground(Color.white);
		btn_close_account.setBounds(300,280,150, 40);
		f.add(btn_close_account);

		// function to be executed when close account button is clicked
		btn_close_account.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int close_status = manager.close_account(tf_account_no.getText(), tf_cic.getText());
				
				if ( close_status == 0 )
				{
					JOptionPane.showMessageDialog(f, "Tài khoản của bạn đã được đóng thành công");
					openManagerMenu(frame, manager);
				}
				else
				{
					JOptionPane.showMessageDialog(f, "Chi tiết bạn đã nhập không khớp với bất kỳ tài khoản nào");
					openManagerMenu(frame, manager);
				}
			}
		});
		
		// Main Menu Button
		JButton btn_mm = new JButton("Main Menu");
		btn_mm.setBackground( new Color(0, 204, 153) );
		btn_mm.setForeground(Color.white);
		btn_mm.setBounds(50,400,100, 30);
		f.add(btn_mm);
		
		// function to be executed when Main Menu Button is clicked
		btn_mm.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				openManagerMenu(frame, manager);
			}
		});
				
		// Sign Out Button
		JButton btn_sign_out = new JButton("Sign Out");
		btn_sign_out.setBackground( new Color(0, 204, 153) );
		btn_sign_out.setForeground(Color.white);
		btn_sign_out.setBounds(650,30,100, 30);
		f.add(btn_sign_out);
		
		// function to be executed when Sign Out Button is clicked
		btn_sign_out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Login_Account user = new Login_Account();
				openSignInForm(frame, user);
			}
		});
	}
	
	
	
	
	
	
	// In this screen manager will enter the client's account number and cic number for search
	void searchAccount(JFrame frame, Manager manager) {
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		
		// Search Account Label
		JLabel lSearch = new JLabel("Search Account");
		lSearch.setFont( lSearch.getFont().deriveFont(30f) );
		lSearch.setBounds(150,50,250, 40);
		f.add(lSearch);	
		

		// Account Number Label
		JLabel lAccNum = new JLabel("Enter account number:");
		lAccNum.setBounds(150,100,150, 40);
		f.add(lAccNum);
		
		// Account Number Text Field
		JTextField tfAccNum = new JTextField();
		tfAccNum.setBounds(150,140,180, 25);
		f.add(tfAccNum);
		
		// 					Client Info Fields
		// Client Name Label
		JLabel lCName = new JLabel("Name: ");
		lCName.setBounds(150,170,250, 40);
		lCName.setVisible(false);
		f.add(lCName);
		
		// Client CIC
		JLabel lCCIC = new JLabel("CIC: ");
		lCCIC.setBounds(150,200,250, 40);
		lCCIC.setVisible(false);
		f.add(lCCIC);
		
		// Client Account Number
		JLabel lCAccNum = new JLabel("Account No: ");
		lCAccNum.setBounds(150,230,250, 40);
		lCAccNum.setVisible(false);
		f.add(lCAccNum);
		
		// Client Account Type
		JLabel lCAccType = new JLabel("Account Type: ");
		lCAccType.setBounds(150,260,250, 40);
		lCAccType.setVisible(false);
		f.add(lCAccType);
		
		// Client Account Balance
		JLabel lCAccBalance = new JLabel("Balance: ");
		lCAccBalance.setBounds(150,290,250, 40);
		lCAccBalance.setVisible(false);
		f.add(lCAccBalance);
		
		// Client Account Status
		JLabel lCAccStatus = new JLabel("Status: ");
		lCAccStatus.setBounds(150,320,250, 40);
		lCAccStatus.setVisible(false);
		f.add(lCAccStatus);
		
		// Client Account Opening Date
		JLabel lCAccODate = new JLabel("Opening Date: ");
		lCAccODate.setBounds(150,350,250, 40);
		lCAccODate.setVisible(false);
		f.add(lCAccODate);
	
		
		// Search Account Button
		JButton btnSearch = new JButton("Search");
		btnSearch.setBackground( new Color(0, 204, 153) );
		btnSearch.setForeground(Color.white);
		btnSearch.setBounds(350,130,110, 40);
		f.add(btnSearch);
		
	
		// function to execute with clicked on Search Button
		btnSearch.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Bank_Account account = manager.getAccountInfo( tfAccNum.getText() );
				Client client = manager.getClientInfo( tfAccNum.getText() );
				
				// check if either account or client was not found
				if( (account.getAccountNum().compareTo("") == 0) || (client.getCIC().compareTo("") == 0 ) ) {
					
					lCName.setVisible(false);
					lCCIC.setVisible(false);
					lCAccNum.setVisible(false);
					lCAccType.setVisible(false);
					lCAccBalance.setVisible(false);
					lCAccStatus.setVisible(false);
					lCAccODate.setVisible(false);
					
					// display a message box that account was not found
					if(account.getAccountNum().compareTo("") == 0) {
						JOptionPane.showMessageDialog(f, "Không tìm thấy Account");
					}
					
					// display a message box that client was not found
					else if (client.getCIC().compareTo("") == 0 ) {
						JOptionPane.showMessageDialog(f, "Không tìm thấy Client");
					}
					account = null;
					client = null;
				}
				
				// Both client and his account was found
				else {
		
					lCName.setText("Name:  "+client.getFName()+" "+client.getLName() );
					lCCIC.setText("CIC:  "+client.getCIC() );
					lCAccNum.setText("Account No:  "+account.getAccountNum() );
					lCAccType.setText("Account Type:  "+account.getType() );
					lCAccBalance.setText("Balance:  "+account.getBalance() );
					String temp = "Open";
					if( Integer.valueOf( account.getStatus() ) ==  0 )
						temp = "Close";
					else if( Integer.valueOf( account.getStatus() ) ==  2 )
						temp = "Block";
					lCAccStatus.setText("Status:  "+ temp );
					lCAccODate.setText("Opening Date:  "+account.getOpeningDate() );
					
					lCName.setVisible(true);
					lCCIC.setVisible(true);
					lCAccNum.setVisible(true);
					lCAccType.setVisible(true);
					lCAccBalance.setVisible(true);
					lCAccStatus.setVisible(true);
					lCAccODate.setVisible(true);
				}
			}
		});
		
		//Search Name
		
		
		// Main Menu Button
		JButton btn_mm = new JButton("Main Menu");
		btn_mm.setBackground( new Color(0, 204, 153) );
		btn_mm.setForeground(Color.white);
		btn_mm.setBounds(50,400,100, 30);
		f.add(btn_mm);
		
		// function to be executed when Main Menu Button is clicked
		btn_mm.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				openManagerMenu(frame, manager);
			}
		});
				
		// Sign Out Button
		JButton btn_sign_out = new JButton("Sign Out");
		btn_sign_out.setBackground( new Color(0, 204, 153) );
		btn_sign_out.setForeground(Color.white);
		btn_sign_out.setBounds(650,30,100, 30);
		f.add(btn_sign_out);
		
		// function to be executed when Sign Out Button is clicked
		btn_sign_out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Login_Account user = new Login_Account();
				openSignInForm(frame, user);
			}
		});
		
		// displaying the new panel on frame	
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}
	
	
	
	
	
	// In this screen manager will enter the client's account number and cic number for search
	void updateClientInfo(JFrame frame, Manager manager) {
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		
		// Update Client Info Label
		JLabel lUCInfo = new JLabel("Update Client Info");
		lUCInfo.setFont( lUCInfo.getFont().deriveFont(30f) );
		lUCInfo.setBounds(150,50,300, 40);
		f.add(lUCInfo);	
		
		// Account Number Label
		JLabel lAccNum = new JLabel("Enter account number:");
		lAccNum.setBounds(150,100,150, 40);
		f.add(lAccNum);
		
		// Account Number Text Field
		JTextField tfAccNum = new JTextField();
		tfAccNum.setBounds(150,140,180, 25);
		f.add(tfAccNum);
		
		// 					Client Info Fields
		// Client Name Label
		JLabel lCName = new JLabel("Name: ");
		lCName.setBounds(150,170,250, 40);
		lCName.setVisible(false);
		f.add(lCName);
		JLabel lCid = new JLabel("");
		lCid.setBounds(450,170,100, 40);
		lCid.setVisible(false);
		f.add(lCid);
		
		// Client CIC
		JLabel lCCIC = new JLabel("CIC: ");
		lCCIC.setBounds(150,200,250, 40);
		lCCIC.setVisible(false);
		f.add(lCCIC);
		
		// Client Phone
		JLabel lCPhone = new JLabel("Phone No: ");
		lCPhone.setBounds(150,230,250, 40);
		lCPhone.setVisible(false);
		f.add(lCPhone);
		JTextField tfPhone = new JTextField();
		tfPhone.setBounds(250,240,180, 25);
		tfPhone.setVisible(false);
		f.add(tfPhone);
		
		// Client Email
		JLabel lCEmail = new JLabel("Email: ");
		lCEmail.setBounds(150,260,250, 40);
		lCEmail.setVisible(false);
		f.add(lCEmail);
		JTextField tfEmail = new JTextField();
		tfEmail.setBounds(250,270,180, 25);
		tfEmail.setVisible(false);
		f.add(tfEmail);	
		
		// Client Address
		JLabel lCAddress = new JLabel("Address: ");
		lCAddress.setBounds(150,290,250, 40);
		lCAddress.setVisible(false);
		f.add(lCAddress);
		JTextField tfAddress = new JTextField();
		tfAddress.setBounds(250,300,180, 25);
		tfAddress.setVisible(false);
		f.add(tfAddress);
		
		// Search Account Button
		JButton btnSearch = new JButton("Search");
		btnSearch.setBackground( new Color(0, 204, 153) );
		btnSearch.setForeground(Color.white);
		btnSearch.setBounds(350,130,110, 40);
		f.add(btnSearch);
		
		// Update Button
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBackground( new Color(0, 204, 153) );
		btnUpdate.setForeground(Color.white);
		btnUpdate.setBounds(150,330,110, 40);
		btnUpdate.setVisible(false);
		f.add(btnUpdate);
				
		// function to execute with clicked on Search Button
		btnSearch.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Bank_Account account = manager.getAccountInfo( tfAccNum.getText() );
				Client client = manager.getClientInfo( tfAccNum.getText() );
				
				// check if either account or client was not found
				if( (account.getAccountNum().compareTo("") == 0) || (client.getCIC().compareTo("") == 0 ) ) {
					
					lCName.setVisible(false);
					lCCIC.setVisible(false);
					lCPhone.setVisible(false);
					lCEmail.setVisible(false);
					lCAddress.setVisible(false);
					tfPhone.setVisible(false);
					tfEmail.setVisible(false);
					tfAddress.setVisible(false);
					btnUpdate.setVisible(false);
				
					// display a message box that account was not found
					if(account.getAccountNum().compareTo("") == 0) {
						JOptionPane.showMessageDialog(f, "Không tìm thấy tài khoản");
					}
					
					// display a message box that client was not found
					else if (client.getCIC().compareTo("") == 0 ) {
						JOptionPane.showMessageDialog(f, "Không tìm thấy khách hàng");
					}
					account = null;
					client = null;
				}
				
				// Both client and his account was found
				else {
		
					lCid.setText( client.getClientID() );
					lCName.setText("Name:  "+client.getFName()+" "+client.getLName() );
					lCCIC.setText("CIC:  "+client.getCIC() );
					tfPhone.setText( client.getPhone() );
					tfEmail.setText( client.getEmail() );
					tfAddress.setText( client.getAddress() );
					
					lCName.setVisible(true);
					lCCIC.setVisible(true);
					lCPhone.setVisible(true);
					lCEmail.setVisible(true);
					lCAddress.setVisible(true);
					tfPhone.setVisible(true);
					tfEmail.setVisible(true);
					tfAddress.setVisible(true);
					btnUpdate.setVisible(true);
				}
			}
		});
				
		// this function is executed when Update Button is clicked
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				manager.updateClientInfo( lCid.getText(), tfPhone.getText(), tfEmail.getText(), tfAddress.getText() );
			
				JOptionPane.showMessageDialog(f, "Thông tin được cập nhật");
				
				frame.remove(f);
				frame.repaint();
				frame.validate();
				openManagerMenu(frame, manager);
			}
		});
		
		// Main Menu Button
		JButton btn_mm = new JButton("Main Menu");
		btn_mm.setBackground( new Color(0, 204, 153) );
		btn_mm.setForeground(Color.white);
		btn_mm.setBounds(50,400,100, 30);
		f.add(btn_mm);
		
		// function to be executed when Main Menu Button is clicked
		btn_mm.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				openManagerMenu(frame, manager);
			}
		});
				
		// Sign Out Button
		JButton btn_sign_out = new JButton("Sign Out");
		btn_sign_out.setBackground( new Color(0, 204, 153) );
		btn_sign_out.setForeground(Color.white);
		btn_sign_out.setBounds(650,30,100, 30);
		f.add(btn_sign_out);
		
		// function to be executed when Sign Out Button is clicked
		btn_sign_out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Login_Account user = new Login_Account();
				openSignInForm(frame, user);
			}
		});
		
		// displaying the new panel on frame	
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}
	
	
	
	//--------------------------------------------------------
	//        Màn hình liên quan đến Accountant(Kế toán)     |
	//-------------------------------------------------------
	
	
	// Accountant Menu
	void openAccountantMenu(JFrame frame, Accountant accountant) {
		JPanel f = new JPanel();
		f.setBackground(Color.white);
				
		// User Name label
		JLabel lUser = new JLabel( accountant.getName() );
		lUser.setFont( lUser.getFont().deriveFont(30f) );
		lUser.setBounds(100,50,300, 40);
		f.add(lUser);
		
		// Designation label
		JLabel lDesg = new JLabel("Accountant");
		//lDesg.setFont( lDesg.getFont().deriveFont(30f) );
		lDesg.setBounds(100,80,100, 40);
		f.add(lDesg);
		
		// Deposit Cash button
		JButton btnDeposit = new JButton("Deposit Cash");
		btnDeposit.setBackground( new Color(0, 204, 153) );
		btnDeposit.setForeground(Color.white);
		btnDeposit.setBounds(100,180,150, 40);
		f.add(btnDeposit);
		
		// Withdraw Cash button
		JButton btnWithdraw = new JButton("Withdraw Cash");
		btnWithdraw.setBackground( new Color(0, 204, 153) );
		btnWithdraw.setForeground(Color.white);
		btnWithdraw.setBounds(300,180,150, 40);
		f.add(btnWithdraw);
		
		// Cheque Deposit button
		JButton btnChequeDeposit = new JButton("Cheque Deposit");
		btnChequeDeposit.setBackground( new Color(0, 204, 153) );
		btnChequeDeposit.setForeground(Color.white);
		btnChequeDeposit.setBounds(530,180,150, 40);
		f.add(btnChequeDeposit);
		
		// E-Statement button
		JButton btnEStatement = new JButton("Issue E-Statement");
		btnEStatement.setBackground( new Color(0, 204, 153) );
		btnEStatement.setForeground(Color.white);
		btnEStatement.setBounds(300,250,150, 40);
		
		f.add(btnEStatement);
		
		// function to be executed when Deposit button is clicked
		btnDeposit.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				searchForm(frame, accountant, 1);
			}
		});
		
		// function to be executed when Withdraw Button is clicked
		btnWithdraw.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				searchForm(frame, accountant, 2);
			}
		});
		
		// function to be executed when Cheque Deposit Button is clicked
		btnChequeDeposit.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				chequeDeposit(frame, accountant);
			}
		});
		
	
		// function to be executed when EStatement Button is clicked
		btnEStatement.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				searchForm(frame, accountant, 3);
			}
		});
		
		// Sign Out Button
		JButton btn_sign_out = new JButton("Sign Out");
		btn_sign_out.setBackground( new Color(0, 204, 153) );
		btn_sign_out.setForeground(Color.white);
		btn_sign_out.setBounds(650,30,100, 30);
		f.add(btn_sign_out);
		
		// function to be executed when Sign Out Button is clicked
		btn_sign_out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Login_Account user = new Login_Account();
				openSignInForm(frame, user);
			}
		});
		
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}
	
	
	
	
	
	
	// Ở màn hình này kế toán sẽ nhập số tài khoản và số cic của khách hàng
	// to search him to deposit money --Tìm kiếm để gửi tiền
	void searchForm(JFrame frame, Accountant accountant, int case_) {
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		
		// Deposit Cash Label
		JLabel lheading = new JLabel("Deposit Cash");
		lheading.setFont( lheading.getFont().deriveFont(30f) );
		lheading.setBounds(150,50,250, 40);
		f.add(lheading);	
		if( case_ ==  2 )
			lheading.setText("Withdraw Cash");
		else if( case_ == 3 )
			lheading.setText("E-Statement");
		
		// Account Number Label
		JLabel lAccNum = new JLabel("Enter account number:");
		lAccNum.setBounds(150,100,150, 40);
		f.add(lAccNum);
		
		// Account Number Text Field
		JTextField tfAccNum = new JTextField();
		tfAccNum.setBounds(150,140,180, 25);
		f.add(tfAccNum);
		
		// CIC Number Label
		JLabel lCIC = new JLabel("Enter CIC number:");
		lCIC.setBounds(150,180,150, 40);
		f.add(lCIC);
		
		// CIC Number Text Field
		JTextField tfCIC = new JTextField();
		tfCIC.setBounds(150,220,180, 25);
		f.add(tfCIC);
		
		// Search Account Button
		JButton btnSearch = new JButton("Search");
		btnSearch.setBackground( new Color(0, 204, 153) );
		btnSearch.setForeground(Color.white);
		btnSearch.setBounds(150,270,110, 40);
		f.add(btnSearch);
				
		// function to execute with clicked on Search Button
		btnSearch.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Bank_Account account = accountant.searchAccount1( tfAccNum.getText(), tfCIC.getText() );
				Client client =  accountant.searchClient1( tfAccNum.getText(), tfCIC.getText()  );
				
				// kiểm tra xem không tìm thấy tài khoản hoặc khách hàng
				if( (account.getAccountNum().compareTo("") == 0) || (client.getCIC().compareTo("") == 0 ) ) {
					
					// display a message box that account was not found
					if(account.getAccountNum().compareTo("") == 0) {
						JOptionPane.showMessageDialog(f, "Không tìm thấy tài khoản");
					}
					
					// display a message box that client was not found
					else if (client.getCIC().compareTo("") == 0 ) {
						JOptionPane.showMessageDialog(f, "Không tìm thấy khách hàng");
					}
					account = null;
					client = null;
				}
				
				// Cả khách hàng và tài khoản của khách đều được tìm thấy
				else {		
					if( Integer.valueOf( account.getStatus() ) == 0 ) {
						JOptionPane.showMessageDialog(f, "Tài khoản bị đóng");
					}
					else if( Integer.valueOf( account.getStatus() ) == 2 ) {
						JOptionPane.showMessageDialog(f, "Tài khoản bị khóa");
					}
					else {
						frame.remove(f);
						frame.repaint();
						frame.validate();
						if( case_ == 1 )
							depositCash(frame, accountant, client, account);
						else if( case_ == 2 )
							withdrawCash(frame, accountant, client, account);
						else if( case_ == 3 )
							eStatement(frame, accountant, client, account);
					}
				}
				
			}
		});
		
		// Main Menu Button
		JButton btn_mm = new JButton("Main Menu");
		btn_mm.setBackground( new Color(0, 204, 153) );
		btn_mm.setForeground(Color.white);
		btn_mm.setBounds(50,400,100, 30);
		f.add(btn_mm);
		
		// function to be executed when Main Menu Button is clicked
		btn_mm.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				openAccountantMenu(frame, accountant);
			}
		});
				
		// Sign Out Button
		JButton btn_sign_out = new JButton("Sign Out");
		btn_sign_out.setBackground( new Color(0, 204, 153) );
		btn_sign_out.setForeground(Color.white);
		btn_sign_out.setBounds(650,30,100, 30);
		f.add(btn_sign_out);
		
		// function to be executed when Sign Out Button is clicked
		btn_sign_out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Login_Account user = new Login_Account();
				openSignInForm(frame, user);
			}
		});
		
		// displaying the new panel on frame	
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}
	
	
	
	
	
	// deposit case screen
	void depositCash(JFrame frame, Accountant accountant, Client client, Bank_Account account) {
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		
		// Deposit Cash Label
		JLabel lDeposit = new JLabel("Deposit Cash");
		lDeposit.setFont( lDeposit.getFont().deriveFont(30f) );
		lDeposit.setBounds(150,50,250, 40);
		f.add(lDeposit);
		
		// Name Label
		JLabel lName = new JLabel( "Client Name: " + client.getFName() + " " + client.getLName() );
		lName.setBounds(150,100,350, 40);
		f.add(lName);
			
		// CIC Label
		JLabel lCic = new JLabel( "CIC: " + client.getCIC() );
		lCic.setBounds(150,130,150, 40);
		f.add(lCic);
		
		// Account Number Label
		JLabel lAccNum = new JLabel( "Account Number: " + account.getAccountNum() );
		lAccNum.setBounds(150,160,150, 40);
		f.add(lAccNum);
		
		// Name Label
		JLabel lType = new JLabel( "Account Type" + account.getType() );
		lType.setBounds(150,190,150, 40);
		f.add(lType);
		
		// Amount Label
		JLabel lAmount = new JLabel("Enter amount:");
		lAmount.setBounds(150,240,150, 40);
		f.add(lAmount);
		
		// Amount Text Field
		JTextField tfAmount = new JTextField();
		tfAmount.setBounds(150,280,180, 25);
		f.add(tfAmount);
		
		// Deposit Button
		JButton btnDeposit = new JButton("Deposit");
		btnDeposit.setBackground( new Color(0, 204, 153) );
		btnDeposit.setForeground(Color.white);
		btnDeposit.setBounds(150,320,100, 30);
		f.add(btnDeposit);
		
		// function to be executed when Deposit Button is clicked
		btnDeposit.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// check if amount is greater than 0
				if( tfAmount.getText().compareTo("") != 0 ) {
					if( Integer.valueOf( tfAmount.getText()) > 0 ) {
						// add amount to account
						int r = account.addAmount( Integer.valueOf( tfAmount.getText()) );
						if( r == 1 ) {
							// show deposit successful message
							JOptionPane.showMessageDialog(f, "Gửi tiền thành công");
						}
						else {
							JOptionPane.showMessageDialog(f, "Gửi tiền thất bại");
						}
						
						// go back to menu of accountant
						frame.remove(f);
						frame.repaint();
						frame.validate();
						openAccountantMenu(frame, accountant);
					}
					// invalid amount
					else {
						JOptionPane.showMessageDialog(f, "Số tiền không hợp lệ");
					}
				}
				// invalid amount
				else {
					JOptionPane.showMessageDialog(f, "Số tiền không hợp lệ");
				}
			}
		});
		
		// Main Menu Button
		JButton btn_mm = new JButton("Main Menu");
		btn_mm.setBackground( new Color(0, 204, 153) );
		btn_mm.setForeground(Color.white);
		btn_mm.setBounds(50,400,100, 30);
		f.add(btn_mm);
		
		// function to be executed when Main Menu Button is clicked
		btn_mm.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				openAccountantMenu(frame, accountant);
			}
		});
				
		// Sign Out Button
		JButton btn_sign_out = new JButton("Sign Out");
		btn_sign_out.setBackground( new Color(0, 204, 153) );
		btn_sign_out.setForeground(Color.white);
		btn_sign_out.setBounds(650,30,100, 30);
		f.add(btn_sign_out);
		
		// function to be executed when Sign Out Button is clicked
		btn_sign_out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Login_Account user = new Login_Account();
				openSignInForm(frame, user);
			}
		});
		
		// displaying the new panel on frame	
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}

	
	
	
	
	// withdraw cash screen --màn hình rút tiền mặt
	void withdrawCash(JFrame frame, Accountant accountant, Client client, Bank_Account account) {
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		
		// Deposit Cash Label
		JLabel lWithdraw = new JLabel("Withdraw Cash");
		lWithdraw.setFont( lWithdraw.getFont().deriveFont(30f) );
		lWithdraw.setBounds(150,50,250, 40);
		f.add(lWithdraw);
		
		// Name Label
		JLabel lName = new JLabel( "Client Name: " + client.getFName() + " " + client.getLName() );
		lName.setBounds(150,100,150, 40);
		f.add(lName);
			
		// CIC Label
		JLabel lCic = new JLabel( "CIC: " + client.getCIC() );
		lCic.setBounds(150,130,150, 40);
		f.add(lCic);
		
		// Account Number Label
		JLabel lAccNum = new JLabel( "Account Number: " + account.getAccountNum() );
		lAccNum.setBounds(150,160,150, 40);
		f.add(lAccNum);
		
		// Name Label
		JLabel lType = new JLabel( "Account Type" + account.getType() );
		lType.setBounds(150,190,150, 40);
		f.add(lType);
		
		// Amount Label
		JLabel lAmount = new JLabel("Enter amount:");
		lAmount.setBounds(150,240,150, 40);
		f.add(lAmount);
		
		// Amount Text Field
		JTextField tfAmount = new JTextField();
		tfAmount.setBounds(150,280,180, 25);
		f.add(tfAmount);
		
		// Withdraw Button
		JButton btnWithdraw = new JButton("Withdraw");
		btnWithdraw.setBackground( new Color(0, 204, 153) );
		btnWithdraw.setForeground(Color.white);
		btnWithdraw.setBounds(150,320,100, 30);
		f.add(btnWithdraw);
		
		// function to be executed with Withdraw Button is clicked
		btnWithdraw.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// check if amount is greater than 0
				if( tfAmount.getText().compareTo("") != 0 ) {
					if( Integer.valueOf( tfAmount.getText()) > 0 ) {						
						// subtract amount from account
						int r = account.removeAmount( Integer.valueOf( tfAmount.getText()) );
						if( r == 2 ) {
							// show low balance message
							JOptionPane.showMessageDialog(f, "Không đủ số dư trong tài khoản");
						}
						else {
							if( r ==  1 ) {
								// show withdrawal success message
								JOptionPane.showMessageDialog(f, "Số tiền rút thành công");
							}
							else 	
								JOptionPane.showMessageDialog(f, "Số tiền rút không thành công");
							
							// go back to menu of accountant
							frame.remove(f);
							frame.repaint();
							frame.validate();
							openAccountantMenu(frame, accountant);
						}
					}
					// invalid amount
					else {
						JOptionPane.showMessageDialog(f, "Số tiền không hợp lệ");
					}
				}
				// invalid amount
				else {
					JOptionPane.showMessageDialog(f, "Số tiền không hợp lệ");
				}
			}
		});	
		
		// Main Menu Button
		JButton btn_mm = new JButton("Main Menu");
		btn_mm.setBackground( new Color(0, 204, 153) );
		btn_mm.setForeground(Color.white);
		btn_mm.setBounds(50,400,100, 30);
		f.add(btn_mm);
		
		// function to be executed when Main Menu Button is clicked
		btn_mm.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				openAccountantMenu(frame, accountant);
			}
		});
				
		// Sign Out Button
		JButton btn_sign_out = new JButton("Sign Out");
		btn_sign_out.setBackground( new Color(0, 204, 153) );
		btn_sign_out.setForeground(Color.white);
		btn_sign_out.setBounds(650,30,100, 30);
		f.add(btn_sign_out);
		
		// function to be executed when Sign Out Button is clicked
		btn_sign_out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Login_Account user = new Login_Account();
				openSignInForm(frame, user);
			}
		});
		
		// displaying the new panel on frame	
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}

	
	
	
	
	// view e-statement screen
	void eStatement(JFrame frame, Accountant accountant, Client client, Bank_Account account) {
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		
		// E-Statement Label
		JLabel lEStatement = new JLabel("E-Statement");
		lEStatement.setFont( lEStatement.getFont().deriveFont(30f) );
		lEStatement.setBounds(50,30,250, 40);
		f.add(lEStatement);
		
		// Name Label
		JLabel lName = new JLabel( "Client Name: " + client.getFName() + " " + client.getLName() );
		lName.setBounds(50,70,150, 40);
		f.add(lName);
			
		// CIC Label
		JLabel lCIC = new JLabel( "CIC: " + client.getCIC() );
		lCIC.setBounds(50,90,150, 40);
		f.add(lCIC);
		
		// Account Number Label
		JLabel lAccNum = new JLabel( "Account Number: " + account.getAccountNum() );
		lAccNum.setBounds(450,70,150, 40);
		f.add(lAccNum);
		
		// Name Label
		JLabel lType = new JLabel( "Account Type: " + account.getType() );
		lType.setBounds(450,90,150, 40);
		f.add(lType);
		
		// From Date Label
		JLabel lFrom = new JLabel("From   ( DD : MM : YY )");
		lFrom.setBounds(50,120,150, 40);
		f.add(lFrom);
		
		// To Date Label
		JLabel lTo = new JLabel("To   ( DD : MM : YY )");
		lTo.setBounds(450,120,150, 40);
		f.add(lTo);
		
		// Day, Month and Year for From Date
		SpinnerModel fromDayValues =  new SpinnerNumberModel(21, 1, 31, 1);  
	    JSpinner fromDaySpinner = new JSpinner(fromDayValues);   
	    fromDaySpinner.setBounds(50,150,40,25);    
	    f.add(fromDaySpinner); 
	    SpinnerModel fromMonthValues =  new SpinnerNumberModel(12, 1, 12, 1);  
	    JSpinner fromMonthSpinner = new JSpinner(fromMonthValues);   
	    fromMonthSpinner.setBounds(100,150,40,25);    
	    f.add(fromMonthSpinner); 
	    SpinnerModel fromYearValues =  new SpinnerNumberModel(2020, 2018, 2023, 1);  
	    JSpinner fromYearSpinner = new JSpinner(fromYearValues);   
	    fromYearSpinner.setBounds(150,150,55,25);    
	    f.add(fromYearSpinner); 
	    
	    // Day, Month and Year for From Date
 		SpinnerModel toDayValues =  new SpinnerNumberModel(30, 1, 31, 1);  
 	    JSpinner toDaySpinner = new JSpinner(toDayValues);   
 	    toDaySpinner.setBounds(450,150,40,25);    
 	    f.add(toDaySpinner); 
 	    SpinnerModel toMonthValues =  new SpinnerNumberModel(12, 1, 12, 1);  
 	    JSpinner toMonthSpinner = new JSpinner( toMonthValues);   
 	    toMonthSpinner.setBounds(500,150,40,25);    
 	    f.add(toMonthSpinner); 
 	    SpinnerModel toYearValues =  new SpinnerNumberModel(2020, 2018, 2023, 1);  
 	    JSpinner toYearSpinner = new JSpinner(toYearValues);   
 	    toYearSpinner.setBounds(550,150,55,25);    
 	    f.add(toYearSpinner); 
 	    
 		JButton btnEStatement = new JButton("Get E-Statement");
		btnEStatement.setBackground( new Color(0, 204, 153) );
		btnEStatement.setForeground(Color.white);
		btnEStatement.setBounds(50,200,150, 30);
		f.add(btnEStatement);
 	    
		JButton btnDownload = new JButton("Download");
		btnDownload.setBackground( new Color(0, 204, 153) );
		btnDownload.setForeground(Color.white);
		btnDownload.setBounds(450,200,100, 30);
		f.add(btnDownload);
		btnDownload.setEnabled(false);
		
		// table for transaction history records
		DefaultTableModel tableModel = new DefaultTableModel();
		JTable table=new JTable(tableModel);   
		tableModel.addColumn("Serial No");
		tableModel.addColumn("Amount");
		tableModel.addColumn("Type");
		tableModel.addColumn("Date");
		tableModel.addColumn("Time");
		tableModel.addColumn("Account Number");
		tableModel.addColumn("Reciever Account Number");
		tableModel.addColumn("Cheque No");
		table.setEnabled(false);
		JScrollPane sp=new JScrollPane(table);
		sp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
		sp.setBounds(50,250,700,150);  
		f.add(sp);
		sp.setVisible(false);
		
		// dunction to be executed when Get EStatement Button is clicked
		btnEStatement.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.setRowCount(0);
				
				String From = String.valueOf(fromYearSpinner.getValue()) +"-"+ String.valueOf(fromMonthSpinner.getValue()) +"-"+ String.valueOf(fromDaySpinner.getValue());
				String To = String.valueOf(toYearSpinner.getValue()) +"-"+ String.valueOf(toMonthSpinner.getValue()) +"-"+ String.valueOf(toDaySpinner.getValue());
			
				List<Transaction_History> list = accountant.getTransactions( account.getAccountNum(), From, To);
				if( list.size() > 0 ) {
					for( Transaction_History th: list) {
						tableModel.addRow(new Object[] { th.getSerialNo(), th.getAmount(), th.getType(), th.getDate(), th.getTime(), th.getAccountNumber(), th.getRecvAccNum(), th.getChequeNum() });
					}	
					tableModel.fireTableDataChanged();
					sp.setVisible(true);
					//btnEStatement.setEnabled(false);
					btnDownload.setEnabled(true);
				}
				else {
					btnDownload.setEnabled(false);
				}
			}
		});

		// function to be executed when Download Button is clicked
//		btnDownload.addActionListener( new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				String From = String.valueOf(fromYearSpinner.getValue()) +"-"+ String.valueOf(fromMonthSpinner.getValue()) +"-"+ String.valueOf(fromDaySpinner.getValue());
//				String To = String.valueOf(toYearSpinner.getValue()) +"-"+ String.valueOf(toMonthSpinner.getValue()) +"-"+ String.valueOf(toDaySpinner.getValue());
//			
//				DB_Handler db = new DB_Handler();
//				int r = db.createPDF(client, account, From, To);
//				if( r == 1 ) 
//					JOptionPane.showMessageDialog(f, "E-Statement is downloaded");
//				else
//					JOptionPane.showMessageDialog(f, "Error in downloading the e-statement");
//				frame.remove(f);
//				frame.repaint();
//				frame.validate();
//				openAccountantMenu(frame, accountant);
//			}
//		});
		
		// Main Menu Button
		JButton btn_mm = new JButton("Main Menu");
		btn_mm.setBackground( new Color(0, 204, 153) );
		btn_mm.setForeground(Color.white);
		btn_mm.setBounds(50,400,100, 30);
		f.add(btn_mm);
		
		// function to be executed when Main Menu Button is clicked
		btn_mm.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				openAccountantMenu(frame, accountant);
			}
		});
				
		// Sign Out Button
		JButton btn_sign_out = new JButton("Sign Out");
		btn_sign_out.setBackground( new Color(0, 204, 153) );
		btn_sign_out.setForeground(Color.white);
		btn_sign_out.setBounds(650,30,100, 30);
		f.add(btn_sign_out);
		
		// function to be executed when Sign Out Button is clicked
		btn_sign_out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Login_Account user = new Login_Account();
				openSignInForm(frame, user);
			}
		});
		
		// displaying the new panel on frame	
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);		
	}

	
	
	
	
	// cheque deposit screen
	void chequeDeposit(JFrame frame, Accountant accountant) {
		JPanel f = new JPanel();
		f.setBackground(Color.white);
		
		// Cheque Deposit Label
		JLabel lCheque = new JLabel("Cheque Deposit");
		lCheque.setFont( lCheque.getFont().deriveFont(30f) );
		lCheque.setBounds(150,50,250, 40);
		f.add(lCheque);
		
		// Account Number Label
		JLabel lAccNum = new JLabel("Enter account number:");
		lAccNum.setBounds(150,100,150, 40);
		f.add(lAccNum);
		
		// Account Number Text Field
		JTextField tfAccNum = new JTextField();
		tfAccNum.setBounds(150,130,180, 25);
		f.add(tfAccNum);
		
		// Amount Label
		JLabel lAmount = new JLabel("Enter Amount:");
		lAmount.setBounds(150,160,150, 40);
		f.add(lAmount);
		
		// Amount Text Field
		JTextField tfAmount = new JTextField();
		tfAmount.setBounds(150,190,180, 25);
		f.add(tfAmount);
		
		// Cheque Number Label
		JLabel lChequeNum = new JLabel("Enter Cheque number:");
		lChequeNum.setBounds(150,220,150, 40);
		f.add(lChequeNum);
		
		// Cheque Number Text Field
		JTextField tfChequeNum = new JTextField();
		tfChequeNum.setBounds(150,250,180, 25);
		f.add(tfChequeNum);
		
		// Deposit Button
		JButton btnDeposit = new JButton("Deposit");
		btnDeposit.setBackground( new Color(0, 204, 153) );
		btnDeposit.setForeground(Color.white);
		btnDeposit.setBounds(150,320,110, 40);
		f.add(btnDeposit);
		
		// function to be executed when Deposit Button is clicked
		btnDeposit.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( Integer.valueOf( tfAmount.getText() ) > 0 ) {
					int z = accountant.chequeDeposit( tfAccNum.getText(), tfChequeNum.getText(), Integer.valueOf( tfAmount.getText() ) );	
					if( z == 2) 
						JOptionPane.showMessageDialog(f, "Không tìm thấy tài khoản");
					else if( z == 3) 
						JOptionPane.showMessageDialog(f, "Tài khoản bị khóa");
					else if( z == 4) 
						JOptionPane.showMessageDialog(f, "Tài khoản bị đóng");
					else
					{
						if( z == 1 ) 
							JOptionPane.showMessageDialog(f, "Gửi tiền thành công");
						else 
							JOptionPane.showMessageDialog(f, "Gửi tiền thất bại");
						frame.remove(f);
						frame.repaint();
						frame.validate();
						openAccountantMenu(frame, accountant);	
					}
				}
				else
					JOptionPane.showMessageDialog(f, "Số tiền không hợp lệ");
			}
		});
		
		// Main Menu Button
		JButton btn_mm = new JButton("Main Menu");
		btn_mm.setBackground( new Color(0, 204, 153) );
		btn_mm.setForeground(Color.white);
		btn_mm.setBounds(50,400,100, 30);
		f.add(btn_mm);
		
		// function to be executed when Main Menu Button is clicked
		btn_mm.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				openAccountantMenu(frame, accountant);
			}
		});
				
		// Sign Out Button
		JButton btn_sign_out = new JButton("Sign Out");
		btn_sign_out.setBackground( new Color(0, 204, 153) );
		btn_sign_out.setForeground(Color.white);
		btn_sign_out.setBounds(650,30,100, 30);
		f.add(btn_sign_out);
		
		// function to be executed when Sign Out Button is clicked
		btn_sign_out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Login_Account user = new Login_Account();
				openSignInForm(frame, user);
			}
		});
		
		// displaying the new panel on frame	
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}
	
	
	
	// end of class
}
