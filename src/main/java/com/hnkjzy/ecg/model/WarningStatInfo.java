package com.hnkjzy.ecg.model;

/**
 * 文件说明：数据模型文件。
 * 主要职责：负责承载实体对象、视图对象和请求对象。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
/**
 * 类说明：WarningStatInfo。
 * 业务定位：负责承载实体对象、视图对象和请求对象。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class WarningStatInfo {

    /**
     * 字段说明：主键或关联标识，字段名为 warningStatId。
     */
    private Integer warningStatId;
    /**
     * 字段说明：时间信息，字段名为 statTime。
     */
    private LocalDateTime statTime;
    /**
     * 字段说明：主键或关联标识，字段名为 wardId。
     */
    private Integer wardId;
    /**
     * 字段说明：业务字段，字段名为 warningLevel。
     */
    private String warningLevel;
    /**
     * 字段说明：类型标记，字段名为 warningType。
     */
    private String warningType;
    /**
     * 字段说明：业务字段，字段名为 totalWarning。
     */
    private Integer totalWarning;
    /**
     * 字段说明：业务字段，字段名为 handledWarning。
     */
    private Integer handledWarning;
    /**
     * 字段说明：业务字段，字段名为 unhandledWarning。
     */
    private Integer unhandledWarning;
    /**
     * 字段说明：时间信息，字段名为 avgHandleTime。
     */
    private BigDecimal avgHandleTime;
    /**
     * 字段说明：业务字段，字段名为 statCycle。
     */
    private String statCycle;
}
