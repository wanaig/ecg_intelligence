package com.hnkjzy.ecg.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AnalysisVo {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardData {
        private List<NamedValue> genderDist;
        private List<NamedValue> ageDist;
        private List<NamedValue> warningWardTop;
        private List<NamedValue> managedOverview;
        private List<NamedValue> activeWards;
        private List<NamedValue> glucoseDistManaged;
        private List<NamedValue> glucoseDistHospital;
        private List<NamedValue> abnormalWardTop;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NamedValue {
        private String label;
        private Number value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientWarningListGroup {
        private String label;
        private String value;
        private List<PatientWarningItem> data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientWarningItem {
        private String info;
        private String dept;
        private String bed;
        private String abnormal;
    }
}
