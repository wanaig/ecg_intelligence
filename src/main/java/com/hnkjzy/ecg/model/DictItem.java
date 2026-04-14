package com.hnkjzy.ecg.model;

/**
 * 文件说明：数据模型文件。
 * 主要职责：负责承载实体对象、视图对象和请求对象。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import lombok.Data;

@Data
/**
 * 类说明：DictItem。
 * 业务定位：负责承载实体对象、视图对象和请求对象。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class DictItem {

    /**
     * 字段说明：主键或关联标识，字段名为 dictId。
     */
    private Integer dictId;
    /**
     * 字段说明：编号或编码信息，字段名为 typeCode。
     */
    private String typeCode;
    /**
     * 字段说明：名称信息，字段名为 typeName。
     */
    private String typeName;
    /**
     * 字段说明：文本说明，字段名为 typeDesc。
     */
    private String typeDesc;
    /**
     * 字段说明：业务字段，字段名为 sort。
     */
    private Integer sort;
}
