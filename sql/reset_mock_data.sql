-- 禁用外键检查，方便清空数据
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 清空所有表数据（确保历史数据完全清除）
TRUNCATE TABLE bed_info;
TRUNCATE TABLE ward_info;
TRUNCATE TABLE sys_ecg_ward_info;
TRUNCATE TABLE sys_ecg_doctor_info;
TRUNCATE TABLE sys_ecg_equipment_info;
TRUNCATE TABLE sys_ecg_patient_info;
TRUNCATE TABLE sys_ecg_measure_record;
TRUNCATE TABLE sys_ecg_core_index;
TRUNCATE TABLE sys_ecg_ai_analysis;
TRUNCATE TABLE sys_ecg_abnormal_warning;
TRUNCATE TABLE sys_ecg_report;
TRUNCATE TABLE sys_ecg_follow_up;
TRUNCATE TABLE sys_ecg_measure_stat;
TRUNCATE TABLE sys_ecg_warning_stat;
TRUNCATE TABLE sys_ecg_report_stat;
TRUNCATE TABLE sys_dict_warning_level;
TRUNCATE TABLE sys_dict_warning_type;
TRUNCATE TABLE sys_dict_equipment_type;

SET FOREIGN_KEY_CHECKS = 1;

-- ==========================================
-- 2. 插入新的一套完整联调流程假数据
-- ==========================================

-- 字典表数据
INSERT INTO sys_dict_warning_level (level_code, level_name) VALUES 
('L1','轻度'), ('L2','中度'), ('L3','重度'), ('L4','危险');

INSERT INTO sys_dict_warning_type (type_code, type_name) VALUES 
('T1','心动过速'), ('T2','室早'), ('T3','房颤'), ('T4','ST段改变');

INSERT INTO sys_dict_equipment_type (type_code, type_name) VALUES 
('E1','动态心电'), ('E2','床旁心电'), ('E3','便携式心电');

-- 病房与科室对应 (ward_info & sys_ecg_ward_info 双表一致性)
INSERT INTO ward_info (ward_id, department, ward_no, nurse_name) VALUES
(1, '心内科', '心内病房101', '张护士'),
(2, '呼吸科', '呼吸病房201', '李护士'),
(3, '急诊科', '急诊留观室', '王护士');

INSERT INTO sys_ecg_ward_info (ward_id, ward_name, ward_phone, ward_desc) VALUES
(1, '心内科-101', '010-888801', '心脏专科核心病房'),
(2, '呼吸科-201', '010-888802', '呼吸重症病房'),
(3, '急诊科-留观', '010-888803', '急诊心电监测区');

-- 医生信息
INSERT INTO sys_ecg_doctor_info (doctor_id, doctor_name, doctor_title, ward_id, user_name, password, is_active) VALUES
(1, '陈主任', '主任医师', 1, 'chen', '123456', 1),
(2, '林主治', '主治医师', 2, 'lin', '123456', 1),
(3, '赵医生', '住院医师', 3, 'zhao', '123456', 1);

-- 设备信息
INSERT INTO sys_ecg_equipment_info (equipment_id, equipment_code, equipment_name, equipment_type, manufacturer, ward_id, equipment_status) VALUES
(1, 'EQ-XN-001', '床旁监护仪A', '床旁心电', '迈瑞', 1, '正常'),
(2, 'EQ-HX-001', '动态心电盒B', '动态心电', '飞利浦', 2, '正常'),
(3, 'EQ-JZ-001', '移动心电车C', '便携式心电', '理邦', 3, '正常');

-- ==========================================
-- 3. 核心业务流程： 患者 -> 床位 -> 检查 -> 分析 -> 预警 -> 报告
-- ==========================================

-- 患者信息 (6个患者)
INSERT INTO sys_ecg_patient_info (patient_id, patient_name, patient_gender, patient_age, inpatient_no, bed_no, inpatient_diagnosis, ward_id, phone) VALUES
(1, '刘大爷', '男', 68, 'INP2025001', '101-01', '冠心病', 1, '13800138001'),
(2, '孙阿姨', '女', 72, 'INP2025002', '101-02', '心力衰竭', 1, '13900139002'),
(3, '李先生', '男', 45, 'INP2025003', '201-01', '慢阻肺伴心悸', 2, '13700137003'),
(4, '周女士', '女', 55, 'INP2025004', '201-02', '重症哮喘', 2, '13600136004'),
(5, '吴大哥', '男', 38, 'INP2025005', '室-01', '急性胸痛', 3, '13500135005'),
(6, '郑小姐', '女', 29, 'INP2025006', '室-02', '心律不齐待查', 3, '13400134006');

-- 床位分配 (每个病房设定3张床，部分空置)
-- ward 1: 101-01(有), 101-02(有), 101-03(空)
-- ward 2: 201-01(有), 201-02(有), 201-03(空)
-- ward 3: 室-01(有), 室-02(有), 室-03(空)
INSERT INTO bed_info (bed_id, ward_id, bed_no, status, patient_id) VALUES
(1, 1, '101-01', 1, 1),
(2, 1, '101-02', 1, 2),
(3, 1, '101-03', 0, NULL),
(4, 2, '201-01', 1, 3),
(5, 2, '201-02', 1, 4),
(6, 2, '201-03', 0, NULL),
(7, 3, '室-01', 1, 5),
(8, 3, '室-02', 1, 6),
(9, 3, '室-03', 0, NULL);

-- 测量记录 (每人1次测量)
INSERT INTO sys_ecg_measure_record (measure_id, patient_id, measure_type, equipment_id, start_time, end_time, measurer, ward_id, measure_status) VALUES
(1, 1, '床旁心电', 1, '2026-04-15 08:00:00', '2026-04-15 08:30:00', '张护士', 1, '已完成'),
(2, 2, '床旁心电', 1, '2026-04-15 09:00:00', '2026-04-15 09:30:00', '张护士', 1, '已完成'),
(3, 3, '动态心电', 2, '2026-04-14 10:00:00', '2026-04-15 10:00:00', '李护士', 2, '已完成'),
(4, 4, '动态心电', 2, '2026-04-14 11:00:00', '2026-04-15 11:00:00', '李护士', 2, '已完成'),
(5, 5, '便携式心电', 3, '2026-04-15 12:00:00', '2026-04-15 12:15:00', '王护士', 3, '已完成'),
(6, 6, '便携式心电', 3, '2026-04-15 13:00:00', '2026-04-15 13:15:00', '王护士', 3, '已完成');

-- 核心指标 (每人对应一条指标)
INSERT INTO sys_ecg_core_index (index_id, measure_id, heartbeat_total, avg_heart_rate, min_heart_rate, max_heart_rate, rhythm_type) VALUES
(1, 1, 1800, 60.5, 45, 90, '窦性心律'),
(2, 2, 2100, 75.0, 50, 110, '心房颤动'),
(3, 3, 105000, 80.2, 55, 120, '窦性心律'),
(4, 4, 95000, 65.5, 40, 95, '窦性心律伴早搏'),
(5, 5, 900, 105.0, 90, 140, '室性心动过速'),
(6, 6, 850, 62.0, 55, 80, '窦性心律');

-- AI 分析
INSERT INTO sys_ecg_ai_analysis (ai_analysis_id, measure_id, ai_diagnosis, confidence, is_verified) VALUES
(1, 1, '未见明显异常结构', 98.5, 1),
(2, 2, '持续性心房颤动', 96.0, 1),
(3, 3, '频发室性早搏', 92.5, 1),
(4, 4, '大致正常', 99.0, 0),
(5, 5, '急性广泛前壁心肌梗死伴室速', 85.5, 1),
(6, 6, '正常心电', 95.0, 1);

-- 异常预警 (选取病情较重的3人产生预警)
INSERT INTO sys_ecg_abnormal_warning (warning_id, measure_id, patient_id, warning_type, warning_level, warning_content, handle_doctor_id, warning_status) VALUES
(1, 2, 2, '房颤', '中度', '检测到持续房颤波形段', 1, '1'),
(2, 3, 3, '室早', '轻度', '24小时内室早次数超标', 2, '1'),
(3, 5, 5, '室速', '危险', '连续室性心动过速，可能猝死', 3, '1');

-- 报告单
INSERT INTO sys_ecg_report (report_id, measure_id, ai_analysis_id, patient_id, report_no, report_content, diagnosis_result, create_doctor_id, report_status) VALUES
(1, 1, 1, 1, 'REP20260415001', '各项指标在正常范围', '正常心电图', 1, '已审核'),
(2, 2, 2, 2, 'REP20260415002', 'P波消失，f波代之', '房颤', 1, '已审核'),
(3, 3, 3, 3, 'REP20260415003', '多发性宽大畸形QRS波群', '频发室上性期前收缩', 2, '已审核'),
(4, 4, 4, 4, 'REP20260415004', '节律规整', '大致正常', 2, '待审核'),
(5, 5, 5, 5, 'REP20260415005', 'V1-V5导联ST段弓背向上抬高', '急性心梗', 3, '已审核'),
(6, 6, 6, 6, 'REP20260415006', '无特殊发现', '正常', 3, '已审核');

-- 随访记录
INSERT INTO sys_ecg_follow_up (follow_id, patient_id, measure_id, follow_type, follow_doctor_id, patient_condition, follow_advice) VALUES
(1, 2, 2, '查房重点关注', 1, '诉胸闷改善', '继续抗凝及控制心室率'),
(2, 5, 5, '紧急干预', 3, '大汗淋漓，胸痛剧烈', '立即启动冠脉造影及介入治疗');

-- 假数据插入完成
