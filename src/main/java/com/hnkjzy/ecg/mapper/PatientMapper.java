package com.hnkjzy.ecg.mapper;

/**
 * 文件说明：数据访问文件。
 * 主要职责：负责定义 MyBatis SQL 访问方法与统计查询。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import com.hnkjzy.ecg.model.PatientInfo;
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
 * 类说明：PatientMapper。
 * 业务定位：负责定义 MyBatis SQL 访问方法与统计查询。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public interface PatientMapper {

    @Select("""
        <script>
        SELECT *
        FROM sys_ecg_patient_info
        <where>
            <if test='name != null and name != ""'>
                AND patient_name LIKE CONCAT('%', #{name}, '%')
            </if>
            <if test='wardId != null'>
                AND ward_id = #{wardId}
            </if>
        </where>
        ORDER BY patient_id DESC
        LIMIT #{offset}, #{size}
        </script>
        """)
    List<PatientInfo> selectPage(@Param("name") String name,
                                 @Param("wardId") Integer wardId,
                                 @Param("offset") int offset,
                                 @Param("size") int size);

    @Select("""
        <script>
        SELECT COUNT(*)
        FROM sys_ecg_patient_info
        <where>
            <if test='name != null and name != ""'>
                AND patient_name LIKE CONCAT('%', #{name}, '%')
            </if>
            <if test='wardId != null'>
                AND ward_id = #{wardId}
            </if>
        </where>
        </script>
        """)
    /**
     * SQL说明：业务处理数据库操作，方法名为 count。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    long count(@Param("name") String name, @Param("wardId") Integer wardId);

    @Select("SELECT * FROM sys_ecg_patient_info WHERE patient_id = #{id}")
    /**
     * SQL说明：查询数据库操作，方法名为 findById。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    PatientInfo findById(@Param("id") Integer id);

    @Insert("""
        INSERT INTO sys_ecg_patient_info (
            patient_name, patient_gender, patient_age, inpatient_no, outpatient_no,
            inpatient_date, bed_no, inpatient_diagnosis, ward_id, phone
        ) VALUES (
            #{patientName}, #{patientGender}, #{patientAge}, #{inpatientNo}, #{outpatientNo},
            #{inpatientDate}, #{bedNo}, #{inpatientDiagnosis}, #{wardId}, #{phone}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "patientId")
    /**
     * SQL说明：新增数据库操作，方法名为 insert。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int insert(PatientInfo patientInfo);

    @Update("""
        UPDATE sys_ecg_patient_info
        SET patient_name = #{patientName},
            patient_gender = #{patientGender},
            patient_age = #{patientAge},
            inpatient_no = #{inpatientNo},
            outpatient_no = #{outpatientNo},
            inpatient_date = #{inpatientDate},
            bed_no = #{bedNo},
            inpatient_diagnosis = #{inpatientDiagnosis},
            ward_id = #{wardId},
            phone = #{phone}
        WHERE patient_id = #{patientId}
        """)
    /**
     * SQL说明：修改数据库操作，方法名为 update。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int update(PatientInfo patientInfo);

    @Delete("DELETE FROM sys_ecg_patient_info WHERE patient_id = #{id}")
    /**
     * SQL说明：删除数据库操作，方法名为 deleteById。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int deleteById(@Param("id") Integer id);
}
