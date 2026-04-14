package com.hnkjzy.ecg.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SystemVo {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserItem {
        private Long id;
        private String userName;
        private String realName;
        private String dept;
        private String role;
        private String phone;
        private String status;
        private String createTime;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentItem {
        private Long id;
        private String deptName;
        private String code;
        private String head;
        private String phone;
        private String status;
        private String createTime;
        private Integer order;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleItem {
        private Long id;
        private String roleName;
        private String roleKey;
        private Integer order;
        private String status;
        private String createTime;
        private String remark;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuTreeItem {
        private Long id;
        private String name;
        private String path;
        private List<MenuTreeItem> children;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperationResult {
        private String targetId;
        private String action;
        private String message;
    }
}
