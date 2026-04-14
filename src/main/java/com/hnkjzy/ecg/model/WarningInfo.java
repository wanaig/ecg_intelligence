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
 * 类说明：WarningInfo。
 * 业务定位：负责承载实体对象、视图对象和请求对象。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class WarningInfo {

    /**
     * 字段说明：主键或关联标识，字段名为 warningId。
     */
    private Integer warningId;
    /**
     * 字段说明：主键或关联标识，字段名为 measureId。
     */
    private Integer measureId;
    /**
     * 字段说明：主键或关联标识，字段名为 patientId。
     */
    private Integer patientId;
    /**
     * 字段说明：时间信息，字段名为 warningTime。
     */
    private LocalDateTime warningTime;
    /**
     * 字段说明：类型标记，字段名为 warningType。
     */
    private String warningType;
    /**
     * 字段说明：业务字段，字段名为 warningLevel。
     */
    private String warningLevel;
    /**
     * 字段说明：文本说明，字段名为 warningContent。
     */
    private String warningContent;
    /**
     * 字段说明：主键或关联标识，字段名为 handleDoctorId。
     */
    private Integer handleDoctorId;
    /**
     * 字段说明：时间信息，字段名为 handleTime。
     */
    private LocalDateTime handleTime;
    /**
     * 字段说明：文本说明，字段名为 handleResult。
     */
    private String handleResult;
    /**
     * 字段说明：状态标记，字段名为 warningStatus。
     */
    private String warningStatus;
}
