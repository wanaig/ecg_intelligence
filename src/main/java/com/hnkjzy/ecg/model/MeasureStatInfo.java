package com.hnkjzy.ecg.model;

/**
 * 文件说明：数据模型文件。
 * 主要职责：负责承载实体对象、视图对象和请求对象。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import java.time.LocalDateTime;
import lombok.Data;

@Data
/**
 * 类说明：MeasureStatInfo。
 * 业务定位：负责承载实体对象、视图对象和请求对象。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class MeasureStatInfo {

    /**
     * 字段说明：主键或关联标识，字段名为 statId。
     */
    private Integer statId;
    /**
     * 字段说明：时间信息，字段名为 statTime。
     */
    private LocalDateTime statTime;
    /**
     * 字段说明：主键或关联标识，字段名为 wardId。
     */
    private Integer wardId;
    /**
     * 字段说明：类型标记，字段名为 equipmentType。
     */
    private String equipmentType;
    /**
     * 字段说明：业务字段，字段名为 totalMeasure。
     */
    private Integer totalMeasure;
    /**
     * 字段说明：业务字段，字段名为 normalMeasure。
     */
    private Integer normalMeasure;
    /**
     * 字段说明：业务字段，字段名为 abnormalMeasure。
     */
    private Integer abnormalMeasure;
    /**
     * 字段说明：业务字段，字段名为 statCycle。
     */
    private String statCycle;
}
