package com.hnkjzy.ecg.mapper;

/**
 * 文件说明：数据访问文件。
 * 主要职责：负责定义 MyBatis SQL 访问方法与统计查询。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import com.hnkjzy.ecg.model.WardInfo;
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
 * 类说明：WardMapper。
 * 业务定位：负责定义 MyBatis SQL 访问方法与统计查询。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public interface WardMapper {

    @Select("SELECT * FROM sys_ecg_ward_info ORDER BY ward_id DESC")
    /**
     * SQL说明：查询数据库操作，方法名为 findAll。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    List<WardInfo> findAll();

    @Select("SELECT * FROM sys_ecg_ward_info WHERE ward_id = #{id}")
    /**
     * SQL说明：查询数据库操作，方法名为 findById。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    WardInfo findById(@Param("id") Integer id);

    @Insert("""
        INSERT INTO sys_ecg_ward_info (ward_name, ward_phone, ward_desc)
        VALUES (#{wardName}, #{wardPhone}, #{wardDesc})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "wardId")
    /**
     * SQL说明：新增数据库操作，方法名为 insert。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int insert(WardInfo wardInfo);

    @Update("""
        UPDATE sys_ecg_ward_info
        SET ward_name = #{wardName},
            ward_phone = #{wardPhone},
            ward_desc = #{wardDesc}
        WHERE ward_id = #{wardId}
        """)
    /**
     * SQL说明：修改数据库操作，方法名为 update。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int update(WardInfo wardInfo);

    @Delete("DELETE FROM sys_ecg_ward_info WHERE ward_id = #{id}")
    /**
     * SQL说明：删除数据库操作，方法名为 deleteById。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int deleteById(@Param("id") Integer id);
}
