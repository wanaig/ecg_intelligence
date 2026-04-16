-- ============================================================================
-- ECG 基础联调测试数据（20组）
-- 使用方式：先执行 sql/init.sql 建表，再执行本脚本。
-- 特点：
-- 1) 20条完整业务链路（患者->测量->指标->AI->预警->报告->随访）
-- 2) 病房每间 6 床（5间 * 6 = 30床）
-- 3) 20名患者入住，剩余床位为空床
-- ============================================================================

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE sys_ecg_follow_up;
TRUNCATE TABLE sys_ecg_report;
TRUNCATE TABLE sys_ecg_abnormal_warning;
TRUNCATE TABLE sys_ecg_ai_analysis;
TRUNCATE TABLE sys_ecg_core_index;
TRUNCATE TABLE sys_ecg_measure_record;
TRUNCATE TABLE bed_info;
TRUNCATE TABLE ward_info;
TRUNCATE TABLE sys_ecg_ward_info;
TRUNCATE TABLE sys_ecg_equipment_info;
TRUNCATE TABLE sys_ecg_doctor_info;
TRUNCATE TABLE sys_ecg_patient_info;
TRUNCATE TABLE sys_dict_warning_type;
TRUNCATE TABLE sys_dict_warning_level;
TRUNCATE TABLE sys_dict_equipment_type;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================================
-- 0) 辅助序列表（1..20，1..30）
-- ============================================================================
DROP TEMPORARY TABLE IF EXISTS tmp_seq_20;
CREATE TEMPORARY TABLE tmp_seq_20 (
  n INT PRIMARY KEY
) ENGINE=Memory;

INSERT INTO tmp_seq_20 (n) VALUES
(1),(2),(3),(4),(5),(6),(7),(8),(9),(10),
(11),(12),(13),(14),(15),(16),(17),(18),(19),(20);

DROP TEMPORARY TABLE IF EXISTS tmp_seq_30;
CREATE TEMPORARY TABLE tmp_seq_30 (
  n INT PRIMARY KEY
) ENGINE=Memory;

INSERT INTO tmp_seq_30 (n) VALUES
(1),(2),(3),(4),(5),(6),(7),(8),(9),(10),
(11),(12),(13),(14),(15),(16),(17),(18),(19),(20),
(21),(22),(23),(24),(25),(26),(27),(28),(29),(30);

-- ============================================================================
-- 1) 字典基础数据
-- ============================================================================
INSERT INTO sys_dict_equipment_type (dict_id, type_code, type_name, type_desc, sort) VALUES
(1, 'ECG_NORMAL', '常规心电', '常规静态心电采集', 1),
(2, 'ECG_HOLTER', '动态心电', '24小时动态心电采集', 2),
(3, 'ECG_TELE', '遥测心电', '遥测连续监护', 3),
(4, 'ECG_OUT', '门诊心电', '门诊快检', 4);

INSERT INTO sys_dict_warning_level (dict_id, level_code, level_name, level_desc, sort) VALUES
(1, 'NORMAL', '正常', '未见明显异常', 1),
(2, 'LOW', '轻度', '轻度异常提示', 2),
(3, 'MID', '中度', '中度异常提示', 3),
(4, 'HIGH', '危急', '需优先处置', 4);

INSERT INTO sys_dict_warning_type (dict_id, type_code, type_name, type_desc, sort) VALUES
(1, 'HR_HIGH', '心率偏高', '心率高于阈值', 1),
(2, 'HR_LOW', '心率偏低', '心率低于阈值', 2),
(3, 'ARR', '心律失常', '节律异常', 3),
(4, 'ST', 'ST段异常', 'ST段变化异常', 4);

-- ============================================================================
-- 2) 病区 / 病房 / 床位（每房6床）
-- ============================================================================
INSERT INTO sys_ecg_ward_info (ward_id, ward_name, ward_phone, ward_desc) VALUES
(1, '心内一病区', '010-86010001', '冠心病与常规监护'),
(2, '心内二病区', '010-86010002', '心律失常重点监护'),
(3, '心内三病区', '010-86010003', '术后恢复观察'),
(4, '心内四病区', '010-86010004', '慢病心电管理'),
(5, '心内五病区', '010-86010005', '综合病区');

INSERT INTO ward_info (ward_id, department, ward_no, nurse_name, total_beds) VALUES
(1, '心血管内科', '101', '张宁', 6),
(2, '心血管内科', '102', '李洁', 6),
(3, '心血管内科', '103', '王颖', 6),
(4, '心血管内科', '104', '赵琳', 6),
(5, '心血管内科', '105', '陈萍', 6);

-- 20名患者入住：
-- 1..6 号 -> 101病房 01..06床
-- 7..12号 -> 102病房 01..06床
-- 13..18号 -> 103病房 01..06床
-- 19..20号 -> 104病房 01..02床
INSERT INTO bed_info (bed_id, ward_id, bed_no, status, patient_id)
SELECT
  b.n AS bed_id,
  FLOOR((b.n - 1) / 6) + 1 AS ward_id,
  CONCAT(
    LPAD(FLOOR((b.n - 1) / 6) + 101, 3, '0'),
    '-',
    LPAD(MOD(b.n - 1, 6) + 1, 2, '0')
  ) AS bed_no,
  CASE WHEN p.patient_id IS NULL THEN 0 ELSE 1 END AS status,
  p.patient_id
FROM tmp_seq_30 b
LEFT JOIN (
  SELECT
    n AS patient_id,
    FLOOR((n - 1) / 6) + 1 AS ward_id,
    MOD(n - 1, 6) + 1 AS bed_index
  FROM tmp_seq_20
) p
  ON p.ward_id = FLOOR((b.n - 1) / 6) + 1
 AND p.bed_index = MOD(b.n - 1, 6) + 1
ORDER BY b.n;

-- ============================================================================
-- 3) 医生 / 设备
-- ============================================================================
INSERT INTO sys_ecg_doctor_info (doctor_id, doctor_name, doctor_title, ward_id, user_name, password, is_active) VALUES
(1, '张伟', '主任医师', 1, 'zhangwei', 'e10adc3949ba59abbe56e057f20f883e', 1),
(2, '李芳', '副主任医师', 2, 'lifang', 'e10adc3949ba59abbe56e057f20f883e', 1),
(3, '王强', '主治医师', 3, 'wangqiang', 'e10adc3949ba59abbe56e057f20f883e', 1),
(4, '赵敏', '主治医师', 4, 'zhaomin', 'e10adc3949ba59abbe56e057f20f883e', 1),
(5, '刘洋', '主任医师', 5, 'liuyang', 'e10adc3949ba59abbe56e057f20f883e', 1);

INSERT INTO sys_ecg_equipment_info (
  equipment_id, equipment_code, equipment_name, equipment_type, manufacturer,
  equipment_version, ward_id, equipment_status, install_time, maintain_time, next_maintain_time
) VALUES
(101, 'ECG-101', '常规心电仪-A1', '常规心电', '迈瑞', 'v3.2', 1, '正常', '2025-11-10 09:00:00', '2026-03-01 10:00:00', '2026-06-01 10:00:00'),
(102, 'ECG-102', '常规心电仪-A2', '常规心电', '迈瑞', 'v3.2', 2, '正常', '2025-11-12 09:00:00', '2026-03-03 10:00:00', '2026-06-03 10:00:00'),
(103, 'ECG-103', '动态心电盒-H1', '动态心电', '理邦', 'v2.8', 3, '正常', '2025-11-15 09:00:00', '2026-03-05 10:00:00', '2026-06-05 10:00:00'),
(104, 'ECG-104', '动态心电盒-H2', '动态心电', '理邦', 'v2.8', 4, '正常', '2025-11-18 09:00:00', '2026-03-07 10:00:00', '2026-06-07 10:00:00'),
(105, 'ECG-105', '遥测监护-W1', '遥测心电', '飞利浦', 'v4.0', 5, '正常', '2025-12-01 09:00:00', '2026-03-09 10:00:00', '2026-06-09 10:00:00');

-- ============================================================================
-- 4) 患者（20条）
-- ============================================================================
INSERT INTO sys_ecg_patient_info (
  patient_id, patient_name, patient_gender, patient_age, inpatient_no, outpatient_no,
  inpatient_date, bed_no, inpatient_diagnosis, discharge_diagnosis,
  ward_id, phone, heart_rate, status, `desc`, discharge_time
)
SELECT
  n AS patient_id,
  CONCAT('患者', LPAD(n, 2, '0')) AS patient_name,
  CASE WHEN MOD(n, 2) = 0 THEN '女' ELSE '男' END AS patient_gender,
  32 + MOD(n * 2, 46) AS patient_age,
  CONCAT('ZY202605', LPAD(n, 3, '0')) AS inpatient_no,
  NULL AS outpatient_no,
  DATE_ADD('2026-05-01', INTERVAL n DAY) AS inpatient_date,
  CONCAT(
    LPAD(FLOOR((n - 1) / 6) + 101, 3, '0'),
    '-',
    LPAD(MOD(n - 1, 6) + 1, 2, '0')
  ) AS bed_no,
  CONCAT('心电监测入院-', LPAD(n, 2, '0')) AS inpatient_diagnosis,
  NULL AS discharge_diagnosis,
  FLOOR((n - 1) / 6) + 1 AS ward_id,
  CONCAT('1391000', LPAD(n, 4, '0')) AS phone,
  ROUND(60 + MOD(n * 17, 141), 1) AS heart_rate,
  CASE WHEN (60 + MOD(n * 17, 141)) > 100 THEN 1 ELSE 0 END AS status,
  CASE WHEN (60 + MOD(n * 17, 141)) > 100 THEN '疑似异常心律' ELSE '心律基本平稳' END AS `desc`,
  NULL AS discharge_time
FROM tmp_seq_20;

-- ============================================================================
-- 5) 测量记录（20条）
-- ============================================================================
INSERT INTO sys_ecg_measure_record (
  measure_id, patient_id, measure_type, equipment_id, start_time, end_time, total_duration,
  wave_data_path, measure_point, measurer, ward_id, measure_status
)
SELECT
  n AS measure_id,
  n AS patient_id,
  CASE WHEN MOD(n, 3) = 0 THEN '动态心电' ELSE '常规心电' END AS measure_type,
  100 + FLOOR((n - 1) / 6) + 1 AS equipment_id,
  DATE_ADD('2026-05-20 08:00:00', INTERVAL (n - 1) * 10 MINUTE) AS start_time,
  DATE_ADD('2026-05-20 08:20:00', INTERVAL (n - 1) * 10 MINUTE) AS end_time,
  '00:20:00' AS total_duration,
  CONCAT('/data/ecg/m', LPAD(n, 4, '0'), '.dat') AS wave_data_path,
  CASE WHEN n <= 8 THEN '晨间' WHEN n <= 16 THEN '上午' ELSE '下午' END AS measure_point,
  CONCAT('护士', LPAD(MOD(n - 1, 5) + 1, 2, '0')) AS measurer,
  FLOOR((n - 1) / 6) + 1 AS ward_id,
  '已完成' AS measure_status
FROM tmp_seq_20;

-- ============================================================================
-- 6) 核心指标（20条）
-- ============================================================================
INSERT INTO sys_ecg_core_index (
  index_id, measure_id, heartbeat_total, avg_heart_rate, min_heart_rate, max_heart_rate,
  max_rr_interval, max_rr_count, tachycardia_times, bradycardia_times,
  atrial_premature, ventricular_premature, rhythm_type, warning_status
)
SELECT
  n AS index_id,
  n AS measure_id,
  1600 + n * 45 AS heartbeat_total,
  ROUND(60 + MOD(n * 17, 141), 1) AS avg_heart_rate,
  48 + MOD(n * 2, 18) AS min_heart_rate,
  95 + MOD(n * 4, 45) AS max_heart_rate,
  ROUND(0.80 + MOD(n, 5) * 0.16, 3) AS max_rr_interval,
  MOD(n, 4) AS max_rr_count,
  CASE WHEN (60 + MOD(n * 17, 141)) > 100 THEN 2 ELSE 0 END AS tachycardia_times,
  CASE WHEN (48 + MOD(n * 2, 18)) < 55 THEN 1 ELSE 0 END AS bradycardia_times,
  MOD(n * 2, 16) AS atrial_premature,
  MOD(n * 3, 12) AS ventricular_premature,
  CASE
    WHEN MOD(n, 5) = 0 THEN '室性早搏'
    WHEN MOD(n, 4) = 0 THEN '窦性心动过速'
    WHEN MOD(n, 3) = 0 THEN '窦性心动过缓'
    ELSE '窦性心律'
  END AS rhythm_type,
  CASE WHEN (60 + MOD(n * 17, 141)) > 100 THEN 0 ELSE 1 END AS warning_status
FROM tmp_seq_20;

-- ============================================================================
-- 7) AI分析（20条）
-- ============================================================================
INSERT INTO sys_ecg_ai_analysis (
  ai_analysis_id, measure_id, model_version, abnormal_fragment, ai_diagnosis,
  abnormal_index, confidence, analysis_time, is_verified
)
SELECT
  n AS ai_analysis_id,
  n AS measure_id,
  'v2.2.0' AS model_version,
  CONCAT('/fragment/', LPAD(n, 3, '0'), '.png') AS abnormal_fragment,
  CASE WHEN (60 + MOD(n * 17, 141)) > 100 THEN '疑似异常心律' ELSE '心律基本平稳' END AS ai_diagnosis,
  CASE WHEN (60 + MOD(n * 17, 141)) > 100 THEN 'HR_HIGH,ARR' ELSE 'NORMAL' END AS abnormal_index,
  ROUND(83 + MOD(n * 3, 14) + MOD(n, 10) * 0.1, 2) AS confidence,
  DATE_ADD('2026-05-20 08:03:00', INTERVAL (n - 1) * 10 MINUTE) AS analysis_time,
  CASE WHEN MOD(n, 2) = 0 THEN 1 ELSE 0 END AS is_verified
FROM tmp_seq_20;

-- ============================================================================
-- 8) 异常预警（20条）
-- ============================================================================
INSERT INTO sys_ecg_abnormal_warning (
  warning_id, measure_id, patient_id, warning_time, warning_type, warning_level,
  warning_content, handle_doctor_id, handle_time, handle_result, warning_status
)
SELECT
  n AS warning_id,
  n AS measure_id,
  n AS patient_id,
  DATE_ADD('2026-05-20 09:00:00', INTERVAL (n - 1) * 9 MINUTE) AS warning_time,
  CASE
    WHEN (60 + MOD(n * 17, 141)) > 130 THEN 'HR_HIGH'
    WHEN (60 + MOD(n * 17, 141)) < 60 THEN 'HR_LOW'
    WHEN MOD(n, 4) = 0 THEN 'ST'
    ELSE 'ARR'
  END AS warning_type,
  CASE
    WHEN (60 + MOD(n * 17, 141)) > 140 THEN '危急'
    WHEN (60 + MOD(n * 17, 141)) > 100 THEN '异常'
    ELSE '正常'
  END AS warning_level,
  CONCAT('自动预警-', LPAD(n, 2, '0'), '，请复核') AS warning_content,
  NULL AS handle_doctor_id,
  NULL AS handle_time,
  NULL AS handle_result,
  CASE WHEN (60 + MOD(n * 17, 141)) > 100 THEN '1' ELSE '0' END AS warning_status
FROM tmp_seq_20;

-- ============================================================================
-- 9) 报告（20条）
-- ============================================================================
INSERT INTO sys_ecg_report (
  report_id, measure_id, ai_analysis_id, patient_id, report_no, report_content,
  diagnosis_result, create_doctor_id, review_doctor_id, create_time, review_time,
  report_status, report_path
)
SELECT
  n AS report_id,
  n AS measure_id,
  n AS ai_analysis_id,
  n AS patient_id,
  CONCAT('REP202605', LPAD(n, 4, '0')) AS report_no,
  CONCAT('自动生成报告内容-', LPAD(n, 2, '0')) AS report_content,
  CASE WHEN (60 + MOD(n * 17, 141)) > 100 THEN '异常心律待干预' ELSE '心律相对平稳' END AS diagnosis_result,
  MOD(n - 1, 5) + 1 AS create_doctor_id,
  CASE WHEN MOD(n, 3) = 0 THEN MOD(n - 1, 5) + 1 ELSE NULL END AS review_doctor_id,
  DATE_ADD('2026-05-20 09:30:00', INTERVAL n * 8 MINUTE) AS create_time,
  CASE WHEN MOD(n, 3) = 0 THEN DATE_ADD('2026-05-20 10:00:00', INTERVAL n * 8 MINUTE) ELSE NULL END AS review_time,
  CASE
    WHEN MOD(n, 3) = 0 THEN '已审核'
    WHEN MOD(n, 3) = 1 THEN '待审核'
    ELSE '草稿'
  END AS report_status,
  CONCAT('/report/rep', LPAD(n, 4, '0'), '.pdf') AS report_path
FROM tmp_seq_20;

-- ============================================================================
-- 10) 随访（20条）
-- ============================================================================
INSERT INTO sys_ecg_follow_up (
  follow_id, patient_id, measure_id, follow_time, follow_type, follow_doctor_id,
  patient_condition, follow_advice, next_follow_time, follow_remark
)
SELECT
  n AS follow_id,
  n AS patient_id,
  n AS measure_id,
  DATE_ADD('2026-05-20 10:30:00', INTERVAL n * 10 MINUTE) AS follow_time,
  '病房查房' AS follow_type,
  MOD(n - 1, 5) + 1 AS follow_doctor_id,
  CASE WHEN (60 + MOD(n * 17, 141)) > 100 THEN '存在心电异常，需持续监测' ELSE '症状相对平稳' END AS patient_condition,
  CASE WHEN (60 + MOD(n * 17, 141)) > 100 THEN '建议24小时内复测并调整治疗方案' ELSE '建议规律作息并按时复诊' END AS follow_advice,
  DATE_ADD('2026-05-21 10:30:00', INTERVAL n * 10 MINUTE) AS next_follow_time,
  CONCAT('随访记录-', LPAD(n, 2, '0')) AS follow_remark
FROM tmp_seq_20;

-- ============================================================================
-- 11) 清理临时表
-- ============================================================================
DROP TEMPORARY TABLE IF EXISTS tmp_seq_20;
DROP TEMPORARY TABLE IF EXISTS tmp_seq_30;

-- ============================================================================
-- 完成
-- ============================================================================
-- 执行后可得到：
-- 1) 5间病房，每间6床，共30床
-- 2) 20条完整业务链路数据
-- 3) 患者、床位、病区关联一致
