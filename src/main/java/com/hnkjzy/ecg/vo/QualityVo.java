package com.hnkjzy.ecg.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class QualityVo {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataQualityItem {
        private String patientId;
        private String name;
        private String recordTime;
        private String type;
        private Double score;
        private String level;
        private String issues;
        private String status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeviceQualityItem {
        private String mac;
        private String model;
        private String dept;
        private String checkTime;
        private String nextCheckTime;
        private Double passRate;
        private String status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeviceRecordItem {
        private String checkItem;
        private String result;
        private String operator;
        private String checkTime;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportQualityItem {
        private String reportId;
        private String patient;
        private String doctor;
        private String reviewer;
        private Double score;
        private String status;
        private String errorType;
        private String time;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppealResult {
        private String reportId;
        private String status;
        private String message;
    }
}
