package com.hnkjzy.ecg.model;

/**
 * 文件说明：数据模型文件。
 * 主要职责：负责承载实体对象、视图对象和请求对象。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
/**
 * 类说明：PatientInfo。
 * 业务定位：负责承载实体对象、视图对象和请求对象。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class PatientInfo {

    /**
     * 字段说明：主键或关联标识，字段名为 patientId。
     */
    private Integer patientId;
    /**
     * 字段说明：名称信息，字段名为 patientName。
     */
    private String patientName;
    /**
     * 字段说明：性别信息，字段名为 patientGender。
     */
    private String patientGender;
    /**
     * 字段说明：年龄信息，字段名为 patientAge。
     */
    private Integer patientAge;
    /**
     * 字段说明：编号或编码信息，字段名为 inpatientNo。
     */
    private String inpatientNo;
    /**
     * 字段说明：编号或编码信息，字段名为 outpatientNo。
     */
    private String outpatientNo;
    /**
     * 字段说明：时间信息，字段名为 inpatientDate。
     */
    private LocalDate inpatientDate;
    /**
     * 字段说明：编号或编码信息，字段名为 bedNo。
     */
    private String bedNo;
    /**
     * 字段说明：文本说明，字段名为 inpatientDiagnosis。
     */
    private String inpatientDiagnosis;
    /**
     * 字段说明：主键或关联标识，字段名为 wardId。
     */
    private Integer wardId;
    /**
     * 字段说明：联系方式，字段名为 phone。
     */
    private String phone;
    /**
     * 字段说明：时间信息，字段名为 createTime。
     */
    private LocalDateTime createTime;
}
