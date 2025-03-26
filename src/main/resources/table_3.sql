CREATE TABLE IF NOT EXISTS `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `hire_date` datetime DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `username` varchar(50) NOT NULL,
  `status` varchar(100) DEFAULT NULL,
  `token` varchar(500) DEFAULT NULL,
  `is_password_set` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



CREATE TABLE IF NOT EXISTS `customer` (
   `customer_id` int unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    `phone` varchar(20) DEFAULT NULL,
    `address` varchar(255) DEFAULT NULL,
    `city` varchar(255) DEFAULT NULL,
    `state` varchar(255) DEFAULT NULL,
    `country` varchar(255) DEFAULT NULL,
    `user_id` int DEFAULT NULL,
    `description` text,
    `position` varchar(255) DEFAULT NULL,
    `twitter` varchar(255) DEFAULT NULL,
    `facebook` varchar(255) DEFAULT NULL,
    `youtube` varchar(255) DEFAULT NULL,
    `created_at` datetime DEFAULT NULL,
    `email` varchar(255) DEFAULT NULL,
    `profile_id` int DEFAULT NULL,
    PRIMARY KEY (`customer_id`),
    KEY `user_id` (`user_id`),
    KEY `profile_id` (`profile_id`),
    CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `customer_ibfk_2` FOREIGN KEY (`profile_id`) REFERENCES `customer_login_info` (`id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



CREATE TABLE IF NOT EXISTS `trigger_lead` (
  `lead_id` int unsigned NOT NULL AUTO_INCREMENT,
  `customer_id` int unsigned NOT NULL,
  `user_id` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `meeting_id` varchar(255) DEFAULT NULL,
  `google_drive` tinyint(1) DEFAULT NULL,
  `google_drive_folder_id` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`lead_id`),
  UNIQUE KEY `meeting_info` (`meeting_id`),
  KEY `customer_id` (`customer_id`),
  KEY `user_id` (`user_id`),
  KEY `employee_id` (`employee_id`),
  CONSTRAINT `trigger_lead_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  CONSTRAINT `trigger_lead_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `trigger_lead_ibfk_3` FOREIGN KEY (`employee_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



CREATE TABLE IF NOT EXISTS `trigger_ticket` (
  `ticket_id` int unsigned NOT NULL AUTO_INCREMENT,
  `subject` varchar(255) DEFAULT NULL,
  `description` text,
  `status` varchar(50) DEFAULT NULL,
  `priority` varchar(50) DEFAULT NULL,
  `customer_id` int unsigned NOT NULL,
  `manager_id` int DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`ticket_id`),
  KEY `fk_ticket_customer` (`customer_id`),
  KEY `fk_ticket_manager` (`manager_id`),
  KEY `fk_ticket_employee` (`employee_id`),
  CONSTRAINT `fk_ticket_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  CONSTRAINT `fk_ticket_employee` FOREIGN KEY (`employee_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_ticket_manager` FOREIGN KEY (`manager_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE IF NOT EXISTS `customer_budget` (
  `budget_id` int unsigned NOT NULL AUTO_INCREMENT,
  `customer_id` int unsigned NOT NULL,
  `label` varchar(255) NOT NULL,
  `amount` decimal(15,2) NOT NULL,
  `transaction_date` date NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`budget_id`),
  KEY `fk_budget_customer` (`customer_id`),
  KEY `fk_budget_user` (`user_id`),
  CONSTRAINT `fk_budget_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  CONSTRAINT `fk_budget_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- Link table for ticket's expenses 
CREATE TABLE IF NOT EXISTS `expense` (
  `expense_id` int unsigned NOT NULL AUTO_INCREMENT,
  `amount` decimal(15,2) NOT NULL,
  `expense_date` date NOT NULL,
  PRIMARY KEY (`ticket_expense_id`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- taux alerte in percentage
CREATE TABLE IF NOT EXISTS `alerte_rate` (
  `alerte_rate_id` int unsigned NOT NULL AUTO_INCREMENT,
  `percentage` decimal(15,2) NOT NULL,
  `alerte_rate_date` datetime DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`alerte_rate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;





SELECT 
    c.customer_id, 
    c.name AS customer_name, 
    COALESCE(SUM(cb.amount), 0) AS total_budget
FROM 
    customer c
LEFT JOIN 
    customer_budget cb ON c.customer_id = cb.customer_id
GROUP BY 
    c.customer_id, 
    c.name
ORDER BY 
    total_budget DESC;