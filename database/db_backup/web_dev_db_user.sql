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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-09-29 18:40:06
