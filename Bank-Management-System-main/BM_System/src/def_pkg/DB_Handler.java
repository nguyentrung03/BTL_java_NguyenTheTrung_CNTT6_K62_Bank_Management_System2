package def_pkg;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.itextpdf.html2pdf.HtmlConverter;



public class DB_Handler {
	
	private String url ="jdbc:mysql:///bank_schema";
	private String username = "root"; 
	private String password = "0358469736";
	private Connection conn;
	
	
	
	public DB_Handler() {
	try {
		conn = DriverManager.getConnection(url, username, password); 
		System.out.println("Connection successfully!");
	}
	catch (SQLException e) {
		throw new IllegalStateException("Unable to connect to the database. " +e.getMessage());
	}
	}
	
	
	
	
	public Login_Account signIn( String username, String passsword ) {
		Login_Account user = new Login_Account();
		try {
				//finding the account in database
			String laQuery = "SELECT * From bank_schema.login_account Where username = \""
								+username+"\" and " +"password= \""+passsword+"\"";
			System.out.println(laQuery);
			Statement laSt = conn.createStatement();
			ResultSet laRs = laSt.executeQuery(laQuery);
			if( laRs.next() ) {
			// Xoa thong tin cu, them thong tin moi
				user = new Login_Account(laRs.getString("login_id"), laRs.getString("username"), "", laRs.getString("type") );
			}
		}
		catch(SQLException e) {
			System.out.println("Đã xảy ra lỗi khi kiểm tra xem tài khoản có tồn tại không");
		}
		return user;
	}
	
	
	public Client getClient( String login_id ) {
		Client client =new Client();
		try {
			// finding the account in database
			String cQuery = "Select * From bank_schema.client Where client_id = (select client_id from bank_schema.bank_account where "
					+ "login_id = "+login_id+")";
			System.out.println(cQuery);
			Statement cSt = conn.createStatement();
			ResultSet cRs = cSt.executeQuery(cQuery);
			if( cRs.next() ) {
				// Xoa thong tin cu, them thong tin moi
				client = new Client( cRs.getString("client_id"),cRs.getString("f_name"), cRs.getString("l_name"), cRs.getString("father_name"), cRs.getString("mother_name"), cRs.getString("CIC"), cRs.getString("DOB"), cRs.getString("phone"), cRs.getString("email"), cRs.getString("address") );
				
			}
		}
		catch(SQLException e) {
				System.out.println("Đã xảy ra lỗi khi kiểm tra xem tài khoản có tồn tại không");
		}
		return client;
	}
	
	
	
	public Bank_Account getAccount( String login_id ) {
		Bank_Account account = new Bank_Account();
		try {
			// finding the account in database
			String aQuery = "Select * from bank_schema.bank_account where login_id = "+login_id;
			System.out.println(aQuery);
			Statement aSt = conn.createStatement();
			ResultSet aRs = aSt.executeQuery(aQuery);
			if( aRs.next() ) {
	        	// Xoa thong tin cu, them thong tin moi
				account = new Bank_Account( aRs.getString("acc_num"), aRs.getString("client_id"), aRs.getString("login_id"), aRs.getString("type"),
						aRs.getString("balance"), aRs.getString("status"), aRs.getString("opening_date"), aRs.getString("closing_date"), aRs.getString("card_num") );
	        }
		}
		catch (SQLException e){
			System.out.println("Đã xảy ra lỗi khi kiểm tra xem tài khoản có tồn tại không");
		}
		return account;
	}
	
	
	
	
	public boolean is_card_active(int card_num)
	{
		try {
			String uaQuery = "Select * From bank_schema.card Where card_num = "+ card_num;
			System.out.println(uaQuery);
			Statement uaSt = conn.createStatement();
			ResultSet uaRs = uaSt.executeQuery(uaQuery);
		if( uaRs.next() )
		{
			String card_status = uaRs.getString("status");
			if( card_status.equals("A") || card_status.equals("a") )
			{
				return true;
			}
			else     // status = B -- Thẻ bị khóa
			{
				System.out.println("card_num: "+ card_num + "bị chặn vì trạng thái không phải là A cũng không phải là a");
				return false;
			}
		}
		else
				return false;
		}
		catch (SQLException e)
		{
			System.out.println("Đã xảy ra lỗi khi xác minh cic");
		}
		return false;
	}
	
	

	public String add_cardless_entry(int acc_num, int card_num, int amount, String temp_pin)
	{
		Random rand = new Random();
		String temp_OTP = ( String.valueOf( (rand.nextInt(9999)+1) ) + String.valueOf( (rand.nextInt(9999)+1) ) + String.valueOf( (rand.nextInt(9999)+1) ) );
		try
		{
			String uaQuery = "Insert into bank_schema.cardless_withdrawl values (NULL, " +card_num +", " +amount +", \"" + temp_OTP 
							+ "\", \"" +temp_pin +"\", \"p\", CURDATE(), CURRENT_TIME() )";																	
			System.out.println(uaQuery);
			Statement uaSt = conn.createStatement();
			uaSt.executeUpdate(uaQuery);
		}
		catch (SQLException e)
		{
			System.out.println("Đã xảy ra lỗi khi thêm vào bảng entry to cardless");
		}
		
		try
		{
			String uaQuery = "Select * from bank_schema.cardless_withdrawl where card_no = " + card_num + " and amount = " + 
							amount + " and temp_pin = \"" + temp_pin + "\"";			
			System.out.println(uaQuery);
			Statement uaSt = conn.createStatement();
			ResultSet uaRs = uaSt.executeQuery(uaQuery);
			if (uaRs.next())
			{
				int temp_serial = uaRs.getInt("serial_no");
				if (temp_serial != 0)
					return temp_OTP;
				else
					return "";
			}
		}
		catch (SQLException e)
		{
			System.out.println("Đã xảy ra lỗi khi truy xuất OTP từ cơ sở dữ liệu");
		}
		return "";
	}
	
	
	
	public void reduce_balance(int amount, int acc_num)
	{
		try
		{
			String uaQuery = "Update bank_schema.bank_account set balance = " + amount + " where acc_num = " + acc_num;			
			System.out.println(uaQuery);
			Statement uaSt = conn.createStatement();
			uaSt.executeUpdate(uaQuery);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	
	
	public int get_client_id(int acc_num)
	{
		try
		{
			String uaQuery = "Select client_id From bank_schema.bank_account Where acc_num = "+ acc_num;
			System.out.println(uaQuery);
			Statement uaSt = conn.createStatement();
			ResultSet uaRs = uaSt.executeQuery(uaQuery);
	        if( uaRs.next() )
	        {
	        	return uaRs.getInt("client_id");	
	        }
	        else
	        	return -1;
		}
		catch (SQLException e) {
			System.out.println("Đã xảy ra lỗi khi lấy client_id");
		}
		return -1;
	}
	
	
	
	public int get_account_status(int acc_num)
	{
		try {	
			// Kiểm tra xem khách hàng đã tồn tại chưa
			String uaQuery = "Select * From bank_schema.bank_account Where acc_num = " + acc_num;
			System.out.println(uaQuery);
			Statement uaSt = conn.createStatement();
			ResultSet uaRs = uaSt.executeQuery(uaQuery);
	        if( uaRs.next() ) {
	        		return uaRs.getInt("status");
	        }
	        else 
	        	return -1;
		}
		catch (SQLException e) {
			System.out.println("Đã xảy ra lỗi khi lấy account_type");
		}
		return -1;
	}
	
	
	
	public String get_cic(int client_id)
	{
		try
		{
			String uaQuery = "Select * From bank_schema.client Where client_id = "+ client_id;
			System.out.println(uaQuery);
			Statement uaSt = conn.createStatement();
			ResultSet uaRs = uaSt.executeQuery(uaQuery);
	        if( uaRs.next() )
	        {
	        	return uaRs.getString("CIC"); 
	        }
	        else
	        	return "";
		}
		catch (SQLException e)
		{
			System.out.println("Đã xảy ra lỗi khi xác minh cic");
		}
		return "";
	}
	
	
	
	public void block_account(int acc_num)
	{
		try {	
			// Kiểm tra xem khách hàng đã tồn tại chưa
			String uaQuery = "Update bank_schema.bank_account set status = 2 Where acc_num = " + acc_num;
			System.out.println(uaQuery);
			Statement uaSt = conn.createStatement();
			uaSt.executeUpdate(uaQuery);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	
	public void unblock_account(int acc_num)
	{
		try {
		// Kiểm tra client đã tồn tại chưa
			String Query = "Update bank_schema.bank_account set status = 1 Where acc_num =" +acc_num;
			System.out.println(Query);
			Statement St = conn.createStatement();
			St.executeUpdate(Query);
			
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	
	
	public int get_card_num(int acc_num)
	{
		try
		{
			String Query ="SELECT * From bank_schema.bank_account WHERE acc_num = "+ acc_num;
			System.out.println(Query);
			Statement St = conn.createStatement();
			ResultSet Rs = St.executeQuery(Query);
		if(Rs.next())
		{
			return Rs.getInt("card_num");
		}
		else
			return 0;
		}
		catch (SQLException e)
		{
			System.out.println("Đã xảy ra lỗi khi xác minh CIC");
		}
		return 0;
	}
	
	
	
	public void block_card(int card_num)
	{
		try
		{
			 String Query = "Update bank_schema.card set status=\"B\" Where card_num = " + card_num;
			 System.out.println(Query);
			 Statement St = conn.createStatement();
			 St.executeUpdate(Query);
		}
		catch (SQLException e)
		{
			System.out.println("Đã xảy ra lỗi khi khóa thẻ");
		}
	}
	
	
	
	public void unblock_card(int card_num)
	{
		try
		{
			 String Query = "Update bank_schema.card set status=\"A\" Where card_num = " + card_num;
			 System.out.println(Query);
			 Statement St = conn.createStatement();
			 St.executeUpdate(Query);
		}
		catch (SQLException e)
		{
			System.out.println("Đã xảy ra lỗi khi mở khóa thẻ");
		}
	}
	
	
	
	public void close_account(int acc_num)
	{
		try
		{
			String Query ="Update bank_schema.bank_account set status = 0 Where acc_num = " + acc_num;
			System.out.println(Query);
			Statement St = conn.createStatement();
			St.executeUpdate(Query);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	
	
	public boolean login_exists(int client_id)
	{
		try
		{
			String Query = "SELECT * From bank_schema.bank_account Where client_id = " + client_id;
			System.out.println(Query);
			Statement St = conn.createStatement();
			ResultSet Rs = St.executeQuery(Query);
		if( Rs.next() )
		{
			int temp_login_id = Rs.getInt("login_id");
			System.out.println("id login của client với client id=" +client_id +" is: " +temp_login_id);
			if(temp_login_id == 0)
				return true;
			else
				return false;
		}
		else
			return false;
		}
		catch (SQLException e)
		{
			System.out.println("Có lỗi xảy ra khi kiểm tra tồn tại của login account");
		}
		return false;
	}
	
	
	
	public boolean verify_cic(int client_id, String cic)
	{
		try
		{
			String Query = "Select * From bank_schema.client Where client_id = " + client_id;
			System.out.println(Query);
			Statement St = conn.createStatement();
			ResultSet Rs = St.executeQuery(Query);
		if (Rs.next())
		{
			String temp_cic = Rs.getString("CIC");
			System.out.println("CLient id=" + client_id + "\tCIC=" +temp_cic);
			if( temp_cic.equals(cic))
				return true;
			else
				return false;
		}
		else
				return false;
		}
		catch (SQLException e)
		{
			System.out.println("Đã xảy ra lỗi khi xác minh CIC");
		}
		return false;
	}
	
	
	
	public int create_login(String username, String password)
	{
		try
		{
			String Query ="INSERT INTO bank_schema.login_account VALUES(NULL,\"" +username +"\",\"" +password +"\",\"C\");";
			System.out.println(Query);
			Statement St = conn.createStatement();
			St.executeUpdate(Query);
		}
		catch (SQLException e)
		{
			throw new IllegalStateException("Không thể thêm giá trị vào login_account " + e.getMessage());
			
		}
		try
		{
			String Query = "SELECT login_id FROM bank_schema.login_account WHERE username =\"" + username + "\" and password = \"" +password +"\"";
			System.out.println(Query);
			Statement St = conn.createStatement();
			ResultSet Rs = St.executeQuery(Query);
			if(Rs.next())
			{
				return Rs.getInt("login_id");
			}
			else
				return-1;	
		}
		catch(SQLException e)
		{
			System.out.println("Đã xảy ra lỗi khi thêm login account");
		}
		return -1;
	}
	
	
	
	public void set_login_id(int login_id, int client_id)
	{
		try
		{
			String Query = "update bank_schema.bank_account set login_id = " + login_id +"	where client_id = " +client_id;
			System.out.println(Query);
			Statement St = conn.createStatement();
			St.executeUpdate(Query);
			
			String Query1 = "select login_id from bank_schema.bank_account where client_id = " +client_id;
			System.out.println(Query1);
			Statement St1 = conn.createStatement();
			ResultSet Rs1 = St1.executeQuery(Query);
		if(Rs1.next())
		{
			System.out.println("login_id của tài khoản này là " +Rs1.getInt("login_id"));	
		}
		else
		{
			System.out.println("Không thể tìm thấy id đăng nhập");
			return;
		}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	

	
	public int DB_CreateAccount( Client new_client, String aType ) {
		int res = 0;
		try {
			// Check if client already exists
			String dQuery = "Select * From bank_schema.client Where cic = \""+new_client.getCIC()+"\"";
			System.out.println(dQuery);
			Statement dSt = conn.createStatement();
			ResultSet dRs = dSt.executeQuery(dQuery);
	        int dRecord = 0;
	        
	        while( dRs.next() )
	        	dRecord++;
	        System.out.println(dRecord);
			if( dRecord < 1 ) {  
				System.out.println("Không có bản ghi trùng lặp");
				
				// Insert new client
				String ciQuery = "Insert Into bank_schema.client Values(NULL, \""+new_client.getFName()+"\", \""+
						new_client.getLName()+"\", \""+new_client.getFatherName()+"\", \""+
						new_client.getMotherName()+"\", \""+new_client.getCIC()+"\", STR_TO_DATE(\""+
						new_client.getDOB()+"\", \"%d,%m,%Y\"), \""+
						new_client.getPhone()+"\", \""+new_client.getEmail()+"\", \""+new_client.getAddress()+"\")";
				System.out.println(ciQuery);
				Statement ciSt = conn.createStatement();
		        ciSt.executeUpdate(ciQuery);
		        
		        // Find the client id
				String cQuery = "Select * From bank_schema.client Where cic = \""+new_client.getCIC()+"\"";
				Statement cSt = conn.createStatement();
				ResultSet cRs = cSt.executeQuery(cQuery);
				if( cRs.next() ) {
					System.out.println("Client was added "+cRs.getString("client_id"));
					int c_id = cRs.getInt("client_id");
					// Make client's bank account
					String baQuery = "Insert Into bank_schema.bank_account Values(NULL, "+String.valueOf(c_id)+", NULL, \""+
							aType+"\", 0, 1, CURDATE(), NULL, NULL)";
					System.out.println(baQuery);
					Statement baSt = conn.createStatement();
			        baSt.executeUpdate(baQuery);
			        
			        res = 1;
				}   
			}
			else 
				res = 2;
		}
		catch (SQLException e) {
			System.out.println("Đã lỗi xảy ra ở DB_CreateAccount");
		}
		return res;
	}
	
	
	
	public int TransferMoney( Client client, String rAccNum, int amount) {
		try {
			//Kiểm tra client đã tồn tại chưa
			String Query = "SELECT acc_num, balance From bank_schema.bank_account Where acc_num =" + rAccNum + " and status = 1" ;
			System.out.println(Query);
			Statement St = conn.createStatement();
			ResultSet Rs = St.executeQuery(Query);
		if(Rs.next())
		{
			int recv_balance = Rs.getInt("balance");
			String bQuery = "Select acc_num, balance From bank_schema.bank_account Where client_id = " +client.getClientID();
			System.out.println(bQuery);
			Statement bSt = conn.createStatement();
			ResultSet bRs = bSt.executeQuery(bQuery);
			if(bRs.next())
			{
				String snd_acc_num = bRs.getString("acc_num");
				int snd_balance  = bRs.getInt("balance");
				if( snd_balance >= amount ) {
					snd_balance -= amount;
					recv_balance += amount;
					
					String usQuery = "Update bank_schema.bank_account Set balance = "+snd_balance+" where client_id=" +client.getClientID();
					System.out.println(usQuery);
					Statement usSt = conn.createStatement();
					usSt.executeUpdate(usQuery);
					
					String urQuery = "Update bank_schema.bank_account Set balance = " +recv_balance + " where acc_num =" +rAccNum;
					System.out.println(urQuery);
					Statement urSt = conn.createStatement();
					urSt.executeUpdate(urQuery);
					
					//insert into transaction history
					String thQuery = "Insert into bank_schema.transaction_history values(NULL,"+String.valueOf(amount)+", \"transfer\", "
							+ "CURDATE(), CURRENT_TIME(), "+snd_acc_num+", "+rAccNum+" , NULL )";
					System.out.println(thQuery);
					Statement thSt = conn.createStatement();
					thSt.executeUpdate(thQuery);
					
					return 3;
				}
				else
					return 2;
			}
		}
		else
				return 1;
		}
		catch (SQLException e) {
			System.out.println("Đã xảy ra lỗi khi đăng nhập");
		}
		
		return 0;
	}
	

	
	// Kiểm tra xem có tài khoản nào có accountNum và CIC trong database không, trả về tài khoản nếu tìm thấy
		Bank_Account searchAccount1( String accountNum, String CIC ) {
			Bank_Account account  = new Bank_Account();
			try {
					//Tìm account trong database
				String aQuery = "Select * From bank_schema.bank_account Where acc_num =" + accountNum  +" and "
								+ "client_id = (select client_id from bank_schema.client where CIC =\""+CIC+"\")";
				System.out.println(aQuery);
				Statement aSt = conn.createStatement();
				ResultSet aRs = aSt.executeQuery(aQuery);
				
				if( aRs.next() ) {
					// Xóa thông tin cũ, thêm vào thông tin mới của account
					account = new Bank_Account( aRs.getString("acc_num"), aRs.getString("client_id"), aRs.getString("login_id"), aRs.getString("type"), 
							aRs.getString("balance"), aRs.getString("Status"), aRs.getString("opening_date"), aRs.getString("closing_date"), aRs.getString("card_num") );
					
				}
				return account;
			}
			catch(SQLException e) {
				System.out.println(e);
				return account;
			}
		}

	
	
		// Kiểm tra xem có client nào có accountNum và CIC trong database không, trà về thông tin client nếu tìm thấy thấy
		Client searchClient1( String accountNum, String CIC ) {
			Client client = new Client();
			try {
					//Tìm client trong database
				String Query = "Select * From bank_schema.client Where CIC = \"" +CIC+"\" and (Select count(*)"
						+ "from bank_schema.bank_account where acc_num = "+accountNum +") =1";
				System.out.println(Query);
				Statement St = conn.createStatement();
				ResultSet Rs = St.executeQuery(Query);
				
				if(Rs.next()) {
					//Xóa thông tin Client cũ, thêm thông tin client mới
					client = new Client(Rs.getString("client_id"), Rs.getString("f_name"), Rs.getString("l_name"), Rs.getString("father_name"),
							Rs.getString("mother_name"), Rs.getString("CIC"), Rs.getString("DOB"), Rs.getString("phone"), Rs.getString("email"), Rs.getString("address"));
				}
				return client;
			}
			catch (SQLException e) {
				System.out.println("Đã xảy ra lỗi khi kiểm tra client có tồn tại không");
				return client;
			}
		}
	
	
	
		// Kiểm tra xem có Account nào  có account Num trong database không, trả về Account nếu tìm thấy
		Bank_Account searchAccount2( String accountNum ) {
				Bank_Account account = new Bank_Account();
				try {
					//Tìm account trong database
					String Query = "Select * From bank_schema.bank_account Where acc_num ="+accountNum;
					System.out.println("Query");
					Statement St = conn.createStatement();
					ResultSet Rs = St.executeQuery(Query);
					
					if(Rs.next()) {
						//Xóa Account cũ, thêm thông tin mới
						account = new Bank_Account( Rs.getString("acc_num"), Rs.getString("client_id"), Rs.getString("login_id"), Rs.getString("type"), 
								Rs.getString("balance"), Rs.getString("status"), Rs.getString("opening_date"), Rs.getString("closing_date"), Rs.getString("card_num") );
						
					}
					return account;
				}
				catch (SQLException e) {
					System.out.println("Đã xảy ra lỗi khi kiểm tra account có tồn tại không");
					return account;
				}
		}

	
	
		// Kiểm tra xem có Client nào có accountNum và CIC trong database không, trả về Thông tin Client nếu tìm thấy
		Client searchClient2( String accountNum ) {
				Client client = new Client();
				try {
						// Tìm Client trong database
					String Query = "Select *From bank_schema.client Where client_id = ( select client_id"
							+ " from bank_schema.bank_account where acc_num =" +accountNum+") = 1";
					System.out.println(Query);
					Statement St =conn.createStatement();
					ResultSet Rs =St.executeQuery(Query);
					
					if(Rs.next()) {
						//Xoas client cũ, thêm client mới
						client = new Client( Rs.getString("client_id"), Rs.getString("f_name"), Rs.getString("l_name"), Rs.getString("father_name"), Rs.getString("mother_name"), 
								Rs.getString("CIC"), Rs.getString("DOB"), Rs.getString("phone"), Rs.getString("email"), Rs.getString("address") );
					}
					return client;
				}
				catch(SQLException e) {
					System.out.println("Đã xảy ra lỗi khi kiểm tra client có tồn tại không");
					return client;
				}
		}
	
	
	
		public int updateBalance( Bank_Account account, int balance, int t ) {
			int res = 0;
			try {	
				// Cập nhật số dư tài khoản
				int temp_balance = Integer.valueOf( account.getBalance() ) + balance;
				String bsQuery = "Update bank_schema.bank_account Set balance = "+temp_balance+" where acc_num="+account.getAccountNum();		
				System.out.println("SQL-> "+bsQuery);
				Statement bsSt = conn.createStatement();
		        bsSt.executeUpdate(bsQuery);
		        
		        // kiểm tra xem nó được cập nhật thành công chưa
	        	String bQuery = "Select balance From bank_schema.bank_account Where acc_num = "+account.getAccountNum();
				System.out.println(bQuery);
				Statement bSt = conn.createStatement();
				ResultSet bRs = bSt.executeQuery(bQuery);
				if( bRs.next() ) {
					int b = bRs.getInt("balance");
					if( b == temp_balance ) {
						
						String type_ = "deposit";
						if( t == 2 ) {
							type_ = "withdraw";
							balance *= -1;
						}
						//thực hiện một mục trong lịch sử giao dịch
						String thQuery = "Insert into bank_schema.transaction_history values(NULL,"+balance+", \""+type_+"\", CURDATE(), CURRENT_TIME(), "+account.getAccountNum()+", NULL, NULL  )";		
						System.out.println("SQL-> "+thQuery);
						Statement thSt = conn.createStatement();
				        bsSt.executeUpdate(thQuery);
			
						res = 1;
					}
				}
		        return res;
			}
			catch (SQLException e) {
				System.out.println("Đã xảy ra lỗi khi kiểm tra account có tồn tại không");
				return res;
			}	
		}
	
	
	
	public List<Transaction_History> getTransactions(String accNum, String From, String To) {
		List<Transaction_History> list=new ArrayList<Transaction_History>();  
		try {	
			// tìm giao dịch
			String tQuery = "Select * From bank_schema.transaction_history Where ( account_num = '"+accNum+"' or recv_acc_num = '"+accNum+"' ) and ( date between '"+From+"'"
					+ " and '"+To+"')"; 
			System.out.println(tQuery);
			Statement tSt = conn.createStatement();
			ResultSet tRs = tSt.executeQuery(tQuery);
			while( tRs.next() ) {
	        	//xóa phiên bản cũ của khách hàng và thêm phiên bản mới với thông tin
				Transaction_History th = new Transaction_History( tRs.getString("serial_no"), tRs.getString("amount"), tRs.getString("type"), tRs.getString("date"), 
										tRs.getString("time"), tRs.getString("account_num"), tRs.getString("recv_acc_num"), tRs.getString("cheque_num") );
				System.out.print("@-");
				list.add( th );
	        }
		}
		catch (SQLException e) {
			System.out.println("Đã xảy ra sự cố");
		}
		return list;
	}
	
	
	

	
	
	
	//Kiểm tra tiền gửi
		public int chequeDeposit( String accNum, String chequeNum, int amount ) { 
			int res = 0;
			try {
					////Tìm account trong database
				String aQuery = "Select * From bank_schema.bank_account Where acc_num =" + accNum;
				System.out.println(aQuery);
				Statement St =conn.createStatement();
				ResultSet Rs = St.executeQuery(aQuery);
				if(Rs.next()) {
					if( Integer.valueOf(Rs.getString("status")) == 0 )
					{
						res = 4; //closed
					}
					else if( Integer.valueOf(Rs.getString("status")) == 2 )
					{
						res = 3; //blocked
					}
					else {
						int balance = amount + Integer.valueOf(Rs.getString("balance") );
						String bsQuery = "Update bank_schema.bank_account Set balance = "+balance+" where acc_num="+accNum;		
						System.out.println("SQL-> "+bsQuery);
						Statement bsSt = conn.createStatement();
				        bsSt.executeUpdate(bsQuery);
					
				        // thực hiện một mục trong lịch sử giao dịch
						String thQuery = "Insert into bank_schema.transaction_history values(NULL,"+balance+", \"deposit\", CURDATE(), CURRENT_TIME(), "+accNum+", NULL, "+chequeNum+")";		
						System.out.println("SQL-> "+thQuery);
						Statement thSt = conn.createStatement();
						thSt.executeUpdate(thQuery);
					
				        res = 1;  // thành công
					}
				}
				else 
					res = 2; // not found
			}
			catch (SQLException e) {
				System.out.println("Đã xảy ra sự cố");
			}
			return res;
		}
	
	
	
	// update client info
	public void updateClientInfo(String client_id, String phone, String email, String address ) {
	 try {   
		 	String ucQuery = "Update bank_schema.client Set phone = \""+phone+"\" , email = \""+email+"\" , address = \""+ address
		 			+ "\" where client_id="+client_id;		
			System.out.println( ucQuery );
			Statement ucSt = conn.createStatement();
	        ucSt.executeUpdate(ucQuery);
        }
        catch (SQLException e) {
        	System.out.println("Đã xảy ra sự cố");
        }
	}
	
	public int getLoginID(int acc_num)
	{
		try
		{
			String uaQuery = "Select login_id From bank_schema.bank_account Where acc_num = "+ acc_num;
			System.out.println(uaQuery);
			Statement uaSt = conn.createStatement();
			ResultSet uaRs = uaSt.executeQuery(uaQuery);
	        if( uaRs.next() )
	        {
	        	return uaRs.getInt("login_id");	
	        }
	        else
	        	return -1;
		}
		catch (SQLException e) {
			System.out.println("Đã xảy ra sự cố trong khi lấy login_id");
		}
		return -1;
	}
	
	public String get_password(int login_id)
	{
		try {	
			// Kiểm tra xem khách hàng đã tồn tại chưa
			String uaQuery = "Select * From bank_schema.login_account Where login_id = " + login_id;
			System.out.println(uaQuery);
			Statement uaSt = conn.createStatement();
			ResultSet uaRs = uaSt.executeQuery(uaQuery);
	        if( uaRs.next() ) {
	        		return uaRs.getString("password");
	        }
	        else 
	        	return "";
		}
		catch (SQLException e) {
			System.out.println("Đã xảy ra lỗi khi lấy mật khẩu hiện tại");
		}
		return "";
	}
	
	
	public String getName(String id) {
		String name = "";
		try
		{
			String eQuery = "Select * From bank_schema.employee Where login_id = "+ id;
			System.out.println(eQuery);
			Statement eSt = conn.createStatement();
			ResultSet eRs = eSt.executeQuery(eQuery);
	        if( eRs.next() )
	        {
	        	name = eRs.getString("f_name") + " " + eRs.getString("l_name");
	        }
		}
		catch (SQLException e)
		{
			System.out.println("Đã xảy ra lỗi khi xác minh cic");
		}
		return name;
	}
	
	public String getBalance(String acc_num) {
		String b = "";
		try {	
			// finding the account in database
			String aQuery = "Select * From bank_schema.bank_account Where acc_num = "+acc_num;
			System.out.println(aQuery);
			Statement uaSt = conn.createStatement();
			ResultSet uaRs = uaSt.executeQuery(aQuery);
			if( uaRs.next() ) {
	        	 b = uaRs.getString("balance");
	        }
		}
		catch (SQLException e) {
			System.out.println("Đã xảy ra lỗi khi kiểm tra xem tài khoản có tồn tại không");
		}
		return b;
	}
	
	public String getAccNum(String CIC) {
		String b = "";
		try {	
			String aQuery = "Select * From bank_schema.bank_account Where client_id = (select client_id from bank_schema.client where CIC=\""+CIC+"\")";
			System.out.println(aQuery);
			Statement uaSt = conn.createStatement();
			ResultSet uaRs = uaSt.executeQuery(aQuery);
			if( uaRs.next() ) {
	        	 b = uaRs.getString("acc_num");
	        }
		}
		catch (SQLException e) {
			System.out.println("Đã xảy ra lỗi khi kiểm tra xem tài khoản có tồn tại không");
		}
		return b;
	}
	
	public void change_password(String pass, int login_id)
	{
		try {	
			// Kiểm tra xem khách hàng đã tồn tại chưa
			String uaQuery = "Update bank_schema.login_account set password = \"" + pass + "\" Where login_id = " + login_id;
			System.out.println(uaQuery);
			Statement uaSt = conn.createStatement();
			uaSt.executeUpdate(uaQuery);
		}
		catch (SQLException e) {
			System.out.println("Đã xảy ra lỗi khi lấy mật khẩu hiện tại");
		}
	}
	public void finalize() {
        try {   
        	System.out.println("Kết nối bị đóng");
            conn.close();
        }
        catch (SQLException e) {
            throw new IllegalStateException("Cố gắng đóng kết nối db chưa mở" + e.getMessage());
        }
	}
	
}
