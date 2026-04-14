package com.hnkjzy.ecg.mapper;

/**
 * 文件说明：数据访问文件。
 * 主要职责：负责定义 MyBatis SQL 访问方法与统计查询。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import com.hnkjzy.ecg.model.EquipmentInfo;
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
 * 类说明：EquipmentMapper。
 * 业务定位：负责定义 MyBatis SQL 访问方法与统计查询。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public interface EquipmentMapper {

    @Select("""
        <script>
        SELECT *
        FROM sys_ecg_equipment_info
        <where>
            <if test='manufacturer != null and manufacturer != ""'>
                AND manufacturer LIKE CONCAT('%', #{manufacturer}, '%')
            </if>
            <if test='wardId != null'>
                AND ward_id = #{wardId}
            </if>
            <if test='equipmentType != null and equipmentType != ""'>
                AND equipment_type = #{equipmentType}
            </if>
        </where>
        ORDER BY equipment_id DESC
        </script>
        """)
    List<EquipmentInfo> findAll(@Param("manufacturer") String manufacturer,
                                @Param("wardId") Integer wardId,
                                @Param("equipmentType") String equipmentType);

    @Select("SELECT * FROM sys_ecg_equipment_info WHERE equipment_id = #{id}")
    /**
     * SQL说明：查询数据库操作，方法名为 findById。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    EquipmentInfo findById(@Param("id") Integer id);

    @Insert("""
        INSERT INTO sys_ecg_equipment_info (
            equipment_code, equipment_name, equipment_type, manufacturer, equipment_version,
            ward_id, equipment_status, install_time, maintain_time, next_maintain_time
        ) VALUES (
            #{equipmentCode}, #{equipmentName}, #{equipmentType}, #{manufacturer}, #{equipmentVersion},
            #{wardId}, #{equipmentStatus}, #{installTime}, #{maintainTime}, #{nextMaintainTime}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "equipmentId")
    /**
     * SQL说明：新增数据库操作，方法名为 insert。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int insert(EquipmentInfo equipmentInfo);

    @Update("""
        UPDATE sys_ecg_equipment_info
        SET equipment_code = #{equipmentCode},
            equipment_name = #{equipmentName},
            equipment_type = #{equipmentType},
            manufacturer = #{manufacturer},
            equipment_version = #{equipmentVersion},
            ward_id = #{wardId},
            equipment_status = #{equipmentStatus},
            install_time = #{installTime},
            maintain_time = #{maintainTime},
            next_maintain_time = #{nextMaintainTime}
        WHERE equipment_id = #{equipmentId}
        """)
    /**
     * SQL说明：修改数据库操作，方法名为 update。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int update(EquipmentInfo equipmentInfo);

    @Delete("DELETE FROM sys_ecg_equipment_info WHERE equipment_id = #{id}")
    /**
     * SQL说明：删除数据库操作，方法名为 deleteById。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int deleteById(@Param("id") Integer id);
}
