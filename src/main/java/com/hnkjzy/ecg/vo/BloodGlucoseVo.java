package com.hnkjzy.ecg.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class BloodGlucoseVo {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentItem {
        private Long departmentId;
        private String departmentName;
        private String wardPhone;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IndexDictItem {
        private String indexCode;
        private String indexName;
        private String unit;
        private Double refMin;
        private Double refMax;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WarningItem {
        private Long warningId;
        private String warningTime;
        private String patientInfo;
        private String admissionNo;
        private String bedNo;
        private String admissionDate;
        private String detailsText;
        private String warningLevel;
        private String warningStatus;
        private WardInfo wardInfo;
        private IndexInfo index;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WardInfo {
        private Long wardId;
        private String wardName;
        private String wardPhone;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IndexInfo {
        private String indexCode;
        private String indexName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientMetricItem {
        private Long id;
        private String patientId;
        private String name;
        private String bedNo;
        private String measureTime;
        private Double heartRate;
        private Double stSegment;
        private Double qt;
        private Double qrs;
        private String status;
        private String desc;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WaveformData {
        private String patientId;
        private Long measureId;
        private Integer sampleRate;
        private List<Double> points;
        private WaveformMetrics metrics;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WaveformMetrics {
        private Double heartRate;
        private Double stSegment;
        private Double qt;
        private Double qrs;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientListItem {
        private String ward;
        private String bedNo;
        private String patientInfo;
        private String tag;
        private String admissionNo;
        private String admissionDate;
        private String diagnosis;
        private String groupStatus;
        private String hospitalStatus;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomItem {
        private Long id;
        private String name;
        private String nurse;
        private Integer totalBeds;
        private Integer occupiedBeds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomPatientItem {
        private String bed;
        private String time;
        private String name;
        private String day;
        private String condition;
        private String status;
        private String medication;
        private Integer hospitalDays;
        private Boolean isEmpty;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExportResult {
        private String downloadUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActionResult {
        private String action;
        private String message;
        private String targetId;
    }
}
