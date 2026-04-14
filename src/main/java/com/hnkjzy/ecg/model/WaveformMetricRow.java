package com.hnkjzy.ecg.model;

import lombok.Data;

@Data
public class WaveformMetricRow {

    private String patientId;
    private Long measureId;
    private Integer sampleRate;
    private Double heartRate;
    private Double stSegment;
    private Double qt;
    private Double qrs;
}
