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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-09-29 18:40:06
