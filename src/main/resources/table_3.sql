-- taux alerte in percentage
CREATE TABLE IF NOT EXISTS `alerte_rate` (
  `alerte_rate_id` int unsigned NOT NULL AUTO_INCREMENT,
  `percentage` decimal(15,2) NOT NULL,
  `alerte_rate_date` datetime DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`alerte_rate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `alerte_rate` (percentage) VALUES (50);