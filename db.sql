/*
SQLyog Community v13.1.5  (64 bit)
MySQL - 5.6.12-log : Database - newspaper
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`newspaper` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `newspaper`;

/*Table structure for table `agent` */

DROP TABLE IF EXISTS `agent`;

CREATE TABLE `agent` (
  `agent_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `agent_name` varchar(50) DEFAULT NULL COMMENT 'Name of Agent',
  `agent_place` varchar(50) DEFAULT NULL COMMENT 'Place of Agent',
  `agent_district` varchar(50) DEFAULT NULL COMMENT 'District of Agent',
  `agent_pin` bigint(11) DEFAULT NULL COMMENT 'Pincode of Agent',
  `agent_contact_number` bigint(11) DEFAULT NULL COMMENT 'Contact Number of Agent',
  `agent_email_id` varchar(50) DEFAULT NULL COMMENT 'Email ID of Agent',
  `agent_image` varchar(100) DEFAULT NULL COMMENT 'Profile Image of Agent',
  `agent_login_id` int(11) DEFAULT NULL COMMENT 'Login ID of Agent',
  PRIMARY KEY (`agent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `agent` */

insert  into `agent`(`agent_id`,`agent_name`,`agent_place`,`agent_district`,`agent_pin`,`agent_contact_number`,`agent_email_id`,`agent_image`,`agent_login_id`) values 
(1,'age1','a','Kozhikode',111111,9898989898,'a@g.c','/static/agent_image/SHUT_11.jpg',3);

/*Table structure for table `allocate_paperboy` */

DROP TABLE IF EXISTS `allocate_paperboy`;

CREATE TABLE `allocate_paperboy` (
  `allocation_id` int(50) NOT NULL AUTO_INCREMENT COMMENT 'Paperboy allocation ID',
  `request_id` int(50) DEFAULT NULL COMMENT 'Paper/Magazine request ID',
  `boy_id` int(50) DEFAULT NULL COMMENT 'Paperboy Login ID',
  `type` varchar(50) DEFAULT NULL COMMENT 'Material Type Newspaper/Magazine',
  `allocated_date` varchar(50) DEFAULT NULL COMMENT 'Material allocated for Date',
  `status` varchar(30) DEFAULT NULL COMMENT 'Material Delivery Status',
  PRIMARY KEY (`allocation_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

/*Data for the table `allocate_paperboy` */

insert  into `allocate_paperboy`(`allocation_id`,`request_id`,`boy_id`,`type`,`allocated_date`,`status`) values 
(0,0,0,'','',''),
(2,1,1,'Magazine','2021-07-20','Allocated'),
(3,1,1,'Magazine','2021-07-20','Allocated'),
(4,1,1,'Magazine','2021-07-20','Allocated'),
(5,1,1,'Magazine','2021-07-20','Allocated'),
(6,1,5,'Newspaper','2021-07-20','Allocated');

/*Table structure for table `bank` */

DROP TABLE IF EXISTS `bank`;

CREATE TABLE `bank` (
  `bank_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `account_no` bigint(20) DEFAULT NULL COMMENT 'Subscriber Account Number',
  `password` int(11) DEFAULT NULL COMMENT 'Subscriber Account Password',
  `amount` bigint(20) DEFAULT NULL COMMENT 'Payable Amount',
  PRIMARY KEY (`bank_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `bank` */

insert  into `bank`(`bank_id`,`account_no`,`password`,`amount`) values 
(1,123,123,4990);

/*Table structure for table `classifieds` */

DROP TABLE IF EXISTS `classifieds`;

CREATE TABLE `classifieds` (
  `classified_id` int(50) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `user_id` int(50) DEFAULT NULL COMMENT 'Login ID of Subscriber',
  `agent_login_id` int(50) DEFAULT NULL COMMENT 'Login ID of Agent',
  `provider_login_id` int(50) DEFAULT NULL COMMENT 'Login ID of Provider',
  `news_file` varchar(100) DEFAULT NULL COMMENT 'Classifieds/Advertisement News File',
  `description` varchar(200) DEFAULT NULL COMMENT 'News description',
  `date` varchar(50) DEFAULT NULL COMMENT 'Classifieds Date',
  `status` varchar(50) DEFAULT NULL COMMENT 'Forwarded/Accepted/Rejected',
  `amount` int(20) DEFAULT NULL COMMENT 'Amount for that News/Advertisement',
  PRIMARY KEY (`classified_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `classifieds` */

/*Table structure for table `complaints` */

DROP TABLE IF EXISTS `complaints`;

CREATE TABLE `complaints` (
  `complaint_id` int(50) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `user_id` int(50) DEFAULT NULL COMMENT 'Login ID of User',
  `date` varchar(20) DEFAULT NULL COMMENT 'Complaint Date',
  `complaint` varchar(200) DEFAULT NULL COMMENT 'Complaint details',
  `replay` varchar(200) DEFAULT 'pending' COMMENT 'Replay from Agent',
  `agent_login_id` int(50) DEFAULT NULL COMMENT 'Login ID of Agent',
  PRIMARY KEY (`complaint_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `complaints` */

insert  into `complaints`(`complaint_id`,`user_id`,`date`,`complaint`,`replay`,`agent_login_id`) values 
(1,4,'2021-07-19','comp1','Noted1',3),
(2,4,'2021-07-19','comp2','Noted 2',3);

/*Table structure for table `edition_languages` */

DROP TABLE IF EXISTS `edition_languages`;

CREATE TABLE `edition_languages` (
  `language_id` int(50) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `edition_language` varchar(50) DEFAULT NULL COMMENT 'Edition Language',
  `edition_id` int(50) DEFAULT NULL COMMENT 'Unique Edition ID',
  PRIMARY KEY (`language_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Data for the table `edition_languages` */

insert  into `edition_languages`(`language_id`,`edition_language`,`edition_id`) values 
(1,'English',1),
(2,'Malayalam',1),
(3,'Hindi',1),
(4,'Tamil',1),
(5,'Kannada',1);

/*Table structure for table `editions` */

DROP TABLE IF EXISTS `editions`;

CREATE TABLE `editions` (
  `edition_id` int(50) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `edition_place` varchar(50) DEFAULT NULL COMMENT 'Name of Edition/Place',
  `provider_login_id` int(50) DEFAULT NULL COMMENT 'Login ID of Provider',
  PRIMARY KEY (`edition_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `editions` */

insert  into `editions`(`edition_id`,`edition_place`,`provider_login_id`) values 
(1,'Kozhikode',2),
(2,'Malappuram',2),
(3,'Wayanad',2),
(4,'Palakkad',2);

/*Table structure for table `location` */

DROP TABLE IF EXISTS `location`;

CREATE TABLE `location` (
  `location_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary Kay',
  `login_id` int(11) DEFAULT NULL COMMENT 'User Login ID',
  `latitude` varchar(50) DEFAULT NULL COMMENT 'Latitude of User',
  `longitude` varchar(50) DEFAULT NULL COMMENT 'Longitude of User',
  PRIMARY KEY (`location_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `location` */

/*Table structure for table `login` */

DROP TABLE IF EXISTS `login`;

CREATE TABLE `login` (
  `login_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Login ID. Primary Key',
  `user_name` varchar(50) DEFAULT NULL COMMENT 'User Name',
  `password` varchar(50) DEFAULT NULL COMMENT 'Password',
  `type` varchar(50) DEFAULT NULL COMMENT 'Admin/Provider/Agent/Subscriber/Paperboy/Rejected',
  PRIMARY KEY (`login_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Data for the table `login` */

insert  into `login`(`login_id`,`user_name`,`password`,`type`) values 
(1,'admin','admin','admin'),
(2,'pro1','111','provider'),
(3,'age1','111','agent'),
(4,'us1','111','user'),
(5,'boy1','111','paperboy');

/*Table structure for table `magazine_requests` */

DROP TABLE IF EXISTS `magazine_requests`;

CREATE TABLE `magazine_requests` (
  `magazine_request_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `user_id` int(50) DEFAULT NULL COMMENT 'Login ID of Subscriber',
  `magazine_id` int(50) DEFAULT NULL COMMENT 'Magazine Unique ID',
  `request_date` varchar(50) DEFAULT NULL COMMENT 'Magazine request date',
  `status` varchar(50) DEFAULT 'pending' COMMENT 'forwarded/accepted/rejected',
  `start_date` varchar(50) DEFAULT NULL COMMENT 'Magazine start/approved Date',
  `agent_login_id` int(50) DEFAULT NULL COMMENT 'Login ID of Agent',
  PRIMARY KEY (`magazine_request_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `magazine_requests` */

insert  into `magazine_requests`(`magazine_request_id`,`user_id`,`magazine_id`,`request_date`,`status`,`start_date`,`agent_login_id`) values 
(1,4,1,'2021-07-19','Approved','2021-07-19',3);

/*Table structure for table `magazines` */

DROP TABLE IF EXISTS `magazines`;

CREATE TABLE `magazines` (
  `magazine_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `magazine_name` varchar(50) DEFAULT NULL COMMENT 'Magazine Name',
  `provider_login_id` int(50) DEFAULT NULL COMMENT 'Login ID of Magazine Provider',
  `language` varchar(20) DEFAULT NULL COMMENT 'Magazine Language',
  `price` int(20) DEFAULT NULL COMMENT 'Magazine Price',
  PRIMARY KEY (`magazine_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `magazines` */

insert  into `magazines`(`magazine_id`,`magazine_name`,`provider_login_id`,`language`,`price`) values 
(1,'m1',2,'Malayalam',10),
(2,'m2',2,'English',20),
(3,'m3',2,'Tamil',30);

/*Table structure for table `notifications` */

DROP TABLE IF EXISTS `notifications`;

CREATE TABLE `notifications` (
  `notification_id` int(50) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `notification` varchar(100) DEFAULT NULL COMMENT 'Notification from Provider',
  `date` varchar(20) DEFAULT NULL COMMENT 'Notification Date',
  `provider_login_id` int(50) DEFAULT NULL COMMENT 'Login ID of Provider',
  PRIMARY KEY (`notification_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `notifications` */

insert  into `notifications`(`notification_id`,`notification`,`date`,`provider_login_id`) values 
(1,'not1','2021-07-19',2),
(2,'not2','2021-07-19',2),
(3,'not3','2021-07-19',2);

/*Table structure for table `paper_requests` */

DROP TABLE IF EXISTS `paper_requests`;

CREATE TABLE `paper_requests` (
  `paper_request_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK. Newspaper request ID',
  `user_id` int(50) DEFAULT NULL COMMENT 'Login ID of Subscriber',
  `edition_id` int(50) DEFAULT NULL COMMENT 'Newspaper Edition ID',
  `edition_language_id` int(50) DEFAULT NULL COMMENT 'Newspaper Edition Language ID',
  `request_date` varchar(50) DEFAULT NULL COMMENT 'Newspaper request Date',
  `status` varchar(50) DEFAULT 'pending' COMMENT 'Forwarded/Accepted/Rejected',
  `start_date` varchar(50) DEFAULT NULL COMMENT 'Subscription start date',
  `agent_login_id` int(50) DEFAULT NULL COMMENT 'Login ID of Agent',
  PRIMARY KEY (`paper_request_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `paper_requests` */

insert  into `paper_requests`(`paper_request_id`,`user_id`,`edition_id`,`edition_language_id`,`request_date`,`status`,`start_date`,`agent_login_id`) values 
(1,4,1,1,NULL,'Approved',NULL,3);

/*Table structure for table `paper_status` */

DROP TABLE IF EXISTS `paper_status`;

CREATE TABLE `paper_status` (
  `paper_status_id` int(50) NOT NULL AUTO_INCREMENT COMMENT 'pk. Daily Material delivery status',
  `paper_request_id` int(50) DEFAULT NULL COMMENT 'Paper request ID',
  `date` varchar(20) DEFAULT NULL COMMENT 'Date of status updation',
  `amount` int(20) DEFAULT NULL COMMENT 'amount',
  `paperboy_id` int(20) DEFAULT NULL COMMENT 'Login ID of paperboy',
  `status` varchar(50) DEFAULT 'pending' COMMENT 'Updated by Paperboy',
  PRIMARY KEY (`paper_status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `paper_status` */

/*Table structure for table `paperboy` */

DROP TABLE IF EXISTS `paperboy`;

CREATE TABLE `paperboy` (
  `boy_id` int(50) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `agent_id` int(50) DEFAULT NULL COMMENT 'Login ID of Agent',
  `boy_name` varchar(50) DEFAULT NULL COMMENT 'Name of Paperboy',
  `boy_place` varchar(100) DEFAULT NULL COMMENT 'Place of Paperboy',
  `boy_contact_number` bigint(20) DEFAULT NULL COMMENT 'Place of Paperboy',
  `boy_email_id` varchar(50) DEFAULT NULL COMMENT 'Contact Number of Paperboy',
  `boy_image` varchar(100) DEFAULT NULL COMMENT 'Email ID of Paperboy',
  `boy_login_id` int(50) DEFAULT NULL COMMENT 'Login ID of Paperboy',
  PRIMARY KEY (`boy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `paperboy` */

insert  into `paperboy`(`boy_id`,`agent_id`,`boy_name`,`boy_place`,`boy_contact_number`,`boy_email_id`,`boy_image`,`boy_login_id`) values 
(1,3,'boy1','b1',9696969696,'b1@g.c','/static/paperboy_image/IMG_20171204_164213.jpg',5);

/*Table structure for table `payments` */

DROP TABLE IF EXISTS `payments`;

CREATE TABLE `payments` (
  `payment_id` int(50) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `user_id` int(50) DEFAULT NULL COMMENT 'Login ID of Subscriber',
  `type` varchar(50) DEFAULT NULL COMMENT 'Newspaper/Magazine/Classifieds',
  `amount` int(20) DEFAULT NULL COMMENT 'Amount',
  `date` varchar(20) DEFAULT NULL COMMENT 'Date of Payment',
  `request_id` int(11) DEFAULT NULL COMMENT 'Material request ID',
  PRIMARY KEY (`payment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `payments` */

insert  into `payments`(`payment_id`,`user_id`,`type`,`amount`,`date`,`request_id`) values 
(1,4,'Magazine',10,'2021-07-19',1);

/*Table structure for table `price` */

DROP TABLE IF EXISTS `price`;

CREATE TABLE `price` (
  `price_id` int(50) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `edition_id` int(50) DEFAULT NULL COMMENT 'Newspaper Edition ID',
  `language_id` int(50) DEFAULT NULL COMMENT 'Newspaper Language ID',
  `price` int(50) DEFAULT NULL COMMENT 'Newspaper Price',
  `provider_login_id` int(50) DEFAULT NULL COMMENT 'Login ID of Provider',
  PRIMARY KEY (`price_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `price` */

insert  into `price`(`price_id`,`edition_id`,`language_id`,`price`,`provider_login_id`) values 
(1,1,2,10,2),
(2,1,1,20,2),
(3,1,3,30,2),
(4,1,4,40,2);

/*Table structure for table `provider` */

DROP TABLE IF EXISTS `provider`;

CREATE TABLE `provider` (
  `provider_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `provider_name` varchar(50) DEFAULT NULL COMMENT 'Printed media Provider Name',
  `place` varchar(50) DEFAULT NULL COMMENT 'Address',
  `pin` bigint(10) DEFAULT NULL COMMENT 'Address',
  `phone` bigint(20) DEFAULT NULL COMMENT 'Contact Number',
  `email_id` varchar(50) DEFAULT NULL COMMENT 'Email ID',
  `provider_logo` varchar(100) DEFAULT NULL COMMENT 'Logo',
  `provider_login_id` int(11) DEFAULT NULL COMMENT 'Login ID',
  PRIMARY KEY (`provider_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `provider` */

insert  into `provider`(`provider_id`,`provider_name`,`place`,`pin`,`phone`,`email_id`,`provider_logo`,`provider_login_id`) values 
(1,'pro1','p',111111,1234567891,'p@g.c','/static/provider_image/SHUT_03.jpg',2);

/*Table structure for table `subscriber` */

DROP TABLE IF EXISTS `subscriber`;

CREATE TABLE `subscriber` (
  `subscriber_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `user_id` int(11) DEFAULT NULL COMMENT 'Subscriber Login ID',
  `agent_id` int(11) DEFAULT NULL COMMENT 'Agent Login ID',
  `status` varchar(50) DEFAULT 'pending' COMMENT 'Accepted/Rejected/Pending',
  PRIMARY KEY (`subscriber_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `subscriber` */

insert  into `subscriber`(`subscriber_id`,`user_id`,`agent_id`,`status`) values 
(1,4,3,'subscriber');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `user_id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `user_name` varchar(50) DEFAULT NULL COMMENT 'User Name',
  `gender` varchar(20) DEFAULT NULL COMMENT 'Gender',
  `place` varchar(50) DEFAULT NULL COMMENT 'Address',
  `post` varchar(50) DEFAULT NULL COMMENT 'Address',
  `district` varchar(50) DEFAULT NULL COMMENT 'Address',
  `pin` bigint(11) DEFAULT NULL COMMENT 'Address',
  `email_id` varchar(50) DEFAULT NULL COMMENT 'Email',
  `contact_number` bigint(20) DEFAULT NULL COMMENT 'Contact Number',
  `user_image` varchar(100) DEFAULT NULL COMMENT 'Profile Picture',
  `user_login_id` int(11) DEFAULT NULL COMMENT 'Login ID',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `user` */

insert  into `user`(`user_id`,`user_name`,`gender`,`place`,`post`,`district`,`pin`,`email_id`,`contact_number`,`user_image`,`user_login_id`) values 
(1,'user1','Male','u1','u1','u1',111222,'u@g.c',1231231231,'/static/user_image/2021_07_19_19_50_18.488990.jpg',4);

/*Table structure for table `user_status` */

DROP TABLE IF EXISTS `user_status`;

CREATE TABLE `user_status` (
  `status_id` int(50) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `user_id` int(50) DEFAULT NULL COMMENT 'Login ID of User',
  `status` varchar(50) DEFAULT NULL COMMENT 'Current Status of Subscriber',
  `from_date` varchar(50) DEFAULT NULL COMMENT 'Status date from',
  `to_date` varchar(50) DEFAULT NULL COMMENT 'Status date to',
  PRIMARY KEY (`status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `user_status` */

insert  into `user_status`(`status_id`,`user_id`,`status`,`from_date`,`to_date`) values 
(1,4,'Unavailable','20','25');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
