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

-- alter
ALTER TABLE `trigger_ticket` 
ADD COLUMN `expense_id` INT UNSIGNED DEFAULT NULL,
ADD CONSTRAINT `fk_ticket_expense` FOREIGN KEY (`expense_id`) REFERENCES `expense` (`expense_id`);

ALTER TABLE `trigger_lead` 
ADD COLUMN `expense_id` INT UNSIGNED DEFAULT NULL,
ADD CONSTRAINT `fk_lead_expense` FOREIGN KEY (`expense_id`) REFERENCES `expense` (`expense_id`);


INSERT INTO `expense` (`amount`, `expense_date`) VALUES
(1000.50, '2023-10-01'),
(200.75, '2023-10-05'),
(150.00, '2023-10-10'),
(300.25, '2023-11-01'),
(250.50, '2023-11-15'),
(400.00, '2023-12-01'),
(350.75, '2023-12-10');


INSERT INTO `customer_budget` (`customer_id`, `label`, `amount`, `transaction_date`) VALUES
(1, 'Initial Budget', 1000.00, '2023-10-01'),
(1, 'Additional Budget', 500.00, '2023-10-15'),
(2, 'Project Budget', 2000.00, '2023-10-05'),
(1, 'Q4 Budget', 1500.00, '2023-11-01'),
(2, 'Marketing Budget', 1000.00, '2023-11-10'),
(1, 'Year-End Budget', 3000.00, '2023-12-01'),
(2, 'Final Budget', 2000.00, '2023-12-15');