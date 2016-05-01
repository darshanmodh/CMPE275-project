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
    picture BLOB,
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

insert into User values('admin@admin.com','admin','A','0000',true);
					