import re

with open(r'C:\Users\aaa\workspace\cosmic\ecg_intelligence\src\main\java\com\hnkjzy\ecg\mapper\ApiSpecMapper.java', 'r', encoding='utf-8') as f:
    content = f.read()

old_query = '''    @Select("""
        SELECT
            LPAD(MOD(CAST(p.bed_no AS UNSIGNED), 100), 2, '0') AS bed,
            DATE_FORMAT(COALESCE(m.start_time, m.create_time), '%Y-%m-%d %H:%i:%s') AS time,
            p.patient_name AS name,
            CONCAT(DATEDIFF(CURDATE(), p.inpatient_date), '天') AS day,
            IFNULL(w.warning_content, '监测中') AS condition,
            CASE
                WHEN w.warning_id IS NULL THEN '观察中'
                WHEN IFNULL(w.warning_level, '') IN ('', '正常') THEN '稳定'
                ELSE '监护中'
            END AS status,
            IFNULL(f.follow_advice, '常规治疗') AS medication,
            DATEDIFF(CURDATE(), p.inpatient_date) AS hospital_days,
            FALSE AS is_empty
        FROM sys_ecg_patient_info p
        LEFT JOIN sys_ecg_measure_record m ON m.measure_id = (
            SELECT mm.measure_id
            FROM sys_ecg_measure_record mm
            WHERE mm.patient_id = p.patient_id
            ORDER BY COALESCE(mm.start_time, mm.create_time) DESC, mm.measure_id DESC
            LIMIT 1
        )
        LEFT JOIN sys_ecg_abnormal_warning w ON w.warning_id = (
            SELECT ww.warning_id
            FROM sys_ecg_abnormal_warning ww
            WHERE ww.patient_id = p.patient_id
            ORDER BY ww.warning_time DESC, ww.warning_id DESC
            LIMIT 1
        )
        LEFT JOIN sys_ecg_follow_up f ON f.follow_id = (
            SELECT ff.follow_id
            FROM sys_ecg_follow_up ff
            WHERE ff.patient_id = p.patient_id
            ORDER BY ff.follow_time DESC, ff.follow_id DESC
            LIMIT 1
        )
        WHERE p.bed_no REGEXP '^[0-9]+$'
                    AND CAST(FLOOR(CAST(p.bed_no AS UNSIGNED) / 100) * 100 + 1 AS UNSIGNED) = #{roomId}
        ORDER BY CAST(p.bed_no AS UNSIGNED)
        """)
    List<BloodGlucoseVo.RoomPatientItem> listRoomPatients(@Param("roomId") Long roomId);'''

new_query = '''    @Select("""
        SELECT
            b.bed_no AS bed,
            DATE_FORMAT(COALESCE(m.start_time, m.create_time), '%Y-%m-%d %H:%i:%s') AS time,
            p.patient_name AS name,
            CONCAT(DATEDIFF(CURDATE(), p.inpatient_date), '天') AS day,
            IFNULL(w.warning_content, '监测中') AS condition,
            CASE
                WHEN p.patient_id IS NULL THEN ''
                WHEN w.warning_id IS NULL THEN '观察中'
                WHEN IFNULL(w.warning_level, '') IN ('', '正常') THEN '稳定'
                ELSE '监护中'
            END AS status,
            IFNULL(f.follow_advice, '常规治疗') AS medication,
            DATEDIFF(CURDATE(), p.inpatient_date) AS hospital_days,
            CASE WHEN IFNULL(b.status, 0) = 0 THEN TRUE ELSE FALSE END AS is_empty
        FROM bed_info b
        LEFT JOIN sys_ecg_patient_info p ON b.patient_id = p.patient_id
        LEFT JOIN sys_ecg_measure_record m ON m.measure_id = (
            SELECT mm.measure_id
            FROM sys_ecg_measure_record mm
            WHERE mm.patient_id = p.patient_id
            ORDER BY COALESCE(mm.start_time, mm.create_time) DESC, mm.measure_id DESC
            LIMIT 1
        )
        LEFT JOIN sys_ecg_abnormal_warning w ON w.warning_id = (
            SELECT ww.warning_id
            FROM sys_ecg_abnormal_warning ww
            WHERE ww.patient_id = p.patient_id
            ORDER BY ww.warning_time DESC, ww.warning_id DESC
            LIMIT 1
        )
        LEFT JOIN sys_ecg_follow_up f ON f.follow_id = (
            SELECT ff.follow_id
            FROM sys_ecg_follow_up ff
            WHERE ff.patient_id = p.patient_id
            ORDER BY ff.follow_time DESC, ff.follow_id DESC
            LIMIT 1
        )
        WHERE b.ward_id = #{roomId}
        ORDER BY b.bed_id
        """)
    List<BloodGlucoseVo.RoomPatientItem> listRoomPatients(@Param("roomId") Long roomId);'''

if old_query in content:
    content = content.replace(old_query, new_query)
    with open(r'C:\Users\aaa\workspace\cosmic\ecg_intelligence\src\main\java\com\hnkjzy\ecg\mapper\ApiSpecMapper.java', 'w', encoding='utf-8') as f:
        f.write(content)
    print("Replaced successfully")
else:
    print("Not found")
