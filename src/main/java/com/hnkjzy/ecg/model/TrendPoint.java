package com.hnkjzy.ecg.model;

/**
 * 文件说明：数据模型文件。
 * 主要职责：负责承载实体对象、视图对象和请求对象。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import lombok.Data;

@Data
/**
 * 类说明：TrendPoint。
 * 业务定位：负责承载实体对象、视图对象和请求对象。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class TrendPoint {

    /**
     * 字段说明：业务字段，字段名为 label。
     */
    private String label;
    /**
     * 字段说明：业务字段，字段名为 value。
     */
    private Long value;
}
