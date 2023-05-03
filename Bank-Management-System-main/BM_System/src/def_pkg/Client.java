package def_pkg;
import java.util.List;
public class Client {
	private String client_id;
	private String f_name;
	private String l_name;
	private String father_name;
	private String mother_name;
	private String CIC;
	private String DOB;
	private String phone;
	private String email;
	private String address;

	//Constructor
	
	public Client() {
			client_id = "";
			f_name = "";
			l_name = "";
			father_name = "";
			mother_name = "";
			CIC = ""; 
			DOB = "";
			phone = "";
			email = "";
			address = "";
	}

	//
	public Client( String f_name, String l_name, String father_name, String mother_name, String cic,
			String dob, String phone, String email, String address) {
		client_id = "";
		this.f_name = f_name;
		this.l_name = l_name;
		this.father_name = father_name;
		this.mother_name = mother_name;
		CIC = cic;
		DOB = dob;
		this.phone = phone;
		this.email = email;
		this.address = address;
	}

	//
	public Client(String client_id, String f_name, String l_name, String father_name, String mother_name, String cic,
			String dob, String phone, String email, String address) {
		this.client_id = client_id;
		this.f_name = f_name;
		this.l_name = l_name;
		this.father_name = father_name;
		this.mother_name = mother_name;
		CIC = cic;
		DOB = dob;
		this.phone = phone;
		this.email = email;
		this.address = address;
	}
	
	void showClientInfo() {
		System.out.println("First Name: "+f_name);
		System.out.println("Last Name; "+l_name);
		System.out.println("Father Name: "+father_name);
		System.out.println("Mother Name: "+mother_name);
		System.out.println("CIC Name: "+CIC);
		System.out.println("DOB Name: "+DOB);
		System.out.println("Phone Name: "+phone);
		System.out.println("Email Name: "+email);
		System.out.println("Address Name: "+address);
	}
	
	//getter

	public String getClientID() {
		return client_id;
	}

	public String getFName() {
		return f_name;
	}

	public String getLName() {
		return l_name;
	}

	public String getFatherName() {
		return father_name;
	}

	public String getMotherName() {
		return mother_name;
	}

	public String getCIC() {
		return CIC;
	}

	public String getDOB() {
		return DOB;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}
	
	//Chuyển tiền
	
	public int tranferMoney( String recv_acc, int amount) {  //recv_acc - SoTaiKhoan
		DB_Handler db = new DB_Handler();
		return db.TransferMoney(this, recv_acc, amount);
	}
	
	//Rut tien khong can the
	String do_card_cash_withdrawal(Bank_Account account, String amount, String pin) {
		DB_Handler db = new DB_Handler();
		if(db.is_card_active(Integer.parseInt(account.getCardNum())))
		{
				int curr_balance = Integer.parseInt(account.getBalance());   //curr_balance: So du hien tai
				int requested_amount = Integer.parseInt(amount);   //So tien yeu cau
				if(curr_balance >= requested_amount)
				{
						String temp_num = db.add_cardless_entry(Integer.parseInt(account.getAccountNum()), Integer.parseInt(account.getCardNum()), requested_amount, pin );
						if (!temp_num.equals(""))
						{
							db.reduce_balance(curr_balance-requested_amount, Integer.parseInt(account.getAccountNum()));
							return temp_num;										// 0 : thanh cong
						}
						else
								return "c";											// -3 : khong thanh cong
				}
				else
				{
					return "b";														// -2 : so du thap hon
				}
		}
		else
		{
			 return "a";															// -1 : thẻ bị chặn
		}
	}
	
	//Doi MK
	int change_password(String curr_pass, String new_pass_1, String new_pass_2, String acc_num)
	{
		DB_Handler db = new DB_Handler();
		int acc_no = Integer.parseInt(acc_num);
		int login_id = db.getLoginID(acc_no);
		if(login_id == -1)
		{
				return -1;						//khong tim thay thong tin
		}
		else if (!curr_pass.equals(db.get_password(login_id)))
		{
				return -2;						//Sai MK
		}
		else if(new_pass_1.equals(new_pass_2))		//khop nhau
		{
			db.change_password(new_pass_1, login_id);
			return 0;
		}
		else								//Khong khop nhau
		{
			return -3;
		}
	}

	
	public List<Transaction_History> getTransactions( String acc_num, String From, String To) {
		DB_Handler db = new DB_Handler();
		List<Transaction_History> list = db.getTransactions( acc_num, From, To);
		return list;
	}
	
	
	
	//end
}
