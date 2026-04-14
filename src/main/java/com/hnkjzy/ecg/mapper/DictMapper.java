package com.hnkjzy.ecg.mapper;

/**
 * 文件说明：数据访问文件。
 * 主要职责：负责定义 MyBatis SQL 访问方法与统计查询。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import com.hnkjzy.ecg.model.DictItem;
import com.hnkjzy.ecg.model.WarningLevelItem;
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
 * 类说明：DictMapper。
 * 业务定位：负责定义 MyBatis SQL 访问方法与统计查询。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public interface DictMapper {

    @Select("SELECT * FROM sys_dict_warning_level ORDER BY sort ASC, dict_id ASC")
    /**
     * SQL说明：查询数据库操作，方法名为 listWarningLevels。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    List<WarningLevelItem> listWarningLevels();

    @Insert("""
        INSERT INTO sys_dict_warning_level (level_code, level_name, level_desc, sort)
        VALUES (#{levelCode}, #{levelName}, #{levelDesc}, #{sort})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "dictId")
    /**
     * SQL说明：新增数据库操作，方法名为 insertWarningLevel。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int insertWarningLevel(WarningLevelItem item);

    @Update("""
        UPDATE sys_dict_warning_level
        SET level_code = #{levelCode},
            level_name = #{levelName},
            level_desc = #{levelDesc},
            sort = #{sort}
        WHERE dict_id = #{dictId}
        """)
    /**
     * SQL说明：修改数据库操作，方法名为 updateWarningLevel。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int updateWarningLevel(WarningLevelItem item);

    @Delete("DELETE FROM sys_dict_warning_level WHERE dict_id = #{id}")
    /**
     * SQL说明：删除数据库操作，方法名为 deleteWarningLevel。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int deleteWarningLevel(@Param("id") Integer id);

    @Select("SELECT * FROM sys_dict_warning_type ORDER BY sort ASC, dict_id ASC")
    /**
     * SQL说明：查询数据库操作，方法名为 listWarningTypes。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    List<DictItem> listWarningTypes();

    @Insert("""
        INSERT INTO sys_dict_warning_type (type_code, type_name, type_desc, sort)
        VALUES (#{typeCode}, #{typeName}, #{typeDesc}, #{sort})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "dictId")
    /**
     * SQL说明：新增数据库操作，方法名为 insertWarningType。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int insertWarningType(DictItem item);

    @Update("""
        UPDATE sys_dict_warning_type
        SET type_code = #{typeCode},
            type_name = #{typeName},
            type_desc = #{typeDesc},
            sort = #{sort}
        WHERE dict_id = #{dictId}
        """)
    /**
     * SQL说明：修改数据库操作，方法名为 updateWarningType。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int updateWarningType(DictItem item);

    @Delete("DELETE FROM sys_dict_warning_type WHERE dict_id = #{id}")
    /**
     * SQL说明：删除数据库操作，方法名为 deleteWarningType。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int deleteWarningType(@Param("id") Integer id);

    @Select("SELECT * FROM sys_dict_equipment_type ORDER BY sort ASC, dict_id ASC")
    /**
     * SQL说明：查询数据库操作，方法名为 listEquipmentTypes。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    List<DictItem> listEquipmentTypes();

    @Insert("""
        INSERT INTO sys_dict_equipment_type (type_code, type_name, type_desc, sort)
        VALUES (#{typeCode}, #{typeName}, #{typeDesc}, #{sort})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "dictId")
    /**
     * SQL说明：新增数据库操作，方法名为 insertEquipmentType。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int insertEquipmentType(DictItem item);

    @Update("""
        UPDATE sys_dict_equipment_type
        SET type_code = #{typeCode},
            type_name = #{typeName},
            type_desc = #{typeDesc},
            sort = #{sort}
        WHERE dict_id = #{dictId}
        """)
    /**
     * SQL说明：修改数据库操作，方法名为 updateEquipmentType。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int updateEquipmentType(DictItem item);

    @Delete("DELETE FROM sys_dict_equipment_type WHERE dict_id = #{id}")
    /**
     * SQL说明：删除数据库操作，方法名为 deleteEquipmentType。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int deleteEquipmentType(@Param("id") Integer id);
}
