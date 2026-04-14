package com.hnkjzy.ecg.model;

/**
 * 文件说明：数据模型文件。
 * 主要职责：负责承载实体对象、视图对象和请求对象。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import lombok.Data;

@Data
/**
 * 类说明：SupplierSummary。
 * 业务定位：负责承载实体对象、视图对象和请求对象。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class SupplierSummary {

    /**
     * 字段说明：名称信息，字段名为 supplierName。
     */
    private String supplierName;
    /**
     * 字段说明：统计数量，字段名为 equipmentCount。
     */
    private Long equipmentCount;
    /**
     * 字段说明：业务字段，字段名为 equipmentTypes。
     */
    private String equipmentTypes;
}
