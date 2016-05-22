create database OrderManagementSystem;
Use OrderManagementSystem;

CREATE TABLE User (
    email VARCHAR(50) PRIMARY KEY,
    password VARCHAR(10),
    role CHAR,
    verificationCode VARCHAR(10),
    isVerified BOOL
);

CREATE TABLE MenuItem (
    menuId INT PRIMARY KEY,
    category VARCHAR(20),
    name VARCHAR(50),
    picture LONGBLOB,
    unitPrice FLOAT,
    calories FLOAT,
    prepTime INT,
    isEnabled BOOL
);

CREATE TABLE FoodOrder (
    orderId INT PRIMARY KEY,
    email VARCHAR(50),
    pickupTime TIMESTAMP,
    status CHAR,
    totalTime INT,
    totalCost FLOAT,
    FOREIGN KEY (email)
        REFERENCES User (email)
);
                        
CREATE TABLE OrderDetail (
    menuId INT,
    orderId INT,
    quantity INT,
    PRIMARY KEY (menuId , orderId),
    FOREIGN KEY (menuId)
        REFERENCES MenuItem (menuId),
    FOREIGN KEY (orderId)
        REFERENCES FoodOrder (orderId)
);


CREATE TABLE OrdersPlaced (
  orderId int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  chefId int(11) NOT NULL,
  prepDate date NOT NULL,
  startTime time NOT NULL,
  endTime time DEFAULT NULL,
  email VARCHAR(50),
  contents VARCHAR(200),
  totalPrice FLOAT
);

delimiter //
CREATE TRIGGER check_trigger
  BEFORE INSERT
  ON MenuItem
  FOR EACH ROW
BEGIN

  IF NEW.menuId <0 or New.menuId>999  THEN
	signal sqlstate '45000' set message_text = 'My Error Message';
	END IF;
END//
insert into User values('admin@admin.com','admin','A','0000',true);
					