package com.hnkjzy.ecg.mapper;

/**
 * 文件说明：数据访问文件。
 * 主要职责：负责定义 MyBatis SQL 访问方法与统计查询。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import com.hnkjzy.ecg.model.ReportInfo;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
/**
 * 类说明：ReportMapper。
 * 业务定位：负责定义 MyBatis SQL 访问方法与统计查询。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public interface ReportMapper {

    @Select("""
        <script>
        SELECT *
        FROM sys_ecg_report
        <where>
            <if test='status != null and status != ""'>
                AND report_status = #{status}
            </if>
            <if test='patientId != null'>
                AND patient_id = #{patientId}
            </if>
        </where>
        ORDER BY create_time DESC, report_id DESC
        LIMIT #{offset}, #{size}
        </script>
        """)
    List<ReportInfo> selectPage(@Param("status") String status,
                                @Param("patientId") Integer patientId,
                                @Param("offset") int offset,
                                @Param("size") int size);

    @Select("""
        <script>
        SELECT COUNT(*)
        FROM sys_ecg_report
        <where>
            <if test='status != null and status != ""'>
                AND report_status = #{status}
            </if>
            <if test='patientId != null'>
                AND patient_id = #{patientId}
            </if>
        </where>
        </script>
        """)
    /**
     * SQL说明：业务处理数据库操作，方法名为 count。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    long count(@Param("status") String status, @Param("patientId") Integer patientId);

    @Select("SELECT * FROM sys_ecg_report WHERE report_id = #{id}")
    /**
     * SQL说明：查询数据库操作，方法名为 findById。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    ReportInfo findById(@Param("id") Integer id);

    @Insert("""
        INSERT INTO sys_ecg_report (
            measure_id, ai_analysis_id, patient_id, report_no, report_content, diagnosis_result,
            create_doctor_id, review_doctor_id, create_time, review_time, report_status, report_path
        ) VALUES (
            #{measureId}, #{aiAnalysisId}, #{patientId}, #{reportNo}, #{reportContent}, #{diagnosisResult},
            #{createDoctorId}, #{reviewDoctorId}, #{createTime}, #{reviewTime}, #{reportStatus}, #{reportPath}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "reportId")
    /**
     * SQL说明：新增数据库操作，方法名为 insert。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int insert(ReportInfo reportInfo);

    @Update("""
        UPDATE sys_ecg_report
        SET measure_id = #{measureId},
            ai_analysis_id = #{aiAnalysisId},
            patient_id = #{patientId},
            report_no = #{reportNo},
            report_content = #{reportContent},
            diagnosis_result = #{diagnosisResult},
            create_doctor_id = #{createDoctorId},
            review_doctor_id = #{reviewDoctorId},
            create_time = #{createTime},
            review_time = #{reviewTime},
            report_status = #{reportStatus},
            report_path = #{reportPath}
        WHERE report_id = #{reportId}
        """)
    /**
     * SQL说明：修改数据库操作，方法名为 update。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int update(ReportInfo reportInfo);

    @Update("""
        UPDATE sys_ecg_report
        SET review_doctor_id = #{reviewDoctorId},
            report_status = #{reportStatus},
            review_time = NOW()
        WHERE report_id = #{reportId}
        """)
    int review(@Param("reportId") Integer reportId,
               @Param("reviewDoctorId") Integer reviewDoctorId,
               @Param("reportStatus") String reportStatus);

    @Delete("DELETE FROM sys_ecg_report WHERE report_id = #{id}")
    /**
     * SQL说明：删除数据库操作，方法名为 deleteById。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int deleteById(@Param("id") Integer id);
}
