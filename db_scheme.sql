use paymybuddy;

DROP TABLE IF EXISTS contact;
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS users;

CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(40) NOT NULL,
  `last_name` varchar(40) NOT NULL,
  `username` varchar(40) NOT NULL,
  `email` varchar(40) NOT NULL,
  `password` varchar(255) NOT NULL,
  `wallet` decimal(5,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `contact` (
  `contact_id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(40) NOT NULL,
  `first_name` varchar(40) NOT NULL,
  `creator` bigint NOT NULL,
  PRIMARY KEY (`contact_id`),
  KEY `creator` (`creator`),
  CONSTRAINT `contact_ibfk_1` FOREIGN KEY (`creator`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `transaction` (
  `transaction_id` bigint NOT NULL AUTO_INCREMENT,
  `reference` varchar(40) NOT NULL,
  `amount` decimal(5,2) NOT NULL,
  `creditor` bigint NOT NULL,
  `debtor` bigint NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `creditor` (`creditor`),
  KEY `debtor` (`debtor`),
  CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`creditor`) REFERENCES `users` (`id`),
  CONSTRAINT `transaction_ibfk_2` FOREIGN KEY (`debtor`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
