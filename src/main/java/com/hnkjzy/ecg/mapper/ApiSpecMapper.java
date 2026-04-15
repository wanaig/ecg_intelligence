package com.hnkjzy.ecg.mapper;

import com.hnkjzy.ecg.model.WarningQueryRow;
import com.hnkjzy.ecg.model.WaveformMetricRow;
import com.hnkjzy.ecg.vo.AnalysisVo;
import com.hnkjzy.ecg.vo.BloodGlucoseVo;
import com.hnkjzy.ecg.vo.QualityVo;
import com.hnkjzy.ecg.vo.SupplierVo;
import com.hnkjzy.ecg.vo.SystemVo;
import com.hnkjzy.ecg.vo.WorkbenchVo;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ApiSpecMapper {

    @Select("""
        <script>
        SELECT
            w.warning_id,
            w.warning_time,
            p.patient_name,
            p.patient_gender,
            p.patient_age,
            p.inpatient_no AS admission_no,
            p.bed_no,
            p.inpatient_date AS admission_date,
            w.warning_content AS details_text,
            w.warning_level,
            w.warning_status,
            p.ward_id,
            wd.ward_name,
            wd.ward_phone,
            COALESCE(dict.type_code, w.warning_type) AS index_code,
            COALESCE(dict.type_name, w.warning_type) AS index_name
        FROM sys_ecg_abnormal_warning w
        JOIN sys_ecg_patient_info p ON p.patient_id = w.patient_id
        LEFT JOIN sys_ecg_ward_info wd ON wd.ward_id = p.ward_id
        LEFT JOIN sys_dict_warning_type dict ON dict.type_name = w.warning_type OR dict.type_code = w.warning_type
        <where>
            <if test='status != null and status != "" and status != "ALL"'>
                AND (
                    (#{status} = '危急' AND IFNULL(w.warning_level, '') IN ('危急', '紧急', '重度'))
                    OR (#{status} = '异常' AND IFNULL(w.warning_level, '') NOT IN ('', '正常'))
                    OR (#{status} IN ('1', '纳入') AND IFNULL(w.warning_status, '0') IN ('1', '已纳入'))
                    OR (#{status} IN ('0', '不纳入') AND IFNULL(w.warning_status, '0') NOT IN ('1', '已纳入'))
                    OR (#{status} NOT IN ('危急', '异常', '1', '0', '纳入', '不纳入')
                        AND (w.warning_level = #{status} OR w.warning_status = #{status}))
                )
            </if>
            <if test='wardId != null'>
                AND p.ward_id = #{wardId}
            </if>
            <if test='patientKeyword != null and patientKeyword != ""'>
                AND (p.patient_name LIKE CONCAT('%', #{patientKeyword}, '%') OR p.inpatient_no LIKE CONCAT('%', #{patientKeyword}, '%'))
            </if>
            <if test='indexName != null and indexName != ""'>
                AND (w.warning_type LIKE CONCAT('%', #{indexName}, '%') OR dict.type_name LIKE CONCAT('%', #{indexName}, '%'))
            </if>
            <if test='startDate != null and startDate != ""'>
                AND DATE(w.warning_time) &gt;= #{startDate}
            </if>
            <if test='endDate != null and endDate != ""'>
                AND DATE(w.warning_time) &lt;= #{endDate}
            </if>
            <if test='excludeWard != null and excludeWard != ""'>
                AND wd.ward_name NOT LIKE CONCAT('%', #{excludeWard}, '%')
            </if>
        </where>
        ORDER BY w.warning_time DESC, w.warning_id DESC
        LIMIT #{offset}, #{size}
        </script>
        """)
    List<WarningQueryRow> listWarningRows(@Param("status") String status,
                                          @Param("wardId") Long wardId,
                                          @Param("patientKeyword") String patientKeyword,
                                          @Param("indexName") String indexName,
                                          @Param("startDate") String startDate,
                                          @Param("endDate") String endDate,
                                          @Param("excludeWard") String excludeWard,
                                          @Param("offset") int offset,
                                          @Param("size") int size);

    @Select("""
        <script>
        SELECT COUNT(*)
        FROM sys_ecg_abnormal_warning w
        JOIN sys_ecg_patient_info p ON p.patient_id = w.patient_id
        LEFT JOIN sys_ecg_ward_info wd ON wd.ward_id = p.ward_id
        LEFT JOIN sys_dict_warning_type dict ON dict.type_name = w.warning_type OR dict.type_code = w.warning_type
        <where>
            <if test='status != null and status != "" and status != "ALL"'>
                AND (
                    (#{status} = '危急' AND IFNULL(w.warning_level, '') IN ('危急', '紧急', '重度'))
                    OR (#{status} = '异常' AND IFNULL(w.warning_level, '') NOT IN ('', '正常'))
                    OR (#{status} IN ('1', '纳入') AND IFNULL(w.warning_status, '0') IN ('1', '已纳入'))
                    OR (#{status} IN ('0', '不纳入') AND IFNULL(w.warning_status, '0') NOT IN ('1', '已纳入'))
                    OR (#{status} NOT IN ('危急', '异常', '1', '0', '纳入', '不纳入')
                        AND (w.warning_level = #{status} OR w.warning_status = #{status}))
                )
            </if>
            <if test='wardId != null'>
                AND p.ward_id = #{wardId}
            </if>
            <if test='patientKeyword != null and patientKeyword != ""'>
                AND (p.patient_name LIKE CONCAT('%', #{patientKeyword}, '%') OR p.inpatient_no LIKE CONCAT('%', #{patientKeyword}, '%'))
            </if>
            <if test='indexName != null and indexName != ""'>
                AND (w.warning_type LIKE CONCAT('%', #{indexName}, '%') OR dict.type_name LIKE CONCAT('%', #{indexName}, '%'))
            </if>
            <if test='startDate != null and startDate != ""'>
                AND DATE(w.warning_time) &gt;= #{startDate}
            </if>
            <if test='endDate != null and endDate != ""'>
                AND DATE(w.warning_time) &lt;= #{endDate}
            </if>
            <if test='excludeWard != null and excludeWard != ""'>
                AND wd.ward_name NOT LIKE CONCAT('%', #{excludeWard}, '%')
            </if>
        </where>
        </script>
        """)
    long countWarningRows(@Param("status") String status,
                          @Param("wardId") Long wardId,
                          @Param("patientKeyword") String patientKeyword,
                          @Param("indexName") String indexName,
                          @Param("startDate") String startDate,
                          @Param("endDate") String endDate,
                          @Param("excludeWard") String excludeWard);

    @Update("""
        UPDATE sys_ecg_abnormal_warning
        SET handle_doctor_id = #{operatorId},
            handle_result = #{reason},
            handle_time = NOW(),
            warning_status = #{warningStatus}
        WHERE warning_id = #{warningId}
        """)
    int updateWarningAction(@Param("warningId") Long warningId,
                            @Param("operatorId") Long operatorId,
                            @Param("reason") String reason,
                            @Param("warningStatus") String warningStatus);

    @Update("""
        UPDATE sys_ecg_abnormal_warning
        SET warning_status = #{managedStatus},
            handle_doctor_id = COALESCE(#{operatorId}, handle_doctor_id),
            handle_result = CASE
                WHEN #{reason} IS NULL OR #{reason} = '' THEN handle_result
                ELSE #{reason}
            END,
            handle_time = NOW()
        WHERE warning_id = (
            SELECT t.warning_id
            FROM (
                SELECT w.warning_id
                FROM sys_ecg_abnormal_warning w
                JOIN sys_ecg_patient_info p ON p.patient_id = w.patient_id
                WHERE p.inpatient_no = #{patientId}
                   OR CAST(p.patient_id AS CHAR) = #{patientId}
                ORDER BY w.warning_time DESC, w.warning_id DESC
                LIMIT 1
            ) t
        )
        """)
    int updateLatestPatientManageStatus(@Param("patientId") String patientId,
                                        @Param("managedStatus") String managedStatus,
                                        @Param("operatorId") Long operatorId,
                                        @Param("reason") String reason);

    @Delete("""
        DELETE FROM sys_ecg_abnormal_warning
        WHERE warning_id = (
            SELECT t.warning_id
            FROM (
                SELECT w.warning_id
                FROM sys_ecg_abnormal_warning w
                JOIN sys_ecg_patient_info p ON p.patient_id = w.patient_id
                WHERE (p.inpatient_no = #{patientId} OR CAST(p.patient_id AS CHAR) = #{patientId})
                  AND IFNULL(w.warning_status, '0') IN ('0', '未纳入')
                ORDER BY w.warning_time DESC, w.warning_id DESC
                LIMIT 1
            ) t
        )
        """)
    int deleteLatestUnmanagedWarning(@Param("patientId") String patientId);

    @Select("""
        SELECT CASE
            WHEN IFNULL(w.warning_status, '0') IN ('1', '已纳入') THEN 1
            ELSE 0
        END
        FROM sys_ecg_abnormal_warning w
        JOIN sys_ecg_patient_info p ON p.patient_id = w.patient_id
        WHERE p.inpatient_no = #{patientId}
           OR CAST(p.patient_id AS CHAR) = #{patientId}
        ORDER BY w.warning_time DESC, w.warning_id DESC
        LIMIT 1
        """)
    Integer selectLatestPatientManagedFlag(@Param("patientId") String patientId);

    @Select("""
        <script>
        SELECT
            p.patient_id AS id,
            p.inpatient_no AS patient_id,
            p.patient_name AS name,
            p.bed_no,
            DATE_FORMAT(COALESCE(m.start_time, m.create_time), '%Y-%m-%d %H:%i:%s') AS measure_time,
            ci.avg_heart_rate AS heart_rate,
            ROUND(IFNULL(ci.hrv_lf_hf, 0), 2) AS st_segment,
            ROUND(IFNULL(ci.max_rr_interval, 0) * 1000, 1) AS qt,
            ROUND(IFNULL(ci.hrv_sdnn, 0), 1) AS qrs,
            CASE
                 WHEN w.warning_id IS NOT NULL
                     AND IFNULL(w.warning_level, '') NOT IN ('', '正常') THEN 'abnormal'
                ELSE 'stable'
            END AS status,
            IFNULL(ai.ai_diagnosis, '暂无AI结论') AS `desc`
        FROM sys_ecg_patient_info p
        LEFT JOIN sys_ecg_measure_record m ON m.measure_id = (
            SELECT mm.measure_id
            FROM sys_ecg_measure_record mm
            WHERE mm.patient_id = p.patient_id
            ORDER BY COALESCE(mm.start_time, mm.create_time) DESC, mm.measure_id DESC
            LIMIT 1
        )
        LEFT JOIN sys_ecg_core_index ci ON ci.measure_id = m.measure_id
        LEFT JOIN sys_ecg_ai_analysis ai ON ai.measure_id = m.measure_id
        LEFT JOIN sys_ecg_abnormal_warning w ON w.warning_id = (
            SELECT ww.warning_id
            FROM sys_ecg_abnormal_warning ww
            WHERE ww.patient_id = p.patient_id
            ORDER BY ww.warning_time DESC, ww.warning_id DESC
            LIMIT 1
        )
        <where>
            <if test='name != null and name != ""'>
                AND p.patient_name LIKE CONCAT('%', #{name}, '%')
            </if>
            <if test='startDate != null and startDate != ""'>
                AND DATE(COALESCE(m.start_time, m.create_time)) &gt;= #{startDate}
            </if>
            <if test='endDate != null and endDate != ""'>
                AND DATE(COALESCE(m.start_time, m.create_time)) &lt;= #{endDate}
            </if>
            <if test='scope != null and scope != ""'>
                <if test='scope.equals("discharged")'>
                    AND (p.bed_no IS NULL OR p.bed_no = '')
                </if>
                <if test='scope.equals("measurement")'>
                    AND m.measure_id IS NOT NULL
                </if>
                <if test='scope.equals("managed")'>
                    AND IFNULL(w.warning_status, '0') IN ('1', '已纳入')
                    AND p.bed_no IS NOT NULL
                    AND p.bed_no != ''
                </if>
                <if test='scope.equals("unmanaged")'>
                    AND w.warning_id IS NOT NULL
                    AND IFNULL(w.warning_status, '0') IN ('0', '未纳入')
                    AND p.bed_no IS NOT NULL
                    AND p.bed_no != ''
                </if>
                <if test='scope.equals("abnormal")'>
                    AND w.warning_id IS NOT NULL
                    AND IFNULL(w.warning_level, '') NOT IN ('', '正常')
                </if>
            </if>
            <if test='status != null and status != ""'>
                <if test='status.equals("abnormal")'>
                    AND w.warning_id IS NOT NULL
                    AND IFNULL(w.warning_level, '') NOT IN ('', '正常')
                </if>
                <if test='status.equals("stable")'>
                    AND (w.warning_id IS NULL OR IFNULL(w.warning_level, '') IN ('', '正常'))
                </if>
            </if>
        </where>
        ORDER BY COALESCE(m.start_time, m.create_time) DESC, p.patient_id DESC
        LIMIT #{offset}, #{size}
        </script>
        """)
    List<BloodGlucoseVo.PatientMetricItem> listPatientMetrics(@Param("scope") String scope,
                                                               @Param("name") String name,
                                                               @Param("status") String status,
                                                               @Param("startDate") String startDate,
                                                               @Param("endDate") String endDate,
                                                               @Param("offset") int offset,
                                                               @Param("size") int size);

    @Select("""
        <script>
        SELECT COUNT(*)
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
        <where>
            <if test='name != null and name != ""'>
                AND p.patient_name LIKE CONCAT('%', #{name}, '%')
            </if>
            <if test='startDate != null and startDate != ""'>
                AND DATE(COALESCE(m.start_time, m.create_time)) &gt;= #{startDate}
            </if>
            <if test='endDate != null and endDate != ""'>
                AND DATE(COALESCE(m.start_time, m.create_time)) &lt;= #{endDate}
            </if>
            <if test='scope != null and scope != ""'>
                <if test='scope.equals("discharged")'>
                    AND (p.bed_no IS NULL OR p.bed_no = '')
                </if>
                <if test='scope.equals("measurement")'>
                    AND m.measure_id IS NOT NULL
                </if>
                <if test='scope.equals("managed")'>
                    AND IFNULL(w.warning_status, '0') IN ('1', '已纳入')
                    AND p.bed_no IS NOT NULL
                    AND p.bed_no != ''
                </if>
                <if test='scope.equals("unmanaged")'>
                    AND w.warning_id IS NOT NULL
                    AND IFNULL(w.warning_status, '0') IN ('0', '未纳入')
                    AND p.bed_no IS NOT NULL
                    AND p.bed_no != ''
                </if>
                <if test='scope.equals("abnormal")'>
                    AND w.warning_id IS NOT NULL
                    AND IFNULL(w.warning_level, '') NOT IN ('', '正常')
                </if>
            </if>
            <if test='status != null and status != ""'>
                <if test='status.equals("abnormal")'>
                    AND w.warning_id IS NOT NULL
                    AND IFNULL(w.warning_level, '') NOT IN ('', '正常')
                </if>
                <if test='status.equals("stable")'>
                    AND (w.warning_id IS NULL OR IFNULL(w.warning_level, '') IN ('', '正常'))
                </if>
            </if>
        </where>
        </script>
        """)
    long countPatientMetrics(@Param("scope") String scope,
                             @Param("name") String name,
                             @Param("status") String status,
                             @Param("startDate") String startDate,
                             @Param("endDate") String endDate);

    @Select("""
        <script>
        SELECT
            p.inpatient_no AS patient_id,
            m.measure_id,
            250 AS sample_rate,
            ci.avg_heart_rate AS heart_rate,
            ROUND(IFNULL(ci.hrv_lf_hf, 0), 2) AS st_segment,
            ROUND(IFNULL(ci.max_rr_interval, 0) * 1000, 1) AS qt,
            ROUND(IFNULL(ci.hrv_sdnn, 0), 1) AS qrs
        FROM sys_ecg_patient_info p
        LEFT JOIN sys_ecg_measure_record m ON m.patient_id = p.patient_id
        LEFT JOIN sys_ecg_core_index ci ON ci.measure_id = m.measure_id
        <where>
            (CAST(p.patient_id AS CHAR) = #{patientId} OR p.inpatient_no = #{patientId})
            <if test='measureId != null'>
                AND m.measure_id = #{measureId}
            </if>
        </where>
        ORDER BY COALESCE(m.start_time, m.create_time) DESC, m.measure_id DESC
        LIMIT 1
        </script>
        """)
    WaveformMetricRow selectWaveformMetric(@Param("patientId") String patientId,
                                           @Param("measureId") Long measureId);

    @Select("""
        <script>
        SELECT
            wd.ward_name AS ward,
            p.bed_no,
            CONCAT(p.patient_name, ' / ', p.patient_gender, ' / ', p.patient_age, '岁') AS patient_info,
            CASE
                WHEN IFNULL(w.warning_level, '') IN ('重度', '紧急', '危急') THEN '重点关注'
                WHEN IFNULL(w.warning_level, '') IN ('中度') THEN '一般关注'
                ELSE '普通'
            END AS tag,
            p.inpatient_no AS admission_no,
            DATE_FORMAT(p.inpatient_date, '%Y-%m-%d') AS admission_date,
            p.inpatient_diagnosis AS diagnosis,
            CASE
                WHEN IFNULL(w.warning_status, '0') IN ('1', '已纳入') THEN '1'
                ELSE '0'
            END AS group_status,
            CASE
                WHEN p.bed_no IS NULL OR p.bed_no = '' THEN '离院'
                ELSE '在院'
            END AS hospital_status
        FROM sys_ecg_patient_info p
        LEFT JOIN sys_ecg_ward_info wd ON wd.ward_id = p.ward_id
        LEFT JOIN sys_ecg_abnormal_warning w ON w.warning_id = (
            SELECT ww.warning_id
            FROM sys_ecg_abnormal_warning ww
            WHERE ww.patient_id = p.patient_id
            ORDER BY ww.warning_time DESC, ww.warning_id DESC
            LIMIT 1
        )
        <where>
            <if test='ward != null and ward != ""'>
                AND wd.ward_name LIKE CONCAT('%', #{ward}, '%')
            </if>
            <if test='patient != null and patient != ""'>
                AND (p.patient_name LIKE CONCAT('%', #{patient}, '%') OR p.inpatient_no LIKE CONCAT('%', #{patient}, '%'))
            </if>
            <if test='startDate != null and startDate != ""'>
                AND p.inpatient_date &gt;= #{startDate}
            </if>
            <if test='endDate != null and endDate != ""'>
                AND p.inpatient_date &lt;= #{endDate}
            </if>
            <if test='groupStatus != null and groupStatus != ""'>
                AND (
                    CASE
                        WHEN IFNULL(w.warning_status, '0') IN ('1', '已纳入') THEN '1'
                        ELSE '0'
                    END
                ) = (
                    CASE
                        WHEN #{groupStatus} IN ('1', '已纳入') THEN '1'
                        WHEN #{groupStatus} IN ('0', '未纳入') THEN '0'
                        ELSE #{groupStatus}
                    END
                )
            </if>
            <if test='hospitalStatus != null and hospitalStatus != ""'>
                AND (
                    CASE
                        WHEN p.bed_no IS NULL OR p.bed_no = '' THEN '离院'
                        ELSE '在院'
                    END
                ) = #{hospitalStatus}
            </if>
            <if test='patientTag != null and patientTag != ""'>
                AND (
                    CASE
                        WHEN IFNULL(w.warning_level, '') IN ('重度', '紧急', '危急') THEN '重点关注'
                        WHEN IFNULL(w.warning_level, '') IN ('中度') THEN '一般关注'
                        ELSE '普通'
                    END
                ) = #{patientTag}
            </if>
        </where>
        ORDER BY p.patient_id DESC
        LIMIT #{offset}, #{size}
        </script>
        """)
    List<BloodGlucoseVo.PatientListItem> listPatients(@Param("ward") String ward,
                                                      @Param("patient") String patient,
                                                      @Param("patientTag") String patientTag,
                                                      @Param("groupStatus") String groupStatus,
                                                      @Param("hospitalStatus") String hospitalStatus,
                                                      @Param("startDate") String startDate,
                                                      @Param("endDate") String endDate,
                                                      @Param("offset") int offset,
                                                      @Param("size") int size);

    @Select("""
        <script>
        SELECT COUNT(*)
        FROM sys_ecg_patient_info p
        LEFT JOIN sys_ecg_ward_info wd ON wd.ward_id = p.ward_id
        LEFT JOIN sys_ecg_abnormal_warning w ON w.warning_id = (
            SELECT ww.warning_id
            FROM sys_ecg_abnormal_warning ww
            WHERE ww.patient_id = p.patient_id
            ORDER BY ww.warning_time DESC, ww.warning_id DESC
            LIMIT 1
        )
        <where>
            <if test='ward != null and ward != ""'>
                AND wd.ward_name LIKE CONCAT('%', #{ward}, '%')
            </if>
            <if test='patient != null and patient != ""'>
                AND (p.patient_name LIKE CONCAT('%', #{patient}, '%') OR p.inpatient_no LIKE CONCAT('%', #{patient}, '%'))
            </if>
            <if test='startDate != null and startDate != ""'>
                AND p.inpatient_date &gt;= #{startDate}
            </if>
            <if test='endDate != null and endDate != ""'>
                AND p.inpatient_date &lt;= #{endDate}
            </if>
            <if test='groupStatus != null and groupStatus != ""'>
                AND (
                    CASE
                        WHEN IFNULL(w.warning_status, '0') IN ('1', '已纳入') THEN '1'
                        ELSE '0'
                    END
                ) = (
                    CASE
                        WHEN #{groupStatus} IN ('1', '已纳入') THEN '1'
                        WHEN #{groupStatus} IN ('0', '未纳入') THEN '0'
                        ELSE #{groupStatus}
                    END
                )
            </if>
            <if test='hospitalStatus != null and hospitalStatus != ""'>
                AND (
                    CASE
                        WHEN p.bed_no IS NULL OR p.bed_no = '' THEN '离院'
                        ELSE '在院'
                    END
                ) = #{hospitalStatus}
            </if>
            <if test='patientTag != null and patientTag != ""'>
                AND (
                    CASE
                        WHEN IFNULL(w.warning_level, '') IN ('重度', '紧急', '危急') THEN '重点关注'
                        WHEN IFNULL(w.warning_level, '') IN ('中度') THEN '一般关注'
                        ELSE '普通'
                    END
                ) = #{patientTag}
            </if>
        </where>
        </script>
        """)
    long countPatients(@Param("ward") String ward,
                       @Param("patient") String patient,
                       @Param("patientTag") String patientTag,
                       @Param("groupStatus") String groupStatus,
                       @Param("hospitalStatus") String hospitalStatus,
                       @Param("startDate") String startDate,
                       @Param("endDate") String endDate);

    @Update("""
        UPDATE sys_ecg_patient_info
        SET bed_no = NULL
        WHERE inpatient_no = #{patientId} OR CAST(patient_id AS CHAR) = #{patientId}
        """)
    int clearPatientBed(@Param("patientId") String patientId);

    @Select("""
        <script>
        SELECT
            t.room_id AS id,
            CONCAT(t.room_id, '病房') AS name,
            (
                SELECT d.doctor_name
                FROM sys_ecg_doctor_info d
                WHERE d.ward_id = t.ward_id
                ORDER BY d.doctor_id
                LIMIT 1
            ) AS nurse,
            2 AS total_beds,
            t.occupied_beds
        FROM (
            SELECT
                p.ward_id,
                                CAST(FLOOR(CAST(p.bed_no AS UNSIGNED) / 100) * 100 + 1 AS UNSIGNED) AS room_id,
                COUNT(*) AS occupied_beds
            FROM sys_ecg_patient_info p
            WHERE p.bed_no IS NOT NULL
              AND p.bed_no != ''
              AND p.bed_no REGEXP '^[0-9]+$'
              <if test='wardId != null'>
                AND p.ward_id = #{wardId}
              </if>
                        GROUP BY p.ward_id, CAST(FLOOR(CAST(p.bed_no AS UNSIGNED) / 100) * 100 + 1 AS UNSIGNED)
        ) t
        ORDER BY t.room_id
        </script>
        """)
    List<BloodGlucoseVo.RoomItem> listRooms(@Param("wardId") Long wardId);

    @Select("""
        SELECT
            LPAD(MOD(CAST(p.bed_no AS UNSIGNED), 100), 2, '0') AS bed,
            DATE_FORMAT(COALESCE(m.start_time, m.create_time), '%Y-%m-%d %H:%i:%s') AS time,
            p.patient_name AS name,
            CONCAT(DATEDIFF(CURDATE(), p.inpatient_date), '天') AS day,
            IFNULL(w.warning_content, '监测中') AS `condition`,
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
    List<BloodGlucoseVo.RoomPatientItem> listRoomPatients(@Param("roomId") Long roomId);

    @Update("""
        UPDATE sys_ecg_patient_info
        SET bed_no = #{bedNo}
        WHERE inpatient_no = #{patientId} OR CAST(patient_id AS CHAR) = #{patientId}
        """)
    int assignBed(@Param("patientId") String patientId, @Param("bedNo") String bedNo);

        @Select("""
                SELECT COUNT(*)
                FROM sys_ecg_patient_info
                WHERE bed_no = #{bedNo}
                    AND NOT (inpatient_no = #{patientId} OR CAST(patient_id AS CHAR) = #{patientId})
                """)
        int countBedOccupiedByOthers(@Param("patientId") String patientId, @Param("bedNo") String bedNo);

    @Select("""
        SELECT
            wd.ward_name AS ward,
            COUNT(*) AS value
        FROM sys_ecg_abnormal_warning w
        JOIN sys_ecg_patient_info p ON p.patient_id = w.patient_id
        LEFT JOIN sys_ecg_ward_info wd ON wd.ward_id = p.ward_id
        GROUP BY p.ward_id, wd.ward_name
        ORDER BY COUNT(*) DESC
        LIMIT 10
        """)
    List<WorkbenchVo.WardWarningRankItem> listWardWarningRank();

    @Select("""
        <script>
        SELECT
            CONCAT(p.patient_name, ' / ', p.patient_gender, ' / ', p.patient_age, '岁') AS info,
            wd.ward_name AS ward,
            p.bed_no,
            p.inpatient_no,
            e.equipment_code AS device_no,
            DATE_FORMAT(COALESCE(m.start_time, m.create_time), '%Y-%m-%d %H:%i:%s') AS attach_time,
            DATE_FORMAT(COALESCE(m.end_time, m.create_time), '%Y-%m-%d %H:%i:%s') AS change_time,
            (
                SELECT COUNT(*)
                FROM sys_ecg_abnormal_warning ww
                WHERE ww.patient_id = p.patient_id
                AND IFNULL(ww.warning_level, '') NOT IN ('', '正常')
            ) AS abn_times,
            CAST(IFNULL(ci.max_heart_rate, 0) AS SIGNED) AS max_hr,
            (
                SELECT COUNT(*)
                FROM sys_ecg_abnormal_warning ww
                WHERE ww.patient_id = p.patient_id
                  AND ww.warning_type LIKE '%过缓%'
            ) AS low_bg_times
        FROM sys_ecg_patient_info p
        LEFT JOIN sys_ecg_ward_info wd ON wd.ward_id = p.ward_id
        LEFT JOIN sys_ecg_measure_record m ON m.measure_id = (
            SELECT mm.measure_id
            FROM sys_ecg_measure_record mm
            WHERE mm.patient_id = p.patient_id
            ORDER BY COALESCE(mm.start_time, mm.create_time) DESC, mm.measure_id DESC
            LIMIT 1
        )
        LEFT JOIN sys_ecg_equipment_info e ON e.equipment_id = m.equipment_id
        LEFT JOIN sys_ecg_core_index ci ON ci.measure_id = m.measure_id
        <where>
            <if test='ward != null and ward != ""'>
                AND wd.ward_name LIKE CONCAT('%', #{ward}, '%')
            </if>
            <if test='tab != null and tab.equals("abnormal")'>
                AND EXISTS (
                    SELECT 1
                    FROM sys_ecg_abnormal_warning ww
                    WHERE ww.patient_id = p.patient_id
                      AND IFNULL(ww.warning_level, '') NOT IN ('', '正常')
                )
            </if>
            <if test='tab != null and tab.equals("normal")'>
                AND NOT EXISTS (
                    SELECT 1
                    FROM sys_ecg_abnormal_warning ww
                    WHERE ww.patient_id = p.patient_id
                      AND IFNULL(ww.warning_level, '') NOT IN ('', '正常')
                )
            </if>
        </where>
        ORDER BY abn_times DESC, p.patient_id DESC
        LIMIT #{offset}, #{size}
        </script>
        """)
    List<WorkbenchVo.WorkbenchPatientItem> listWorkbenchPatients(@Param("ward") String ward,
                                                                 @Param("tab") String tab,
                                                                 @Param("offset") int offset,
                                                                 @Param("size") int size);

    @Select("""
        <script>
        SELECT COUNT(*)
        FROM sys_ecg_patient_info p
        LEFT JOIN sys_ecg_ward_info wd ON wd.ward_id = p.ward_id
        <where>
            <if test='ward != null and ward != ""'>
                AND wd.ward_name LIKE CONCAT('%', #{ward}, '%')
            </if>
            <if test='tab != null and tab.equals("abnormal")'>
                AND EXISTS (
                    SELECT 1
                    FROM sys_ecg_abnormal_warning ww
                    WHERE ww.patient_id = p.patient_id
                      AND IFNULL(ww.warning_level, '') NOT IN ('', '正常')
                )
            </if>
            <if test='tab != null and tab.equals("normal")'>
                AND NOT EXISTS (
                    SELECT 1
                    FROM sys_ecg_abnormal_warning ww
                    WHERE ww.patient_id = p.patient_id
                      AND IFNULL(ww.warning_level, '') NOT IN ('', '正常')
                )
            </if>
        </where>
        </script>
        """)
    long countWorkbenchPatients(@Param("ward") String ward, @Param("tab") String tab);

    @Select("""
        SELECT
            DATE_FORMAT(t.record_time, '%Y-%m-%d %H:%i:%s') AS record_time,
            t.event_type,
            t.detail,
            t.operator
        FROM (
            SELECT
                w.warning_time AS record_time,
                '预警' AS event_type,
                w.warning_content AS detail,
                COALESCE(d.doctor_name, '系统') AS operator
            FROM sys_ecg_abnormal_warning w
            JOIN sys_ecg_patient_info p ON p.patient_id = w.patient_id
            LEFT JOIN sys_ecg_doctor_info d ON d.doctor_id = w.handle_doctor_id
            WHERE p.inpatient_no = #{inpatientNo}
            UNION ALL
            SELECT
                f.follow_time AS record_time,
                '随访' AS event_type,
                CONCAT(IFNULL(f.patient_condition, ''), ' ', IFNULL(f.follow_advice, '')) AS detail,
                COALESCE(d2.doctor_name, '系统') AS operator
            FROM sys_ecg_follow_up f
            JOIN sys_ecg_patient_info p2 ON p2.patient_id = f.patient_id
            LEFT JOIN sys_ecg_doctor_info d2 ON d2.doctor_id = f.follow_doctor_id
            WHERE p2.inpatient_no = #{inpatientNo}
        ) t
        ORDER BY t.record_time DESC
        """)
    List<WorkbenchVo.PatientRecordItem> listPatientRecords(@Param("inpatientNo") String inpatientNo);

    @Select("""
        SELECT IFNULL(patient_gender, '未知') AS label, COUNT(*) AS value
        FROM sys_ecg_patient_info
        GROUP BY patient_gender
        ORDER BY value DESC
        """)
    List<AnalysisVo.NamedValue> listGenderDist();

    @Select("""
        SELECT
            CASE
                WHEN patient_age < 40 THEN '18-40'
                WHEN patient_age <= 60 THEN '41-60'
                ELSE '60+'
            END AS label,
            COUNT(*) AS value
        FROM sys_ecg_patient_info
        GROUP BY CASE
            WHEN patient_age < 40 THEN '18-40'
            WHEN patient_age <= 60 THEN '41-60'
            ELSE '60+'
        END
        ORDER BY value DESC
        """)
    List<AnalysisVo.NamedValue> listAgeDist();

    @Select("""
        SELECT wd.ward_name AS label, COUNT(*) AS value
        FROM sys_ecg_abnormal_warning w
        JOIN sys_ecg_patient_info p ON p.patient_id = w.patient_id
        LEFT JOIN sys_ecg_ward_info wd ON wd.ward_id = p.ward_id
        GROUP BY p.ward_id, wd.ward_name
        ORDER BY value DESC
        LIMIT 8
        """)
    List<AnalysisVo.NamedValue> listWarningWardTop();

    @Select("""
         SELECT '已纳入' AS label,
             SUM(CASE WHEN IFNULL(warning_status, '0') IN ('1', '已纳入') THEN 1 ELSE 0 END) AS value
        FROM sys_ecg_abnormal_warning
        UNION ALL
         SELECT '待纳入' AS label,
             SUM(CASE WHEN IFNULL(warning_status, '0') IN ('1', '已纳入') THEN 0 ELSE 1 END) AS value
        FROM sys_ecg_abnormal_warning
        """)
    List<AnalysisVo.NamedValue> listManagedOverview();

    @Select("""
        SELECT wd.ward_name AS label, COUNT(*) AS value
        FROM sys_ecg_measure_record m
        LEFT JOIN sys_ecg_ward_info wd ON wd.ward_id = m.ward_id
        GROUP BY m.ward_id, wd.ward_name
        ORDER BY value DESC
        LIMIT 8
        """)
    List<AnalysisVo.NamedValue> listActiveWards();

    @Select("""
        SELECT
            CASE WHEN ci.avg_heart_rate BETWEEN 60 AND 100 THEN '正常' ELSE '异常' END AS label,
            COUNT(*) AS value
        FROM sys_ecg_core_index ci
        GROUP BY CASE WHEN ci.avg_heart_rate BETWEEN 60 AND 100 THEN '正常' ELSE '异常' END
        """)
    List<AnalysisVo.NamedValue> listGlucoseDistManaged();

    @Select("""
        SELECT
            CASE WHEN w.warning_level IN ('正常') THEN '正常' ELSE '异常' END AS label,
            COUNT(*) AS value
        FROM sys_ecg_abnormal_warning w
        GROUP BY CASE WHEN w.warning_level IN ('正常') THEN '正常' ELSE '异常' END
        """)
    List<AnalysisVo.NamedValue> listGlucoseDistHospital();

    @Select("""
        SELECT wd.ward_name AS label, COUNT(*) AS value
        FROM sys_ecg_abnormal_warning w
        JOIN sys_ecg_patient_info p ON p.patient_id = w.patient_id
        LEFT JOIN sys_ecg_ward_info wd ON wd.ward_id = p.ward_id
        WHERE w.warning_level != '正常'
        GROUP BY p.ward_id, wd.ward_name
        ORDER BY value DESC
        LIMIT 8
        """)
    List<AnalysisVo.NamedValue> listAbnormalWardTop();

    @Select("""
        SELECT
            CONCAT(p.patient_name, ' / ', p.patient_gender, ' / ', p.patient_age, '岁') AS info,
            wd.ward_name AS dept,
            p.bed_no AS bed,
            w.warning_content AS abnormal
        FROM sys_ecg_abnormal_warning w
        JOIN sys_ecg_patient_info p ON p.patient_id = w.patient_id
        LEFT JOIN sys_ecg_ward_info wd ON wd.ward_id = p.ward_id
        WHERE w.warning_level IN ('重度', '紧急', '危急')
        ORDER BY w.warning_time DESC, w.warning_id DESC
        LIMIT 20
        """)
    List<AnalysisVo.PatientWarningItem> listCriticalWarnings();

    @Select("""
        SELECT
            CONCAT(p.patient_name, ' / ', p.patient_gender, ' / ', p.patient_age, '岁') AS info,
            wd.ward_name AS dept,
            p.bed_no AS bed,
            w.warning_content AS abnormal
        FROM sys_ecg_abnormal_warning w
        JOIN sys_ecg_patient_info p ON p.patient_id = w.patient_id
        LEFT JOIN sys_ecg_ward_info wd ON wd.ward_id = p.ward_id
        WHERE w.warning_level NOT IN ('正常', '重度', '紧急', '危急')
        ORDER BY w.warning_time DESC, w.warning_id DESC
        LIMIT 20
        """)
    List<AnalysisVo.PatientWarningItem> listAbnormalWarnings();

    @Select("""
        <script>
        SELECT
            t.patient_id,
            t.name,
            t.record_time,
            t.type,
            t.score,
            t.level,
            t.issues,
            t.status
        FROM (
            SELECT
                m.measure_id,
                p.inpatient_no AS patient_id,
                p.patient_name AS name,
                DATE_FORMAT(COALESCE(m.start_time, m.create_time), '%Y-%m-%d %H:%i:%s') AS record_time,
                IFNULL(m.measure_type, '动态心电') AS type,
                ROUND(
                    LEAST(100, GREATEST(60,
                        100 - IFNULL(ABS(ci.max_heart_rate - ci.min_heart_rate), 0) * 0.6
                          - IFNULL(CASE WHEN w.warning_level IN ('重度', '紧急', '危急') THEN 18
                                        WHEN w.warning_level = '中度' THEN 10
                                        WHEN w.warning_level = '轻度' THEN 4
                                        ELSE 0 END, 0)
                    )),
                1) AS score,
                CASE
                    WHEN ROUND(
                        LEAST(100, GREATEST(60,
                            100 - IFNULL(ABS(ci.max_heart_rate - ci.min_heart_rate), 0) * 0.6
                              - IFNULL(CASE WHEN w.warning_level IN ('重度', '紧急', '危急') THEN 18
                                            WHEN w.warning_level = '中度' THEN 10
                                            WHEN w.warning_level = '轻度' THEN 4
                                            ELSE 0 END, 0)
                        )),
                    1) &gt;= 90 THEN 'A'
                    WHEN ROUND(
                        LEAST(100, GREATEST(60,
                            100 - IFNULL(ABS(ci.max_heart_rate - ci.min_heart_rate), 0) * 0.6
                              - IFNULL(CASE WHEN w.warning_level IN ('重度', '紧急', '危急') THEN 18
                                            WHEN w.warning_level = '中度' THEN 10
                                            WHEN w.warning_level = '轻度' THEN 4
                                            ELSE 0 END, 0)
                        )),
                    1) &gt;= 80 THEN 'B'
                    ELSE 'C'
                END AS level,
                IFNULL(ai.abnormal_index, IFNULL(w.warning_content, '无')) AS issues,
                IFNULL(m.measure_status, '已完成') AS status
            FROM sys_ecg_measure_record m
            JOIN sys_ecg_patient_info p ON p.patient_id = m.patient_id
            LEFT JOIN sys_ecg_core_index ci ON ci.measure_id = m.measure_id
            LEFT JOIN sys_ecg_ai_analysis ai ON ai.measure_id = m.measure_id
            LEFT JOIN sys_ecg_abnormal_warning w ON w.warning_id = (
                SELECT ww.warning_id
                FROM sys_ecg_abnormal_warning ww
                WHERE ww.measure_id = m.measure_id
                ORDER BY ww.warning_time DESC, ww.warning_id DESC
                LIMIT 1
            )
        ) t
        <where>
            <if test='patientId != null and patientId != ""'>
                AND t.patient_id LIKE CONCAT('%', #{patientId}, '%')
            </if>
            <if test='qualityLevel != null and qualityLevel != ""'>
                AND t.level = #{qualityLevel}
            </if>
        </where>
        ORDER BY t.record_time DESC
        LIMIT #{offset}, #{size}
        </script>
        """)
    List<QualityVo.DataQualityItem> listDataQuality(@Param("patientId") String patientId,
                                                    @Param("qualityLevel") String qualityLevel,
                                                    @Param("offset") int offset,
                                                    @Param("size") int size);

    @Select("""
        <script>
        SELECT COUNT(*)
        FROM (
            SELECT
                m.measure_id,
                p.inpatient_no AS patient_id,
                CASE
                    WHEN ROUND(
                        LEAST(100, GREATEST(60,
                            100 - IFNULL(ABS(ci.max_heart_rate - ci.min_heart_rate), 0) * 0.6
                              - IFNULL(CASE WHEN w.warning_level IN ('重度', '紧急', '危急') THEN 18
                                            WHEN w.warning_level = '中度' THEN 10
                                            WHEN w.warning_level = '轻度' THEN 4
                                            ELSE 0 END, 0)
                        )),
                    1) &gt;= 90 THEN 'A'
                    WHEN ROUND(
                        LEAST(100, GREATEST(60,
                            100 - IFNULL(ABS(ci.max_heart_rate - ci.min_heart_rate), 0) * 0.6
                              - IFNULL(CASE WHEN w.warning_level IN ('重度', '紧急', '危急') THEN 18
                                            WHEN w.warning_level = '中度' THEN 10
                                            WHEN w.warning_level = '轻度' THEN 4
                                            ELSE 0 END, 0)
                        )),
                    1) &gt;= 80 THEN 'B'
                    ELSE 'C'
                END AS level
            FROM sys_ecg_measure_record m
            JOIN sys_ecg_patient_info p ON p.patient_id = m.patient_id
            LEFT JOIN sys_ecg_core_index ci ON ci.measure_id = m.measure_id
            LEFT JOIN sys_ecg_abnormal_warning w ON w.warning_id = (
                SELECT ww.warning_id
                FROM sys_ecg_abnormal_warning ww
                WHERE ww.measure_id = m.measure_id
                ORDER BY ww.warning_time DESC, ww.warning_id DESC
                LIMIT 1
            )
        ) t
        <where>
            <if test='patientId != null and patientId != ""'>
                AND t.patient_id LIKE CONCAT('%', #{patientId}, '%')
            </if>
            <if test='qualityLevel != null and qualityLevel != ""'>
                AND t.level = #{qualityLevel}
            </if>
        </where>
        </script>
        """)
    long countDataQuality(@Param("patientId") String patientId,
                          @Param("qualityLevel") String qualityLevel);

    @Select("""
        SELECT
            p.inpatient_no AS patient_id,
            p.patient_name AS name,
            DATE_FORMAT(COALESCE(m.start_time, m.create_time), '%Y-%m-%d %H:%i:%s') AS record_time,
            IFNULL(m.measure_type, '动态心电') AS type,
            ROUND(
                LEAST(100, GREATEST(60,
                    100 - IFNULL(ABS(ci.max_heart_rate - ci.min_heart_rate), 0) * 0.6
                      - IFNULL(CASE WHEN w.warning_level IN ('重度', '紧急', '危急') THEN 18
                                    WHEN w.warning_level = '中度' THEN 10
                                    WHEN w.warning_level = '轻度' THEN 4
                                    ELSE 0 END, 0)
                )),
            1) AS score,
            CASE
                WHEN ROUND(
                    LEAST(100, GREATEST(60,
                        100 - IFNULL(ABS(ci.max_heart_rate - ci.min_heart_rate), 0) * 0.6
                          - IFNULL(CASE WHEN w.warning_level IN ('重度', '紧急', '危急') THEN 18
                                        WHEN w.warning_level = '中度' THEN 10
                                        WHEN w.warning_level = '轻度' THEN 4
                                        ELSE 0 END, 0)
                    )),
                1) >= 90 THEN 'A'
                WHEN ROUND(
                    LEAST(100, GREATEST(60,
                        100 - IFNULL(ABS(ci.max_heart_rate - ci.min_heart_rate), 0) * 0.6
                          - IFNULL(CASE WHEN w.warning_level IN ('重度', '紧急', '危急') THEN 18
                                        WHEN w.warning_level = '中度' THEN 10
                                        WHEN w.warning_level = '轻度' THEN 4
                                        ELSE 0 END, 0)
                    )),
                1) >= 80 THEN 'B'
                ELSE 'C'
            END AS level,
            IFNULL(ai.abnormal_index, IFNULL(w.warning_content, '无')) AS issues,
            IFNULL(m.measure_status, '已完成') AS status
        FROM sys_ecg_measure_record m
        JOIN sys_ecg_patient_info p ON p.patient_id = m.patient_id
        LEFT JOIN sys_ecg_core_index ci ON ci.measure_id = m.measure_id
        LEFT JOIN sys_ecg_ai_analysis ai ON ai.measure_id = m.measure_id
        LEFT JOIN sys_ecg_abnormal_warning w ON w.warning_id = (
            SELECT ww.warning_id
            FROM sys_ecg_abnormal_warning ww
            WHERE ww.measure_id = m.measure_id
            ORDER BY ww.warning_time DESC, ww.warning_id DESC
            LIMIT 1
        )
        WHERE m.measure_id = #{id}
        LIMIT 1
        """)
    QualityVo.DataQualityItem selectDataQualityDetail(@Param("id") Long id);

    @Select("""
        <script>
        SELECT
            e.equipment_code AS mac,
            e.equipment_name AS model,
            wd.ward_name AS dept,
            DATE_FORMAT(COALESCE(e.maintain_time, e.install_time, NOW()), '%Y-%m-%d %H:%i:%s') AS check_time,
            DATE_FORMAT(COALESCE(e.next_maintain_time, DATE_ADD(NOW(), INTERVAL 30 DAY)), '%Y-%m-%d') AS next_check_time,
            ROUND(CASE WHEN e.equipment_status = '正常' THEN 98.0 ELSE 82.0 END, 1) AS pass_rate,
            e.equipment_status AS status
        FROM sys_ecg_equipment_info e
        LEFT JOIN sys_ecg_ward_info wd ON wd.ward_id = e.ward_id
        <where>
            <if test='mac != null and mac != ""'>
                AND e.equipment_code LIKE CONCAT('%', #{mac}, '%')
            </if>
            <if test='deviceStatus != null and deviceStatus != ""'>
                AND e.equipment_status = #{deviceStatus}
            </if>
        </where>
        ORDER BY e.equipment_id DESC
        LIMIT #{offset}, #{size}
        </script>
        """)
    List<QualityVo.DeviceQualityItem> listDeviceQuality(@Param("mac") String mac,
                                                        @Param("deviceStatus") String deviceStatus,
                                                        @Param("offset") int offset,
                                                        @Param("size") int size);

    @Select("""
        <script>
        SELECT COUNT(*)
        FROM sys_ecg_equipment_info e
        <where>
            <if test='mac != null and mac != ""'>
                AND e.equipment_code LIKE CONCAT('%', #{mac}, '%')
            </if>
            <if test='deviceStatus != null and deviceStatus != ""'>
                AND e.equipment_status = #{deviceStatus}
            </if>
        </where>
        </script>
        """)
    long countDeviceQuality(@Param("mac") String mac, @Param("deviceStatus") String deviceStatus);

    @Select("""
        SELECT
            '信号完整性' AS check_item,
            CASE WHEN e.equipment_status = '正常' THEN '通过' ELSE '待复检' END AS result,
            '系统巡检' AS operator,
            DATE_FORMAT(COALESCE(e.maintain_time, e.install_time, NOW()), '%Y-%m-%d %H:%i:%s') AS check_time
        FROM sys_ecg_equipment_info e
        WHERE e.equipment_id = #{id}
        UNION ALL
        SELECT
            '电池健康' AS check_item,
            CASE WHEN e.equipment_status = '正常' THEN '通过' ELSE '异常' END AS result,
            '系统巡检' AS operator,
            DATE_FORMAT(COALESCE(e.maintain_time, e.install_time, NOW()), '%Y-%m-%d %H:%i:%s') AS check_time
        FROM sys_ecg_equipment_info e
        WHERE e.equipment_id = #{id}
        """)
    List<QualityVo.DeviceRecordItem> listDeviceRecords(@Param("id") Long id);

    @Select("""
        <script>
        SELECT
            r.report_no AS report_id,
            p.patient_name AS patient,
            d1.doctor_name AS doctor,
            d2.doctor_name AS reviewer,
            ROUND(COALESCE(ai.confidence, 90.0), 1) AS score,
            r.report_status AS status,
            CASE
                WHEN r.diagnosis_result LIKE '%异常%' OR r.diagnosis_result LIKE '%改变%' THEN '诊断异常'
                ELSE '无'
            END AS error_type,
            DATE_FORMAT(r.create_time, '%Y-%m-%d %H:%i:%s') AS time
        FROM sys_ecg_report r
        JOIN sys_ecg_patient_info p ON p.patient_id = r.patient_id
        LEFT JOIN sys_ecg_doctor_info d1 ON d1.doctor_id = r.create_doctor_id
        LEFT JOIN sys_ecg_doctor_info d2 ON d2.doctor_id = r.review_doctor_id
        LEFT JOIN sys_ecg_ai_analysis ai ON ai.ai_analysis_id = r.ai_analysis_id
        <where>
            <if test='doctor != null and doctor != ""'>
                AND (d1.doctor_name LIKE CONCAT('%', #{doctor}, '%') OR d2.doctor_name LIKE CONCAT('%', #{doctor}, '%'))
            </if>
            <if test='resultType != null and resultType != ""'>
                AND r.report_status = #{resultType}
            </if>
        </where>
        ORDER BY r.create_time DESC, r.report_id DESC
        LIMIT #{offset}, #{size}
        </script>
        """)
    List<QualityVo.ReportQualityItem> listReportQuality(@Param("doctor") String doctor,
                                                        @Param("resultType") String resultType,
                                                        @Param("offset") int offset,
                                                        @Param("size") int size);

    @Select("""
        <script>
        SELECT COUNT(*)
        FROM sys_ecg_report r
        LEFT JOIN sys_ecg_doctor_info d1 ON d1.doctor_id = r.create_doctor_id
        LEFT JOIN sys_ecg_doctor_info d2 ON d2.doctor_id = r.review_doctor_id
        <where>
            <if test='doctor != null and doctor != ""'>
                AND (d1.doctor_name LIKE CONCAT('%', #{doctor}, '%') OR d2.doctor_name LIKE CONCAT('%', #{doctor}, '%'))
            </if>
            <if test='resultType != null and resultType != ""'>
                AND r.report_status = #{resultType}
            </if>
        </where>
        </script>
        """)
    long countReportQuality(@Param("doctor") String doctor, @Param("resultType") String resultType);

    @Select("""
        SELECT
            r.report_no AS report_id,
            p.patient_name AS patient,
            d1.doctor_name AS doctor,
            d2.doctor_name AS reviewer,
            ROUND(COALESCE(ai.confidence, 90.0), 1) AS score,
            r.report_status AS status,
            CASE
                WHEN r.diagnosis_result LIKE '%异常%' OR r.diagnosis_result LIKE '%改变%' THEN '诊断异常'
                ELSE '无'
            END AS error_type,
            DATE_FORMAT(r.create_time, '%Y-%m-%d %H:%i:%s') AS time
        FROM sys_ecg_report r
        JOIN sys_ecg_patient_info p ON p.patient_id = r.patient_id
        LEFT JOIN sys_ecg_doctor_info d1 ON d1.doctor_id = r.create_doctor_id
        LEFT JOIN sys_ecg_doctor_info d2 ON d2.doctor_id = r.review_doctor_id
        LEFT JOIN sys_ecg_ai_analysis ai ON ai.ai_analysis_id = r.ai_analysis_id
        WHERE r.report_no = #{reportId}
        LIMIT 1
        """)
    QualityVo.ReportQualityItem selectReportQualityByNo(@Param("reportId") String reportId);

    @Select("""
        SELECT
            r.report_no AS report_id,
            p.patient_name AS patient,
            d1.doctor_name AS doctor,
            d2.doctor_name AS reviewer,
            ROUND(COALESCE(ai.confidence, 90.0), 1) AS score,
            r.report_status AS status,
            CASE
                WHEN r.diagnosis_result LIKE '%异常%' OR r.diagnosis_result LIKE '%改变%' THEN '诊断异常'
                ELSE '无'
            END AS error_type,
            DATE_FORMAT(r.create_time, '%Y-%m-%d %H:%i:%s') AS time
        FROM sys_ecg_report r
        JOIN sys_ecg_patient_info p ON p.patient_id = r.patient_id
        LEFT JOIN sys_ecg_doctor_info d1 ON d1.doctor_id = r.create_doctor_id
        LEFT JOIN sys_ecg_doctor_info d2 ON d2.doctor_id = r.review_doctor_id
        LEFT JOIN sys_ecg_ai_analysis ai ON ai.ai_analysis_id = r.ai_analysis_id
        WHERE r.report_id = #{reportId}
        LIMIT 1
        """)
    QualityVo.ReportQualityItem selectReportQualityById(@Param("reportId") Long reportId);

    @Select("SELECT report_id FROM sys_ecg_report WHERE report_no = #{reportNo} LIMIT 1")
    Long selectReportIdByNo(@Param("reportNo") String reportNo);

    @Update("""
        UPDATE sys_ecg_report
        SET report_status = '申诉中',
            report_content = CONCAT(IFNULL(report_content, ''), '\\n[申诉] ', #{appealReason})
        WHERE report_id = #{reportId}
        """)
    int updateReportAppeal(@Param("reportId") Long reportId, @Param("appealReason") String appealReason);

    @Select("""
        <script>
        SELECT
            MIN(e.equipment_id) AS id,
            MIN(e.equipment_code) AS code,
            e.manufacturer AS name,
            GROUP_CONCAT(DISTINCT e.equipment_type ORDER BY e.equipment_type SEPARATOR '/') AS type,
            '设备负责人' AS contact,
            MAX(w.ward_phone) AS phone,
            CASE WHEN SUM(CASE WHEN e.equipment_status = '正常' THEN 0 ELSE 1 END) = 0 THEN '启用' ELSE '关注' END AS status,
            DATE_FORMAT(COALESCE(MIN(e.install_time), NOW()), '%Y-%m-%d %H:%i:%s') AS create_time
        FROM sys_ecg_equipment_info e
        LEFT JOIN sys_ecg_ward_info w ON w.ward_id = e.ward_id
        <where>
            e.manufacturer IS NOT NULL
            AND e.manufacturer != ''
            <if test='vendorName != null and vendorName != ""'>
                AND e.manufacturer LIKE CONCAT('%', #{vendorName}, '%')
            </if>
        </where>
        GROUP BY e.manufacturer
        ORDER BY MIN(e.equipment_id) DESC
        LIMIT #{offset}, #{size}
        </script>
        """)
    List<SupplierVo.VendorItem> listVendors(@Param("vendorName") String vendorName,
                                            @Param("offset") int offset,
                                            @Param("size") int size);

    @Select("""
        <script>
        SELECT COUNT(DISTINCT e.manufacturer)
        FROM sys_ecg_equipment_info e
        <where>
            e.manufacturer IS NOT NULL
            AND e.manufacturer != ''
            <if test='vendorName != null and vendorName != ""'>
                AND e.manufacturer LIKE CONCAT('%', #{vendorName}, '%')
            </if>
        </where>
        </script>
        """)
    long countVendors(@Param("vendorName") String vendorName);

    @Select("SELECT manufacturer FROM sys_ecg_equipment_info WHERE equipment_id = #{id} LIMIT 1")
    String selectVendorNameByEquipmentId(@Param("id") Long id);

    @Update("UPDATE sys_ecg_equipment_info SET manufacturer = #{newName} WHERE manufacturer = #{oldName}")
    int renameVendor(@Param("oldName") String oldName, @Param("newName") String newName);

    @Delete("DELETE FROM sys_ecg_equipment_info WHERE manufacturer = #{vendorName}")
    int deleteVendorByName(@Param("vendorName") String vendorName);

    @Select("""
        SELECT
            e.manufacturer AS vendor_name,
            '医疗器械经营许可证' AS cert_name,
            DATE_FORMAT(DATE_ADD(COALESCE(MAX(e.next_maintain_time), NOW()), INTERVAL 180 DAY), '%Y-%m-%d') AS expire_date,
            '有效' AS status
        FROM sys_ecg_equipment_info e
        WHERE e.manufacturer IS NOT NULL AND e.manufacturer != ''
        GROUP BY e.manufacturer
        ORDER BY e.manufacturer
        """)
    List<SupplierVo.VendorQualificationItem> listVendorQualifications();

    @Select("""
        SELECT
            e.manufacturer AS vendor_name,
            e.equipment_name AS device_model,
            e.equipment_type AS device_type,
            COUNT(*) AS total_supply
        FROM sys_ecg_equipment_info e
        WHERE e.manufacturer IS NOT NULL AND e.manufacturer != ''
        GROUP BY e.manufacturer, e.equipment_name, e.equipment_type
        ORDER BY total_supply DESC
        """)
    List<SupplierVo.VendorDeviceLedgerItem> listVendorDeviceLedger();

    @Select("""
        SELECT
            e.equipment_code AS code,
            e.equipment_name AS name,
            CONCAT('SN-', LPAD(e.equipment_id, 6, '0')) AS sn,
            IFNULL(w.ward_name, '未分配') AS dept,
            e.equipment_status AS status
        FROM sys_ecg_equipment_info e
        LEFT JOIN sys_ecg_ward_info w ON w.ward_id = e.ward_id
        ORDER BY e.equipment_id DESC
        """)
    List<SupplierVo.DeviceBasicLedgerItem> listDeviceBasicLedger();

    @Select("""
        SELECT
            e.equipment_code AS code,
            IFNULL(p.patient_name, '未绑定') AS patient,
            DATE_FORMAT(COALESCE(m.start_time, m.create_time, NOW()), '%Y-%m-%d %H:%i:%s') AS bind_time,
            CASE WHEN m.measure_id IS NULL THEN '空闲' ELSE '绑定中' END AS status
        FROM sys_ecg_equipment_info e
        LEFT JOIN sys_ecg_measure_record m ON m.measure_id = (
            SELECT mm.measure_id
            FROM sys_ecg_measure_record mm
            WHERE mm.equipment_id = e.equipment_id
            ORDER BY COALESCE(mm.start_time, mm.create_time) DESC, mm.measure_id DESC
            LIMIT 1
        )
        LEFT JOIN sys_ecg_patient_info p ON p.patient_id = m.patient_id
        ORDER BY e.equipment_id DESC
        """)
    List<SupplierVo.DeviceBindingItem> listDeviceBinding();

    @Select("""
        SELECT
            e.equipment_code AS code,
            '常规维护' AS type,
            DATE_FORMAT(COALESCE(e.maintain_time, NOW()), '%Y-%m-%d') AS date,
            '系统巡检' AS operator,
            CASE WHEN e.equipment_status = '正常' THEN '通过' ELSE '待复检' END AS result
        FROM sys_ecg_equipment_info e
        ORDER BY e.equipment_id DESC
        """)
    List<SupplierVo.DeviceMaintenanceItem> listDeviceMaintenance();

    @Select("""
        SELECT
            e.equipment_code AS code,
            60 + MOD(e.equipment_id * 7, 40) AS battery,
            70 + MOD(e.equipment_id * 11, 30) AS signal,
            CASE WHEN e.equipment_status = '正常' THEN '在线' ELSE '告警' END AS online_status,
            DATE_FORMAT(COALESCE(e.maintain_time, e.install_time, NOW()), '%Y-%m-%d %H:%i:%s') AS last_active
        FROM sys_ecg_equipment_info e
        ORDER BY e.equipment_id DESC
        """)
    List<SupplierVo.DeviceStatusItem> listDeviceStatus();

    @Select("""
        SELECT
            CONCAT('C', LPAD(dict_id, 3, '0')) AS code,
            type_name AS name,
            IFNULL(type_desc, '标准规格') AS spec,
            '个' AS unit,
            '迈瑞' AS brand
        FROM sys_dict_equipment_type
        ORDER BY dict_id
        """)
    List<SupplierVo.ConsumableInfoItem> listConsumableInfo();

    @Select("""
        SELECT
            type_name AS name,
            50 + MOD(dict_id * 13, 120) AS stock,
            '个' AS unit,
            CASE WHEN MOD(dict_id, 3) = 0 THEN '低' WHEN MOD(dict_id, 3) = 1 THEN '中' ELSE '高' END AS warning_level,
            CASE WHEN MOD(dict_id, 5) = 0 THEN '偏低' ELSE '充足' END AS status
        FROM sys_dict_equipment_type
        ORDER BY dict_id
        """)
    List<SupplierVo.ConsumableInventoryItem> listConsumableInventory();

    @Select("""
        SELECT
            IFNULL(f.follow_type, '耗材申请') AS name,
            1 AS amount,
            IFNULL(w.ward_name, '未知病区') AS dept,
            IFNULL(d.doctor_name, '系统') AS applicant,
            DATE_FORMAT(f.follow_time, '%Y-%m-%d %H:%i:%s') AS time
        FROM sys_ecg_follow_up f
        LEFT JOIN sys_ecg_patient_info p ON p.patient_id = f.patient_id
        LEFT JOIN sys_ecg_ward_info w ON w.ward_id = p.ward_id
        LEFT JOIN sys_ecg_doctor_info d ON d.doctor_id = f.follow_doctor_id
        ORDER BY f.follow_time DESC, f.follow_id DESC
        LIMIT 50
        """)
    List<SupplierVo.ConsumableTraceabilityItem> listConsumableTraceability();

    @Select("""
        SELECT
            CONCAT('PO', DATE_FORMAT(COALESCE(e.install_time, NOW()), '%Y%m%d'), LPAD(e.equipment_id, 3, '0')) AS order_no,
            e.equipment_name AS item_name,
            1 AS amount,
            e.manufacturer AS vendor,
            CASE WHEN e.equipment_status = '正常' THEN '已到货' ELSE '待到货' END AS status,
            DATE_FORMAT(COALESCE(e.install_time, NOW()), '%Y-%m-%d') AS date
        FROM sys_ecg_equipment_info e
        ORDER BY e.equipment_id DESC
        """)
    List<SupplierVo.ProcurementOrderItem> listProcurementOrder();

    @Select("""
        SELECT
            CONCAT('AC', LPAD(r.report_id, 6, '0')) AS order_no,
            IFNULL(r.diagnosis_result, '心电图报告') AS item_name,
            1 AS amount,
            IFNULL(d.doctor_name, '系统') AS receiver,
            DATE_FORMAT(r.create_time, '%Y-%m-%d') AS date,
            CASE WHEN r.report_status = '已审核' THEN '已验收' ELSE '待验收' END AS status
        FROM sys_ecg_report r
        LEFT JOIN sys_ecg_doctor_info d ON d.doctor_id = r.create_doctor_id
        ORDER BY r.report_id DESC
        """)
    List<SupplierVo.ProcurementAcceptanceItem> listProcurementAcceptance();

    @Select("""
        SELECT
            DATE_FORMAT(COALESCE(stat_time, NOW()), '%Y-%m') AS month,
            ROUND(SUM(total_report) * 120.0, 2) AS total_amount,
            SUM(approved_report) AS device_count,
            SUM(GREATEST(total_report - approved_report, 0)) AS consumable_count
        FROM sys_ecg_report_stat
        GROUP BY DATE_FORMAT(COALESCE(stat_time, NOW()), '%Y-%m')
        ORDER BY month DESC
        """)
    List<SupplierVo.ProcurementStatisticsItem> listProcurementStatistics();

    @Select("""
        <script>
        SELECT
            d.doctor_id AS id,
            d.user_name,
            d.doctor_name AS real_name,
            IFNULL(w.ward_name, '未分配') AS dept,
            IFNULL(d.doctor_title, '医生') AS role,
            IFNULL(w.ward_phone, '') AS phone,
            CASE WHEN d.is_active = 1 THEN '启用' ELSE '禁用' END AS status,
            DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s') AS create_time
        FROM sys_ecg_doctor_info d
        LEFT JOIN sys_ecg_ward_info w ON w.ward_id = d.ward_id
        <where>
            <if test='userName != null and userName != ""'>
                AND (d.user_name LIKE CONCAT('%', #{userName}, '%') OR d.doctor_name LIKE CONCAT('%', #{userName}, '%'))
            </if>
            <if test='phone != null and phone != ""'>
                AND w.ward_phone LIKE CONCAT('%', #{phone}, '%')
            </if>
            <if test='status != null and status != ""'>
                AND (CASE WHEN d.is_active = 1 THEN '启用' ELSE '禁用' END) = #{status}
            </if>
        </where>
        ORDER BY d.doctor_id DESC
        LIMIT #{offset}, #{size}
        </script>
        """)
    List<SystemVo.UserItem> listUsers(@Param("userName") String userName,
                                      @Param("phone") String phone,
                                      @Param("status") String status,
                                      @Param("offset") int offset,
                                      @Param("size") int size);

    @Select("""
        <script>
        SELECT COUNT(*)
        FROM sys_ecg_doctor_info d
        LEFT JOIN sys_ecg_ward_info w ON w.ward_id = d.ward_id
        <where>
            <if test='userName != null and userName != ""'>
                AND (d.user_name LIKE CONCAT('%', #{userName}, '%') OR d.doctor_name LIKE CONCAT('%', #{userName}, '%'))
            </if>
            <if test='phone != null and phone != ""'>
                AND w.ward_phone LIKE CONCAT('%', #{phone}, '%')
            </if>
            <if test='status != null and status != ""'>
                AND (CASE WHEN d.is_active = 1 THEN '启用' ELSE '禁用' END) = #{status}
            </if>
        </where>
        </script>
        """)
    long countUsers(@Param("userName") String userName,
                    @Param("phone") String phone,
                    @Param("status") String status);

    @Update("UPDATE sys_ecg_doctor_info SET is_active = #{active} WHERE doctor_id = #{id}")
    int updateDoctorStatus(@Param("id") Long id, @Param("active") Integer active);

    @Select("""
        <script>
        SELECT
            w.ward_id AS id,
            w.ward_name AS dept_name,
            CONCAT('D', LPAD(w.ward_id, 3, '0')) AS code,
            (
                SELECT d.doctor_name
                FROM sys_ecg_doctor_info d
                WHERE d.ward_id = w.ward_id
                ORDER BY d.doctor_id
                LIMIT 1
            ) AS head,
            IFNULL(w.ward_phone, '') AS phone,
            CASE
                WHEN EXISTS (SELECT 1 FROM sys_ecg_doctor_info d2 WHERE d2.ward_id = w.ward_id AND d2.is_active = 1)
                    THEN '启用'
                ELSE '禁用'
            END AS status,
            DATE_FORMAT(w.create_time, '%Y-%m-%d %H:%i:%s') AS create_time,
            w.ward_id AS `order`
        FROM sys_ecg_ward_info w
        <where>
            <if test='deptName != null and deptName != ""'>
                AND w.ward_name LIKE CONCAT('%', #{deptName}, '%')
            </if>
            <if test='status != null and status != ""'>
                AND (
                    CASE
                        WHEN EXISTS (SELECT 1 FROM sys_ecg_doctor_info d2 WHERE d2.ward_id = w.ward_id AND d2.is_active = 1)
                            THEN '启用'
                        ELSE '禁用'
                    END
                ) = #{status}
            </if>
        </where>
        ORDER BY w.ward_id DESC
        LIMIT #{offset}, #{size}
        </script>
        """)
    List<SystemVo.DepartmentItem> listDepartments(@Param("deptName") String deptName,
                                                  @Param("status") String status,
                                                  @Param("offset") int offset,
                                                  @Param("size") int size);

    @Select("""
        <script>
        SELECT COUNT(*)
        FROM sys_ecg_ward_info w
        <where>
            <if test='deptName != null and deptName != ""'>
                AND w.ward_name LIKE CONCAT('%', #{deptName}, '%')
            </if>
            <if test='status != null and status != ""'>
                AND (
                    CASE
                        WHEN EXISTS (SELECT 1 FROM sys_ecg_doctor_info d2 WHERE d2.ward_id = w.ward_id AND d2.is_active = 1)
                            THEN '启用'
                        ELSE '禁用'
                    END
                ) = #{status}
            </if>
        </where>
        </script>
        """)
    long countDepartments(@Param("deptName") String deptName,
                          @Param("status") String status);

    @Select("""
        <script>
        SELECT
            d.dict_id AS id,
            d.type_name AS role_name,
            d.type_code AS role_key,
            IFNULL(d.sort, 0) AS `order`,
            '启用' AS status,
            DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s') AS create_time,
            d.type_desc AS remark
        FROM sys_dict_warning_type d
        <where>
            <if test='roleName != null and roleName != ""'>
                AND d.type_name LIKE CONCAT('%', #{roleName}, '%')
            </if>
            <if test='status != null and status != ""'>
                AND #{status} = '启用'
            </if>
        </where>
        ORDER BY d.sort ASC, d.dict_id ASC
        LIMIT #{offset}, #{size}
        </script>
        """)
    List<SystemVo.RoleItem> listRoles(@Param("roleName") String roleName,
                                      @Param("status") String status,
                                      @Param("offset") int offset,
                                      @Param("size") int size);

    @Select("""
        <script>
        SELECT COUNT(*)
        FROM sys_dict_warning_type d
        <where>
            <if test='roleName != null and roleName != ""'>
                AND d.type_name LIKE CONCAT('%', #{roleName}, '%')
            </if>
            <if test='status != null and status != ""'>
                AND #{status} = '启用'
            </if>
        </where>
        </script>
        """)
    long countRoles(@Param("roleName") String roleName,
                    @Param("status") String status);

    @Select("""
        SELECT
            dict_id + 1000 AS id,
            type_name AS name,
            CONCAT('/system/menu/', type_code) AS path
        FROM sys_dict_equipment_type
        ORDER BY sort ASC, dict_id ASC
        """)
    List<SystemVo.MenuTreeItem> listMenuItems();

    @Update("UPDATE sys_dict_warning_type SET type_desc = #{menuIdsCsv} WHERE dict_id = #{roleId}")
    int updateRoleMenuDesc(@Param("roleId") Long roleId, @Param("menuIdsCsv") String menuIdsCsv);

    @Select("SELECT ward_id FROM sys_ecg_ward_info WHERE ward_name = #{wardName} LIMIT 1")
    Integer selectWardIdByName(@Param("wardName") String wardName);
}
