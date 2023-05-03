package def_pkg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountsFromFile {
	
	
    public void importAccountsFromFile(String fileName) {
 
        ExecutorService executorService = Executors.newFixedThreadPool(5); // Số lượng luồng đọc file
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                // Tạo một Runnable task để xử lý dữ liệu từng dòng trong file
                Runnable task = new ProcessDataRunnable(line);
                // Thêm task vào thread pool để thực thi đa luồng
                executorService.execute(task);
            }
            br.close();
        } catch (Exception e) {
 
            e.printStackTrace();
    
        } finally {
            // Đóng thread pool sau khi hoàn thành công việc
            executorService.shutdown();
        }
  
    }
  

    static class ProcessDataRunnable implements Runnable {
        private String line;

        public ProcessDataRunnable(String line) {
            this.line = line;
        }

        @Override
        public void run() {
            // Kiểm tra định dạng dữ liệu trước khi xử lý
            if (!line.matches(".*;.*;.*;.*;.*;.*;.*;.*;.*;.*")) {
                throw new RuntimeException("Invalid data format: " + line);
            }
        	
            // Xử lý dữ liệu từng dòng trong file
            String[] data = line.split(";");
            String firstName = data[0].trim();
            String lastName = data[1].trim();
            String fatherName = data[2].trim();
            String motherName = data[3].trim();
            String cic = data[4].trim();
            String dob = data[5].trim();
            String phone = data[6].trim();
            String email = data[7].trim();
            String address = data[8].trim();
            String accountType = data[9].trim();

            Client newClient = new Client( "",firstName, lastName, fatherName, motherName,cic, dob, phone, email,address);
            Manager manager = new Manager();
			  manager.createAccount(newClient, accountType);
			
        }
    }
    
    
}
