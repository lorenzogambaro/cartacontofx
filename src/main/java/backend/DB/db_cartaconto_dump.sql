-- MariaDB dump 10.19  Distrib 10.4.28-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: db_banca
-- ------------------------------------------------------
-- Server version	10.4.28-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `administrators`
--

DROP TABLE IF EXISTS `administrators`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `administrators` (
  `id_admin` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `hashed_pw` varchar(255) NOT NULL,
  `fiscal_code` char(16) NOT NULL,
  `name` varchar(64) NOT NULL,
  `surname` varchar(255) NOT NULL,
  `power_level` int(11) NOT NULL,
  PRIMARY KEY (`id_admin`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `fiscal_code` (`fiscal_code`),
  KEY `FK_administrators_adminpowers` (`power_level`),
  CONSTRAINT `FK_administrators_adminpowers` FOREIGN KEY (`power_level`) REFERENCES `adminpowers` (`id_power`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrators`
--

LOCK TABLES `administrators` WRITE;
/*!40000 ALTER TABLE `administrators` DISABLE KEYS */;
INSERT INTO `administrators` VALUES (0,'admin','c775e7b757ede630cd0aa1113bd102661ab38829ca52a6422ab782862f268646','FRRPLA82C52H501R','Admin','Admin',0);
/*!40000 ALTER TABLE `administrators` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `adminpowers`
--

DROP TABLE IF EXISTS `adminpowers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adminpowers` (
  `id_power` int(11) NOT NULL AUTO_INCREMENT,
  `powerDesc` varchar(255) NOT NULL,
  PRIMARY KEY (`id_power`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adminpowers`
--

LOCK TABLES `adminpowers` WRITE;
/*!40000 ALTER TABLE `adminpowers` DISABLE KEYS */;
INSERT INTO `adminpowers` VALUES (0,'*'),(1,'View'),(2,'Edit'),(3,'Add-Delete');
/*!40000 ALTER TABLE `adminpowers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `banche`
--

DROP TABLE IF EXISTS `banche`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `banche` (
  `abi_code` char(5) NOT NULL,
  `cab_code` char(5) NOT NULL,
  `bank_name` varchar(255) NOT NULL,
  `location_chars` char(2) NOT NULL,
  PRIMARY KEY (`abi_code`,`cab_code`),
  UNIQUE KEY `abi_code` (`abi_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `banche`
--

LOCK TABLES `banche` WRITE;
/*!40000 ALTER TABLE `banche` DISABLE KEYS */;
INSERT INTO `banche` VALUES ('01234','56789','Banca 1','IT');
/*!40000 ALTER TABLE `banche` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conti`
--

DROP TABLE IF EXISTS `conti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conti` (
  `iban_conto` varchar(27) NOT NULL DEFAULT '',
  `opening_date` date NOT NULL,
  `closing_date` date DEFAULT NULL,
  `abi_banca_associata` char(5) NOT NULL DEFAULT '',
  `cab_banca_associata` char(5) NOT NULL DEFAULT '',
  PRIMARY KEY (`iban_conto`),
  UNIQUE KEY `iban_conto` (`iban_conto`),
  KEY `conti_ibfk_1` (`abi_banca_associata`,`cab_banca_associata`),
  CONSTRAINT `conti_ibfk_1` FOREIGN KEY (`abi_banca_associata`, `cab_banca_associata`) REFERENCES `banche` (`abi_code`, `cab_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conti`
--

LOCK TABLES `conti` WRITE;
/*!40000 ALTER TABLE `conti` DISABLE KEYS */;
/*!40000 ALTER TABLE `conti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `intestatari`
--

DROP TABLE IF EXISTS `intestatari`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `intestatari` (
  `id_intestatario` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `hashed_pw` varchar(255) NOT NULL,
  `fiscal_code` varchar(16) NOT NULL,
  `surname_intestatario` varchar(32) NOT NULL,
  `name_intestatario` varchar(32) NOT NULL,
  `birth_date` date NOT NULL,
  `via` varchar(64) NOT NULL,
  `numero` varchar(32) NOT NULL,
  `cap` varchar(5) NOT NULL,
  `comune` varchar(32) NOT NULL,
  `provincia` varchar(2) NOT NULL,
  `phoneNumber` varchar(13) NOT NULL,
  `email_address` varchar(64) NOT NULL,
  `power` int(11) NOT NULL,
  PRIMARY KEY (`id_intestatario`),
  KEY `FK_intestatari_potenzeintestatario` (`power`),
  CONSTRAINT `FK_intestatari_potenzeintestatario` FOREIGN KEY (`power`) REFERENCES `potenzeintestatario` (`id_potenza`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `intestatari`
--

LOCK TABLES `intestatari` WRITE;
/*!40000 ALTER TABLE `intestatari` DISABLE KEYS */;
INSERT INTO `intestatari` VALUES (0,'user1','c775e7b757ede630cd0aa1113bd102661ab38829ca52a6422ab782862f268646','RSSMRA94P15H501S','User','FirstUser','2024-02-23','Via Roma','32/A','00184','Roma','RM','3456789012','user1@user.com',0);
/*!40000 ALTER TABLE `intestatari` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `intestatari_conti`
--

DROP TABLE IF EXISTS `intestatari_conti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `intestatari_conti` (
  `id_relazione` int(11) NOT NULL AUTO_INCREMENT,
  `id_intestatario` int(11) NOT NULL,
  `iban_conto` varchar(27) NOT NULL,
  PRIMARY KEY (`id_relazione`),
  KEY `intestatari_conti_ibfk_1` (`id_intestatario`),
  KEY `intestatari_conti_ibfk_2` (`iban_conto`),
  CONSTRAINT `intestatari_conti_ibfk_1` FOREIGN KEY (`id_intestatario`) REFERENCES `intestatari` (`id_intestatario`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `intestatari_conti_ibfk_2` FOREIGN KEY (`iban_conto`) REFERENCES `conti` (`iban_conto`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `intestatari_conti`
--

LOCK TABLES `intestatari_conti` WRITE;
/*!40000 ALTER TABLE `intestatari_conti` DISABLE KEYS */;
/*!40000 ALTER TABLE `intestatari_conti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movimenti`
--

DROP TABLE IF EXISTS `movimenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movimenti` (
  `id_movimento` int(11) NOT NULL AUTO_INCREMENT,
  `iban_richiedente` varchar(27) NOT NULL,
  `iban_destinatario` varchar(27) NOT NULL,
  `operation_date` date NOT NULL,
  `valute_date` date NOT NULL,
  `causal` varchar(512) NOT NULL,
  `amount` double NOT NULL,
  `movement_type` int(11) NOT NULL,
  PRIMARY KEY (`id_movimento`),
  KEY `movimenti_ibfk_1` (`movement_type`),
  KEY `FK_movimenti_conti` (`iban_richiedente`),
  KEY `FK_movimenti_conti_2` (`iban_destinatario`),
  CONSTRAINT `movimenti_ibfk_1` FOREIGN KEY (`movement_type`) REFERENCES `tipimovimento` (`id_type`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movimenti`
--

LOCK TABLES `movimenti` WRITE;
/*!40000 ALTER TABLE `movimenti` DISABLE KEYS */;
/*!40000 ALTER TABLE `movimenti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `potenzeintestatario`
--

DROP TABLE IF EXISTS `potenzeintestatario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `potenzeintestatario` (
  `id_potenza` int(11) NOT NULL AUTO_INCREMENT,
  `descPerms` varchar(255) NOT NULL,
  PRIMARY KEY (`id_potenza`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `potenzeintestatario`
--

LOCK TABLES `potenzeintestatario` WRITE;
/*!40000 ALTER TABLE `potenzeintestatario` DISABLE KEYS */;
INSERT INTO `potenzeintestatario` VALUES (0,'*'),(1,'basic'),(2,'medium'),(3,'premium'),(4,'deluxe');
/*!40000 ALTER TABLE `potenzeintestatario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipimovimento`
--

DROP TABLE IF EXISTS `tipimovimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipimovimento` (
  `id_type` int(11) NOT NULL AUTO_INCREMENT,
  `descript` varchar(255) NOT NULL,
  `cost` double NOT NULL,
  `direction` bit(1) NOT NULL DEFAULT b'0',
  `min_power_required_id` int(11) NOT NULL,
  `days` int(11) NOT NULL,
  PRIMARY KEY (`id_type`),
  KEY `FK_tipimovimento_potenzeintestatario` (`min_power_required_id`),
  CONSTRAINT `FK_tipimovimento_potenzeintestatario` FOREIGN KEY (`min_power_required_id`) REFERENCES `potenzeintestatario` (`id_potenza`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipimovimento`
--

LOCK TABLES `tipimovimento` WRITE;
/*!40000 ALTER TABLE `tipimovimento` DISABLE KEYS */;
INSERT INTO `tipimovimento` VALUES (1,'Bonifico1',1,'\0',4,2),(2,'Prelievo 1',0,'',4,0),(3,'Versamento Volontario',1.5,'\0',4,0);
/*!40000 ALTER TABLE `tipimovimento` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-28  5:37:41
