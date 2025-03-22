-- Table for managing budgets linked to customers
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
CREATE TABLE IF NOT EXISTS `ticket_expense` (
  `ticket_expense_id` int unsigned NOT NULL AUTO_INCREMENT,
  `ticket_id` int unsigned NOT NULL,
  `amount` decimal(15,2) NOT NULL,
  `ticket_expense_date` date NOT NULL,
  PRIMARY KEY (`ticket_expense_id`),
  CONSTRAINT `fk_ticketexp_ticket` FOREIGN KEY (`ticket_id`) REFERENCES `trigger_ticket` (`ticket_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Link table for lead's expenses
CREATE TABLE IF NOT EXISTS `lead_expense` (
  `lead_expense_id` int unsigned NOT NULL AUTO_INCREMENT,
  `lead_id` int unsigned NOT NULL,
  `amount` decimal(15,2) NOT NULL,
  `lead_expense_date` date NOT NULL,
  PRIMARY KEY (`lead_expense_id`),
  CONSTRAINT `fk_leadexp_lead` FOREIGN KEY (`lead_id`) REFERENCES `trigger_lead` (`lead_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;