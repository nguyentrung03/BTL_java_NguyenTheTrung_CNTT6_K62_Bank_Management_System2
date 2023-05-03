package def_pkg;

public class Bank_Account {
	private String acc_num;
	private String client_id;
	private String login_id;
	private String type;
	private String balance;
	private String status;
	private String opening_date;
	private String closing_date;
	private String card_num;

	//Constructor
	public Bank_Account() {
		acc_num = "";
		client_id = ""; 
		login_id = "";
		type = "";
		balance = "";
		status = "";
		opening_date = "";
		closing_date = "";
		card_num = "";
	}

	public Bank_Account(String acc_num, String client_id, String login_id, String type, String balance, String status,
							String opening_date, String closing_date, String card_num) {
		this.acc_num = acc_num;
		this.client_id = client_id;
		this.login_id = login_id;
		this.type = type;
		this.balance = balance;
		this.status = status;
		this.opening_date = opening_date;
		this.closing_date = closing_date;
		this.card_num = card_num;
	}
	
	// updates
	
	public void addInfo(String acc_num, String client_id, String login_id, String type, String balance, String status,
							String opening_date, String closing_date, String card_num) {
		this.acc_num = acc_num;
		this.client_id = client_id;
		this.login_id = login_id;
		this.type = type;
		this.balance = balance;
		this.status = status;
		this.opening_date = opening_date;
		this.closing_date = closing_date;
		this.card_num = card_num;
	}

	
	//getter and setter
	
	public String getAccountNum() {
		return acc_num;
	}

	public void setAccountNum(String acc_num) {
		this.acc_num = acc_num;
	}

	public String getClientId() {
		return client_id;
	}


	public String getLoginId() {
		return login_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOpeningDate() {
		return opening_date;
	}


	public String getClosingDate() {
		return closing_date;
	}

	public void setClosingDate(String closing_date) {
		this.closing_date = closing_date;
	}

	public String getCardNum() {
		return card_num;
	}

	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}
	
	public int addAmount(int b) {
		DB_Handler db = new DB_Handler();
		int i = db.updateBalance( this, b, 1 );
		if( i == 1) {
			b += Integer.valueOf(this.balance);  //Interger.valueOf() - đổi sang kiểu nguyên
			balance = String.valueOf(b);
		}
		return i;
		
	}
	
	public int removeAmount(int b) {
		if( b > Integer.valueOf(this.balance) ) {
			return 2;
		}
		DB_Handler db = new DB_Handler();
		b *= -1;
		int i =  db.updateBalance( this, b, 2 );
		if( i == 1 ) {
			b += Integer.valueOf( this.balance );
			balance = String.valueOf(b);
		}
		return i;
	}
	
	public void updateBalance() {
			DB_Handler db = new DB_Handler();
			String b = db.getBalance(this.acc_num);
			if(!b.equals("")) {
				this.balance = b;
			}
		
	}
	
	public void finalize() {
		System.out.println("Bank Account Destructor");
	}

}
