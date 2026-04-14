package com.hnkjzy.ecg.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class WarningQueryRow {

    private Long warningId;
    private LocalDateTime warningTime;
    private String patientName;
    private String patientGender;
    private Integer patientAge;
    private String admissionNo;
    private String bedNo;
    private LocalDate admissionDate;
    private String detailsText;
    private String warningLevel;
    private String warningStatus;
    private Long wardId;
    private String wardName;
    private String wardPhone;
    private String indexCode;
    private String indexName;
}
