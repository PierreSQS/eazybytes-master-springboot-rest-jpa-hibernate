INSERT INTO `holidays` (`day`,`reason`,`type`,`created_at`, `created_by`)
 VALUES (' Jan 1 ','New Year''s Day','FESTIVAL',CURDATE(),'DBA');

INSERT INTO `holidays` (`day`,`reason`,`type`,`created_at`, `created_by`)
 VALUES (' Oct 31 ','Halloween','FESTIVAL',CURDATE(),'DBA');

INSERT INTO `holidays` (`day`,`reason`,`type`,`created_at`, `created_by`)
 VALUES (' Nov 24 ','Thanksgiving Day','FESTIVAL',CURDATE(),'DBA');

INSERT INTO `holidays` (`day`,`reason`,`type`,`created_at`, `created_by`)
 VALUES (' Dec 25 ','Christmas','FESTIVAL',CURDATE(),'DBA');

INSERT INTO `holidays` (`day`,`reason`,`type`,`created_at`, `created_by`)
 VALUES (' Jan 17 ','Martin Luther King Jr. Day','FEDERAL',CURDATE(),'DBA');

INSERT INTO `holidays` (`day`,`reason`,`type`,`created_at`, `created_by`)
 VALUES (' July 4 ','Independence Day','FEDERAL',CURDATE(),'DBA');

INSERT INTO `holidays` (`day`,`reason`,`type`,`created_at`, `created_by`)
 VALUES (' Sep 5 ','Labor Day','FEDERAL',CURDATE(),'DBA');

INSERT INTO `holidays` (`day`,`reason`,`type`,`created_at`, `created_by`)
  VALUES (' Nov 11 ','Veterans Day','FEDERAL',CURDATE(),'DBA');

INSERT INTO `roles` (`role_name`,`created_at`, `created_by`)
  VALUES ('ADMIN',CURDATE(),'DBA');

INSERT INTO `roles` (`role_name`,`created_at`, `created_by`)
  VALUES ('STUDENT',CURDATE(),'DBA');

INSERT INTO `person` (`name`,`email`,`pwd`,`mobile_number`,`created_at`, `created_by`, `role_id`)
  VALUES ('user','user@eazyschool.com','$2a$10$EXH5ZD7afIS2dQ9CTI2WeOBjofhvd8xBqkQ9dS0GV/ZTNoZbXFJpe','2323232323',CURDATE(),'DBA',2);

INSERT INTO `person` (`name`,`email`,`pwd`,`mobile_number`,`created_at`, `created_by`, `role_id`)
  VALUES ('admin','admin@eazyschool.com','$2a$10$.kX761Sh6u7hUMmu4yJgTe2IIXnpdg.qip78KWdD1GfWj6v/rLFB.','1212121212',CURDATE(),'DBA',1);

