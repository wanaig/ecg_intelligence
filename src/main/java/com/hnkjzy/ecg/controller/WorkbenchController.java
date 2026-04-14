package com.hnkjzy.ecg.controller;

import com.hnkjzy.ecg.common.ApiResponse;
import com.hnkjzy.ecg.common.PageResult;
import com.hnkjzy.ecg.service.ApiSpecService;
import com.hnkjzy.ecg.vo.WorkbenchVo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workbench")
@Validated
public class WorkbenchController {

    private final ApiSpecService apiSpecService;

    public WorkbenchController(ApiSpecService apiSpecService) {
        this.apiSpecService = apiSpecService;
    }

    @GetMapping("/overview")
    public ApiResponse<WorkbenchVo.OverviewData> overview(@RequestParam(required = false) String dateType,
                                                          @RequestParam(required = false) String startDate,
                                                          @RequestParam(required = false) String endDate) {
        return ApiResponse.success(apiSpecService.getWorkbenchOverview(dateType, startDate, endDate));
    }

    @GetMapping("/charts/warning-trend")
    public ApiResponse<List<WorkbenchVo.TrendPointItem>> warningTrend() {
        return ApiResponse.success(apiSpecService.getWarningTrendChart());
    }

    @GetMapping("/charts/ward-warning-rank")
    public ApiResponse<List<WorkbenchVo.WardWarningRankItem>> wardWarningRank() {
        return ApiResponse.success(apiSpecService.getWardWarningRankChart());
    }

    @GetMapping("/patients")
    public ApiResponse<PageResult<WorkbenchVo.WorkbenchPatientItem>> patients(
            @RequestParam(required = false) String ward,
            @RequestParam(required = false) String tab,
            @RequestParam(required = false) @Min(value = 1, message = "pageNum必须大于等于1") Integer pageNum,
            @RequestParam(required = false) @Min(value = 1, message = "pageSize必须大于等于1") Integer pageSize) {
        return ApiResponse.success(apiSpecService.pageWorkbenchPatients(ward, tab, pageNum, pageSize));
    }

    @GetMapping("/patients/{inpatientNo}/records")
    public ApiResponse<List<WorkbenchVo.PatientRecordItem>> records(@PathVariable @NotBlank(message = "inpatientNo不能为空") String inpatientNo) {
        return ApiResponse.success(apiSpecService.listWorkbenchPatientRecords(inpatientNo));
    }
}
