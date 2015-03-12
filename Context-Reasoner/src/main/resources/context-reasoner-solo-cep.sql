CREATE DATABASE  IF NOT EXISTS `context_reasoner` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `context_reasoner`;
-- MySQL dump 10.13  Distrib 5.5.41, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: context_reasoner
-- ------------------------------------------------------
-- Server version	5.5.41-0ubuntu0.14.04.1

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
-- Table structure for table `RULE_VERSION`
--

DROP TABLE IF EXISTS `RULE_VERSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RULE_VERSION` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DRL` varbinary(5000) NOT NULL,
  `RULE_ID` bigint(20) DEFAULT NULL,
  `VERSION_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_RULE_VERSION_1` (`RULE_ID`),
  KEY `FK_RULE_VERSION_2` (`VERSION_ID`),
  CONSTRAINT `FK_RULE_VERSION_1` FOREIGN KEY (`RULE_ID`) REFERENCES `RULE` (`ID`),
  CONSTRAINT `FK_RULE_VERSION_2` FOREIGN KEY (`VERSION_ID`) REFERENCES `VERSION` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RULE_VERSION`
--

LOCK TABLES `RULE_VERSION` WRITE;
/*!40000 ALTER TABLE `RULE_VERSION` DISABLE KEYS */;
INSERT INTO `RULE_VERSION` VALUES (1,'package edu.fing.drools;\n\nimport java.util.HashMap;\nimport edu.fing.cep.engine.utils.CepUtils;\n\n\ndeclare ContextReasonerNotification\n@role(event)\n	situationName:	String\n	userId:			String\n	contextualData:	HashMap\nend\n\n\nrule \"ContextReasoner Notification...\"\nwhen\n    notif:ContextReasonerNotification()\nthen\n    System.out.println(\"ContextReasoner Notification!!! Preparing to notify!\");\n	HashMap hash = new HashMap<String,Object>();\n	hash.put(\"situationName\",notif.getSituationName());\n	hash.put(\"userId\",notif.getUserId());\n	hash.put(\"contextualData\",notif.getContextualData());\n	CepUtils.notifyContextReasoner(hash);\nend\n\n',1,1);
/*!40000 ALTER TABLE `RULE_VERSION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RULE`
--

DROP TABLE IF EXISTS `RULE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RULE` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RULE`
--

LOCK TABLES `RULE` WRITE;
/*!40000 ALTER TABLE `RULE` DISABLE KEYS */;
INSERT INTO `RULE` VALUES (1,'ContextReasonerNotification');
/*!40000 ALTER TABLE `RULE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ACTIVE_CONFIGURATION`
--

DROP TABLE IF EXISTS `ACTIVE_CONFIGURATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ACTIVE_CONFIGURATION` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `LAST_DEPLOY_DATE` datetime DEFAULT NULL,
  `ACTIVE_VERSION_ID` bigint(20) DEFAULT NULL,
  `LAST_VERSION_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ACTIVE_CONFIGURATION_1` (`ACTIVE_VERSION_ID`),
  KEY `FK_ACTIVE_CONFIGURATION_2` (`LAST_VERSION_ID`),
  CONSTRAINT `FK_ACTIVE_CONFIGURATION_1` FOREIGN KEY (`ACTIVE_VERSION_ID`) REFERENCES `VERSION` (`ID`),
  CONSTRAINT `FK_ACTIVE_CONFIGURATION_2` FOREIGN KEY (`LAST_VERSION_ID`) REFERENCES `VERSION` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ACTIVE_CONFIGURATION`
--

LOCK TABLES `ACTIVE_CONFIGURATION` WRITE;
/*!40000 ALTER TABLE `ACTIVE_CONFIGURATION` DISABLE KEYS */;
INSERT INTO `ACTIVE_CONFIGURATION` VALUES (1,'2015-01-01 00:00:00',1,1);
/*!40000 ALTER TABLE `ACTIVE_CONFIGURATION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `VERSION`
--

DROP TABLE IF EXISTS `VERSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VERSION` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION_NUMBER` varchar(255) NOT NULL,
  `CREATION_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VERSION`
--

LOCK TABLES `VERSION` WRITE;
/*!40000 ALTER TABLE `VERSION` DISABLE KEYS */;
INSERT INTO `VERSION` VALUES (1,'1.2.0','2015-01-01 00:00:00');
/*!40000 ALTER TABLE `VERSION` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-03-12 19:57:33
