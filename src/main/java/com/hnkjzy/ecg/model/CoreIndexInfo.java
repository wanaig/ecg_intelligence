package com.hnkjzy.ecg.model;

/**
 * 文件说明：数据模型文件。
 * 主要职责：负责承载实体对象、视图对象和请求对象。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import java.math.BigDecimal;
import lombok.Data;

@Data
/**
 * 类说明：CoreIndexInfo。
 * 业务定位：负责承载实体对象、视图对象和请求对象。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class CoreIndexInfo {

    /**
     * 字段说明：主键或关联标识，字段名为 indexId。
     */
    private Integer indexId;
    /**
     * 字段说明：主键或关联标识，字段名为 measureId。
     */
    private Integer measureId;
    /**
     * 字段说明：统计数量，字段名为 heartbeatTotal。
     */
    private Integer heartbeatTotal;
    /**
     * 字段说明：指标数值，字段名为 avgHeartRate。
     */
    private BigDecimal avgHeartRate;
    /**
     * 字段说明：指标数值，字段名为 minHeartRate。
     */
    private Integer minHeartRate;
    /**
     * 字段说明：指标数值，字段名为 maxHeartRate。
     */
    private Integer maxHeartRate;
    /**
     * 字段说明：业务字段，字段名为 maxRrInterval。
     */
    private BigDecimal maxRrInterval;
    /**
     * 字段说明：统计数量，字段名为 maxRrCount。
     */
    private Integer maxRrCount;
    /**
     * 字段说明：业务字段，字段名为 tachycardiaTimes。
     */
    private Integer tachycardiaTimes;
    /**
     * 字段说明：业务字段，字段名为 tachycardiaHeartbeat。
     */
    private Integer tachycardiaHeartbeat;
    /**
     * 字段说明：业务字段，字段名为 bradycardiaTimes。
     */
    private Integer bradycardiaTimes;
    /**
     * 字段说明：业务字段，字段名为 atrialPremature。
     */
    private Integer atrialPremature;
    /**
     * 字段说明：业务字段，字段名为 atrialPrematureSingle。
     */
    private Integer atrialPrematureSingle;
    /**
     * 字段说明：业务字段，字段名为 atrialPrematurePair。
     */
    private Integer atrialPrematurePair;
    /**
     * 字段说明：业务字段，字段名为 ventricularPremature。
     */
    private Integer ventricularPremature;
    /**
     * 字段说明：业务字段，字段名为 ventricularPrematureSingle。
     */
    private Integer ventricularPrematureSingle;
    /**
     * 字段说明：业务字段，字段名为 ventricularPrematurePair。
     */
    private Integer ventricularPrematurePair;
    /**
     * 字段说明：指标数值，字段名为 hrvSdnn。
     */
    private BigDecimal hrvSdnn;
    /**
     * 字段说明：指标数值，字段名为 hrvSdann。
     */
    private BigDecimal hrvSdann;
    /**
     * 字段说明：指标数值，字段名为 hrvRmssd。
     */
    private BigDecimal hrvRmssd;
    /**
     * 字段说明：指标数值，字段名为 hrvPnn50。
     */
    private BigDecimal hrvPnn50;
    /**
     * 字段说明：指标数值，字段名为 hrvLf。
     */
    private BigDecimal hrvLf;
    /**
     * 字段说明：指标数值，字段名为 hrvHf。
     */
    private BigDecimal hrvHf;
    /**
     * 字段说明：指标数值，字段名为 hrvLfHf。
     */
    private BigDecimal hrvLfHf;
    /**
     * 字段说明：业务字段，字段名为 ventricularTachycardia。
     */
    private Integer ventricularTachycardia;
    /**
     * 字段说明：业务字段，字段名为 atrialTachycardia。
     */
    private Integer atrialTachycardia;
    /**
     * 字段说明：类型标记，字段名为 rhythmType。
     */
    private String rhythmType;
}
