package com.hnkjzy.ecg.mapper;

/**
 * 文件说明：数据访问文件。
 * 主要职责：负责定义 MyBatis SQL 访问方法与统计查询。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import com.hnkjzy.ecg.model.CoreIndexInfo;
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
 * 类说明：CoreIndexMapper。
 * 业务定位：负责定义 MyBatis SQL 访问方法与统计查询。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public interface CoreIndexMapper {

    @Select("""
        <script>
        SELECT *
        FROM sys_ecg_core_index
        <where>
            <if test='measureId != null'>
                AND measure_id = #{measureId}
            </if>
            <if test='rhythmType != null and rhythmType != ""'>
                AND rhythm_type = #{rhythmType}
            </if>
        </where>
        ORDER BY index_id DESC
        LIMIT #{offset}, #{size}
        </script>
        """)
    List<CoreIndexInfo> selectPage(@Param("measureId") Integer measureId,
                                   @Param("rhythmType") String rhythmType,
                                   @Param("offset") int offset,
                                   @Param("size") int size);

    @Select("""
        <script>
        SELECT COUNT(*)
        FROM sys_ecg_core_index
        <where>
            <if test='measureId != null'>
                AND measure_id = #{measureId}
            </if>
            <if test='rhythmType != null and rhythmType != ""'>
                AND rhythm_type = #{rhythmType}
            </if>
        </where>
        </script>
        """)
    /**
     * SQL说明：业务处理数据库操作，方法名为 count。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    long count(@Param("measureId") Integer measureId, @Param("rhythmType") String rhythmType);

    @Select("SELECT * FROM sys_ecg_core_index WHERE index_id = #{id}")
    /**
     * SQL说明：查询数据库操作，方法名为 findById。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    CoreIndexInfo findById(@Param("id") Integer id);

    @Select("""
        SELECT *
        FROM sys_ecg_core_index
        WHERE (max_rr_interval IS NOT NULL AND max_rr_interval >= 2.0)
           OR (ventricular_tachycardia IS NOT NULL AND ventricular_tachycardia > 0)
           OR (atrial_tachycardia IS NOT NULL AND atrial_tachycardia > 0)
        ORDER BY index_id DESC
        """)
    /**
     * SQL说明：业务处理数据库操作，方法名为 selectHighRisk。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    List<CoreIndexInfo> selectHighRisk();

    @Insert("""
        INSERT INTO sys_ecg_core_index (
            measure_id, heartbeat_total, avg_heart_rate, min_heart_rate, max_heart_rate,
            max_rr_interval, max_rr_count, tachycardia_times, tachycardia_heartbeat,
            bradycardia_times, atrial_premature, atrial_premature_single, atrial_premature_pair,
            ventricular_premature, ventricular_premature_single, ventricular_premature_pair,
            hrv_sdnn, hrv_sdann, hrv_rmssd, hrv_pnn50, hrv_lf, hrv_hf, hrv_lf_hf,
            ventricular_tachycardia, atrial_tachycardia, rhythm_type
        ) VALUES (
            #{measureId}, #{heartbeatTotal}, #{avgHeartRate}, #{minHeartRate}, #{maxHeartRate},
            #{maxRrInterval}, #{maxRrCount}, #{tachycardiaTimes}, #{tachycardiaHeartbeat},
            #{bradycardiaTimes}, #{atrialPremature}, #{atrialPrematureSingle}, #{atrialPrematurePair},
            #{ventricularPremature}, #{ventricularPrematureSingle}, #{ventricularPrematurePair},
            #{hrvSdnn}, #{hrvSdann}, #{hrvRmssd}, #{hrvPnn50}, #{hrvLf}, #{hrvHf}, #{hrvLfHf},
            #{ventricularTachycardia}, #{atrialTachycardia}, #{rhythmType}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "indexId")
    /**
     * SQL说明：新增数据库操作，方法名为 insert。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int insert(CoreIndexInfo coreIndexInfo);

    @Update("""
        UPDATE sys_ecg_core_index
        SET measure_id = #{measureId},
            heartbeat_total = #{heartbeatTotal},
            avg_heart_rate = #{avgHeartRate},
            min_heart_rate = #{minHeartRate},
            max_heart_rate = #{maxHeartRate},
            max_rr_interval = #{maxRrInterval},
            max_rr_count = #{maxRrCount},
            tachycardia_times = #{tachycardiaTimes},
            tachycardia_heartbeat = #{tachycardiaHeartbeat},
            bradycardia_times = #{bradycardiaTimes},
            atrial_premature = #{atrialPremature},
            atrial_premature_single = #{atrialPrematureSingle},
            atrial_premature_pair = #{atrialPrematurePair},
            ventricular_premature = #{ventricularPremature},
            ventricular_premature_single = #{ventricularPrematureSingle},
            ventricular_premature_pair = #{ventricularPrematurePair},
            hrv_sdnn = #{hrvSdnn},
            hrv_sdann = #{hrvSdann},
            hrv_rmssd = #{hrvRmssd},
            hrv_pnn50 = #{hrvPnn50},
            hrv_lf = #{hrvLf},
            hrv_hf = #{hrvHf},
            hrv_lf_hf = #{hrvLfHf},
            ventricular_tachycardia = #{ventricularTachycardia},
            atrial_tachycardia = #{atrialTachycardia},
            rhythm_type = #{rhythmType}
        WHERE index_id = #{indexId}
        """)
    /**
     * SQL说明：修改数据库操作，方法名为 update。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int update(CoreIndexInfo coreIndexInfo);

    @Delete("DELETE FROM sys_ecg_core_index WHERE index_id = #{id}")
    /**
     * SQL说明：删除数据库操作，方法名为 deleteById。
     * 使用约定：入参与SQL条件保持一致，出参与业务模型字段保持映射一致。
     */
    int deleteById(@Param("id") Integer id);
}
