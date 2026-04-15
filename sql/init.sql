-- ============================================================================
-- 患者、医生、测量及核心业务部分 (参考 reset_mock_data.sql)
-- ============================================================================

-- 患者信息表
CREATE TABLE `sys_ecg_patient_info` (
  `patient_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '患者ID，主键',
  `patient_name` VARCHAR(20) NOT NULL COMMENT '患者姓名',
  `patient_gender` VARCHAR(2) NOT NULL COMMENT ' 患者性别（男 / 女）',
  `patient_age` INT NULL COMMENT '患者年龄',
  `inpatient_no` VARCHAR(20) NULL COMMENT '住院号',
  `outpatient_no` VARCHAR(20) NULL COMMENT '门诊号',
  `inpatient_date` DATE NULL COMMENT '住院日期',
  `bed_no` VARCHAR(10) NULL COMMENT '床号',
  `inpatient_diagnosis` VARCHAR(100) NULL COMMENT '入院诊断',
  `discharge_diagnosis` VARCHAR(100) NULL COMMENT '出院诊断',
  `ward_id` INT NULL COMMENT '病区ID',
  `phone` VARCHAR(20) NULL COMMENT '联系方式',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1=在院 0=出院',
  `discharge_time` DATETIME NULL COMMENT '出院时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='患者信息表';

-- 医生信息表
CREATE TABLE `sys_ecg_doctor_info` (
  `doctor_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '医生ID，主键',
  `doctor_name` VARCHAR(20) NOT NULL COMMENT '医生姓名',
  `doctor_title` VARCHAR(30) NULL COMMENT '医生职称',
  `ward_id` INT NULL COMMENT '所属病区ID',
  `user_name` VARCHAR(20) NOT NULL COMMENT '登录账号',
  `password` VARCHAR(50) NOT NULL COMMENT '加密密码',
  `is_active` TINYINT(1) DEFAULT 1 NULL COMMENT '是否启用 1启用 0禁用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生信息表';

-- 设备信息表
CREATE TABLE `sys_ecg_equipment_info` (
  `equipment_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '设备ID，主键',
  `equipment_code` VARCHAR(30) NOT NULL COMMENT '设备编号',
  `equipment_name` VARCHAR(50) NULL COMMENT '设备名称',
  `equipment_type` VARCHAR(30) NULL COMMENT '设备类型',
  `manufacturer` VARCHAR(50) NULL COMMENT '厂商',
  `equipment_version` VARCHAR(30) NULL COMMENT '固件版本',
  `ward_id` INT NULL COMMENT '所属病区ID',
  `equipment_status` VARCHAR(20) DEFAULT '正常' NULL COMMENT '设备状态',
  `install_time` DATETIME NULL COMMENT '安装时间',
  `maintain_time` DATETIME NULL COMMENT '上次维护时间',
  `next_maintain_time` DATETIME NULL COMMENT '下次维护时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备信息表';



-- ============================================================================
-- 病房及床位部分 (参考 reset_mock_data.sql)
-- ============================================================================

CREATE TABLE `ward_info` (
  `ward_id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '病房ID',
  `department` VARCHAR(50) NOT NULL COMMENT '科室',
  `ward_no` VARCHAR(20) NOT NULL COMMENT '病房号',
  `nurse_name` VARCHAR(20) NOT NULL COMMENT '责任护士',
  `total_beds` INT DEFAULT 0 NOT NULL COMMENT '总床位数量',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP NULL,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='病房信息表';

CREATE TABLE `bed_info` (
  `bed_id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '床位ID',
  `ward_id` BIGINT NOT NULL COMMENT '关联病房ID',
  `bed_no` VARCHAR(10) NOT NULL COMMENT '床位编号',
  `status` TINYINT DEFAULT 0 NOT NULL COMMENT '床位状态：0-空床，1-已占用',
  `patient_id` BIGINT NULL COMMENT '关联患者ID（空床为NULL）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP NULL,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `uk_ward_bed` UNIQUE (`ward_id`, `bed_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='床位明细表';

CREATE TABLE `sys_ecg_ward_info` (
  `ward_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '病区ID，主键',
  `ward_name` VARCHAR(50) NOT NULL COMMENT '病区名称',
  `ward_phone` VARCHAR(20) NULL COMMENT '病区电话',
  `ward_desc` VARCHAR(200) NULL COMMENT '病区描述',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='病区表';



-- ============================================================================
-- 分析与预警部分
-- ============================================================================

-- 测量记录表
CREATE TABLE `sys_ecg_measure_record` (
  `measure_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '测量ID，主键',
  `patient_id` INT NOT NULL COMMENT '患者ID',
  `measure_type` VARCHAR(30) NULL COMMENT '测量类型',
  `equipment_id` INT NULL COMMENT '设备ID',
  `start_time` DATETIME NULL COMMENT '测量开始时间',
  `end_time` DATETIME NULL COMMENT '测量结束时间',
  `total_duration` VARCHAR(50) NULL COMMENT '测量总时长',
  `wave_data_path` VARCHAR(255) NULL COMMENT '波形数据路径',
  `measure_point` VARCHAR(30) NULL COMMENT '测量时间点',
  `measurer` VARCHAR(20) NULL COMMENT '测量者',
  `ward_id` INT NULL COMMENT '病区ID',
  `measure_status` VARCHAR(20) NULL COMMENT '测量状态',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心电图采集记录表';

-- 测量信息表
CREATE TABLE `sys_ecg_abnormal_warning` (
  `warning_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '预警ID，主键',
  `measure_id` INT NOT NULL COMMENT '测量ID',
  `patient_id` INT NOT NULL COMMENT '患者ID',
  `warning_time` DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '预警时间',
  `warning_type` VARCHAR(30) NULL COMMENT '预警类型',
  `warning_level` VARCHAR(20) NULL COMMENT '预警级别',
  `warning_content` TEXT NULL COMMENT '预警详情',
  `handle_doctor_id` INT NULL COMMENT '处理医生ID',
  `handle_time` DATETIME NULL COMMENT '处理时间',
  `handle_result` TEXT NULL COMMENT '处理结果',
  `warning_status` VARCHAR(20) DEFAULT '0' NULL COMMENT '纳入状态 1纳入 0不纳入'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心电图异常预警表';

CREATE TABLE `sys_ecg_ai_analysis` (
  `ai_analysis_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT 'AI分析ID，主键',
  `measure_id` INT NOT NULL COMMENT '测量ID',
  `model_version` VARCHAR(50) NULL COMMENT '模型版本',
  `abnormal_fragment` VARCHAR(255) NULL COMMENT '异常片段',
  `ai_diagnosis` TEXT NULL COMMENT 'AI初步诊断',
  `abnormal_index` VARCHAR(255) NULL COMMENT '异常指标汇总',
  `confidence` DECIMAL(5, 2) NULL COMMENT '置信度%',
  `analysis_time` DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '分析完成时间',
  `is_verified` TINYINT(1) DEFAULT 0 NULL COMMENT '是否医生验证'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心电图AI分析表';

CREATE TABLE `sys_ecg_core_index` (
  `index_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '指标ID，主键',
  `measure_id` INT NOT NULL COMMENT '测量ID',
  `heartbeat_total` INT NULL COMMENT '心搏总数',
  `avg_heart_rate` DECIMAL(5, 1) NULL COMMENT '平均心率',
  `min_heart_rate` INT NULL COMMENT '最慢心率',
  `max_heart_rate` INT NULL COMMENT '最快心率',
  `max_rr_interval` DECIMAL(5, 3) NULL COMMENT '最长RR间期',
  `max_rr_count` INT NULL COMMENT '≥2.0s长RR间期次数',
  `tachycardia_times` INT NULL COMMENT '心动过速次数',
  `tachycardia_heartbeat` INT NULL COMMENT '心动过速心搏数',
  `bradycardia_times` INT NULL COMMENT '心动过缓次数',
  `atrial_premature` INT NULL COMMENT '房早总数',
  `atrial_premature_single` INT NULL COMMENT '单发房早',
  `atrial_premature_pair` INT NULL COMMENT '成对房早',
  `ventricular_premature` INT NULL COMMENT '室早总数',
  `ventricular_premature_single` INT NULL COMMENT '单发室早',
  `ventricular_premature_pair` INT NULL COMMENT '成对室早',
  `hrv_sdnn` DECIMAL(5, 1) NULL COMMENT 'SDNN',
  `hrv_sdann` DECIMAL(5, 1) NULL COMMENT 'SDANN',
  `hrv_rmssd` DECIMAL(5, 1) NULL COMMENT 'rMSSD',
  `hrv_pnn50` DECIMAL(5, 2) NULL COMMENT 'pNN50',
  `hrv_lf` DECIMAL(5, 1) NULL COMMENT 'LF',
  `hrv_hf` DECIMAL(5, 1) NULL COMMENT 'HF',
  `hrv_lf_hf` DECIMAL(5, 2) NULL COMMENT 'LF/HF',
  `ventricular_tachycardia` INT NULL COMMENT '室速阵数',
  `atrial_tachycardia` INT NULL COMMENT '房速阵数',
  `rhythm_type` VARCHAR(30) NULL COMMENT '节律类型',
  `warning_status` INT DEFAULT 1 COMMENT '预警状态：1=平稳，0=异常；最快心率>100则标记为0，否则为1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心电图核心指标表';

CREATE TABLE `sys_ecg_follow_up` (
  `follow_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '随访ID，主键',
  `patient_id` INT NOT NULL COMMENT '患者ID',
  `measure_id` INT NULL COMMENT '测量ID',
  `follow_time` DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '随访时间',
  `follow_type` VARCHAR(30) NULL COMMENT '随访方式',
  `follow_doctor_id` INT NOT NULL COMMENT '随访医生ID',
  `patient_condition` TEXT NULL COMMENT '患者病情',
  `follow_advice` TEXT NULL COMMENT '随访建议',
  `next_follow_time` DATETIME NULL COMMENT '下次随访时间',
  `follow_remark` TEXT NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心电图随访记录表';

CREATE TABLE `sys_ecg_report` (
  `report_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '报告ID，主键',
  `measure_id` INT NOT NULL COMMENT '测量ID',
  `ai_analysis_id` INT NULL COMMENT 'AI分析ID',
  `patient_id` INT NOT NULL COMMENT '患者ID',
  `report_no` VARCHAR(30) NOT NULL COMMENT '报告编号',
  `report_content` TEXT NULL COMMENT '报告内容',
  `diagnosis_result` VARCHAR(255) NULL COMMENT '诊断结果',
  `create_doctor_id` INT NOT NULL COMMENT '创建医生ID',
  `review_doctor_id` INT NULL COMMENT '审核医生ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
  `review_time` DATETIME NULL COMMENT '审核时间',
  `report_status` VARCHAR(20) DEFAULT '草稿' NULL COMMENT '报告状态',
  `report_path` VARCHAR(255) NULL COMMENT '报告PDF路径'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心电图报告表';