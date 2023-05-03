package def_pkg;

import java.util.List;

public class Accountant {
	
		public String name;
		
		public Accountant() {
			this.name="";
		}
		
		public Accountant( String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
		
		// phuong thuc search account
		Bank_Account searchAccount1(String accountNum, String CIC) {
			DB_Handler db = new DB_Handler();
			Bank_Account account = db.searchAccount1(accountNum, CIC);
			db = null;
			System.out.println("Account infoACC: "+account.getType());
			return account;
		}
		
		// pt search Client
		public Client searchClient1(String accountNum, String CIC) {
			DB_Handler db = new DB_Handler();
			Client client = db.searchClient1( accountNum, CIC);
			db = null;
			return client;
		}
		//Kiểm tra khoản tiền gửi bằng Séc
		public int chequeDeposit(String accNum, String chequeNum, int amount ) {
			DB_Handler db = new DB_Handler();
			return db.chequeDeposit(accNum, chequeNum, amount);
		}
		
		public List<Transaction_History> getTransactions( String acc_num, String From, String To){
			DB_Handler db = new DB_Handler();
			List<Transaction_History> list = db.getTransactions( acc_num, From, To);
			return list;
		}
		// end
}
