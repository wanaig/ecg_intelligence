package com.hnkjzy.ecg.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BloodGlucoseDto {

    @Data
    public static class WarningActionRequest {
        @Size(max = 200, message = "reason长度不能超过200")
        private String reason;

        @Positive(message = "operatorId必须为正数")
        private Long operatorId;
    }

    @Data
    public static class LeaveBedRequest {
        @NotBlank(message = "leaveTime不能为空")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}( \\d{2}:\\d{2}:\\d{2})?$", message = "leaveTime格式应为YYYY-MM-DD或YYYY-MM-DD HH:mm:ss")
        private String leaveTime;

        @NotBlank(message = "reason不能为空")
        @Size(max = 200, message = "reason长度不能超过200")
        private String reason;
    }

    @Data
    public static class AssignBedRequest {
        @NotBlank(message = "patientId不能为空")
        @Size(max = 32, message = "patientId长度不能超过32")
        private String patientId;

        @NotNull(message = "roomId不能为空")
        @Positive(message = "roomId必须为正数")
        private Long roomId;

        @NotBlank(message = "assignTime不能为空")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}( \\d{2}:\\d{2}:\\d{2})?$", message = "assignTime格式应为YYYY-MM-DD或YYYY-MM-DD HH:mm:ss")
        private String assignTime;
    }

    @Data
    public static class PatientManageActionRequest {
        @Size(max = 200, message = "reason长度不能超过200")
        private String reason;

        @Positive(message = "operatorId必须为正数")
        private Long operatorId;
    }
}
