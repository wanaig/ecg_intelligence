package com.hnkjzy.ecg.mapper;

/**
 * 文件说明：数据访问文件。
 * 主要职责：负责定义 MyBatis SQL 访问方法与统计查询。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import com.hnkjzy.ecg.model.BedOverviewItem;
import com.hnkjzy.ecg.model.MeasureStatInfo;
import com.hnkjzy.ecg.model.PatientManageView;
import com.hnkjzy.ecg.model.ReportStatInfo;
import com.hnkjzy.ecg.model.SupplierSummary;
import com.hnkjzy.ecg.model.TrendPoint;
import com.hnkjzy.ecg.model.WarningStatInfo;
import com.hnkjzy.ecg.model.WorkbenchOverview;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
/**
 * 类说明：AnalysisMapper。
 * 业务定位：负责定义 MyBatis SQL 访问方法与统计查询。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public interface AnalysisMapper {

    @Select("""
        SELECT
            (SELECT COUNT(*) FROM sys_ecg_patient_info) AS total_patients,
            (SELECT COUNT(*) FROM sys_ecg_abnormal_warning) AS total_warnings,
            (SELECT COUNT(*) FROM sys_ecg_abnormal_warning WHERE IFNULL(warning_status, '0') IN ('1', '已纳入', '未处理', '处理中')) AS unhandled_warnings,
            (SELECT COUNT(*) FROM sys_ecg_report WHERE report_status IN ('待审核', '草稿')) AS pending_reports,
            (SELECT COUNT(*) FROM sys_ecg_equipment_info) AS total_devices,
            (SELECT COUNT(*) FROM sys_ecg_ward_info) AS total_wards
        """)
    /**
     * SQL说明：业务处理数据库操作，方法名为 selectWorkbenchOverview。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    WorkbenchOverview selectWorkbenchOverview();

    @Select("""
        SELECT DATE_FORMAT(warning_time, '%m-%d') AS label, COUNT(*) AS value
        FROM sys_ecg_abnormal_warning
        WHERE warning_time >= DATE_SUB(CURDATE(), INTERVAL #{days} DAY)
        GROUP BY DATE(warning_time)
        ORDER BY DATE(warning_time)
        """)
    /**
     * SQL说明：业务处理数据库操作，方法名为 warningTrend。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    List<TrendPoint> warningTrend(@Param("days") int days);

    @Select("""
        <script>
        SELECT *
        FROM sys_ecg_measure_stat
        <where>
            <if test='statCycle != null and statCycle != ""'>
                AND stat_cycle = #{statCycle}
            </if>
        </where>
        ORDER BY stat_id DESC
        </script>
        """)
    /**
     * SQL说明：查询数据库操作，方法名为 listMeasureStats。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    List<MeasureStatInfo> listMeasureStats(@Param("statCycle") String statCycle);

    @Select("""
        <script>
        SELECT *
        FROM sys_ecg_warning_stat
        <where>
            <if test='statCycle != null and statCycle != ""'>
                AND stat_cycle = #{statCycle}
            </if>
        </where>
        ORDER BY warning_stat_id DESC
        </script>
        """)
    /**
     * SQL说明：查询数据库操作，方法名为 listWarningStats。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    List<WarningStatInfo> listWarningStats(@Param("statCycle") String statCycle);

    @Select("""
        <script>
        SELECT *
        FROM sys_ecg_report_stat
        <where>
            <if test='statCycle != null and statCycle != ""'>
                AND stat_cycle = #{statCycle}
            </if>
        </where>
        ORDER BY report_stat_id DESC
        </script>
        """)
    /**
     * SQL说明：查询数据库操作，方法名为 listReportStats。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    List<ReportStatInfo> listReportStats(@Param("statCycle") String statCycle);

    @Select("""
        SELECT
            p.patient_id,
            p.patient_name,
            p.patient_gender,
            p.patient_age,
            p.ward_id,
            p.bed_no,
            p.inpatient_no,
            w.warning_level AS latest_warning_level,
            w.warning_status AS latest_warning_status,
            w.warning_time AS latest_warning_time
        FROM sys_ecg_patient_info p
        JOIN sys_ecg_abnormal_warning w
          ON w.warning_id = (
              SELECT ww.warning_id
              FROM sys_ecg_abnormal_warning ww
              WHERE ww.patient_id = p.patient_id
              ORDER BY ww.warning_time DESC, ww.warning_id DESC
              LIMIT 1
          )
                WHERE IFNULL(w.warning_status, '0') IN ('0', '未纳入', '未处理', '处理中', '已撤销', '正常')
        ORDER BY w.warning_time DESC
        """)
    /**
     * SQL说明：查询数据库操作，方法名为 listPendingPatients。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    List<PatientManageView> listPendingPatients();

    @Select("""
        SELECT
            p.patient_id,
            p.patient_name,
            p.patient_gender,
            p.patient_age,
            p.ward_id,
            p.bed_no,
            p.inpatient_no,
            w.warning_level AS latest_warning_level,
            w.warning_status AS latest_warning_status,
            w.warning_time AS latest_warning_time
        FROM sys_ecg_patient_info p
        LEFT JOIN sys_ecg_abnormal_warning w
          ON w.warning_id = (
              SELECT ww.warning_id
              FROM sys_ecg_abnormal_warning ww
              WHERE ww.patient_id = p.patient_id
              ORDER BY ww.warning_time DESC, ww.warning_id DESC
              LIMIT 1
          )
                WHERE w.warning_id IS NOT NULL AND IFNULL(w.warning_status, '0') IN ('1', '已纳入', '已处理')
        ORDER BY p.patient_id DESC
        """)
    /**
     * SQL说明：查询数据库操作，方法名为 listManagedPatients。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    List<PatientManageView> listManagedPatients();

    @Select("""
        SELECT
            p.ward_id,
            w.ward_name,
            p.bed_no,
            p.patient_id,
            p.patient_name,
            p.inpatient_no
        FROM sys_ecg_patient_info p
        LEFT JOIN sys_ecg_ward_info w ON w.ward_id = p.ward_id
        WHERE p.bed_no IS NOT NULL AND p.bed_no != ''
        ORDER BY p.ward_id ASC, p.bed_no ASC
        """)
    /**
     * SQL说明：查询数据库操作，方法名为 listBedOverview。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    List<BedOverviewItem> listBedOverview();

    @Select("""
        SELECT
            manufacturer AS supplier_name,
            COUNT(*) AS equipment_count,
            GROUP_CONCAT(DISTINCT equipment_type ORDER BY equipment_type SEPARATOR ',') AS equipment_types
        FROM sys_ecg_equipment_info
        WHERE manufacturer IS NOT NULL AND manufacturer != ''
        GROUP BY manufacturer
        ORDER BY equipment_count DESC
        """)
    /**
     * SQL说明：查询数据库操作，方法名为 listSupplierSummary。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    List<SupplierSummary> listSupplierSummary();
}
