-- ============================================================================
-- ECG 业务联调测试数据（30组完整链路）
-- 使用方式：先执行 sql/init.sql 建表，再执行本脚本。
-- 特点：
-- 1) 30条完整业务链路（患者->测量->指标->AI->预警->报告->随访）
-- 2) 房间表每间 6 床（10间 * 6 = 60床）
-- 3) 人员分散：24名住院患者分散在10间病房，6名门诊/离院患者无床位
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

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================================
-- 0) 生成辅助序列表（1..30, 1..60）
-- ============================================================================
DROP TEMPORARY TABLE IF EXISTS tmp_seq_30;
CREATE TEMPORARY TABLE tmp_seq_30 (
  n INT PRIMARY KEY
) ENGINE=Memory;

INSERT INTO tmp_seq_30 (n) VALUES
(1),(2),(3),(4),(5),(6),(7),(8),(9),(10),
(11),(12),(13),(14),(15),(16),(17),(18),(19),(20),
(21),(22),(23),(24),(25),(26),(27),(28),(29),(30);

DROP TEMPORARY TABLE IF EXISTS tmp_seq_60;
CREATE TEMPORARY TABLE tmp_seq_60 (
  n INT PRIMARY KEY
) ENGINE=Memory;

INSERT INTO tmp_seq_60 (n) VALUES
(1),(2),(3),(4),(5),(6),(7),(8),(9),(10),
(11),(12),(13),(14),(15),(16),(17),(18),(19),(20),
(21),(22),(23),(24),(25),(26),(27),(28),(29),(30),
(31),(32),(33),(34),(35),(36),(37),(38),(39),(40),
(41),(42),(43),(44),(45),(46),(47),(48),(49),(50),
(51),(52),(53),(54),(55),(56),(57),(58),(59),(60);

-- ============================================================================
-- 1) 病区 / 病房 / 床位（每房6床）
-- ============================================================================

INSERT INTO sys_ecg_ward_info (ward_id, ward_name, ward_phone, ward_desc) VALUES
(1, '心内一病区', '010-86000001', '冠心病与心绞痛监护病区'),
(2, '心内二病区', '010-86000002', '心律失常重点病区'),
(3, '心内三病区', '010-86000003', '术后恢复病区'),
(4, '心内四病区', '010-86000004', '高血压并发症病区'),
(5, '心内五病区', '010-86000005', '心衰管理病区'),
(6, '心内六病区', '010-86000006', '心电监护重点病区'),
(7, '心内七病区', '010-86000007', '老年心血管病区'),
(8, '心内八病区', '010-86000008', '起搏与电生理病区'),
(9, '心内九病区', '010-86000009', '康复观察病区'),
(10, '心内十病区', '010-86000010', '综合病区');

INSERT INTO ward_info (ward_id, department, ward_no, nurse_name, total_beds) VALUES
(1, '心血管内科', '101', '张宁', 6),
(2, '心血管内科', '102', '李洁', 6),
(3, '心血管内科', '103', '王颖', 6),
(4, '心血管内科', '104', '赵琳', 6),
(5, '心血管内科', '105', '陈萍', 6),
(6, '心血管内科', '106', '刘莎', 6),
(7, '心血管内科', '107', '杨璐', 6),
(8, '心血管内科', '108', '黄倩', 6),
(9, '心血管内科', '109', '周敏', 6),
(10, '心血管内科', '110', '吴静', 6);

-- 24名住院患者分散入住：
-- 1..10 号患者占各病房01床
-- 11..20 号患者占各病房02床
-- 21..24 号患者占 101/102/103/104 的03床
INSERT INTO bed_info (bed_id, ward_id, bed_no, status, patient_id)
SELECT
  b.n AS bed_id,
  FLOOR((b.n - 1) / 6) + 1 AS ward_id,
  CONCAT(LPAD(FLOOR((b.n - 1) / 6) + 101, 3, '0'), '-', LPAD(MOD(b.n - 1, 6) + 1, 2, '0')) AS bed_no,
  CASE WHEN p.patient_id IS NULL THEN 0 ELSE 1 END AS status,
  p.patient_id
FROM tmp_seq_60 b
LEFT JOIN (
  SELECT
    n AS patient_id,
    MOD(n - 1, 10) + 1 AS ward_id,
    CASE
      WHEN n <= 10 THEN 1
      WHEN n <= 20 THEN 2
      ELSE 3
    END AS bed_index
  FROM tmp_seq_30
  WHERE n <= 24
) p
  ON p.ward_id = FLOOR((b.n - 1) / 6) + 1
 AND p.bed_index = MOD(b.n - 1, 6) + 1
ORDER BY b.n;

-- ============================================================================
-- 2) 医生 / 设备
-- ============================================================================

INSERT INTO sys_ecg_doctor_info (doctor_id, doctor_name, doctor_title, ward_id, user_name, password, is_active) VALUES
(1, '张伟', '主任医师', 1, 'zhangwei', 'e10adc3949ba59abbe56e057f20f883e', 1),
(2, '李芳', '副主任医师', 2, 'lifang', 'e10adc3949ba59abbe56e057f20f883e', 1),
(3, '王强', '主治医师', 3, 'wangqiang', 'e10adc3949ba59abbe56e057f20f883e', 1),
(4, '赵敏', '主治医师', 4, 'zhaomin', 'e10adc3949ba59abbe56e057f20f883e', 1),
(5, '刘洋', '主任医师', 5, 'liuyang', 'e10adc3949ba59abbe56e057f20f883e', 1),
(6, '陈静', '副主任医师', 6, 'chenjing', 'e10adc3949ba59abbe56e057f20f883e', 1),
(7, '杨勇', '主治医师', 7, 'yangyong', 'e10adc3949ba59abbe56e057f20f883e', 1),
(8, '黄燕', '住院医师', 8, 'huangyan', 'e10adc3949ba59abbe56e057f20f883e', 1),
(9, '周杰', '主任医师', 9, 'zhoujie', 'e10adc3949ba59abbe56e057f20f883e', 1),
(10, '吴娟', '副主任医师', 10, 'wujuan', 'e10adc3949ba59abbe56e057f20f883e', 1);

INSERT INTO sys_ecg_equipment_info (
  equipment_id, equipment_code, equipment_name, equipment_type, manufacturer,
  equipment_version, ward_id, equipment_status, install_time, maintain_time, next_maintain_time
) VALUES
(101, 'ECG-101', '常规心电仪-A1', '常规心电', '迈瑞', 'v3.2', 1, '正常', '2025-11-10 09:00:00', '2026-03-01 10:00:00', '2026-06-01 10:00:00'),
(102, 'ECG-102', '常规心电仪-A2', '常规心电', '迈瑞', 'v3.2', 2, '正常', '2025-11-12 09:00:00', '2026-03-03 10:00:00', '2026-06-03 10:00:00'),
(103, 'ECG-103', '动态心电盒-H1', '动态心电', '理邦', 'v2.8', 3, '正常', '2025-11-15 09:00:00', '2026-03-05 10:00:00', '2026-06-05 10:00:00'),
(104, 'ECG-104', '动态心电盒-H2', '动态心电', '理邦', 'v2.8', 4, '正常', '2025-11-18 09:00:00', '2026-03-07 10:00:00', '2026-06-07 10:00:00'),
(105, 'ECG-105', '遥测监护-W1', '遥测心电', '飞利浦', 'v4.0', 5, '正常', '2025-12-01 09:00:00', '2026-03-09 10:00:00', '2026-06-09 10:00:00'),
(106, 'ECG-106', '遥测监护-W2', '遥测心电', '飞利浦', 'v4.0', 6, '正常', '2025-12-05 09:00:00', '2026-03-11 10:00:00', '2026-06-11 10:00:00'),
(107, 'ECG-107', '便携贴片-P1', '便携心电', '微心', 'v1.9', 7, '正常', '2025-12-10 09:00:00', '2026-03-13 10:00:00', '2026-06-13 10:00:00'),
(108, 'ECG-108', '便携贴片-P2', '便携心电', '微心', 'v1.9', 8, '正常', '2025-12-12 09:00:00', '2026-03-15 10:00:00', '2026-06-15 10:00:00'),
(109, 'ECG-109', '常规心电仪-A3', '常规心电', '迈瑞', 'v3.3', 9, '正常', '2025-12-15 09:00:00', '2026-03-17 10:00:00', '2026-06-17 10:00:00'),
(110, 'ECG-110', '动态心电盒-H3', '动态心电', '理邦', 'v2.9', 10, '正常', '2025-12-18 09:00:00', '2026-03-19 10:00:00', '2026-06-19 10:00:00'),
(111, 'ECG-111', '门诊快检仪-O1', '门诊心电', '理邦', 'v1.5', NULL, '正常', '2025-12-20 09:00:00', '2026-03-21 10:00:00', '2026-06-21 10:00:00'),
(112, 'ECG-112', '门诊快检仪-O2', '门诊心电', '迈瑞', 'v1.5', NULL, '正常', '2025-12-22 09:00:00', '2026-03-23 10:00:00', '2026-06-23 10:00:00');

-- ============================================================================
-- 3) 患者（30条）
-- ============================================================================

INSERT INTO sys_ecg_patient_info (
  patient_id, patient_name, patient_gender, patient_age, inpatient_no, outpatient_no,
  inpatient_date, bed_no, inpatient_diagnosis, discharge_diagnosis,
  ward_id, phone, status, discharge_time
)
SELECT
  n AS patient_id,
  CONCAT('患者', LPAD(n, 2, '0')) AS patient_name,
  CASE WHEN MOD(n, 2) = 0 THEN '女' ELSE '男' END AS patient_gender,
  35 + MOD(n * 3, 45) AS patient_age,
  CONCAT('ZY202604', LPAD(n, 3, '0')) AS inpatient_no,
  CASE WHEN n > 24 THEN CONCAT('MZ202604', LPAD(n, 3, '0')) ELSE NULL END AS outpatient_no,
  DATE_ADD('2026-04-01', INTERVAL n DAY) AS inpatient_date,
  CASE
    WHEN n <= 24 THEN CONCAT(
      LPAD(MOD(n - 1, 10) + 101, 3, '0'),
      '-',
      LPAD(CASE WHEN n <= 10 THEN 1 WHEN n <= 20 THEN 2 ELSE 3 END, 2, '0')
    )
    ELSE NULL
  END AS bed_no,
  CONCAT('心电异常待评估-', LPAD(n, 2, '0')) AS inpatient_diagnosis,
  CASE WHEN n IN (28, 29, 30) THEN '症状缓解出院' ELSE NULL END AS discharge_diagnosis,
  CASE WHEN n <= 24 THEN MOD(n - 1, 10) + 1 ELSE NULL END AS ward_id,
  CONCAT('1390002', LPAD(n, 4, '0')) AS phone,
  CASE WHEN n IN (28, 29, 30) THEN 0 ELSE 1 END AS status,
  CASE
    WHEN n IN (28, 29, 30) THEN DATE_ADD('2026-04-16 15:00:00', INTERVAL n MINUTE)
    ELSE NULL
  END AS discharge_time
FROM tmp_seq_30;

-- ============================================================================
-- 4) 测量记录（30条）
-- ============================================================================

INSERT INTO sys_ecg_measure_record (
  measure_id, patient_id, measure_type, equipment_id, start_time, end_time, total_duration,
  wave_data_path, measure_point, measurer, ward_id, measure_status
)
SELECT
  n AS measure_id,
  n AS patient_id,
  CASE
    WHEN n <= 24 AND MOD(n, 3) = 0 THEN '动态心电'
    WHEN n <= 24 THEN '常规心电'
    ELSE '门诊心电'
  END AS measure_type,
  CASE
    WHEN n <= 24 THEN 100 + MOD(n - 1, 10) + 1
    WHEN MOD(n, 2) = 0 THEN 111
    ELSE 112
  END AS equipment_id,
  DATE_ADD('2026-04-16 07:30:00', INTERVAL (n - 1) * 7 MINUTE) AS start_time,
  DATE_ADD(
    '2026-04-16 07:30:00',
    INTERVAL ((n - 1) * 7 +
      CASE
        WHEN n <= 24 AND MOD(n, 3) = 0 THEN 35
        WHEN n <= 24 THEN 20
        ELSE 15
      END) MINUTE
  ) AS end_time,
  CASE
    WHEN n <= 24 AND MOD(n, 3) = 0 THEN '00:35:00'
    WHEN n <= 24 THEN '00:20:00'
    ELSE '00:15:00'
  END AS total_duration,
  CONCAT('/data/ecg/m', LPAD(n, 4, '0'), '.dat') AS wave_data_path,
  CASE
    WHEN n <= 8 THEN '晨间'
    WHEN n <= 20 THEN '上午'
    WHEN n <= 24 THEN '下午'
    ELSE '门诊'
  END AS measure_point,
  CASE
    WHEN n <= 24 THEN CONCAT('护士', LPAD(MOD(n - 1, 10) + 1, 2, '0'))
    ELSE '门诊护士'
  END AS measurer,
  CASE WHEN n <= 24 THEN MOD(n - 1, 10) + 1 ELSE NULL END AS ward_id,
  '已完成' AS measure_status
FROM tmp_seq_30;

-- ============================================================================
-- 5) 核心指标（30条）
-- ============================================================================

INSERT INTO sys_ecg_core_index (
  index_id, measure_id, heartbeat_total, avg_heart_rate, min_heart_rate, max_heart_rate,
  max_rr_interval, max_rr_count, tachycardia_times, bradycardia_times,
  atrial_premature, ventricular_premature, rhythm_type, warning_status
)
SELECT
  n AS index_id,
  n AS measure_id,
  1800 + n * 40 AS heartbeat_total,
  ROUND(58 + MOD(n * 7, 68), 1) AS avg_heart_rate,
  40 + MOD(n * 3, 35) AS min_heart_rate,
  90 + MOD(n * 5, 65) AS max_heart_rate,
  ROUND(0.70 + MOD(n, 6) * 0.19, 3) AS max_rr_interval,
  MOD(n, 4) AS max_rr_count,
  CASE WHEN (58 + MOD(n * 7, 68)) > 100 THEN 2 ELSE 0 END AS tachycardia_times,
  CASE WHEN (40 + MOD(n * 3, 35)) < 55 THEN 1 ELSE 0 END AS bradycardia_times,
  MOD(n * 2, 18) AS atrial_premature,
  MOD(n * 3, 14) AS ventricular_premature,
  CASE
    WHEN MOD(n, 5) = 0 THEN '室性早搏'
    WHEN MOD(n, 4) = 0 THEN '窦性心动过速'
    WHEN MOD(n, 3) = 0 THEN '窦性心动过缓'
    ELSE '窦性心律'
  END AS rhythm_type,
  CASE WHEN n <= 15 THEN 0 ELSE 1 END AS warning_status
FROM tmp_seq_30;

-- ============================================================================
-- 6) AI分析（30条）
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
  CASE
    WHEN n <= 15 THEN '疑似异常心律'
    ELSE '心律基本平稳'
  END AS ai_diagnosis,
  CASE
    WHEN n <= 15 AND MOD(n, 2) = 0 THEN 'HR_HIGH,ARR'
    WHEN n <= 15 THEN 'ARR'
    ELSE 'NORMAL'
  END AS abnormal_index,
  ROUND(82 + MOD(n * 3, 18) + MOD(n, 10) * 0.1, 2) AS confidence,
  DATE_ADD('2026-04-16 07:35:00', INTERVAL (n - 1) * 7 MINUTE) AS analysis_time,
  CASE WHEN MOD(n, 2) = 0 THEN 1 ELSE 0 END AS is_verified
FROM tmp_seq_30;

-- ============================================================================
-- 7) 异常预警（30条）
--    初始化状态：全部待管(0)
-- ============================================================================

INSERT INTO sys_ecg_abnormal_warning (
  warning_id, measure_id, patient_id, warning_time, warning_type, warning_level,
  warning_content, handle_doctor_id, handle_time, handle_result, warning_status
)
SELECT
  n AS warning_id,
  n AS measure_id,
  n AS patient_id,
  DATE_ADD('2026-04-16 08:20:00', INTERVAL (n - 1) * 8 MINUTE) AS warning_time,
  CASE MOD(n, 4)
    WHEN 1 THEN 'HR_HIGH'
    WHEN 2 THEN 'HR_LOW'
    WHEN 3 THEN 'ARR'
    ELSE 'ST'
  END AS warning_type,
  CASE
    WHEN n <= 15 AND MOD(n, 5) = 0 THEN '危急'
    WHEN n <= 15 THEN '异常'
    WHEN MOD(n, 4) = 0 THEN '中度'
    ELSE '异常'
  END AS warning_level,
  CONCAT('自动生成预警-', LPAD(n, 2, '0'), '，请复核') AS warning_content,
  NULL AS handle_doctor_id,
  NULL AS handle_time,
  NULL AS handle_result,
  '0' AS warning_status
FROM tmp_seq_30;

-- ============================================================================
-- 8) 报告（30条）
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
  CONCAT('REP20260416', LPAD(n, 4, '0')) AS report_no,
  CONCAT('自动生成报告内容-', LPAD(n, 2, '0')) AS report_content,
  CASE
    WHEN n <= 15 THEN '异常心律待干预'
    ELSE '心律相对平稳'
  END AS diagnosis_result,
  MOD(n - 1, 10) + 1 AS create_doctor_id,
  CASE WHEN n <= 15 THEN MOD(n, 10) + 1 ELSE NULL END AS review_doctor_id,
  DATE_ADD('2026-04-16 09:00:00', INTERVAL n * 9 MINUTE) AS create_time,
  CASE
    WHEN n <= 15 THEN DATE_ADD('2026-04-16 09:15:00', INTERVAL n * 9 MINUTE)
    ELSE NULL
  END AS review_time,
  CASE
    WHEN n <= 15 THEN '已审核'
    WHEN n <= 24 THEN '待审核'
    ELSE '草稿'
  END AS report_status,
  CONCAT('/report/rep', LPAD(n, 4, '0'), '.pdf') AS report_path
FROM tmp_seq_30;

-- ============================================================================
-- 9) 随访（30条）
-- ============================================================================

INSERT INTO sys_ecg_follow_up (
  follow_id, patient_id, measure_id, follow_time, follow_type, follow_doctor_id,
  patient_condition, follow_advice, next_follow_time, follow_remark
)
SELECT
  n AS follow_id,
  n AS patient_id,
  n AS measure_id,
  DATE_ADD('2026-04-16 09:30:00', INTERVAL n * 10 MINUTE) AS follow_time,
  CASE WHEN n <= 24 THEN '病房查房' ELSE '门诊随访' END AS follow_type,
  MOD(n - 1, 10) + 1 AS follow_doctor_id,
  CASE
    WHEN n <= 15 THEN '存在心电异常，需持续监测'
    ELSE '症状相对平稳'
  END AS patient_condition,
  CASE
    WHEN n <= 15 THEN '建议24小时内复测并调整治疗方案'
    ELSE '建议规律作息并按时复诊'
  END AS follow_advice,
  DATE_ADD('2026-04-17 09:30:00', INTERVAL n * 10 MINUTE) AS next_follow_time,
  CONCAT('随访记录-', LPAD(n, 2, '0')) AS follow_remark
FROM tmp_seq_30;

-- ============================================================================
-- 10) 清理临时表
-- ============================================================================
DROP TEMPORARY TABLE IF EXISTS tmp_seq_30;
DROP TEMPORARY TABLE IF EXISTS tmp_seq_60;

-- ============================================================================
-- 完成
-- ============================================================================
-- 执行后可得到：
-- 1) 10间病房，每间6床，共60床
-- 2) 30条完整业务链路数据
-- 3) 人员分散入住，房间床位数据完整
