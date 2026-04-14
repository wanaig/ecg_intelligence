package com.hnkjzy.ecg.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class SystemDto {

    @Data
    public static class UserUpsertRequest {
        @NotBlank(message = "userName不能为空")
        @Size(max = 32, message = "userName长度不能超过32")
        private String userName;

        @NotBlank(message = "realName不能为空")
        @Size(max = 32, message = "realName长度不能超过32")
        private String realName;

        @NotBlank(message = "dept不能为空")
        @Size(max = 64, message = "dept长度不能超过64")
        private String dept;

        @NotBlank(message = "role不能为空")
        @Size(max = 32, message = "role长度不能超过32")
        private String role;

        @NotBlank(message = "phone不能为空")
        @Pattern(regexp = "^[0-9-]{6,20}$", message = "phone格式不合法")
        private String phone;

        @NotBlank(message = "status不能为空")
        @Size(max = 16, message = "status长度不能超过16")
        private String status;
    }

    @Data
    public static class UserStatusPatchRequest {
        @NotBlank(message = "status不能为空")
        @Size(max = 16, message = "status长度不能超过16")
        private String status;
    }

    @Data
    public static class DepartmentUpsertRequest {
        @NotBlank(message = "deptName不能为空")
        @Size(max = 64, message = "deptName长度不能超过64")
        private String deptName;

        @NotBlank(message = "code不能为空")
        @Size(max = 32, message = "code长度不能超过32")
        private String code;

        @NotBlank(message = "head不能为空")
        @Size(max = 32, message = "head长度不能超过32")
        private String head;

        @NotBlank(message = "phone不能为空")
        @Pattern(regexp = "^[0-9-]{6,20}$", message = "phone格式不合法")
        private String phone;

        @NotBlank(message = "status不能为空")
        @Size(max = 16, message = "status长度不能超过16")
        private String status;

        @NotNull(message = "order不能为空")
        @Positive(message = "order必须为正数")
        private Integer order;
    }

    @Data
    public static class RoleUpsertRequest {
        @NotBlank(message = "roleName不能为空")
        @Size(max = 64, message = "roleName长度不能超过64")
        private String roleName;

        @NotBlank(message = "roleKey不能为空")
        @Size(max = 64, message = "roleKey长度不能超过64")
        private String roleKey;

        @NotNull(message = "order不能为空")
        @Positive(message = "order必须为正数")
        private Integer order;

        @NotBlank(message = "status不能为空")
        @Size(max = 16, message = "status长度不能超过16")
        private String status;

        @Size(max = 32, message = "createTime长度不能超过32")
        private String createTime;

        @Size(max = 500, message = "remark长度不能超过500")
        private String remark;
    }

    @Data
    public static class RoleMenuUpdateRequest {
        @NotEmpty(message = "menuIds不能为空")
        private List<Long> menuIds;
    }
}
