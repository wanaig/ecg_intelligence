package com.hnkjzy.ecg.controller;

import com.hnkjzy.ecg.common.ApiResponse;
import com.hnkjzy.ecg.service.ApiSpecService;
import com.hnkjzy.ecg.vo.AnalysisVo;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final ApiSpecService apiSpecService;

    public AnalysisController(ApiSpecService apiSpecService) {
        this.apiSpecService = apiSpecService;
    }

    @GetMapping("/dashboard")
    public ApiResponse<AnalysisVo.DashboardData> dashboard() {
        return ApiResponse.success(apiSpecService.getAnalysisDashboard());
    }

    @GetMapping("/patient-warning-lists")
    public ApiResponse<List<AnalysisVo.PatientWarningListGroup>> patientWarningLists() {
        return ApiResponse.success(apiSpecService.listPatientWarningLists());
    }
}
