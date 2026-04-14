-- 创建数据库
CREATE DATABASE IF NOT EXISTS ecg_intelligence_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ecg_intelligence_platform;

-- ----------------------------
-- 一、基础表
-- ----------------------------

-- 1.患者信息表
DROP TABLE IF EXISTS sys_ecg_patient_info;
CREATE TABLE sys_ecg_patient_info (
  patient_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '患者ID，主键',
  patient_name VARCHAR(20) NOT NULL COMMENT '患者姓名',
  patient_gender CHAR(1) DEFAULT NULL COMMENT '患者性别 男/女',
  patient_age INT(3) DEFAULT NULL COMMENT '患者年龄',
  inpatient_no VARCHAR(20) DEFAULT NULL COMMENT '住院号',
  outpatient_no VARCHAR(20) DEFAULT NULL COMMENT '门诊号',
  inpatient_date DATE DEFAULT NULL COMMENT '住院日期',
  bed_no VARCHAR(10) DEFAULT NULL COMMENT '床号',
  inpatient_diagnosis VARCHAR(100) DEFAULT NULL COMMENT '住院诊断',
  ward_id INT(11) DEFAULT NULL COMMENT '病区ID',
  phone VARCHAR(20) DEFAULT NULL COMMENT '联系方式',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (patient_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='患者信息表';

-- 2.病区表
DROP TABLE IF EXISTS sys_ecg_ward_info;
CREATE TABLE sys_ecg_ward_info (
  ward_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '病区ID，主键',
  ward_name VARCHAR(50) NOT NULL COMMENT '病区名称',
  ward_phone VARCHAR(20) DEFAULT NULL COMMENT '病区电话',
  ward_desc VARCHAR(200) DEFAULT NULL COMMENT '病区描述',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (ward_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='病区表';

-- 3.医生信息表
DROP TABLE IF EXISTS sys_ecg_doctor_info;
CREATE TABLE sys_ecg_doctor_info (
  doctor_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '医生ID，主键',
  doctor_name VARCHAR(20) NOT NULL COMMENT '医生姓名',
  doctor_title VARCHAR(30) DEFAULT NULL COMMENT '医生职称',
  ward_id INT(11) DEFAULT NULL COMMENT '所属病区ID',
  user_name VARCHAR(20) NOT NULL COMMENT '登录账号',
  password VARCHAR(50) NOT NULL COMMENT '加密密码',
  is_active TINYINT(1) DEFAULT 1 COMMENT '是否启用 1启用 0禁用',
  PRIMARY KEY (doctor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生信息表';

-- ----------------------------
-- 二、业务表
-- ----------------------------

-- 1.心电图采集记录表
DROP TABLE IF EXISTS sys_ecg_measure_record;
CREATE TABLE sys_ecg_measure_record (
  measure_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '测量ID，主键',
  patient_id INT(11) NOT NULL COMMENT '患者ID',
  measure_type VARCHAR(30) DEFAULT NULL COMMENT '测量类型',
  equipment_id INT(11) DEFAULT NULL COMMENT '设备ID',
  start_time DATETIME DEFAULT NULL COMMENT '测量开始时间',
  end_time DATETIME DEFAULT NULL COMMENT '测量结束时间',
  total_duration VARCHAR(50) DEFAULT NULL COMMENT '测量总时长',
  wave_data_path VARCHAR(255) DEFAULT NULL COMMENT '波形数据路径',
  measure_point VARCHAR(30) DEFAULT NULL COMMENT '测量时间点',
  measurer VARCHAR(20) DEFAULT NULL COMMENT '测量者',
  ward_id INT(11) DEFAULT NULL COMMENT '病区ID',
  measure_status VARCHAR(20) DEFAULT NULL COMMENT '测量状态',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (measure_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心电图采集记录表';

-- 2.心电图核心指标表
DROP TABLE IF EXISTS sys_ecg_core_index;
CREATE TABLE sys_ecg_core_index (
  index_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '指标ID，主键',
  measure_id INT(11) NOT NULL COMMENT '测量ID',
  heartbeat_total INT(11) DEFAULT NULL COMMENT '心搏总数',
  avg_heart_rate DECIMAL(5,1) DEFAULT NULL COMMENT '平均心率',
  min_heart_rate INT(3) DEFAULT NULL COMMENT '最慢心率',
  max_heart_rate INT(3) DEFAULT NULL COMMENT '最快心率',
  max_rr_interval DECIMAL(5,3) DEFAULT NULL COMMENT '最长RR间期',
  max_rr_count INT(11) DEFAULT NULL COMMENT '≥2.0s长RR间期次数',
  tachycardia_times INT(11) DEFAULT NULL COMMENT '心动过速次数',
  tachycardia_heartbeat INT(11) DEFAULT NULL COMMENT '心动过速心搏数',
  bradycardia_times INT(11) DEFAULT NULL COMMENT '心动过缓次数',
  atrial_premature INT(11) DEFAULT NULL COMMENT '房早总数',
  atrial_premature_single INT(11) DEFAULT NULL COMMENT '单发房早',
  atrial_premature_pair INT(11) DEFAULT NULL COMMENT '成对房早',
  ventricular_premature INT(11) DEFAULT NULL COMMENT '室早总数',
  ventricular_premature_single INT(11) DEFAULT NULL COMMENT '单发室早',
  ventricular_premature_pair INT(11) DEFAULT NULL COMMENT '成对室早',
  hrv_sdnn DECIMAL(5,1) DEFAULT NULL COMMENT 'SDNN',
  hrv_sdann DECIMAL(5,1) DEFAULT NULL COMMENT 'SDANN',
  hrv_rmssd DECIMAL(5,1) DEFAULT NULL COMMENT 'rMSSD',
  hrv_pnn50 DECIMAL(5,2) DEFAULT NULL COMMENT 'pNN50',
  hrv_lf DECIMAL(5,1) DEFAULT NULL COMMENT 'LF',
  hrv_hf DECIMAL(5,1) DEFAULT NULL COMMENT 'HF',
  hrv_lf_hf DECIMAL(5,2) DEFAULT NULL COMMENT 'LF/HF',
  ventricular_tachycardia INT(11) DEFAULT NULL COMMENT '室速阵数',
  atrial_tachycardia INT(11) DEFAULT NULL COMMENT '房速阵数',
  rhythm_type VARCHAR(30) DEFAULT NULL COMMENT '节律类型',
  PRIMARY KEY (index_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心电图核心指标表';

-- 3.心电图AI分析表
DROP TABLE IF EXISTS sys_ecg_ai_analysis;
CREATE TABLE sys_ecg_ai_analysis (
  ai_analysis_id INT(11) NOT NULL AUTO_INCREMENT COMMENT 'AI分析ID，主键',
  measure_id INT(11) NOT NULL COMMENT '测量ID',
  model_version VARCHAR(50) DEFAULT NULL COMMENT '模型版本',
  abnormal_fragment VARCHAR(255) DEFAULT NULL COMMENT '异常片段',
  ai_diagnosis TEXT DEFAULT NULL COMMENT 'AI初步诊断',
  abnormal_index VARCHAR(255) DEFAULT NULL COMMENT '异常指标汇总',
  confidence DECIMAL(5,2) DEFAULT NULL COMMENT '置信度%',
  analysis_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '分析完成时间',
  is_verified TINYINT(1) DEFAULT 0 COMMENT '是否医生验证',
  PRIMARY KEY (ai_analysis_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心电图AI分析表';

-- 4.心电图异常预警表
DROP TABLE IF EXISTS sys_ecg_abnormal_warning;
CREATE TABLE sys_ecg_abnormal_warning (
  warning_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '预警ID，主键',
  measure_id INT(11) NOT NULL COMMENT '测量ID',
  patient_id INT(11) NOT NULL COMMENT '患者ID',
  warning_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '预警时间',
  warning_type VARCHAR(30) DEFAULT NULL COMMENT '预警类型',
  warning_level VARCHAR(20) DEFAULT NULL COMMENT '预警级别',
  warning_content TEXT DEFAULT NULL COMMENT '预警详情',
  handle_doctor_id INT(11) DEFAULT NULL COMMENT '处理医生ID',
  handle_time DATETIME DEFAULT NULL COMMENT '处理时间',
  handle_result TEXT DEFAULT NULL COMMENT '处理结果',
  warning_status VARCHAR(20) DEFAULT '未处理' COMMENT '预警状态',
  PRIMARY KEY (warning_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心电图异常预警表';

-- 5.心电图报告表
DROP TABLE IF EXISTS sys_ecg_report;
CREATE TABLE sys_ecg_report (
  report_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '报告ID，主键',
  measure_id INT(11) NOT NULL COMMENT '测量ID',
  ai_analysis_id INT(11) DEFAULT NULL COMMENT 'AI分析ID',
  patient_id INT(11) NOT NULL COMMENT '患者ID',
  report_no VARCHAR(30) NOT NULL COMMENT '报告编号',
  report_content TEXT DEFAULT NULL COMMENT '报告内容',
  diagnosis_result VARCHAR(255) DEFAULT NULL COMMENT '诊断结果',
  create_doctor_id INT(11) NOT NULL COMMENT '创建医生ID',
  review_doctor_id INT(11) DEFAULT NULL COMMENT '审核医生ID',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  review_time DATETIME DEFAULT NULL COMMENT '审核时间',
  report_status VARCHAR(20) DEFAULT '草稿' COMMENT '报告状态',
  report_path VARCHAR(255) DEFAULT NULL COMMENT '报告PDF路径',
  PRIMARY KEY (report_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心电图报告表';

-- 6.心电图随访记录表
DROP TABLE IF EXISTS sys_ecg_follow_up;
CREATE TABLE sys_ecg_follow_up (
  follow_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '随访ID，主键',
  patient_id INT(11) NOT NULL COMMENT '患者ID',
  measure_id INT(11) DEFAULT NULL COMMENT '测量ID',
  follow_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '随访时间',
  follow_type VARCHAR(30) DEFAULT NULL COMMENT '随访方式',
  follow_doctor_id INT(11) NOT NULL COMMENT '随访医生ID',
  patient_condition TEXT DEFAULT NULL COMMENT '患者病情',
  follow_advice TEXT DEFAULT NULL COMMENT '随访建议',
  next_follow_time DATETIME DEFAULT NULL COMMENT '下次随访时间',
  follow_remark TEXT DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (follow_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心电图随访记录表';

-- 7.设备信息表
DROP TABLE IF EXISTS sys_ecg_equipment_info;
CREATE TABLE sys_ecg_equipment_info (
  equipment_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '设备ID，主键',
  equipment_code VARCHAR(30) NOT NULL COMMENT '设备编号',
  equipment_name VARCHAR(50) DEFAULT NULL COMMENT '设备名称',
  equipment_type VARCHAR(30) DEFAULT NULL COMMENT '设备类型',
  manufacturer VARCHAR(50) DEFAULT NULL COMMENT '厂商',
  equipment_version VARCHAR(30) DEFAULT NULL COMMENT '固件版本',
  ward_id INT(11) DEFAULT NULL COMMENT '所属病区ID',
  equipment_status VARCHAR(20) DEFAULT '正常' COMMENT '设备状态',
  install_time DATETIME DEFAULT NULL COMMENT '安装时间',
  maintain_time DATETIME DEFAULT NULL COMMENT '上次维护时间',
  next_maintain_time DATETIME DEFAULT NULL COMMENT '下次维护时间',
  PRIMARY KEY (equipment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备信息表';

-- ----------------------------
-- 三、统计报表
-- ----------------------------

-- 1.心电测量统计报表
DROP TABLE IF EXISTS sys_ecg_measure_stat;
CREATE TABLE sys_ecg_measure_stat (
  stat_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  stat_time DATETIME DEFAULT NULL COMMENT '统计时间',
  ward_id INT(11) DEFAULT NULL COMMENT '病区ID',
  equipment_type VARCHAR(30) DEFAULT NULL COMMENT '设备类型',
  total_measure INT(11) DEFAULT 0 COMMENT '总测量次数',
  normal_measure INT(11) DEFAULT 0 COMMENT '正常次数',
  abnormal_measure INT(11) DEFAULT 0 COMMENT '异常次数',
  stat_cycle VARCHAR(20) DEFAULT NULL COMMENT '统计周期',
  PRIMARY KEY (stat_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心电测量统计报表';

-- 2.心电图异常预警统计报表
DROP TABLE IF EXISTS sys_ecg_warning_stat;
CREATE TABLE sys_ecg_warning_stat (
  warning_stat_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '预警统计ID',
  stat_time DATETIME DEFAULT NULL COMMENT '统计时间',
  ward_id INT(11) DEFAULT NULL COMMENT '病区ID',
  warning_level VARCHAR(20) DEFAULT NULL COMMENT '预警级别',
  warning_type VARCHAR(30) DEFAULT NULL COMMENT '预警类型',
  total_warning INT(11) DEFAULT 0 COMMENT '总预警次数',
  handled_warning INT(11) DEFAULT 0 COMMENT '已处理次数',
  unhandled_warning INT(11) DEFAULT 0 COMMENT '未处理次数',
  avg_handle_time DECIMAL(10,2) DEFAULT 0.00 COMMENT '平均处理时长(分钟)',
  stat_cycle VARCHAR(20) DEFAULT NULL COMMENT '统计周期',
  PRIMARY KEY (warning_stat_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心电图异常预警统计报表';

-- 3.心电图报告统计报表
DROP TABLE IF EXISTS sys_ecg_report_stat;
CREATE TABLE sys_ecg_report_stat (
  report_stat_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '报告统计ID',
  stat_time DATETIME DEFAULT NULL COMMENT '统计时间',
  ward_id INT(11) DEFAULT NULL COMMENT '病区ID',
  total_report INT(11) DEFAULT 0 COMMENT '总报告数',
  draft_report INT(11) DEFAULT 0 COMMENT '草稿数',
  pending_review_report INT(11) DEFAULT 0 COMMENT '待审核数',
  approved_report INT(11) DEFAULT 0 COMMENT '已审核数',
  archived_report INT(11) DEFAULT 0 COMMENT '已归档数',
  avg_review_time DECIMAL(10,2) DEFAULT 0.00 COMMENT '平均审核时长(分钟)',
  stat_cycle VARCHAR(20) DEFAULT NULL COMMENT '统计周期',
  PRIMARY KEY (report_stat_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心电图报告统计报表';

-- ----------------------------
-- 四、字典表
-- ----------------------------

-- 1.预警级别字典表
DROP TABLE IF EXISTS sys_dict_warning_level;
CREATE TABLE sys_dict_warning_level (
  dict_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  level_code VARCHAR(20) NOT NULL COMMENT '级别编码',
  level_name VARCHAR(20) NOT NULL COMMENT '级别名称',
  level_desc VARCHAR(255) DEFAULT NULL COMMENT '级别描述',
  sort INT(11) DEFAULT 0 COMMENT '排序号',
  PRIMARY KEY (dict_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警级别字典表';

-- 2.预警类型字典表
DROP TABLE IF EXISTS sys_dict_warning_type;
CREATE TABLE sys_dict_warning_type (
  dict_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  type_code VARCHAR(30) NOT NULL COMMENT '类型编码',
  type_name VARCHAR(30) NOT NULL COMMENT '类型名称',
  type_desc VARCHAR(255) DEFAULT NULL COMMENT '类型描述',
  sort INT(11) DEFAULT 0 COMMENT '排序号',
  PRIMARY KEY (dict_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警类型字典表';

-- 3.设备类型字典表
DROP TABLE IF EXISTS sys_dict_equipment_type;
CREATE TABLE sys_dict_equipment_type (
  dict_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  type_code VARCHAR(30) NOT NULL COMMENT '类型编码',
  type_name VARCHAR(30) NOT NULL COMMENT '类型名称',
  type_desc VARCHAR(255) DEFAULT NULL COMMENT '类型描述',
  sort INT(11) DEFAULT 0 COMMENT '排序号',
  PRIMARY KEY (dict_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备类型字典表';

-- ==============================================================================================
-- 以下开始：每表 20 条真实业务假数据，无外键，直接执行，100% 成功
-- ==============================================================================================

-- 1. 病区表 20条
INSERT INTO sys_ecg_ward_info (ward_name, ward_phone, ward_desc) VALUES
('心内科一病区','010-10001','心脏内科普通病房'),('心内科二病区','010-10002','心脏重症病房'),
('心内科三病区','010-10003','心律失常病房'),('老年病科','010-10004','老年心血管病房'),
('神经内科','010-10005','神经科病房'),('呼吸内科','010-10006','呼吸科病房'),
('肾内科','010-10007','肾病科病房'),('内分泌科','010-10008','糖尿病科病房'),
('普外科','010-10009','外科普通病房'),('骨科','010-10010','骨科病房'),
('急诊科','010-10011','急诊观察病房'),('ICU','010-10012','重症监护室'),
('CCU','010-10013','冠心病监护室'),('儿科','010-10014','儿科病房'),
('妇产科','010-10015','妇产科病房'),('肿瘤科','010-10016','肿瘤病房'),
('康复科','010-10017','康复病房'),('疼痛科','010-10018','疼痛科病房'),
('血液科','010-10019','血液科病房'),('感染科','010-10020','感染科病房');

-- 2. 医生信息表 20条
INSERT INTO sys_ecg_doctor_info (doctor_name, doctor_title, ward_id, user_name, password) VALUES
('张医生','主任医师',1,'doc001','123456'),('王医生','副主任医师',2,'doc002','123456'),
('李医生','主治医师',3,'doc003','123456'),('赵医生','住院医师',4,'doc004','123456'),
('刘医生','主任医师',5,'doc005','123456'),('陈医生','副主任医师',6,'doc006','123456'),
('杨医生','主治医师',7,'doc007','123456'),('黄医生','住院医师',8,'doc008','123456'),
('吴医生','主任医师',9,'doc009','123456'),('周医生','副主任医师',10,'doc010','123456'),
('徐医生','主治医师',11,'doc011','123456'),('孙医生','住院医师',12,'doc012','123456'),
('马医生','主任医师',13,'doc013','123456'),('朱医生','副主任医师',14,'doc014','123456'),
('胡医生','主治医师',15,'doc015','123456'),('林医生','住院医师',16,'doc016','123456'),
('郭医生','主任医师',17,'doc017','123456'),('何医生','副主任医师',18,'doc018','123456'),
('高医生','主治医师',19,'doc019','123456'),('罗医生','住院医师',20,'doc020','123456');

-- 3. 设备信息表 20条
INSERT INTO sys_ecg_equipment_info (equipment_code, equipment_name, equipment_type, manufacturer, ward_id) VALUES
('ECG001','动态心电分析仪','动态心电','迈瑞',1),('ECG002','床旁心电监测仪','床旁心电','理邦',2),
('ECG003','心电工作站','台式心电','飞利浦',3),('ECG004','便携式心电仪','便携心电','科曼',4),
('ECG005','中央监护系统','中央监护','迈瑞',5),('ECG006','心电记录仪','记录型','理邦',6),
('ECG007','holter记录仪','动态心电','迈瑞',7),('ECG008','重症心电仪','重症专用','飞利浦',8),
('ECG009','急诊心电仪','急诊专用','科曼',9),('ECG010','老年心电仪','老年专用','理邦',10),
('ECG011','儿科心电仪','儿科专用','迈瑞',11),('ECG012','产科心电仪','产科专用','理邦',12),
('ECG013','康复心电仪','康复专用','科曼',13),('ECG014','肿瘤心电仪','肿瘤专用','迈瑞',14),
('ECG015','神经科心电仪','神经专用','理邦',15),('ECG016','呼吸科心电仪','呼吸专用','科曼',16),
('ECG017','肾内科心电仪','肾病专用','迈瑞',17),('ECG018','内分泌心电仪','糖尿病专用','理邦',18),
('ECG019','骨科心电仪','骨科专用','科曼',19),('ECG020','普外科心电仪','外科专用','迈瑞',20);

-- 4. 患者信息表 20条
INSERT INTO sys_ecg_patient_info (patient_name, patient_gender, patient_age, inpatient_no, bed_no, ward_id, inpatient_diagnosis) VALUES
('患者01','男',55,'Z2025001','101',1,'冠心病'),('患者02','女',62,'Z2025002','102',1,'高血压'),
('患者03','男',68,'Z2025003','201',2,'心律失常'),('患者04','女',70,'Z2025004','202',2,'心衰'),
('患者05','男',52,'Z2025005','301',3,'房颤'),('患者06','女',65,'Z2025006','302',3,'心肌缺血'),
('患者07','男',72,'Z2025007','401',4,'老年心脏病'),('患者08','女',58,'Z2025008','402',4,'心绞痛'),
('患者09','男',60,'Z2025009','501',5,'脑梗伴心电异常'),('患者10','女',66,'Z2025010','502',5,'肺炎伴心悸'),
('患者11','男',59,'Z2025011','601',6,'慢阻肺'),('患者12','女',75,'Z2025012','602',6,'肾衰'),
('患者13','男',54,'Z2025013','701',7,'糖尿病心肌病'),('患者14','女',63,'Z2025014','702',7,'胆结石'),
('患者15','男',61,'Z2025015','801',8,'骨折'),('患者16','女',57,'Z2025016','802',8,'急诊胸痛'),
('患者17','男',69,'Z2025017','901',9,'ICU重症'),('患者18','女',73,'Z2025018','902',9,'冠心病监护'),
('患者19','男',51,'Z2025019','1001',10,'儿科心电异常'),('患者20','女',56,'Z2025020','1002',10,'产后心电监测');

-- 5. 心电图采集记录表 20条
INSERT INTO sys_ecg_measure_record (patient_id, measure_type, equipment_id, measurer, ward_id, measure_status) VALUES
(1,'动态心电',1,'张医生',1,'已完成'),(2,'床旁心电',2,'王医生',1,'已完成'),
(3,'动态心电',3,'李医生',2,'已完成'),(4,'床旁心电',4,'赵医生',2,'已完成'),
(5,'动态心电',5,'刘医生',3,'已完成'),(6,'床旁心电',6,'陈医生',3,'已完成'),
(7,'动态心电',7,'杨医生',4,'已完成'),(8,'床旁心电',8,'黄医生',4,'已完成'),
(9,'动态心电',9,'吴医生',5,'已完成'),(10,'床旁心电',10,'周医生',5,'已完成'),
(11,'动态心电',11,'徐医生',6,'已完成'),(12,'床旁心电',12,'孙医生',6,'已完成'),
(13,'动态心电',13,'马医生',7,'已完成'),(14,'床旁心电',14,'朱医生',7,'已完成'),
(15,'动态心电',15,'胡医生',8,'已完成'),(16,'床旁心电',16,'林医生',8,'已完成'),
(17,'动态心电',17,'郭医生',9,'已完成'),(18,'床旁心电',18,'何医生',9,'已完成'),
(19,'动态心电',19,'高医生',10,'已完成'),(20,'床旁心电',20,'罗医生',10,'已完成');

-- 6. 心电图核心指标表 20条
INSERT INTO sys_ecg_core_index (measure_id, heartbeat_total, avg_heart_rate, min_heart_rate, max_heart_rate, hrv_sdnn, rhythm_type) VALUES
(1,860,72.3,54,96,124.5,'窦性心律'),(2,830,69.1,51,91,113.2,'窦性心律'),
(3,910,79.5,61,106,138.7,'窦性心律'),(4,800,65.4,49,88,106.8,'窦性心律'),
(5,940,82.7,63,110,145.3,'房颤心律'),(6,810,66.8,50,89,109.1,'窦性心律'),
(7,880,75.1,57,99,129.6,'窦性心律'),(8,790,64.2,48,87,104.5,'窦性心律'),
(9,920,80.3,62,107,140.2,'窦性心律'),(10,820,67.9,52,90,111.7,'窦性心律'),
(11,870,73.8,55,97,126.9,'窦性心律'),(12,780,63.5,47,86,102.3,'窦性心律'),
(13,900,77.6,59,103,134.1,'窦性心律'),(14,805,65.9,49,88,107.4,'窦性心律'),
(15,890,76.2,58,102,131.5,'窦性心律'),(16,815,66.3,50,89,108.6,'窦性心律'),
(17,930,81.4,62,108,142.8,'窦性心律'),(18,795,64.8,48,87,105.2,'窦性心律'),
(19,850,71.5,54,95,122.1,'窦性心律'),(20,840,70.2,53,94,120.4,'窦性心律');

-- 7. 心电图AI分析表 20条
INSERT INTO sys_ecg_ai_analysis (measure_id, model_version, ai_diagnosis, confidence) VALUES
(1,'V2.0','窦性心律，大致正常',95.80),(2,'V2.0','窦性心律，偶发房早',92.30),
(3,'V2.0','窦性心律，心动过速',88.50),(4,'V2.0','窦性心律，心动过缓',90.20),
(5,'V2.0','心房颤动，快心室率',97.10),(6,'V2.0','窦性心律，ST段改变',85.40),
(7,'V2.0','窦性心律，大致正常',96.70),(8,'V2.0','窦性心律，偶发室早',91.60),
(9,'V2.0','窦性心律，心动过速',87.90),(10,'V2.0','窦性心律，大致正常',94.20),
(11,'V2.0','窦性心律，偶发房早',93.50),(12,'V2.0','窦性心律，心动过缓',89.80),
(13,'V2.0','窦性心律，ST段改变',86.10),(14,'V2.0','窦性心律，大致正常',95.30),
(15,'V2.0','窦性心律，偶发室早',92.80),(16,'V2.0','窦性心律，心动过速',88.20),
(17,'V2.0','心房颤动，慢心室率',96.40),(18,'V2.0','窦性心律，大致正常',97.50),
(19,'V2.0','窦性心律，偶发房早',94.10),(20,'V2.0','窦性心律，大致正常',93.70);

-- 8. 心电图异常预警表 20条
INSERT INTO sys_ecg_abnormal_warning (measure_id, patient_id, warning_type, warning_level, warning_content) VALUES
(1,1,'房早','轻度','偶发房早，无需处理'),(2,2,'室早','轻度','偶发室早，建议观察'),
(3,3,'心动过速','中度','心率过快，持续监测'),(4,4,'心动过缓','中度','心率过慢，关注'),
(5,5,'房颤','重度','心房颤动，紧急处理'),(6,6,'ST改变','中度','心肌缺血可能'),
(7,7,'心律正常','正常','无异常'),(8,8,'室早','轻度','偶发室早'),
(9,9,'心动过速','中度','心率超标'),(10,10,'心律正常','正常','无异常'),
(11,11,'房早','轻度','偶发房早'),(12,12,'心动过缓','中度','心率偏低'),
(13,13,'ST改变','中度','缺血提示'),(14,14,'心律正常','正常','无异常'),
(15,15,'室早','轻度','单发室早'),(16,16,'心动过速','中度','心率偏快'),
(17,17,'房颤','重度','房颤发作'),(18,18,'心律正常','正常','无异常'),
(19,19,'房早','轻度','偶发房早'),(20,20,'心律正常','正常','无异常');

-- 9. 心电图报告表 20条
INSERT INTO sys_ecg_report (measure_id, ai_analysis_id, patient_id, report_no, diagnosis_result, create_doctor_id, report_status) VALUES
(1,1,1,'R2025001','窦性心律，正常心电图',1,'已审核'),(2,2,2,'R2025002','偶发房早，无临床意义',2,'已审核'),
(3,3,3,'R2025003','窦性心动过速',3,'已审核'),(4,4,4,'R2025004','窦性心动过缓',4,'已审核'),
(5,5,5,'R2025005','心房颤动',5,'已审核'),(6,6,6,'R2025006','ST-T改变，心肌缺血',6,'已审核'),
(7,7,7,'R2025007','正常心电图',7,'已审核'),(8,8,8,'R2025008','偶发室早',8,'已审核'),
(9,9,9,'R2025009','窦性心动过速',9,'已审核'),(10,10,10,'R2025010','正常心电图',10,'已审核'),
(11,11,11,'R2025011','偶发房早',11,'已审核'),(12,12,12,'R2025012','窦性心动过缓',12,'已审核'),
(13,13,13,'R2025013','心肌缺血样改变',13,'已审核'),(14,14,14,'R2025014','正常心电图',14,'已审核'),
(15,15,15,'R2025015','偶发室早',15,'已审核'),(16,16,16,'R2025016','窦性心动过速',16,'已审核'),
(17,17,17,'R2025017','心房颤动',17,'已审核'),(18,18,18,'R2025018','正常心电图',18,'已审核'),
(19,19,19,'R2025019','偶发房早',19,'已审核'),(20,20,20,'R2025020','正常心电图',20,'已审核');

-- 10. 心电图随访记录表 20条
INSERT INTO sys_ecg_follow_up (patient_id, measure_id, follow_type, follow_doctor_id, patient_condition) VALUES
(1,1,'电话随访',1,'病情稳定'),(2,2,'门诊随访',2,'症状减轻'),
(3,3,'电话随访',3,'心率控制良好'),(4,4,'门诊随访',4,'病情稳定'),
(5,5,'住院随访',5,'房颤转复'),(6,6,'电话随访',6,'缺血改善'),
(7,7,'门诊随访',7,'正常'),(8,8,'电话随访',8,'偶有早搏'),
(9,9,'门诊随访',9,'心率下降'),(10,10,'电话随访',10,'正常'),
(11,11,'门诊随访',11,'早搏减少'),(12,12,'电话随访',12,'心率提升'),
(13,13,'门诊随访',13,'缺血好转'),(14,14,'电话随访',14,'正常'),
(15,15,'门诊随访',15,'早搏消失'),(16,16,'电话随访',16,'心率正常'),
(17,17,'住院随访',17,'房颤控制'),(18,18,'电话随访',18,'正常'),
(19,19,'门诊随访',19,'早搏减少'),(20,20,'电话随访',20,'正常');

-- 11. 心电测量统计报表 20条
INSERT INTO sys_ecg_measure_stat (ward_id, total_measure, normal_measure, abnormal_measure, stat_cycle) VALUES
(1,120,100,20,'日统计'),(2,110,85,25,'日统计'),
(3,100,75,25,'日统计'),(4,90,70,20,'日统计'),
(5,85,65,20,'日统计'),(6,80,60,20,'日统计'),
(7,75,55,20,'日统计'),(8,70,50,20,'日统计'),
(9,65,45,20,'日统计'),(10,60,40,20,'日统计'),
(11,55,38,17,'周统计'),(12,50,35,15,'周统计'),
(13,48,33,15,'周统计'),(14,45,30,15,'周统计'),
(15,42,28,14,'周统计'),(16,40,25,15,'周统计'),
(17,38,23,15,'周统计'),(18,35,20,15,'周统计'),
(19,32,18,14,'周统计'),(20,30,15,15,'周统计');

-- 12. 心电图异常预警统计报表 20条
INSERT INTO sys_ecg_warning_stat (ward_id, warning_level, total_warning, handled_warning, unhandled_warning) VALUES
(1,'轻度',20,18,2),(2,'轻度',18,15,3),
(3,'中度',15,12,3),(4,'中度',12,10,2),
(5,'重度',5,5,0),(6,'轻度',16,14,2),
(7,'轻度',14,12,2),(8,'中度',10,8,2),
(9,'中度',8,7,1),(10,'轻度',19,17,2),
(11,'轻度',17,15,2),(12,'中度',13,11,2),
(13,'重度',4,4,0),(14,'轻度',15,13,2),
(15,'轻度',13,11,2),(16,'中度',9,7,2),
(17,'重度',3,3,0),(18,'轻度',11,9,2),
(19,'轻度',9,7,2),(20,'中度',7,6,1);

-- 13. 心电图报告统计报表 20条
INSERT INTO sys_ecg_report_stat (ward_id, total_report, approved_report, avg_review_time) VALUES
(1,50,45,8.5),(2,45,40,9.2),
(3,40,35,10.1),(4,38,33,11.3),
(5,35,30,12.5),(6,32,28,13.8),
(7,30,25,14.2),(8,28,23,15.1),
(9,25,20,16.3),(10,22,18,17.5),
(11,20,16,18.1),(12,18,14,19.4),
(13,15,12,20.5),(14,13,10,21.7),
(15,10,8,22.3),(16,8,6,23.8),
(17,5,4,24.1),(18,4,3,25.2),
(19,3,2,26.5),(20,2,1,27.8);

-- 14. 预警级别字典表 20条
INSERT INTO sys_dict_warning_level (level_code, level_name, level_desc) VALUES
('L0','正常','无异常'),('L1','轻度','轻微异常，无需处理'),
('L2','中度','需要观察监测'),('L3','重度','需要及时处理'),
('L4','紧急','立即处理'),('L5','提示','常规提示'),
('L6','关注','需要关注'),('L7','一般','一般异常'),
('L8','重要','重要异常'),('L9','危急','危及生命'),
('LA','一级','一级预警'),('LB','二级','二级预警'),
('LC','三级','三级预警'),('LD','四级','四级预警'),
('LE','五级','五级预警'),('LF','安全','安全状态'),
('LG','监测','持续监测'),('LH','随访','需要随访'),
('LI','复查','需要复查'),('LJ','康复','康复阶段');

-- 15. 预警类型字典表 20条
INSERT INTO sys_dict_warning_type (type_code, type_name) VALUES
('T01','房性早搏'),('T02','室性早搏'),
('T03','心动过速'),('T04','心动过缓'),
('T05','心房颤动'),('T06','心室颤动'),
('T07','ST段改变'),('T08','T波异常'),
('T09','QT间期延长'),('T10','房室传导阻滞'),
('T11','窦性停搏'),('T12','室上速'),
('T13','室速'),('T14','早搏二联律'),
('T15','早搏三联律'),('T16','心率不齐'),
('T17','心肌缺血'),('T18','心肌梗死'),
('T19','心率超标'),('T20','设备异常');

-- 16. 设备类型字典表 20条
INSERT INTO sys_dict_equipment_type (type_code, type_name) VALUES
('E01','动态心电'),('E02','床旁心电'),
('E03','台式心电'),('E04','便携式心电'),
('E05','中央监护'),('E06','记录型心电'),
('E07','holter'),('E08','重症专用'),
('E09','急诊专用'),('E10','老年专用'),
('E11','儿科专用'),('E12','产科专用'),
('E13','康复专用'),('E14','肿瘤专用'),
('E15','神经专用'),('E16','呼吸专用'),
('E17','肾病专用'),('E18','糖尿病专用'),
('E19','骨科专用'),('E20','外科专用');