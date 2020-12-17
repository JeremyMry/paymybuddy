/* Setting up paymybuddy DB */
create database paymybuddy;
use paymybuddy;

create table user(
ID int PRIMARY KEY AUTO_INCREMENT,
FIRST_NAME varchar(64) NOT NULL,
LAST_NAME varchar(64) NOT NULL,
EMAIL varchar(64) NOT NULL,
PASSWORD varchar(64) NOT NULL,
WALLET int NOT NULL
);

create table transaction(
ID int PRIMARY KEY AUTO_INCREMENT,
CREDITOR varchar(64) NOT NULL,
DEBTOR varchar(64) NOT NULL,
REFERENCE varchar(64) NOT NULL,
AMOUNT int NOT NULL,
FOREIGN KEY (ID)
REFERENCES user(ID)
);

create table contact(
ID int PRIMARY KEY AUTO_INCREMENT,
EMAIL varchar(64) NOT NULL,
FIRST_NAME varchar(64) NOT NULL,
FOREIGN KEY (EMAIL)
REFERENCES user(EMAIL)
);

commit;