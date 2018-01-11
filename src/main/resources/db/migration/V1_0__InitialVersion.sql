CREATE TABLE `address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified` bigint(20) DEFAULT NULL,
  `building_name` varchar(255) DEFAULT NULL,
  `building_number` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `post_code` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `town` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified` bigint(20) DEFAULT NULL,
  `account_number` varchar(255) DEFAULT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9lna4d7ow9qbs27m5psafys58` (`address_id`),
  CONSTRAINT `FK9lna4d7ow9qbs27m5psafys58` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified` bigint(20) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `customer_number` varchar(255) DEFAULT NULL,
  `email_address` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_n9x2k8svpxj3r328iy1rpur83` (`account_id`),
  CONSTRAINT `FKn9x2k8svpxj3r328iy1rpur83` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `payment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified` bigint(20) DEFAULT NULL,
  `card_number` varchar(255) DEFAULT NULL,
  `card_type` int(11) DEFAULT NULL,
  `expiry_date` tinyblob,
  `start_date` tinyblob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified` bigint(20) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `customer_roles` (
  `customer_id` bigint(20) NOT NULL,
  `roles_id` bigint(20) NOT NULL,
  PRIMARY KEY (`customer_id`,`roles_id`),
  UNIQUE KEY `UK_skrqd5xpsyymoehejj5yq1swr` (`roles_id`),
  CONSTRAINT `FK40q5lhh32kood5ortc34hv05n` FOREIGN KEY (`roles_id`) REFERENCES `user_role` (`id`),
  CONSTRAINT `FK5mxc61l5u87g0rjsselvw7dk3` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `account_payments` (
  `account_id` bigint(20) NOT NULL,
  `payments_id` bigint(20) NOT NULL,
  PRIMARY KEY (`account_id`,`payments_id`),
  UNIQUE KEY `UK_jveip92q2ayqrltx9n82t1uq7` (`payments_id`),
  CONSTRAINT `FK7c89ory4dp54c9o1odysbpkh` FOREIGN KEY (`payments_id`) REFERENCES `payment` (`id`),
  CONSTRAINT `FKi84t7vnfcx0lm300tfs504lcn` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
