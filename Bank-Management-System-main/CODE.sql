create database bank_schema;
use bank_schema;
-- 1 Client Table
CREATE TABLE client (
  client_id int NOT NULL AUTO_INCREMENT,
  f_name nvarchar(45) NOT NULL,
  l_name nvarchar(45) NOT NULL,
  father_name nvarchar(45) NOT NULL,
  mother_name nvarchar(45) NOT NULL,
  CIC nvarchar(45) NOT NULL,
  DOB date NOT NULL,
  phone nvarchar(45) NOT NULL,
  email nvarchar(45) DEFAULT NULL,
  address nvarchar(100) NOT NULL,
  PRIMARY KEY (client_id),
  UNIQUE KEY client_id_UNIQUE (client_id)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
use bank_schema;
ALTER TABLE client AUTO_INCREMENT=10000;

-- 2 Login_Account Table
CREATE TABLE login_account (
  login_id int NOT NULL AUTO_INCREMENT,
  username nvarchar(45) NOT NULL,
  password char(8) NOT NULL,
  type char(1) NOT NULL,
  PRIMARY KEY (login_id),
  UNIQUE KEY login_id_UNIQUE (login_id)
) ENGINE=InnoDB AUTO_INCREMENT=206 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE login_account AUTO_INCREMENT=60000;

-- 3 Employee Table - Bang Nhan Vien
CREATE TABLE employee (   
  employee_id int NOT NULL AUTO_INCREMENT,
  f_name nvarchar(45) NOT NULL,
  l_name nvarchar(45) NOT NULL,
  father_name nvarchar(45) NOT NULL,
  mother_name nvarchar(45) NOT NULL,
  job nvarchar(45) NOT NULL,
  phone_no nvarchar(45) NOT NULL,
  email nvarchar(45) NOT NULL,
  login_id int DEFAULT NULL,
  PRIMARY KEY (employee_id),
  UNIQUE KEY employee_id_UNIQUE (employee_id),
  KEY login_id_idx (login_id),
  CONSTRAINT login_id FOREIGN KEY (login_id) REFERENCES login_account (login_id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE employee AUTO_INCREMENT=20000;

-- 4 Card Table
CREATE TABLE card (
  card_num int NOT NULL AUTO_INCREMENT,
  type char(1) NOT NULL,
  Status char(1) NOT NULL,
  Pin_code char(4) NOT NULL,
  Issue_date date NOT NULL,  /* Ngay phat hanh*/
  PRIMARY KEY (card_num)
) ENGINE=InnoDB AUTO_INCREMENT=301 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE card AUTO_INCREMENT=40000;

-- 5 Bank_Account table
CREATE TABLE bank_account (
  acc_num int NOT NULL AUTO_INCREMENT,
  client_id int NOT NULL,
  login_id int DEFAULT NULL,
  type char(10) NOT NULL,
  balance int NOT NULL,
  status int NOT NULL,
  opening_date date NOT NULL,
  closing_date date DEFAULT NULL,
  card_num int DEFAULT NULL,
  PRIMARY KEY (acc_num),
  UNIQUE KEY acc_num_UNIQUE (acc_num),
  KEY client_id (client_id),
  KEY login_id (login_id),
  KEY card_num (card_num),
  CONSTRAINT bank_account_ibfk_1 FOREIGN KEY (client_id) REFERENCES client (client_id),
  CONSTRAINT bank_account_ibfk_2 FOREIGN KEY (login_id) REFERENCES login_account (login_id),
  CONSTRAINT bank_account_ibfk_3 FOREIGN KEY (card_num) REFERENCES card (card_num)
) ENGINE=InnoDB AUTO_INCREMENT=402 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE bank_account AUTO_INCREMENT=500000;

-- 6 Transaction_History Table
CREATE TABLE transaction_history (
  serial_no int unsigned NOT NULL AUTO_INCREMENT,
  amount int NOT NULL,
  type nvarchar(45) NOT NULL,
  date date NOT NULL,
  time nvarchar(45) NOT NULL,
  account_num int NOT NULL,
  recv_acc_num int DEFAULT NULL,
  cheque_num int DEFAULT NULL,  -- Số Séc - Để kiểm tra
  PRIMARY KEY (serial_no),
  KEY account_num (account_num),
  CONSTRAINT transaction_history_ibfk_1 FOREIGN KEY (account_num) REFERENCES bank_account (acc_num)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE transaction_history AUTO_INCREMENT=70000;

-- 7 Cardless Withdrawal Table
CREATE TABLE cardless_withdrawl (
  serial_no int NOT NULL AUTO_INCREMENT,
  card_no int NOT NULL,
  amount int NOT NULL,
  OTP nvarchar(13) NOT NULL, -- Mã xác thực
  temp_pin char(4) NOT NULL,
  Status char(1) NOT NULL,
  date date DEFAULT NULL,
  time nvarchar(45) DEFAULT NULL,
  PRIMARY KEY (serial_no),
  UNIQUE KEY serial_no_UNIQUE (serial_no),
  KEY card_no (card_no),
  CONSTRAINT cardless_withdrawl_ibfk_1 FOREIGN KEY (card_no) REFERENCES card (card_num)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE cardless_withdrawl AUTO_INCREMENT=80000;


-- data set
use bank_schema;
-- employees
Insert into employee values(NULL, "Trung", "Nguyễn", "Bố_Trung", "Mẹ_Trung", "Manager", "0358469736", "trung03@gmail.com", NULL );
Insert into employee values(NULL, "Tùng", "Nguyễn", "Bố_Tùng", "Mẹ_Tùng", "Accountant", "0123456789", "TienTung03@gmail.com", NULL );

-- clients
Insert Into client Values(NULL, "Đức", "Đình", "Bố_Đức", "Mẹ_Đức", "67153-7853257-8", STR_TO_DATE("17,9,2003", "%d,%m,%Y"), "0378781786", "DinhDuc03@gmail.com", "Số nhà 22, Đường A, Chí Linh, Hải Dương");
Insert Into client Values(NULL, "Vinh", "Vũ", "Bố_Vinh", "Mẹ_Vinh", "78342-0978912-8", STR_TO_DATE("21,3,2003", "%d,%m,%Y"), "0356989962", "VinhVu03@gmail.com", "Số nhà 8, Đường B, Cát Bà, Hải Phòng");
Insert Into client Values(NULL, "Phương", "Trần", "Bố_Phương", "Mẹ_Phương", "43210-7809821-1", STR_TO_DATE("7,11,2003", "%d,%m,%Y"), "0366513244", "PhuongTranzz@gmail.com", "Số nhà 23, Đường C, Cầu Giấy, Hà Nội");

-- bank accounts
Insert Into bank_account Values(NULL, 10000, NULL,"Current", 1000, 1, STR_TO_DATE("17,3,2023", "%d,%m,%Y"), NULL, NULL);
Insert Into bank_account Values(NULL, 10001, NULL,"Current", 20000, 1, STR_TO_DATE("15,4,2003", "%d,%m,%Y"), NULL, NULL);
Insert Into bank_account Values(NULL, 10002, NULL,"Saving", 7000, 1, CURDATE(), NULL, NULL);

-- login account
Insert into login_account values (NULL, "trung03", "12345", 'M');
Insert into login_account values (NULL, "TienTung03", "909094", 'A');
Insert into login_account values (NULL, "DinhDuc03", "tom891", 'C');
Insert into login_account values (NULL, "VinhVu03", "green8", 'C');
Insert into login_account values (NULL, "PhuongTran", "90jeep", 'C');

update employee set login_id = 60000 where employee_id = 20000;
update employee set login_id = 60001 where employee_id = 20001;
update bank_account set login_id = 60002 where acc_num = 500000;
update bank_account set login_id = 60003 where acc_num = 500001;
update bank_account set login_id = 60004 where acc_num = 500002;

-- cards
Insert into card values( NULL, "C", "A", "8947", CURDATE() );
Insert into card values( NULL, "D", "A", "3921", CURDATE() );

update bank_account set card_num = 40000 where acc_num = 500000;
update bank_account set card_num = 40001 where acc_num = 500001;

-- transactions
Insert into transaction_history values(NULL, 1500, "withdraw", CURDATE(), CURRENT_TIME(), 500000, NULL, NULL);
Insert into transaction_history values(NULL, 2000, "deposit", CURDATE(), CURRENT_TIME(), 500000, NULL, 7856459);
Insert into transaction_history values(NULL, 5000, "deposit", CURDATE(), CURRENT_TIME(), 500000, NULL, NULL);

Insert into transaction_history values(NULL, 1500, "transfer", CURDATE(), CURRENT_TIME(), 500001, 500000, NULL);
Insert into transaction_history values(NULL, 1000, "withdraw", CURDATE(), CURRENT_TIME(), 500001, NULL, NULL);

Insert into transaction_history values(NULL, 1800, "withdraw", CURDATE(), CURRENT_TIME(), 500002, NULL, NULL);
Insert into transaction_history values(NULL, 1200, "deposit", CURDATE(), CURRENT_TIME(), 500002, NULL, 1674534);

DELETE from client where client_id=10008

