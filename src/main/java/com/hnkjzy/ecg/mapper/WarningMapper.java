package com.hnkjzy.ecg.mapper;

/**
 * 文件说明：数据访问文件。
 * 主要职责：负责定义 MyBatis SQL 访问方法与统计查询。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import com.hnkjzy.ecg.model.WarningInfo;
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
 * 类说明：WarningMapper。
 * 业务定位：负责定义 MyBatis SQL 访问方法与统计查询。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public interface WarningMapper {

    @Select("""
        <script>
        SELECT *
        FROM sys_ecg_abnormal_warning
        <where>
            <if test='status != null and status != ""'>
                AND warning_status = #{status}
            </if>
            <if test='level != null and level != ""'>
                AND warning_level = #{level}
            </if>
            <if test='type != null and type != ""'>
                AND warning_type = #{type}
            </if>
            <if test='patientId != null'>
                AND patient_id = #{patientId}
            </if>
        </where>
        ORDER BY warning_time DESC, warning_id DESC
        LIMIT #{offset}, #{size}
        </script>
        """)
    List<WarningInfo> selectPage(@Param("status") String status,
                                 @Param("level") String level,
                                 @Param("type") String type,
                                 @Param("patientId") Integer patientId,
                                 @Param("offset") int offset,
                                 @Param("size") int size);

    @Select("""
        <script>
        SELECT COUNT(*)
        FROM sys_ecg_abnormal_warning
        <where>
            <if test='status != null and status != ""'>
                AND warning_status = #{status}
            </if>
            <if test='level != null and level != ""'>
                AND warning_level = #{level}
            </if>
            <if test='type != null and type != ""'>
                AND warning_type = #{type}
            </if>
            <if test='patientId != null'>
                AND patient_id = #{patientId}
            </if>
        </where>
        </script>
        """)
    long count(@Param("status") String status,
               @Param("level") String level,
               @Param("type") String type,
               @Param("patientId") Integer patientId);

    @Select("SELECT * FROM sys_ecg_abnormal_warning WHERE warning_id = #{id}")
    /**
     * SQL说明：查询数据库操作，方法名为 findById。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    WarningInfo findById(@Param("id") Integer id);

    @Insert("""
        INSERT INTO sys_ecg_abnormal_warning (
            measure_id, patient_id, warning_time, warning_type, warning_level,
            warning_content, handle_doctor_id, handle_time, handle_result, warning_status
        ) VALUES (
            #{measureId}, #{patientId}, #{warningTime}, #{warningType}, #{warningLevel},
            #{warningContent}, #{handleDoctorId}, #{handleTime}, #{handleResult}, #{warningStatus}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "warningId")
    /**
     * SQL说明：新增数据库操作，方法名为 insert。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int insert(WarningInfo warningInfo);

    @Update("""
        UPDATE sys_ecg_abnormal_warning
        SET measure_id = #{measureId},
            patient_id = #{patientId},
            warning_time = #{warningTime},
            warning_type = #{warningType},
            warning_level = #{warningLevel},
            warning_content = #{warningContent},
            handle_doctor_id = #{handleDoctorId},
            handle_time = #{handleTime},
            handle_result = #{handleResult},
            warning_status = #{warningStatus}
        WHERE warning_id = #{warningId}
        """)
    /**
     * SQL说明：修改数据库操作，方法名为 update。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int update(WarningInfo warningInfo);

    @Update("""
        UPDATE sys_ecg_abnormal_warning
        SET handle_doctor_id = #{handleDoctorId},
            handle_time = NOW(),
            handle_result = #{handleResult},
            warning_status = #{warningStatus}
        WHERE warning_id = #{warningId}
        """)
    int handle(@Param("warningId") Integer warningId,
               @Param("handleDoctorId") Integer handleDoctorId,
               @Param("handleResult") String handleResult,
               @Param("warningStatus") String warningStatus);

    @Delete("DELETE FROM sys_ecg_abnormal_warning WHERE warning_id = #{id}")
    /**
     * SQL说明：删除数据库操作，方法名为 deleteById。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int deleteById(@Param("id") Integer id);
}
