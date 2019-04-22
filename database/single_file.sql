-- MySQL dump 10.13  Distrib 5.7.20, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: web_dev_db
-- ------------------------------------------------------
-- Server version	5.7.23-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chat`
--
USE web_dev_db;

DROP TABLE IF EXISTS `chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_1` int(11) NOT NULL,
  `user_2` int(11) NOT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`,`user_1`,`user_2`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat`
--

LOCK TABLES `chat` WRITE;
/*!40000 ALTER TABLE `chat` DISABLE KEYS */;
INSERT INTO `chat` VALUES (1,3,2,1),(2,5,4,0),(3,7,3,0),(4,5,9,0),(5,7,6,0),(6,7,4,0);
/*!40000 ALTER TABLE `chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_history`
--

DROP TABLE IF EXISTS `chat_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chat_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `chat_id` int(11) NOT NULL,
  `sender_user_id` int(11) NOT NULL,
  `message_content` varchar(280) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`chat_id`,`sender_user_id`),
  KEY `fk_chat_history_chat1` (`chat_id`),
  CONSTRAINT `fk_chat_history_chat1` FOREIGN KEY (`chat_id`) REFERENCES `chat` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_history`
--

LOCK TABLES `chat_history` WRITE;
/*!40000 ALTER TABLE `chat_history` DISABLE KEYS */;
INSERT INTO `chat_history` VALUES (1,1,2,'Hi there','2018-09-29 17:24:16'),(2,1,3,'Hi','2018-09-29 17:25:06');
/*!40000 ALTER TABLE `chat_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notifications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `notified_by_user` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  `interest` tinyint(1) DEFAULT NULL,
  `comment` tinyint(1) DEFAULT NULL,
  `jobapply` tinyint(1) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`user_id`,`notified_by_user`,`post_id`),
  KEY `fk_notifications_user1_idx` (`user_id`),
  CONSTRAINT `fk_notifications_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (1,2,3,2,0,1,0,'2018-09-29 15:57:01'),(2,2,3,2,0,0,1,'2018-09-29 15:59:06'),(3,2,4,4,0,0,1,'2018-09-29 16:13:12'),(4,3,6,7,1,0,0,'2018-09-29 16:27:38'),(5,3,6,7,0,1,0,'2018-09-29 16:27:45'),(6,3,4,7,1,0,0,'2018-09-29 16:28:03'),(7,3,4,7,0,1,0,'2018-09-29 16:44:42'),(8,3,6,9,1,0,0,'2018-09-29 16:50:34'),(9,3,4,9,1,0,0,'2018-09-29 16:51:31');
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(280) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `is_advertisment` tinyint(1) DEFAULT NULL,
  `is_public` tinyint(1) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `image` varchar(45) DEFAULT NULL,
  `has_image` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`,`user_id`),
  KEY `fk_post_user1_idx` (`user_id`),
  CONSTRAINT `fk_post_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,'Just created an account in this awesome website.','2018-09-29 14:42:50',0,0,2,'dPY3BrAuy7Ypq9M4k1HO',1),(2,'Looking for lawyer\nLocation: New York\nNice to have: Harvard B.Sc.','2018-09-29 15:45:32',1,1,2,'',0),(3,'Looking for software developer\nLocation: Silicon Valley, California\nNice to have: Experience (5yr) as system administrator or IT infrastracture manager','2018-09-29 16:07:19',1,1,2,'',0),(4,'Looking for web developer (full stack)\nLocation: Silicon Valley, California\nNice to have: Experience with REST-ful APIs','2018-09-29 16:08:28',1,1,2,'',0),(5,'Good morning linkdin users!','2018-09-29 16:16:40',0,1,4,'LmeQ8yn3t3J8MRcSM1Nl',1),(6,'How is everyone doing?','2018-09-29 16:17:03',0,1,4,'',0),(7,'Got my hair done today!','2018-09-29 16:22:03',0,1,3,'HDsNNf8MqoX5uxWZtdW4',1),(8,'Hello everyone','2018-09-29 16:29:47',0,1,6,'',0),(9,'New photoshoot','2018-09-29 16:50:06',0,1,3,'MyXiGBjFyLLba6TZcd2Z',1),(10,'We are looking for software engineer & software developer.\n\nLocation: \nSan Francisco Microsoft Technology Center\n\nRequirements:\n* At least 3 years experience in similar position\n* C/C++','2018-09-29 17:07:23',1,1,8,'',0),(11,'hello world','2018-09-29 17:32:44',0,0,9,'',0),(12,'HACKERMAN','2018-09-29 18:25:18',0,1,9,'AUy7cMZwX5AClZjpQAkA',1);
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_applications`
--

DROP TABLE IF EXISTS `post_applications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post_applications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `post_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`post_id`,`user_id`),
  KEY `fk_post_applications_post1_idx` (`post_id`),
  CONSTRAINT `fk_post_applications_post1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_applications`
--

LOCK TABLES `post_applications` WRITE;
/*!40000 ALTER TABLE `post_applications` DISABLE KEYS */;
INSERT INTO `post_applications` VALUES (1,2,3),(2,4,4);
/*!40000 ALTER TABLE `post_applications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_comment`
--

DROP TABLE IF EXISTS `post_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(280) DEFAULT NULL,
  `comment_timestamp` datetime DEFAULT NULL,
  `post_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`post_id`,`user_id`),
  KEY `fk_PostComment_post1_idx` (`post_id`),
  CONSTRAINT `fk_PostComment_post1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_comment`
--

LOCK TABLES `post_comment` WRITE;
/*!40000 ALTER TABLE `post_comment` DISABLE KEYS */;
INSERT INTO `post_comment` VALUES (1,'Question: How much is the salary?','2018-09-29 15:57:01',2,3),(2,'Looking good!','2018-09-29 16:27:45',7,6),(3,'Love the natural look','2018-09-29 16:44:42',7,4);
/*!40000 ALTER TABLE `post_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_interest`
--

DROP TABLE IF EXISTS `post_interest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post_interest` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `post_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`post_id`,`user_id`),
  KEY `fk_post_interests_post1_idx` (`post_id`),
  CONSTRAINT `fk_post_interests_post1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_interest`
--

LOCK TABLES `post_interest` WRITE;
/*!40000 ALTER TABLE `post_interest` DISABLE KEYS */;
INSERT INTO `post_interest` VALUES (1,7,6),(2,7,4),(4,9,6),(5,9,4);
/*!40000 ALTER TABLE `post_interest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `surname` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `phone_number` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `profile_picture` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `city` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `profession` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `company` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `education` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `public_phone_number` tinyint(1) DEFAULT NULL,
  `public_city` tinyint(1) DEFAULT NULL,
  `public_profession` tinyint(1) DEFAULT NULL,
  `public_company` tinyint(1) DEFAULT NULL,
  `public_education` tinyint(1) DEFAULT NULL,
  `is_admin` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin@admin','a665a45920422f9d417e4867efdc4fb8a04a1f3f','Drunk','Admin','123','','','','','',0,0,0,0,0,1),(2,'test1@test','a665a45920422f9d417e4867efdc4fb8a04a1f3f','John','Doe','123','4XoSjaj6eYi9dHmpfpCv','London','Manager','Tinder','Stanford University',1,1,1,1,1,0),(3,'test2@test','a665a45920422f9d417e4867efdc4fb8a04a1f3f','Alice','Alison','123','CE4fhRcrfvpcR2Yk3Zmw','New York','Lawyer','Pearson Specter Litt Law Firm','Harvard Law School',1,1,1,1,1,0),(4,'test4@test','a665a45920422f9d417e4867efdc4fb8a04a1f3f','Dinesh','Chugtai','123','EByEwU4K4MAZohtk2PK4','Silicon Valley','Web developer','Pied Piper','Walden University',1,1,1,1,1,0),(5,'test3@test','a665a45920422f9d417e4867efdc4fb8a04a1f3f','Bertram','Gilfoyle','123','BJ3EnLepR1xZuyM7heh0','Silicon Valley, San Francisco','System Administrator','Pied Piper','Google coding bootcamp',1,1,1,1,1,0),(6,'test5@test','a665a45920422f9d417e4867efdc4fb8a04a1f3f','Moss','Maurice','123','KwJF4l5XruCxNPmzxLtP','London','Software application developer','Reynholm Industries','',0,1,1,0,0,0),(7,'test6@test','a665a45920422f9d417e4867efdc4fb8a04a1f3f','Sheldon','Cooper','123','DONa3cW35cff1BqG7Rcb','San Fransisco','Theoretical Physicist','California Institute of Technology',' Sc.D. MIT',1,1,1,1,1,0),(8,'test7@test','a665a45920422f9d417e4867efdc4fb8a04a1f3f','Microsoft','Corp.','123','ER3WOCJDMQuSALQydkrP','San Fransisco, California','Computer software','','',1,1,1,0,0,0),(9,'test9@test','a665a45920422f9d417e4867efdc4fb8a04a1f3f','Elliot','Alderson','123','AGzWE8i3Tu2ywzGD7efF','New York City','Cybersecurity Engineer','Allsafe Cybersecurity','fsociety',1,1,1,1,0,0),(10,'test8@test','a665a45920422f9d417e4867efdc4fb8a04a1f3f','Tony','Stark','123','WdebFZEB551qUPDcChtR','San Diego, CA','Iron Man & CEO at Stark Industries','Stark Industries','Master of Science Electrical Engineering',0,1,1,1,1,0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_network`
--

DROP TABLE IF EXISTS `user_network`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_network` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_1` int(11) NOT NULL,
  `user_2` int(11) NOT NULL,
  `is_accepted` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`,`user_1`,`user_2`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_network`
--

LOCK TABLES `user_network` WRITE;
/*!40000 ALTER TABLE `user_network` DISABLE KEYS */;
INSERT INTO `user_network` VALUES (1,3,2,1),(2,5,4,1),(3,7,3,1),(4,5,9,1),(5,7,4,1),(6,7,6,1);
/*!40000 ALTER TABLE `user_network` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-09-29 18:41:29
