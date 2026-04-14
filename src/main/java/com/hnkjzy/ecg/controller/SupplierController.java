package com.hnkjzy.ecg.controller;

import com.hnkjzy.ecg.common.ApiResponse;
import com.hnkjzy.ecg.common.PageResult;
import com.hnkjzy.ecg.dto.SupplierDto;
import com.hnkjzy.ecg.service.ApiSpecService;
import com.hnkjzy.ecg.vo.CommonVo;
import com.hnkjzy.ecg.vo.SupplierVo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/supplier")
@Validated
public class SupplierController {

    private final ApiSpecService apiSpecService;

    public SupplierController(ApiSpecService apiSpecService) {
        this.apiSpecService = apiSpecService;
    }

    @GetMapping("/vendors")
    public ApiResponse<PageResult<SupplierVo.VendorItem>> vendors(@RequestParam(required = false) String vendorName,
                                                                  @RequestParam(required = false) @Min(value = 1, message = "pageNum必须大于等于1") Integer pageNum,
                                                                  @RequestParam(required = false) @Min(value = 1, message = "pageSize必须大于等于1") Integer pageSize) {
        return ApiResponse.success(apiSpecService.pageVendors(vendorName, pageNum, pageSize));
    }

    @PostMapping("/vendors")
    public ApiResponse<SupplierVo.VendorItem> createVendor(@Valid @RequestBody SupplierDto.VendorUpsertRequest request) {
        return ApiResponse.success(apiSpecService.createVendor(request));
    }

    @PutMapping("/vendors/{id}")
    public ApiResponse<SupplierVo.VendorItem> updateVendor(@PathVariable @Positive(message = "id必须为正数") Long id,
                                                           @Valid @RequestBody SupplierDto.VendorUpsertRequest request) {
        return ApiResponse.success(apiSpecService.updateVendor(id, request));
    }

    @DeleteMapping("/vendors/{id}")
    public ApiResponse<CommonVo.OperationResult> deleteVendor(@PathVariable @Positive(message = "id必须为正数") Long id) {
        return ApiResponse.success(apiSpecService.deleteVendor(id));
    }

    @GetMapping("/vendors/qualifications")
    public ApiResponse<List<SupplierVo.VendorQualificationItem>> vendorQualifications() {
        return ApiResponse.success(apiSpecService.listVendorQualifications());
    }

    @GetMapping("/vendors/device-ledger")
    public ApiResponse<List<SupplierVo.VendorDeviceLedgerItem>> vendorDeviceLedger() {
        return ApiResponse.success(apiSpecService.listVendorDeviceLedger());
    }

    @GetMapping("/devices/basic-ledger")
    public ApiResponse<List<SupplierVo.DeviceBasicLedgerItem>> deviceBasicLedger() {
        return ApiResponse.success(apiSpecService.listDeviceBasicLedger());
    }

    @GetMapping("/devices/binding")
    public ApiResponse<List<SupplierVo.DeviceBindingItem>> deviceBinding() {
        return ApiResponse.success(apiSpecService.listDeviceBinding());
    }

    @GetMapping("/devices/maintenance")
    public ApiResponse<List<SupplierVo.DeviceMaintenanceItem>> deviceMaintenance() {
        return ApiResponse.success(apiSpecService.listDeviceMaintenance());
    }

    @GetMapping("/devices/status")
    public ApiResponse<List<SupplierVo.DeviceStatusItem>> deviceStatus() {
        return ApiResponse.success(apiSpecService.listDeviceStatus());
    }

    @GetMapping("/consumables/info")
    public ApiResponse<List<SupplierVo.ConsumableInfoItem>> consumableInfo() {
        return ApiResponse.success(apiSpecService.listConsumableInfo());
    }

    @GetMapping("/consumables/inventory")
    public ApiResponse<List<SupplierVo.ConsumableInventoryItem>> consumableInventory() {
        return ApiResponse.success(apiSpecService.listConsumableInventory());
    }

    @GetMapping("/consumables/traceability")
    public ApiResponse<List<SupplierVo.ConsumableTraceabilityItem>> consumableTraceability() {
        return ApiResponse.success(apiSpecService.listConsumableTraceability());
    }

    @GetMapping("/procurement/order")
    public ApiResponse<List<SupplierVo.ProcurementOrderItem>> procurementOrder() {
        return ApiResponse.success(apiSpecService.listProcurementOrder());
    }

    @GetMapping("/procurement/acceptance")
    public ApiResponse<List<SupplierVo.ProcurementAcceptanceItem>> procurementAcceptance() {
        return ApiResponse.success(apiSpecService.listProcurementAcceptance());
    }

    @GetMapping("/procurement/statistics")
    public ApiResponse<List<SupplierVo.ProcurementStatisticsItem>> procurementStatistics() {
        return ApiResponse.success(apiSpecService.listProcurementStatistics());
    }
}
