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
 * 类说明：ReportInfo。
 * 业务定位：负责承载实体对象、视图对象和请求对象。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class ReportInfo {

    /**
     * 字段说明：主键或关联标识，字段名为 reportId。
     */
    private Integer reportId;
    /**
     * 字段说明：主键或关联标识，字段名为 measureId。
     */
    private Integer measureId;
    /**
     * 字段说明：主键或关联标识，字段名为 aiAnalysisId。
     */
    private Integer aiAnalysisId;
    /**
     * 字段说明：主键或关联标识，字段名为 patientId。
     */
    private Integer patientId;
    /**
     * 字段说明：编号或编码信息，字段名为 reportNo。
     */
    private String reportNo;
    /**
     * 字段说明：文本说明，字段名为 reportContent。
     */
    private String reportContent;
    /**
     * 字段说明：文本说明，字段名为 diagnosisResult。
     */
    private String diagnosisResult;
    /**
     * 字段说明：主键或关联标识，字段名为 createDoctorId。
     */
    private Integer createDoctorId;
    /**
     * 字段说明：主键或关联标识，字段名为 reviewDoctorId。
     */
    private Integer reviewDoctorId;
    /**
     * 字段说明：时间信息，字段名为 createTime。
     */
    private LocalDateTime createTime;
    /**
     * 字段说明：时间信息，字段名为 reviewTime。
     */
    private LocalDateTime reviewTime;
    /**
     * 字段说明：状态标记，字段名为 reportStatus。
     */
    private String reportStatus;
    /**
     * 字段说明：文本说明，字段名为 reportPath。
     */
    private String reportPath;
}
