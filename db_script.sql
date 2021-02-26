DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact` (
  `contact_id` bigint NOT NULL AUTO_INCREMENT,
  `creator` bigint NOT NULL,
  `email` varchar(40) NOT NULL,
  `first_name` varchar(25) NOT NULL,
  PRIMARY KEY (`contact_id`),
  KEY `FKbxl6anxo14q097g8cd2e51v55` (`user_id`),
  CONSTRAINT `FKbxl6anxo14q097g8cd2e51v55` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction` (
  `transaction_id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(5,2) NOT NULL,
  `creditor` bigint NOT NULL,
  `debtor` bigint NOT NULL,
  `reference` varchar(25) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `FKh6ps8cc8x8j9qgw4545o009g3` (`creditor_id`),
  KEY `FKjw8y8vv4de7xp8bcowxaoaawi` (`debtor_id`),
  CONSTRAINT `FKh6ps8cc8x8j9qgw4545o009g3` FOREIGN KEY (`creditor_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKjw8y8vv4de7xp8bcowxaoaawi` FOREIGN KEY (`debtor_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(40) NOT NULL,
  `first_name` varchar(25) NOT NULL,
  `last_name` varchar(25) NOT NULL,
  `password` varchar(40) NOT NULL,
  `username` varchar(25) NOT NULL,
  `wallet` decimal(5,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
