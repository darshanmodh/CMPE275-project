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
    menuId INT PRIMARY KEY AUTO_INCREMENT,
    category VARCHAR(20),
    name VARCHAR(50),
    picture LONGBLOB,
    unitPrice FLOAT,
    calories FLOAT,
    prepTime INT,
    isEnabled BOOL
);

CREATE TABLE OrdersPlaced (
  orderId int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  chefId int(11) NOT NULL,
  prepDate date NOT NULL,
  startTime time NOT NULL,
  endTime time DEFAULT NULL,
  pickupTime time DEFAULT NULL,
  pickupDate date NOT NULL,
  orderTime timestamp DEFAULT CURRENT_TIMESTAMP,
  email VARCHAR(50) REFERENCES User(email),
  totalPrice FLOAT,
  status varchar(20)
);

CREATE table Rating (
	menuId int,
    email varchar(50),
    rate int,
    total int,
    Primary key (menuId,email),
    Foreign key (menuId) references MenuItem(menuId),
    foreign key (email) references User(email)
    
);

CREATE TABLE OrderDetail (
    menuId INT,
    orderId INT,
    quantity INT,
    PRIMARY KEY (menuId , orderId),
    FOREIGN KEY (menuId)
        REFERENCES MenuItem (menuId),
    FOREIGN KEY (orderId)
        REFERENCES OrdersPlaced (orderId)
);

insert into User values('admin@admin.com','admin','A','0000',true);
		