package com.hnkjzy.ecg.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class QualityDto {

    @Data
    public static class ReportAppealRequest {
        @NotBlank(message = "appealReason不能为空")
        @Size(max = 500, message = "appealReason长度不能超过500")
        private String appealReason;

        @Size(max = 10, message = "attachments数量不能超过10")
        private List<String> attachments;
    }
}
