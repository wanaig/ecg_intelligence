package com.hnkjzy.ecg.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SupplierDto {

    @Data
    public static class VendorUpsertRequest {
        @NotBlank(message = "code不能为空")
        @Size(max = 32, message = "code长度不能超过32")
        private String code;

        @NotBlank(message = "name不能为空")
        @Size(max = 64, message = "name长度不能超过64")
        private String name;

        @NotBlank(message = "type不能为空")
        @Size(max = 32, message = "type长度不能超过32")
        private String type;

        @NotBlank(message = "contact不能为空")
        @Size(max = 32, message = "contact长度不能超过32")
        private String contact;

        @NotBlank(message = "phone不能为空")
        @Pattern(regexp = "^[0-9-]{6,20}$", message = "phone格式不合法")
        private String phone;

        @NotBlank(message = "status不能为空")
        @Size(max = 16, message = "status长度不能超过16")
        private String status;
    }
}
