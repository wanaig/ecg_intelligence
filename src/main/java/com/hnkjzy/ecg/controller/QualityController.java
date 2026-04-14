package com.hnkjzy.ecg.controller;

import com.hnkjzy.ecg.common.ApiResponse;
import com.hnkjzy.ecg.common.PageResult;
import com.hnkjzy.ecg.dto.QualityDto;
import com.hnkjzy.ecg.service.ApiSpecService;
import com.hnkjzy.ecg.vo.QualityVo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quality")
@Validated
public class QualityController {

    private final ApiSpecService apiSpecService;

    public QualityController(ApiSpecService apiSpecService) {
        this.apiSpecService = apiSpecService;
    }

    @GetMapping("/data")
    public ApiResponse<PageResult<QualityVo.DataQualityItem>> data(
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String qualityLevel,
            @RequestParam(required = false) @Min(value = 1, message = "pageNum必须大于等于1") Integer pageNum,
            @RequestParam(required = false) @Min(value = 1, message = "pageSize必须大于等于1") Integer pageSize) {
        return ApiResponse.success(apiSpecService.pageDataQuality(patientId, qualityLevel, pageNum, pageSize));
    }

    @GetMapping("/data/{id}")
    public ApiResponse<QualityVo.DataQualityItem> dataDetail(@PathVariable @Positive(message = "id必须为正数") Long id) {
        return ApiResponse.success(apiSpecService.getDataQualityDetail(id));
    }

    @GetMapping("/device")
    public ApiResponse<PageResult<QualityVo.DeviceQualityItem>> device(
            @RequestParam(required = false) String mac,
            @RequestParam(required = false) String deviceStatus,
            @RequestParam(required = false) @Min(value = 1, message = "pageNum必须大于等于1") Integer pageNum,
            @RequestParam(required = false) @Min(value = 1, message = "pageSize必须大于等于1") Integer pageSize) {
        return ApiResponse.success(apiSpecService.pageDeviceQuality(mac, deviceStatus, pageNum, pageSize));
    }

    @GetMapping("/device/{id}/records")
    public ApiResponse<List<QualityVo.DeviceRecordItem>> deviceRecords(@PathVariable @Positive(message = "id必须为正数") Long id) {
        return ApiResponse.success(apiSpecService.listDeviceQualityRecords(id));
    }

    @GetMapping("/report")
    public ApiResponse<PageResult<QualityVo.ReportQualityItem>> report(
            @RequestParam(required = false) String doctor,
            @RequestParam(required = false) String resultType,
            @RequestParam(required = false) @Min(value = 1, message = "pageNum必须大于等于1") Integer pageNum,
            @RequestParam(required = false) @Min(value = 1, message = "pageSize必须大于等于1") Integer pageSize) {
        return ApiResponse.success(apiSpecService.pageReportQuality(doctor, resultType, pageNum, pageSize));
    }

    @GetMapping("/report/{reportId}")
    public ApiResponse<QualityVo.ReportQualityItem> reportDetail(@PathVariable @NotBlank(message = "reportId不能为空") String reportId) {
        return ApiResponse.success(apiSpecService.getReportQualityDetail(reportId));
    }

    @PostMapping("/report/{reportId}/appeals")
    public ApiResponse<QualityVo.AppealResult> appeal(
            @PathVariable @NotBlank(message = "reportId不能为空") String reportId,
            @Valid @RequestBody QualityDto.ReportAppealRequest request) {
        return ApiResponse.success(apiSpecService.appealReport(reportId, request));
    }
}
