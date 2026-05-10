-- 创建包裹表
CREATE TABLE IF NOT EXISTS `package` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `barcode` VARCHAR(100) NOT NULL,
  `pickup_code` VARCHAR(20) NOT NULL,
  `shelf_position` VARCHAR(20) NOT NULL,
  `recipient_name` VARCHAR(50) NOT NULL,
  `recipient_phone` VARCHAR(20) NOT NULL,
  `courier_name` VARCHAR(50) NOT NULL,
  `courier_phone` VARCHAR(20) NOT NULL,
  `storage_time` DATETIME NOT NULL,
  `pickup_time` DATETIME,
  `status` INT NOT NULL DEFAULT 0,
  `notes` VARCHAR(255),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_barcode` (`barcode`),
  UNIQUE KEY `uk_pickup_code` (`pickup_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建寄件订单表
CREATE TABLE IF NOT EXISTS `send_order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_no` VARCHAR(50) NOT NULL,
  `sender_name` VARCHAR(50) NOT NULL,
  `sender_phone` VARCHAR(20) NOT NULL,
  `sender_address` VARCHAR(255) NOT NULL,
  `recipient_name` VARCHAR(50) NOT NULL,
  `recipient_phone` VARCHAR(20) NOT NULL,
  `recipient_address` VARCHAR(255) NOT NULL,
  `weight` DOUBLE NOT NULL,
  `price` DOUBLE NOT NULL,
  `courier_company` VARCHAR(50) NOT NULL,
  `create_time` DATETIME NOT NULL,
  `pickup_time` DATETIME,
  `status` INT NOT NULL DEFAULT 0,
  `notes` VARCHAR(255),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建收费记录表
CREATE TABLE IF NOT EXISTS `charge` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `charge_no` VARCHAR(50) NOT NULL,
  `package_id` BIGINT,
  `send_order_id` BIGINT,
  `type` VARCHAR(20) NOT NULL,
  `amount` DOUBLE NOT NULL,
  `create_time` DATETIME NOT NULL,
  `status` INT NOT NULL DEFAULT 0,
  `notes` VARCHAR(255),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_charge_no` (`charge_no`),
  KEY `fk_package_id` (`package_id`),
  KEY `fk_send_order_id` (`send_order_id`),
  CONSTRAINT `fk_package_id` FOREIGN KEY (`package_id`) REFERENCES `package` (`id`),
  CONSTRAINT `fk_send_order_id` FOREIGN KEY (`send_order_id`) REFERENCES `send_order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建通知表
CREATE TABLE IF NOT EXISTS `notification` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `package_id` BIGINT NOT NULL,
  `recipient_phone` VARCHAR(20) NOT NULL,
  `content` VARCHAR(255) NOT NULL,
  `send_time` DATETIME NOT NULL,
  `status` INT NOT NULL DEFAULT 0,
  `type` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_notification_package_id` (`package_id`),
  CONSTRAINT `fk_notification_package_id` FOREIGN KEY (`package_id`) REFERENCES `package` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;