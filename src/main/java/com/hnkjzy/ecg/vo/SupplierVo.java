package com.hnkjzy.ecg.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SupplierVo {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VendorItem {
        private Long id;
        private String code;
        private String name;
        private String type;
        private String contact;
        private String phone;
        private String status;
        private String createTime;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VendorQualificationItem {
        private String vendorName;
        private String certName;
        private String expireDate;
        private String status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VendorDeviceLedgerItem {
        private String vendorName;
        private String deviceModel;
        private String deviceType;
        private Integer totalSupply;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeviceBasicLedgerItem {
        private String code;
        private String name;
        private String sn;
        private String dept;
        private String status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeviceBindingItem {
        private String code;
        private String patient;
        private String bindTime;
        private String status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeviceMaintenanceItem {
        private String code;
        private String type;
        private String date;
        private String operator;
        private String result;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeviceStatusItem {
        private String code;
        private Integer battery;
        private Integer signal;
        private String onlineStatus;
        private String lastActive;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsumableInfoItem {
        private String code;
        private String name;
        private String spec;
        private String unit;
        private String brand;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsumableInventoryItem {
        private String name;
        private Integer stock;
        private String unit;
        private String warningLevel;
        private String status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsumableTraceabilityItem {
        private String name;
        private Integer amount;
        private String dept;
        private String applicant;
        private String time;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcurementOrderItem {
        private String orderNo;
        private String itemName;
        private Integer amount;
        private String vendor;
        private String status;
        private String date;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcurementAcceptanceItem {
        private String orderNo;
        private String itemName;
        private Integer amount;
        private String receiver;
        private String date;
        private String status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcurementStatisticsItem {
        private String month;
        private Double totalAmount;
        private Integer deviceCount;
        private Integer consumableCount;
    }
}
