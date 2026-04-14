package com.hnkjzy.ecg.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class WorkbenchVo {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OverviewData {
        private List<StatItem> stats;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatItem {
        private String label;
        private Number value;
        private String unit;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrendPointItem {
        private String day;
        private Integer value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WardWarningRankItem {
        private String ward;
        private Integer value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkbenchPatientItem {
        private String info;
        private String ward;
        private String bedNo;
        private String inpatientNo;
        private String deviceNo;
        private String attachTime;
        private String changeTime;
        private Integer abnTimes;
        private Integer maxHr;
        private Integer lowBgTimes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientRecordItem {
        private String recordTime;
        private String eventType;
        private String detail;
        private String operator;
    }
}
