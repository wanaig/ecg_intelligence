package com.hnkjzy.ecg.mapper;

/**
 * 文件说明：数据访问文件。
 * 主要职责：负责定义 MyBatis SQL 访问方法与统计查询。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import com.hnkjzy.ecg.model.DoctorInfo;
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
 * 类说明：DoctorMapper。
 * 业务定位：负责定义 MyBatis SQL 访问方法与统计查询。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public interface DoctorMapper {

    @Select("""
        <script>
        SELECT *
        FROM sys_ecg_doctor_info
        <where>
            <if test='wardId != null'>
                AND ward_id = #{wardId}
            </if>
            <if test='isActive != null'>
                AND is_active = #{isActive}
            </if>
        </where>
        ORDER BY doctor_id DESC
        </script>
        """)
    /**
     * SQL说明：查询数据库操作，方法名为 findAll。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    List<DoctorInfo> findAll(@Param("wardId") Integer wardId, @Param("isActive") Integer isActive);

    @Select("SELECT * FROM sys_ecg_doctor_info WHERE doctor_id = #{id}")
    /**
     * SQL说明：查询数据库操作，方法名为 findById。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    DoctorInfo findById(@Param("id") Integer id);

    @Insert("""
        INSERT INTO sys_ecg_doctor_info (
            doctor_name, doctor_title, ward_id, user_name, password, is_active
        ) VALUES (
            #{doctorName}, #{doctorTitle}, #{wardId}, #{userName}, #{password}, #{isActive}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "doctorId")
    /**
     * SQL说明：新增数据库操作，方法名为 insert。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int insert(DoctorInfo doctorInfo);

    @Update("""
        UPDATE sys_ecg_doctor_info
        SET doctor_name = #{doctorName},
            doctor_title = #{doctorTitle},
            ward_id = #{wardId},
            user_name = #{userName},
            password = #{password},
            is_active = #{isActive}
        WHERE doctor_id = #{doctorId}
        """)
    /**
     * SQL说明：修改数据库操作，方法名为 update。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int update(DoctorInfo doctorInfo);

    @Delete("DELETE FROM sys_ecg_doctor_info WHERE doctor_id = #{id}")
    /**
     * SQL说明：删除数据库操作，方法名为 deleteById。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int deleteById(@Param("id") Integer id);
}
