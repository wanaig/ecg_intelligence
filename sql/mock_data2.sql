-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: ecg_intelligence_platform
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bed_info`
--

DROP TABLE IF EXISTS `bed_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bed_info` (
  `bed_id` bigint NOT NULL AUTO_INCREMENT COMMENT '床位ID',
  `ward_id` bigint NOT NULL COMMENT '关联病房ID',
  `bed_no` varchar(10) NOT NULL COMMENT '床位编号',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '床位状态：0-空床，1-已占用',
  `patient_id` bigint DEFAULT NULL COMMENT '关联患者ID（空床为NULL）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`bed_id`),
  UNIQUE KEY `uk_ward_bed` (`ward_id`,`bed_no`),
  CONSTRAINT `bed_info_ibfk_1` FOREIGN KEY (`ward_id`) REFERENCES `ward_info` (`ward_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='床位明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bed_info`
--

LOCK TABLES `bed_info` WRITE;
/*!40000 ALTER TABLE `bed_info` DISABLE KEYS */;
INSERT INTO `bed_info` VALUES (1,1,'101-01',1,1,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(2,1,'101-02',1,2,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(3,1,'101-03',1,3,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(4,2,'102-01',1,4,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(5,2,'102-02',1,5,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(6,2,'102-03',1,6,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(7,3,'103-01',1,7,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(8,3,'103-02',1,8,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(9,3,'103-03',1,9,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(10,4,'104-01',1,10,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(11,4,'104-02',1,11,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(12,4,'104-03',1,12,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(13,5,'105-01',1,13,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(14,5,'105-02',1,14,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(15,5,'105-03',1,15,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(16,6,'106-01',1,16,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(17,6,'106-02',1,17,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(18,6,'106-03',1,18,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(19,7,'107-01',1,19,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(20,7,'107-02',1,20,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(21,7,'107-03',1,21,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(22,8,'108-01',1,22,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(23,8,'108-02',1,23,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(24,8,'108-03',1,24,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(25,9,'109-01',1,25,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(26,9,'109-02',1,25,'2026-04-15 18:14:16','2026-04-15 18:21:24'),(27,9,'109-03',1,27,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(28,10,'110-01',1,28,'2026-04-15 18:14:16','2026-04-15 18:14:16'),(29,10,'110-02',0,NULL,'2026-04-15 18:14:16','2026-04-15 18:26:28'),(30,10,'110-03',1,20,'2026-04-15 18:14:16','2026-04-15 18:21:25');
/*!40000 ALTER TABLE `bed_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_equipment_type`
--

DROP TABLE IF EXISTS `sys_dict_equipment_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_equipment_type` (
  `dict_id` int NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  `type_code` varchar(30) NOT NULL COMMENT '类型编码',
  `type_name` varchar(30) NOT NULL COMMENT '类型名称',
  `type_desc` varchar(255) DEFAULT NULL COMMENT '类型描述',
  `sort` int DEFAULT '0' COMMENT '排序号',
  PRIMARY KEY (`dict_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='设备类型字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_equipment_type`
--

LOCK TABLES `sys_dict_equipment_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_equipment_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_dict_equipment_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_warning_level`
--

DROP TABLE IF EXISTS `sys_dict_warning_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_warning_level` (
  `dict_id` int NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  `level_code` varchar(20) NOT NULL COMMENT '级别编码',
  `level_name` varchar(20) NOT NULL COMMENT '级别名称',
  `level_desc` varchar(255) DEFAULT NULL COMMENT '级别描述',
  `sort` int DEFAULT '0' COMMENT '排序号',
  PRIMARY KEY (`dict_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='预警级别字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_warning_level`
--

LOCK TABLES `sys_dict_warning_level` WRITE;
/*!40000 ALTER TABLE `sys_dict_warning_level` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_dict_warning_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_warning_type`
--

DROP TABLE IF EXISTS `sys_dict_warning_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_warning_type` (
  `dict_id` int NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  `type_code` varchar(30) NOT NULL COMMENT '类型编码',
  `type_name` varchar(30) NOT NULL COMMENT '类型名称',
  `type_desc` varchar(255) DEFAULT NULL COMMENT '类型描述',
  `sort` int DEFAULT '0' COMMENT '排序号',
  PRIMARY KEY (`dict_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='预警类型字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_warning_type`
--

LOCK TABLES `sys_dict_warning_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_warning_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_dict_warning_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_ecg_abnormal_warning`
--

DROP TABLE IF EXISTS `sys_ecg_abnormal_warning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_ecg_abnormal_warning` (
  `warning_id` int NOT NULL AUTO_INCREMENT COMMENT '预警ID，主键',
  `measure_id` int NOT NULL COMMENT '测量ID',
  `patient_id` int NOT NULL COMMENT '患者ID',
  `warning_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '预警时间',
  `warning_type` varchar(30) DEFAULT NULL COMMENT '预警类型',
  `warning_level` varchar(20) DEFAULT NULL COMMENT '预警级别',
  `warning_content` text COMMENT '预警详情',
  `handle_doctor_id` int DEFAULT NULL COMMENT '处理医生ID',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_result` text COMMENT '处理结果',
  `warning_status` varchar(20) DEFAULT '0' COMMENT '纳入状态 1纳入 0不纳入',
  PRIMARY KEY (`warning_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='心电图异常预警表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_ecg_abnormal_warning`
--

LOCK TABLES `sys_ecg_abnormal_warning` WRITE;
/*!40000 ALTER TABLE `sys_ecg_abnormal_warning` DISABLE KEYS */;
INSERT INTO `sys_ecg_abnormal_warning` VALUES (1,1,1,'2026-04-15 08:05:00',NULL,' 危急 ',' 室性心动过速 ',NULL,NULL,NULL,'1'),(2,2,2,'2026-04-15 08:45:00',NULL,' 异常 ',' 频发室早 ',NULL,NULL,NULL,'0'),(3,3,3,'2026-04-15 09:02:00',NULL,' 正常 ',' 窦性心律 ',NULL,NULL,NULL,'1'),(4,4,4,'2026-04-15 09:20:00',NULL,' 危急 ',' 心室颤动 ',NULL,NULL,NULL,'1'),(5,5,5,'2026-04-15 09:40:00',NULL,' 异常 ',' 心房颤动 ',NULL,NULL,NULL,'0'),(6,6,6,'2026-04-15 10:05:00',NULL,' 异常 ','T 波改变 ',NULL,NULL,NULL,'1'),(7,7,7,'2026-04-15 10:20:00',NULL,' 危急 ','ST 段抬高 ',NULL,NULL,NULL,'0'),(8,8,8,'2026-04-15 10:45:00',NULL,' 异常 ',' 心动过缓 ',NULL,NULL,NULL,'1'),(9,9,9,'2026-04-15 11:08:00',NULL,' 异常 ',' 房室传导阻滞 ',NULL,NULL,NULL,'1'),(10,10,10,'2026-04-15 11:40:00',NULL,' 危急 ',' 极度心动过速 ',NULL,NULL,NULL,'0'),(11,11,11,'2026-04-16 08:05:00',NULL,' 异常 ',' 房性早搏 ',NULL,NULL,NULL,'1'),(12,12,12,'2026-04-16 08:45:00',NULL,' 危急 ',' 室颤前兆 ',NULL,NULL,NULL,'0'),(13,13,13,'2026-04-16 09:02:00',NULL,' 正常 ',' 窦性心律不齐 ',NULL,NULL,NULL,'1'),(14,14,14,'2026-04-16 09:20:00',NULL,' 异常 ','QT 间期延长 ',NULL,NULL,NULL,'1'),(15,15,15,'2026-04-16 09:40:00',NULL,' 危急 ',' 心肌缺血 ',NULL,NULL,NULL,'0'),(16,16,16,'2026-04-16 10:05:00',NULL,' 异常 ','ST 段压低 ',NULL,NULL,NULL,'1'),(17,17,17,'2026-04-16 10:20:00',NULL,' 正常 ',' 交界性心律 ',NULL,NULL,NULL,'0'),(18,18,18,'2026-04-16 10:45:00',NULL,' 危急 ',' 室性逸搏心律 ',NULL,NULL,NULL,'1'),(19,19,19,'2026-04-16 11:08:00',NULL,' 异常 ',' 右束支传导阻滞 ',NULL,NULL,NULL,'1'),(20,20,20,'2026-04-16 11:40:00',NULL,' 异常 ',' 左心室肥厚 ',1001,'2026-04-15 18:21:25','常规纳入','1'),(21,21,21,'2026-04-17 08:05:00',NULL,' 危急 ',' 室性心动过速 ',NULL,NULL,NULL,'1'),(22,22,22,'2026-04-17 08:45:00',NULL,' 正常 ',' 窦性心动过速 ',NULL,NULL,NULL,'0'),(23,23,23,'2026-04-17 09:02:00',NULL,' 异常 ',' 频发房早 ',NULL,NULL,NULL,'1'),(24,24,24,'2026-04-17 09:20:00',NULL,' 危急 ',' 心室颤动 ',NULL,NULL,NULL,'1'),(25,25,25,'2026-04-17 09:40:00',NULL,' 正常 ',' 窦性心动过缓 ',1001,'2026-04-15 18:21:24','常规纳入','1'),(26,26,26,'2026-04-17 10:05:00',NULL,' 异常 ','T 波倒置 ',NULL,NULL,NULL,'1'),(27,27,27,'2026-04-17 10:20:00',NULL,' 危急 ','ST 段抬高心肌梗死 ',1001,'2026-04-15 18:15:01','常规纳入','1'),(28,28,28,'2026-04-17 10:45:00',NULL,' 异常 ',' 一度房室传导阻滞 ',NULL,NULL,NULL,'1'),(29,29,29,'2026-04-17 11:08:00',NULL,' 正常 ',' 房性心动过速 ',1001,'2026-04-15 18:26:28','常规纳入','0'),(30,30,30,'2026-04-17 11:40:00',NULL,' 危急 ',' 极度心动过缓 ',1001,'2026-04-15 18:14:55','常规纳入','1');
/*!40000 ALTER TABLE `sys_ecg_abnormal_warning` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_ecg_ai_analysis`
--

DROP TABLE IF EXISTS `sys_ecg_ai_analysis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_ecg_ai_analysis` (
  `ai_analysis_id` int NOT NULL AUTO_INCREMENT COMMENT 'AI分析ID，主键',
  `measure_id` int NOT NULL COMMENT '测量ID',
  `model_version` varchar(50) DEFAULT NULL COMMENT '模型版本',
  `abnormal_fragment` varchar(255) DEFAULT NULL COMMENT '异常片段',
  `ai_diagnosis` text COMMENT 'AI初步诊断',
  `abnormal_index` varchar(255) DEFAULT NULL COMMENT '异常指标汇总',
  `confidence` decimal(5,2) DEFAULT NULL COMMENT '置信度%',
  `analysis_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '分析完成时间',
  `is_verified` tinyint(1) DEFAULT '0' COMMENT '是否医生验证',
  PRIMARY KEY (`ai_analysis_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='心电图AI分析表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_ecg_ai_analysis`
--

LOCK TABLES `sys_ecg_ai_analysis` WRITE;
/*!40000 ALTER TABLE `sys_ecg_ai_analysis` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_ecg_ai_analysis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_ecg_core_index`
--

DROP TABLE IF EXISTS `sys_ecg_core_index`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_ecg_core_index` (
  `index_id` int NOT NULL AUTO_INCREMENT COMMENT '指标ID，主键',
  `measure_id` int NOT NULL COMMENT '测量ID',
  `heartbeat_total` int DEFAULT NULL COMMENT '心搏总数',
  `avg_heart_rate` decimal(5,1) DEFAULT NULL COMMENT '平均心率',
  `min_heart_rate` int DEFAULT NULL COMMENT '最慢心率',
  `max_heart_rate` int DEFAULT NULL COMMENT '最快心率',
  `max_rr_interval` decimal(5,3) DEFAULT NULL COMMENT '最长RR间期',
  `max_rr_count` int DEFAULT NULL COMMENT '≥2.0s长RR间期次数',
  `tachycardia_times` int DEFAULT NULL COMMENT '心动过速次数',
  `tachycardia_heartbeat` int DEFAULT NULL COMMENT '心动过速心搏数',
  `bradycardia_times` int DEFAULT NULL COMMENT '心动过缓次数',
  `atrial_premature` int DEFAULT NULL COMMENT '房早总数',
  `atrial_premature_single` int DEFAULT NULL COMMENT '单发房早',
  `atrial_premature_pair` int DEFAULT NULL COMMENT '成对房早',
  `ventricular_premature` int DEFAULT NULL COMMENT '室早总数',
  `ventricular_premature_single` int DEFAULT NULL COMMENT '单发室早',
  `ventricular_premature_pair` int DEFAULT NULL COMMENT '成对室早',
  `hrv_sdnn` decimal(5,1) DEFAULT NULL COMMENT 'SDNN',
  `hrv_sdann` decimal(5,1) DEFAULT NULL COMMENT 'SDANN',
  `hrv_rmssd` decimal(5,1) DEFAULT NULL COMMENT 'rMSSD',
  `hrv_pnn50` decimal(5,2) DEFAULT NULL COMMENT 'pNN50',
  `hrv_lf` decimal(5,1) DEFAULT NULL COMMENT 'LF',
  `hrv_hf` decimal(5,1) DEFAULT NULL COMMENT 'HF',
  `hrv_lf_hf` decimal(5,2) DEFAULT NULL COMMENT 'LF/HF',
  `ventricular_tachycardia` int DEFAULT NULL COMMENT '室速阵数',
  `atrial_tachycardia` int DEFAULT NULL COMMENT '房速阵数',
  `rhythm_type` varchar(30) DEFAULT NULL COMMENT '节律类型',
  PRIMARY KEY (`index_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='心电图核心指标表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_ecg_core_index`
--

LOCK TABLES `sys_ecg_core_index` WRITE;
/*!40000 ALTER TABLE `sys_ecg_core_index` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_ecg_core_index` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_ecg_doctor_info`
--

DROP TABLE IF EXISTS `sys_ecg_doctor_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_ecg_doctor_info` (
  `doctor_id` int NOT NULL AUTO_INCREMENT COMMENT '医生ID，主键',
  `doctor_name` varchar(20) NOT NULL COMMENT '医生姓名',
  `doctor_title` varchar(30) DEFAULT NULL COMMENT '医生职称',
  `ward_id` int DEFAULT NULL COMMENT '所属病区ID',
  `user_name` varchar(20) NOT NULL COMMENT '登录账号',
  `password` varchar(50) NOT NULL COMMENT '加密密码',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '是否启用 1启用 0禁用',
  PRIMARY KEY (`doctor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='医生信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_ecg_doctor_info`
--

LOCK TABLES `sys_ecg_doctor_info` WRITE;
/*!40000 ALTER TABLE `sys_ecg_doctor_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_ecg_doctor_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_ecg_equipment_info`
--

DROP TABLE IF EXISTS `sys_ecg_equipment_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_ecg_equipment_info` (
  `equipment_id` int NOT NULL AUTO_INCREMENT COMMENT '设备ID，主键',
  `equipment_code` varchar(30) NOT NULL COMMENT '设备编号',
  `equipment_name` varchar(50) DEFAULT NULL COMMENT '设备名称',
  `equipment_type` varchar(30) DEFAULT NULL COMMENT '设备类型',
  `manufacturer` varchar(50) DEFAULT NULL COMMENT '厂商',
  `equipment_version` varchar(30) DEFAULT NULL COMMENT '固件版本',
  `ward_id` int DEFAULT NULL COMMENT '所属病区ID',
  `equipment_status` varchar(20) DEFAULT '正常' COMMENT '设备状态',
  `install_time` datetime DEFAULT NULL COMMENT '安装时间',
  `maintain_time` datetime DEFAULT NULL COMMENT '上次维护时间',
  `next_maintain_time` datetime DEFAULT NULL COMMENT '下次维护时间',
  PRIMARY KEY (`equipment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='设备信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_ecg_equipment_info`
--

LOCK TABLES `sys_ecg_equipment_info` WRITE;
/*!40000 ALTER TABLE `sys_ecg_equipment_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_ecg_equipment_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_ecg_follow_up`
--

DROP TABLE IF EXISTS `sys_ecg_follow_up`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_ecg_follow_up` (
  `follow_id` int NOT NULL AUTO_INCREMENT COMMENT '随访ID，主键',
  `patient_id` int NOT NULL COMMENT '患者ID',
  `measure_id` int DEFAULT NULL COMMENT '测量ID',
  `follow_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '随访时间',
  `follow_type` varchar(30) DEFAULT NULL COMMENT '随访方式',
  `follow_doctor_id` int NOT NULL COMMENT '随访医生ID',
  `patient_condition` text COMMENT '患者病情',
  `follow_advice` text COMMENT '随访建议',
  `next_follow_time` datetime DEFAULT NULL COMMENT '下次随访时间',
  `follow_remark` text COMMENT '备注',
  PRIMARY KEY (`follow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='心电图随访记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_ecg_follow_up`
--

LOCK TABLES `sys_ecg_follow_up` WRITE;
/*!40000 ALTER TABLE `sys_ecg_follow_up` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_ecg_follow_up` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_ecg_measure_record`
--

DROP TABLE IF EXISTS `sys_ecg_measure_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_ecg_measure_record` (
  `measure_id` int NOT NULL AUTO_INCREMENT COMMENT '测量ID，主键',
  `patient_id` int NOT NULL COMMENT '患者ID',
  `measure_type` varchar(30) DEFAULT NULL COMMENT '测量类型',
  `equipment_id` int DEFAULT NULL COMMENT '设备ID',
  `start_time` datetime DEFAULT NULL COMMENT '测量开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '测量结束时间',
  `total_duration` varchar(50) DEFAULT NULL COMMENT '测量总时长',
  `wave_data_path` varchar(255) DEFAULT NULL COMMENT '波形数据路径',
  `measure_point` varchar(30) DEFAULT NULL COMMENT '测量时间点',
  `measurer` varchar(20) DEFAULT NULL COMMENT '测量者',
  `ward_id` int DEFAULT NULL COMMENT '病区ID',
  `measure_status` varchar(20) DEFAULT NULL COMMENT '测量状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`measure_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='心电图采集记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_ecg_measure_record`
--

LOCK TABLES `sys_ecg_measure_record` WRITE;
/*!40000 ALTER TABLE `sys_ecg_measure_record` DISABLE KEYS */;
INSERT INTO `sys_ecg_measure_record` VALUES (1,1,' 常规心电 ',NULL,'2026-04-15 08:00:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(2,2,' 动态心电 ',NULL,'2026-04-15 08:30:00',NULL,NULL,NULL,NULL,NULL,NULL,' 测量中 ','2026-04-15 18:14:16'),(3,3,' 常规心电 ',NULL,'2026-04-15 09:00:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(4,4,' 常规心电 ',NULL,'2026-04-15 09:15:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(5,5,' 常规心电 ',NULL,'2026-04-15 09:30:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(6,6,' 常规心电 ',NULL,'2026-04-15 10:00:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(7,7,' 常规心电 ',NULL,'2026-04-15 10:15:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(8,8,' 动态心电 ',NULL,'2026-04-15 10:30:00',NULL,NULL,NULL,NULL,NULL,NULL,' 测量中 ','2026-04-15 18:14:16'),(9,9,' 常规心电 ',NULL,'2026-04-15 11:00:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(10,10,' 常规心电 ',NULL,'2026-04-15 11:30:00',NULL,NULL,NULL,NULL,NULL,NULL,' 测量中 ','2026-04-15 18:14:16'),(11,11,' 常规心电 ',NULL,'2026-04-16 08:00:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(12,12,' 动态心电 ',NULL,'2026-04-16 08:30:00',NULL,NULL,NULL,NULL,NULL,NULL,' 测量中 ','2026-04-15 18:14:16'),(13,13,' 常规心电 ',NULL,'2026-04-16 09:00:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(14,14,' 常规心电 ',NULL,'2026-04-16 09:15:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(15,15,' 常规心电 ',NULL,'2026-04-16 09:30:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(16,16,' 常规心电 ',NULL,'2026-04-16 10:00:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(17,17,' 常规心电 ',NULL,'2026-04-16 10:15:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(18,18,' 动态心电 ',NULL,'2026-04-16 10:30:00',NULL,NULL,NULL,NULL,NULL,NULL,' 测量中 ','2026-04-15 18:14:16'),(19,19,' 常规心电 ',NULL,'2026-04-16 11:00:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(20,20,' 常规心电 ',NULL,'2026-04-16 11:30:00',NULL,NULL,NULL,NULL,NULL,NULL,' 测量中 ','2026-04-15 18:14:16'),(21,21,' 常规心电 ',NULL,'2026-04-17 08:00:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(22,22,' 动态心电 ',NULL,'2026-04-17 08:30:00',NULL,NULL,NULL,NULL,NULL,NULL,' 测量中 ','2026-04-15 18:14:16'),(23,23,' 常规心电 ',NULL,'2026-04-17 09:00:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(24,24,' 常规心电 ',NULL,'2026-04-17 09:15:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(25,25,' 常规心电 ',NULL,'2026-04-17 09:30:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(26,26,' 常规心电 ',NULL,'2026-04-17 10:00:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(27,27,' 常规心电 ',NULL,'2026-04-17 10:15:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(28,28,' 动态心电 ',NULL,'2026-04-17 10:30:00',NULL,NULL,NULL,NULL,NULL,NULL,' 测量中 ','2026-04-15 18:14:16'),(29,29,' 常规心电 ',NULL,'2026-04-17 11:00:00',NULL,NULL,NULL,NULL,NULL,NULL,' 已完成 ','2026-04-15 18:14:16'),(30,30,' 常规心电 ',NULL,'2026-04-17 11:30:00',NULL,NULL,NULL,NULL,NULL,NULL,' 测量中 ','2026-04-15 18:14:16');
/*!40000 ALTER TABLE `sys_ecg_measure_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_ecg_measure_stat`
--

DROP TABLE IF EXISTS `sys_ecg_measure_stat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_ecg_measure_stat` (
  `stat_id` int NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `stat_time` datetime DEFAULT NULL COMMENT '统计时间',
  `ward_id` int DEFAULT NULL COMMENT '病区ID',
  `equipment_type` varchar(30) DEFAULT NULL COMMENT '设备类型',
  `total_measure` int DEFAULT '0' COMMENT '总测量次数',
  `normal_measure` int DEFAULT '0' COMMENT '正常次数',
  `abnormal_measure` int DEFAULT '0' COMMENT '异常次数',
  `stat_cycle` varchar(20) DEFAULT NULL COMMENT '统计周期',
  PRIMARY KEY (`stat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='心电测量统计报表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_ecg_measure_stat`
--

LOCK TABLES `sys_ecg_measure_stat` WRITE;
/*!40000 ALTER TABLE `sys_ecg_measure_stat` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_ecg_measure_stat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_ecg_patient_info`
--

DROP TABLE IF EXISTS `sys_ecg_patient_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_ecg_patient_info` (
  `patient_id` int NOT NULL AUTO_INCREMENT COMMENT '患者ID，主键',
  `patient_name` varchar(20) NOT NULL COMMENT '患者姓名',
  `patient_gender` varchar(2) NOT NULL COMMENT ' 患者性别（男 / 女）',
  `patient_age` int DEFAULT NULL COMMENT '患者年龄',
  `inpatient_no` varchar(20) DEFAULT NULL COMMENT '住院号',
  `outpatient_no` varchar(20) DEFAULT NULL COMMENT '门诊号',
  `inpatient_date` date DEFAULT NULL COMMENT '住院日期',
  `bed_no` varchar(10) DEFAULT NULL COMMENT '床号',
  `inpatient_diagnosis` varchar(100) DEFAULT NULL COMMENT '住院诊断',
  `ward_id` int DEFAULT NULL COMMENT '病区ID',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系方式',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`patient_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='患者信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_ecg_patient_info`
--

LOCK TABLES `sys_ecg_patient_info` WRITE;
/*!40000 ALTER TABLE `sys_ecg_patient_info` DISABLE KEYS */;
INSERT INTO `sys_ecg_patient_info` VALUES (1,' 赵建国 ',' 男',65,'IP2026001',NULL,'2026-04-10','101-01',' 冠心病 ',1,NULL,'2026-04-15 18:14:16'),(2,' 钱玉兰 ',' 女',72,'IP2026002',NULL,'2026-04-11','101-02',' 心绞痛 ',1,NULL,'2026-04-15 18:14:16'),(3,' 孙大山 ',' 男',58,'IP2026003',NULL,'2026-04-10','101-03',' 高血压心脏病 ',1,NULL,'2026-04-15 18:14:16'),(4,' 李淑芬 ',' 女',81,'IP2026004',NULL,'2026-04-12','102-01',' 心力衰竭 ',2,NULL,'2026-04-15 18:14:16'),(5,' 周卫东 ',' 男',45,'IP2026005',NULL,'2026-04-13','102-02',' 心律失常 ',2,NULL,'2026-04-15 18:14:16'),(6,' 吴丽华 ',' 女',50,'IP2026006',NULL,'2026-04-14','102-03',' 冠心病 ',2,NULL,'2026-04-15 18:14:16'),(7,' 郑胜利 ',' 男',68,'IP2026007',NULL,'2026-04-11','103-01',' 心肌梗死恢复期 ',3,NULL,'2026-04-15 18:14:16'),(8,' 王秀英 ',' 女',77,'IP2026008',NULL,'2026-04-12','103-02',' 先天性心脏病 ',3,NULL,'2026-04-15 18:14:16'),(9,' 冯建平 ',' 男',61,'IP2026009',NULL,'2026-04-15','103-03',' 风湿性心脏病 ',3,NULL,'2026-04-15 18:14:16'),(10,' 陈晓燕 ',' 女',54,'IP2026010',NULL,'2026-04-14','104-01',' 原发性心肌病 ',4,NULL,'2026-04-15 18:14:16'),(11,' 刘海洋 ',' 男',62,'IP2026011',NULL,'2026-04-16','104-02',' 冠心病 ',4,NULL,'2026-04-15 18:14:16'),(12,' 陈秀琴 ',' 女',70,'IP2026012',NULL,'2026-04-16','104-03',' 高血压心脏病 ',4,NULL,'2026-04-15 18:14:16'),(13,' 杨志强 ',' 男',59,'IP2026013',NULL,'2026-04-17','105-01',' 心绞痛 ',5,NULL,'2026-04-15 18:14:16'),(14,' 张桂兰 ',' 女',75,'IP2026014',NULL,'2026-04-17','105-02',' 心力衰竭 ',5,NULL,'2026-04-15 18:14:16'),(15,' 黄伟 ',' 男',53,'IP2026015',NULL,'2026-04-18','105-03',' 心律失常 ',5,NULL,'2026-04-15 18:14:16'),(16,' 林小红 ',' 女',48,'IP2026016',NULL,'2026-04-18','106-01',' 冠心病 ',6,NULL,'2026-04-15 18:14:16'),(17,' 王大勇 ',' 男',66,'IP2026017',NULL,'2026-04-19','106-02',' 心肌梗死 ',6,NULL,'2026-04-15 18:14:16'),(18,' 刘玉兰 ',' 女',79,'IP2026018',NULL,'2026-04-19','106-03',' 先天性心脏病 ',6,NULL,'2026-04-15 18:14:16'),(19,' 陈明 ',' 男',60,'IP2026019',NULL,'2026-04-20','107-01',' 风湿性心脏病 ',7,NULL,'2026-04-15 18:14:16'),(20,' 赵雪 ',' 女',55,'IP2026020',NULL,'2026-04-20','110-03',' 原发性心肌病 ',10,NULL,'2026-04-15 18:14:16'),(21,' 郭建峰 ',' 男',64,'IP2026021',NULL,'2026-04-21','107-03',' 冠心病 ',7,NULL,'2026-04-15 18:14:16'),(22,' 孙丽 ',' 女',71,'IP2026022',NULL,'2026-04-21','108-01',' 高血压 ',8,NULL,'2026-04-15 18:14:16'),(23,' 马红军 ',' 男',57,'IP2026023',NULL,'2026-04-22','108-02',' 心绞痛 ',8,NULL,'2026-04-15 18:14:16'),(24,' 朱敏 ',' 女',73,'IP2026024',NULL,'2026-04-22','108-03',' 心力衰竭 ',8,NULL,'2026-04-15 18:14:16'),(25,' 董军 ',' 男',51,'IP2026025',NULL,'2026-04-23','109-02',' 心律失常 ',9,NULL,'2026-04-15 18:14:16'),(26,' 程琳 ',' 女',49,'IP2026026',NULL,'2026-04-23',NULL,' 冠心病 ',9,NULL,'2026-04-15 18:14:16'),(27,' 胡光明 ',' 男',69,'IP2026027',NULL,'2026-04-24','109-03',' 心肌梗死恢复期 ',9,NULL,'2026-04-15 18:14:16'),(28,' 曹静 ',' 女',76,'IP2026028',NULL,'2026-04-24','110-01',' 先天性心脏病 ',10,NULL,'2026-04-15 18:14:16'),(29,' 吕刚 ',' 男',63,'IP2026029',NULL,'2026-04-25','110-02',' 风湿性心脏病 ',10,NULL,'2026-04-15 18:14:16'),(30,' 任芳 ',' 女',56,'IP2026030',NULL,'2026-04-25',NULL,' 原发性心肌病 ',9,NULL,'2026-04-15 18:14:16');
/*!40000 ALTER TABLE `sys_ecg_patient_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_ecg_report`
--

DROP TABLE IF EXISTS `sys_ecg_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_ecg_report` (
  `report_id` int NOT NULL AUTO_INCREMENT COMMENT '报告ID，主键',
  `measure_id` int NOT NULL COMMENT '测量ID',
  `ai_analysis_id` int DEFAULT NULL COMMENT 'AI分析ID',
  `patient_id` int NOT NULL COMMENT '患者ID',
  `report_no` varchar(30) NOT NULL COMMENT '报告编号',
  `report_content` text COMMENT '报告内容',
  `diagnosis_result` varchar(255) DEFAULT NULL COMMENT '诊断结果',
  `create_doctor_id` int NOT NULL COMMENT '创建医生ID',
  `review_doctor_id` int DEFAULT NULL COMMENT '审核医生ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `review_time` datetime DEFAULT NULL COMMENT '审核时间',
  `report_status` varchar(20) DEFAULT '草稿' COMMENT '报告状态',
  `report_path` varchar(255) DEFAULT NULL COMMENT '报告PDF路径',
  PRIMARY KEY (`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='心电图报告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_ecg_report`
--

LOCK TABLES `sys_ecg_report` WRITE;
/*!40000 ALTER TABLE `sys_ecg_report` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_ecg_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_ecg_report_stat`
--

DROP TABLE IF EXISTS `sys_ecg_report_stat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_ecg_report_stat` (
  `report_stat_id` int NOT NULL AUTO_INCREMENT COMMENT '报告统计ID',
  `stat_time` datetime DEFAULT NULL COMMENT '统计时间',
  `ward_id` int DEFAULT NULL COMMENT '病区ID',
  `total_report` int DEFAULT '0' COMMENT '总报告数',
  `draft_report` int DEFAULT '0' COMMENT '草稿数',
  `pending_review_report` int DEFAULT '0' COMMENT '待审核数',
  `approved_report` int DEFAULT '0' COMMENT '已审核数',
  `archived_report` int DEFAULT '0' COMMENT '已归档数',
  `avg_review_time` decimal(10,2) DEFAULT '0.00' COMMENT '平均审核时长(分钟)',
  `stat_cycle` varchar(20) DEFAULT NULL COMMENT '统计周期',
  PRIMARY KEY (`report_stat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='心电图报告统计报表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_ecg_report_stat`
--

LOCK TABLES `sys_ecg_report_stat` WRITE;
/*!40000 ALTER TABLE `sys_ecg_report_stat` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_ecg_report_stat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_ecg_ward_info`
--

DROP TABLE IF EXISTS `sys_ecg_ward_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_ecg_ward_info` (
  `ward_id` int NOT NULL AUTO_INCREMENT COMMENT '病区ID，主键',
  `ward_name` varchar(50) NOT NULL COMMENT '病区名称',
  `ward_phone` varchar(20) DEFAULT NULL COMMENT '病区电话',
  `ward_desc` varchar(200) DEFAULT NULL COMMENT '病区描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`ward_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='病区表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_ecg_ward_info`
--

LOCK TABLES `sys_ecg_ward_info` WRITE;
/*!40000 ALTER TABLE `sys_ecg_ward_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_ecg_ward_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_ecg_warning_stat`
--

DROP TABLE IF EXISTS `sys_ecg_warning_stat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_ecg_warning_stat` (
  `warning_stat_id` int NOT NULL AUTO_INCREMENT COMMENT '预警统计ID',
  `stat_time` datetime DEFAULT NULL COMMENT '统计时间',
  `ward_id` int DEFAULT NULL COMMENT '病区ID',
  `warning_level` varchar(20) DEFAULT NULL COMMENT '预警级别',
  `warning_type` varchar(30) DEFAULT NULL COMMENT '预警类型',
  `total_warning` int DEFAULT '0' COMMENT '总预警次数',
  `handled_warning` int DEFAULT '0' COMMENT '已处理次数',
  `unhandled_warning` int DEFAULT '0' COMMENT '未处理次数',
  `avg_handle_time` decimal(10,2) DEFAULT '0.00' COMMENT '平均处理时长(分钟)',
  `stat_cycle` varchar(20) DEFAULT NULL COMMENT '统计周期',
  PRIMARY KEY (`warning_stat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='心电图异常预警统计报表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_ecg_warning_stat`
--

LOCK TABLES `sys_ecg_warning_stat` WRITE;
/*!40000 ALTER TABLE `sys_ecg_warning_stat` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_ecg_warning_stat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ward_info`
--

DROP TABLE IF EXISTS `ward_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ward_info` (
  `ward_id` bigint NOT NULL AUTO_INCREMENT COMMENT '病房ID',
  `department` varchar(50) NOT NULL COMMENT '科室',
  `ward_no` varchar(20) NOT NULL COMMENT '病房号',
  `nurse_name` varchar(20) NOT NULL COMMENT '责任护士',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `total_beds` int NOT NULL DEFAULT '0' COMMENT '总床位数量',
  PRIMARY KEY (`ward_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='病房信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ward_info`
--

LOCK TABLES `ward_info` WRITE;
/*!40000 ALTER TABLE `ward_info` DISABLE KEYS */;
INSERT INTO `ward_info` VALUES (1,' 心血管内科 ','101 房间 ',' 张晓红 ','2026-04-15 18:14:16','2026-04-15 18:24:27',6),(2,' 心血管内科 ','102 房间 ',' 李小明 ','2026-04-15 18:14:16','2026-04-15 18:24:27',6),(3,' 心血管内科 ','103 房间 ',' 王芳 ','2026-04-15 18:14:16','2026-04-15 18:24:27',6),(4,' 心血管内科 ','104 房间 ',' 赵强 ','2026-04-15 18:14:16','2026-04-15 18:24:27',6),(5,' 心血管内科 ','105 房间 ',' 孙丽 ','2026-04-15 18:14:16','2026-04-15 18:24:27',6),(6,' 心血管内科 ','106 房间 ',' 周敏 ','2026-04-15 18:14:16','2026-04-15 18:24:27',6),(7,' 心血管内科 ','107 房间 ',' 吴涛 ','2026-04-15 18:14:16','2026-04-15 18:24:27',6),(8,' 心血管内科 ','108 房间 ',' 郑华 ','2026-04-15 18:14:16','2026-04-15 18:24:27',6),(9,' 心血管内科 ','109 房间 ',' 冯静 ','2026-04-15 18:14:16','2026-04-15 18:24:27',6),(10,' 心血管内科 ','110 房间 ',' 陈亮 ','2026-04-15 18:14:16','2026-04-15 18:24:27',6);
/*!40000 ALTER TABLE `ward_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-15 18:35:48



