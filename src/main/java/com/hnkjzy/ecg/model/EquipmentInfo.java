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
 * 类说明：EquipmentInfo。
 * 业务定位：负责承载实体对象、视图对象和请求对象。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class EquipmentInfo {

    /**
     * 字段说明：主键或关联标识，字段名为 equipmentId。
     */
    private Integer equipmentId;
    /**
     * 字段说明：编号或编码信息，字段名为 equipmentCode。
     */
    private String equipmentCode;
    /**
     * 字段说明：名称信息，字段名为 equipmentName。
     */
    private String equipmentName;
    /**
     * 字段说明：类型标记，字段名为 equipmentType。
     */
    private String equipmentType;
    /**
     * 字段说明：业务字段，字段名为 manufacturer。
     */
    private String manufacturer;
    /**
     * 字段说明：业务字段，字段名为 equipmentVersion。
     */
    private String equipmentVersion;
    /**
     * 字段说明：主键或关联标识，字段名为 wardId。
     */
    private Integer wardId;
    /**
     * 字段说明：状态标记，字段名为 equipmentStatus。
     */
    private String equipmentStatus;
    /**
     * 字段说明：时间信息，字段名为 installTime。
     */
    private LocalDateTime installTime;
    /**
     * 字段说明：时间信息，字段名为 maintainTime。
     */
    private LocalDateTime maintainTime;
    /**
     * 字段说明：时间信息，字段名为 nextMaintainTime。
     */
    private LocalDateTime nextMaintainTime;
}
