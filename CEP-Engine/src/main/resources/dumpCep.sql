CREATE DATABASE  IF NOT EXISTS `cep` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `cep`;
-- MySQL dump 10.13  Distrib 5.5.41, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: cep
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
-- Table structure for table `ACTIVE_CONFIGURATION`
--

DROP TABLE IF EXISTS `ACTIVE_CONFIGURATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ACTIVE_CONFIGURATION` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `LAST_DEPLOY_DATE` datetime NOT NULL,
  `VERSION_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK21610F5D94402783` (`VERSION_ID`),
  CONSTRAINT `FK21610F5D94402783` FOREIGN KEY (`VERSION_ID`) REFERENCES `VERSION` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ACTIVE_CONFIGURATION`
--

LOCK TABLES `ACTIVE_CONFIGURATION` WRITE;
/*!40000 ALTER TABLE `ACTIVE_CONFIGURATION` DISABLE KEYS */;
INSERT INTO `ACTIVE_CONFIGURATION` VALUES (1,'2015-01-01 00:00:00',1);
/*!40000 ALTER TABLE `ACTIVE_CONFIGURATION` ENABLE KEYS */;
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
  `RULE` mediumtext NOT NULL,
  `VERSION_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK268EFC94402783` (`VERSION_ID`),
  CONSTRAINT `FK268EFC94402783` FOREIGN KEY (`VERSION_ID`) REFERENCES `VERSION` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RULE`
--

LOCK TABLES `RULE` WRITE;
/*!40000 ALTER TABLE `RULE` DISABLE KEYS */;
INSERT INTO `RULE` VALUES (1,'CityWeather','package edu.fing.drools;\n\nimport java.util.HashMap;\nimport edu.fing.cep.engine.utils.CepUtils;\n\ndeclare CityWeatherUpdate\n@role(event)\n	city:	String\n	raining:boolean\nend\n\n\nrule \"CityWeatherUpdate Message Mapper\"\nwhen\n    message:HashMap(this[\"type\"] == \"CITY_WEATHER\")\n\nthen\n    System.out.println(\"Mapping message to CityWeatherUpdate...\");\n	String body = (String)message.get(\"body\");\n	CityWeatherUpdate cityWeatherUpdate = new CityWeatherUpdate();\n	String[] split = body.split(\",\");\n	cityWeatherUpdate.setCity(split[0]);\n	cityWeatherUpdate.setRaining(Boolean.parseBoolean(split[1]));\n	insert(cityWeatherUpdate);    \nend\n\nrule \"CityWeather, new city!\"\nwhen\n    cityWeatherUpdate:CityWeatherUpdate()\n    not (exists CityWeather(city==cityWeatherUpdate.city ))\nthen\n    System.out.println(\"CityWeather, new city! city= \" + cityWeatherUpdate.getCity());\n	CityWeather cityWeather = new CityWeather();\n	cityWeather.setCity(cityWeatherUpdate.getCity());\n	cityWeather.setRaining(cityWeatherUpdate.isRaining());\n	insert(cityWeather);     \nend\n\nrule \"CityWeather, update weather from existing city!\"\nno-loop true\nwhen\n    cityWeatherUpdate:CityWeatherUpdate()\n    cityWeather:CityWeather(city==cityWeatherUpdate.city)\nthen\n    System.out.println(\"CityWeather, update weather from existing city! city= \" + cityWeatherUpdate.getCity());\n	cityWeather.setRaining(cityWeatherUpdate.isRaining()); \n	update(cityWeather);\nend\n\n',1),(2,'ContextReasonerNotification','package edu.fing.drools;\n\nimport java.util.HashMap;\nimport edu.fing.cep.engine.utils.CepUtils;\n\n\ndeclare ContextReasonerNotification\n@role(event)\n	situationName:	String\n	userId:			String\n	contextualData:	HashMap\nend\n\n\nrule \"ContextReasoner Notification...\"\nwhen\n    notif:ContextReasonerNotification()\nthen\n    System.out.println(\"ContextReasoner Notification!!! Preparing to notify!\");\n	HashMap hash = new HashMap<String,Object>();\n	hash.put(\"situationName\",notif.getSituationName());\n	hash.put(\"userId\",notif.getUserId());\n	hash.put(\"contextualData\",notif.getContextualData());\n	CepUtils.notifyContextReasoner(hash);\nend\n\n',1),(3,'UserLocation','package edu.fing.drools;\nimport java.util.HashMap;\n\n\ndeclare UserLocation\n@role(event)\n	lat:	long\n	lon:	long\n	userId:	String\nend\n\ndeclare InCity\n@role(event)\n	city:	String\n	userId:	String\nend\n\ndeclare CityWeather\n@role(event)\n	city:	String\n	raining:boolean\nend\n\nrule \"UserLocationMessageMapper\"\nwhen\n    message:HashMap(this[\"type\"] == \"USER_LOCATION\")\nthen\n    System.out.println(\"Mapping message to UserLocation...\");\n	String body = (String)message.get(\"body\");\n	UserLocation usrLoc = new UserLocation();\n	String[] split = body.split(\",\");\n	usrLoc.setLat(Long.parseLong(split[0]));\n	usrLoc.setLon(Long.parseLong(split[1]));\n	usrLoc.setUserId(split[2]);\n	insert(usrLoc);    \nend\n\n\nrule \"User in City, new\"\nwhen\n    usrLoc:UserLocation()\n    not (exists InCity(userId==usrLoc.userId))\nthen\n    System.out.println(\"UserLocation received NEW... userId= \" + usrLoc.getUserId());\n	InCity usrCity = new InCity();\n	usrCity.setCity(\"CUIDAD 1\");\n	usrCity.setUserId(usrLoc.getUserId());\n	insert(usrCity);    \nend\n\nrule \"User in City, update\"\nno-loop true\nwhen\n    usrLoc:UserLocation()\n    usrInCity:InCity(userId==usrLoc.userId)\nthen\n    System.out.println(\"UserLocation received UPDATE... userId= \" + usrLoc.getUserId());\n	usrInCity.setCity(\"CUIDAD 1\");\n	update(usrInCity);    \nend\n\n\nrule \"User in City Raining\"\nwhen\n    usrInCity:InCity()\n    CityWeather (city == usrInCity.city, raining == true) over window:time( 30m )\nthen\n    System.out.println(\"UserInCity RAINING!!! userId= \" + usrInCity.getUserId() + \"city= \" + usrInCity.getCity() + \"  ---  NOTIFICAR CONTEXT REASONER!!!!!\");\n    ContextReasonerNotification notif = new ContextReasonerNotification();\n    notif.setSituationName(\"UserInCityRaining\");\n    notif.setUserId(usrInCity.getUserId());\n    HashMap<String,String> contextualData = new HashMap<String,String>();\n    contextualData.put(\"city\",usrInCity.getCity());\n    notif.setContextualData(contextualData);\n    System.out.println(\"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\");\n	insert(notif);\nend\n\n',1);
/*!40000 ALTER TABLE `RULE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `VERSION`
--

DROP TABLE IF EXISTS `VERSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VERSION` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CREATION_DATE` datetime NOT NULL,
  `VERSION_NUMBER` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VERSION`
--

LOCK TABLES `VERSION` WRITE;
/*!40000 ALTER TABLE `VERSION` DISABLE KEYS */;
INSERT INTO `VERSION` VALUES (1,'2012-06-27 00:00:00','1.0.0');
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

-- Dump completed on 2015-02-02 23:23:56
