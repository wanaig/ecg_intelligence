package com.hnkjzy.ecg.controller;

import com.hnkjzy.ecg.common.ApiResponse;
import com.hnkjzy.ecg.common.PageResult;
import com.hnkjzy.ecg.dto.BloodGlucoseDto;
import com.hnkjzy.ecg.service.ApiSpecService;
import com.hnkjzy.ecg.vo.BloodGlucoseVo;
import com.hnkjzy.ecg.vo.CommonVo;
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
@RequestMapping("/api/blood-glucose")
@Validated
public class BloodGlucoseController {

    private final ApiSpecService apiSpecService;

    public BloodGlucoseController(ApiSpecService apiSpecService) {
        this.apiSpecService = apiSpecService;
    }

    @GetMapping("/bed-view/departments")
    public ApiResponse<List<BloodGlucoseVo.DepartmentItem>> departments() {
        return ApiResponse.success(apiSpecService.listDepartments());
    }

    @GetMapping("/dict/indexes")
    public ApiResponse<List<BloodGlucoseVo.IndexDictItem>> indexes() {
        return ApiResponse.success(apiSpecService.listIndexes());
    }

    @GetMapping("/warnings")
    public ApiResponse<PageResult<BloodGlucoseVo.WarningItem>> warnings(
            @RequestParam(required = false) @Min(value = 1, message = "pageNum必须大于等于1") Integer pageNum,
            @RequestParam(required = false) @Min(value = 1, message = "pageSize必须大于等于1") Integer pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @Positive(message = "wardId必须为正数") Long wardId,
            @RequestParam(required = false) String patientKeyword,
            @RequestParam(required = false) String indexName,
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String excludeWard) {
        return ApiResponse.success(apiSpecService.pageWarnings(
                pageNum,
                pageSize,
                status,
                wardId,
                patientKeyword,
                indexName,
                condition,
                startDate,
                endDate,
                excludeWard
        ));
    }

    @PostMapping("/warnings/{warningId}/include")
    public ApiResponse<CommonVo.OperationResult> includeWarning(@PathVariable @Positive(message = "warningId必须为正数") Long warningId,
                                                                @RequestParam(required = false) String reason,
                                                                @RequestParam(required = false) @Positive(message = "operatorId必须为正数") Long operatorId) {
        return ApiResponse.success(apiSpecService.includeWarning(warningId, buildWarningActionRequest(reason, operatorId)));
    }

    @PostMapping("/warnings/{warningId}/exclude")
    public ApiResponse<CommonVo.OperationResult> excludeWarning(@PathVariable @Positive(message = "warningId必须为正数") Long warningId,
                                                                @RequestParam(required = false) String reason,
                                                                @RequestParam(required = false) @Positive(message = "operatorId必须为正数") Long operatorId) {
        return ApiResponse.success(apiSpecService.excludeWarning(warningId, buildWarningActionRequest(reason, operatorId)));
    }

    @GetMapping("/warnings/export")
    public ApiResponse<BloodGlucoseVo.ExportResult> exportWarnings(
            @RequestParam(required = false) @Min(value = 1, message = "pageNum必须大于等于1") Integer pageNum,
            @RequestParam(required = false) @Min(value = 1, message = "pageSize必须大于等于1") Integer pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @Positive(message = "wardId必须为正数") Long wardId,
            @RequestParam(required = false) String patientKeyword,
            @RequestParam(required = false) String indexName,
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String excludeWard) {
        return ApiResponse.success(apiSpecService.exportWarnings(
                pageNum,
                pageSize,
                status,
                wardId,
                patientKeyword,
                indexName,
                condition,
                startDate,
                endDate,
                excludeWard
        ));
    }

    @GetMapping("/patients/metrics")
    public ApiResponse<PageResult<BloodGlucoseVo.PatientMetricItem>> patientMetrics(
            @RequestParam(required = false) String scope,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) @Min(value = 1, message = "pageNum必须大于等于1") Integer pageNum,
            @RequestParam(required = false) @Min(value = 1, message = "pageSize必须大于等于1") Integer pageSize) {
        return ApiResponse.success(apiSpecService.pagePatientMetrics(
                scope,
                name,
                status,
                startDate,
                endDate,
                pageNum,
                pageSize
        ));
    }

    @GetMapping("/patients/managed")
    public ApiResponse<PageResult<BloodGlucoseVo.PatientMetricItem>> managedPatients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) @Min(value = 1, message = "pageNum必须大于等于1") Integer pageNum,
            @RequestParam(required = false) @Min(value = 1, message = "pageSize必须大于等于1") Integer pageSize) {
        return ApiResponse.success(apiSpecService.pagePatientMetrics(
                "managed",
                name,
                status,
                startDate,
                endDate,
                pageNum,
                pageSize
        ));
    }

    @GetMapping("/patients/unmanaged")
    public ApiResponse<PageResult<BloodGlucoseVo.PatientMetricItem>> unmanagedPatients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) @Min(value = 1, message = "pageNum必须大于等于1") Integer pageNum,
            @RequestParam(required = false) @Min(value = 1, message = "pageSize必须大于等于1") Integer pageSize) {
        String keyword = (patientId == null || patientId.isBlank()) ? name : patientId;
        return ApiResponse.success(apiSpecService.pagePatientMetrics(
                "unmanaged",
            keyword,
                status,
                startDate,
                endDate,
                pageNum,
                pageSize
        ));
    }

    @GetMapping("/patients/measurement")
    public ApiResponse<PageResult<BloodGlucoseVo.PatientMetricItem>> measurementPatients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) @Min(value = 1, message = "pageNum必须大于等于1") Integer pageNum,
            @RequestParam(required = false) @Min(value = 1, message = "pageSize必须大于等于1") Integer pageSize) {
        return ApiResponse.success(apiSpecService.pagePatientMetrics(
                "measurement",
                name,
                status,
                startDate,
                endDate,
                pageNum,
                pageSize
        ));
    }

    @GetMapping("/patients/abnormal")
    public ApiResponse<PageResult<BloodGlucoseVo.PatientMetricItem>> abnormalPatients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) @Min(value = 1, message = "pageNum必须大于等于1") Integer pageNum,
            @RequestParam(required = false) @Min(value = 1, message = "pageSize必须大于等于1") Integer pageSize) {
        String statusFilter = (status == null || status.isBlank()) ? "abnormal" : status;
        return ApiResponse.success(apiSpecService.pagePatientMetrics(
                "abnormal",
                name,
                statusFilter,
                startDate,
                endDate,
                pageNum,
                pageSize
        ));
    }

    @GetMapping("/patients/discharged")
    public ApiResponse<PageResult<BloodGlucoseVo.PatientMetricItem>> dischargedPatients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) @Min(value = 1, message = "pageNum必须大于等于1") Integer pageNum,
            @RequestParam(required = false) @Min(value = 1, message = "pageSize必须大于等于1") Integer pageSize) {
        return ApiResponse.success(apiSpecService.pagePatientMetrics(
                "discharged",
                name,
                status,
                startDate,
                endDate,
                pageNum,
                pageSize
        ));
    }

    @PostMapping("/patients/{patientId}/include")
    public ApiResponse<CommonVo.OperationResult> includePatient(@PathVariable @NotBlank(message = "patientId不能为空") String patientId,
                                                                @RequestBody(required = false) BloodGlucoseDto.PatientManageActionRequest request) {
        return ApiResponse.success(apiSpecService.includePatient(patientId, request));
    }
    

    @PostMapping("/patients/{patientId}/exclude")
    public ApiResponse<CommonVo.OperationResult> excludePatient(@PathVariable @NotBlank(message = "patientId不能为空") String patientId,
                                                                @RequestBody(required = false) BloodGlucoseDto.PatientManageActionRequest request) {
        return ApiResponse.success(apiSpecService.excludePatient(patientId, request));
    }

    @PostMapping("/patients/{patientId}/unmanaged/delete")
    public ApiResponse<CommonVo.OperationResult> deleteUnmanagedPatient(@PathVariable @NotBlank(message = "patientId不能为空") String patientId) {
        return ApiResponse.success(apiSpecService.deleteUnmanagedPatient(patientId));
    }

    @PostMapping("/patients/{patientId}/managed/include")
    public ApiResponse<CommonVo.OperationResult> includeManagedPatientToDischarged(@PathVariable @NotBlank(message = "patientId不能为空") String patientId) {
        return ApiResponse.success(apiSpecService.includeManagedPatientToDischarged(patientId));
    }

    @GetMapping("/patients/{patientId}/waveform")
    public ApiResponse<BloodGlucoseVo.WaveformData> waveform(
            @PathVariable @NotBlank(message = "patientId不能为空") String patientId,
            @RequestParam(required = false) @Positive(message = "measureId必须为正数") Long measureId,
            @RequestParam(required = false) @Min(value = 5, message = "durationSec最小为5") Integer durationSec) {
        return ApiResponse.success(apiSpecService.getWaveform(patientId, measureId, durationSec));
    }

    @GetMapping("/patients")
    public ApiResponse<PageResult<BloodGlucoseVo.PatientListItem>> patients(
            @RequestParam(required = false) String ward,
            @RequestParam(required = false) String patient,
            @RequestParam(required = false) String patientTag,
            @RequestParam(required = false) String groupStatus,
            @RequestParam(required = false) String hospitalStatus,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) @Min(value = 1, message = "pageNum必须大于等于1") Integer pageNum,
            @RequestParam(required = false) @Min(value = 1, message = "pageSize必须大于等于1") Integer pageSize) {
        return ApiResponse.success(apiSpecService.pagePatients(
                ward,
                patient,
                patientTag,
                groupStatus,
                hospitalStatus,
                startDate,
                endDate,
                pageNum,
                pageSize
        ));
    }

    @PostMapping("/patients/{patientId}/leave-bed")
    public ApiResponse<CommonVo.OperationResult> leaveBed(@PathVariable @NotBlank(message = "patientId不能为空") String patientId,
                                                          @Valid @RequestBody BloodGlucoseDto.LeaveBedRequest request) {
        return ApiResponse.success(apiSpecService.leaveBed(patientId, request));
    }

    @GetMapping("/bed-view/rooms")
    public ApiResponse<List<BloodGlucoseVo.RoomItem>> rooms(@RequestParam(required = false) @Positive(message = "wardId必须为正数") Long wardId) {
        return ApiResponse.success(apiSpecService.listRooms(wardId));
    }

    @GetMapping("/bed-view/rooms/{roomId}/patients")
    public ApiResponse<List<BloodGlucoseVo.RoomPatientItem>> roomPatients(@PathVariable @Positive(message = "roomId必须为正数") Long roomId) {
        return ApiResponse.success(apiSpecService.listRoomPatients(roomId));
    }

    @PostMapping("/bed-view/beds/{bedNo}/assign")
    public ApiResponse<CommonVo.OperationResult> assignBed(@PathVariable @NotBlank(message = "bedNo不能为空") String bedNo,
                                                           @Valid @RequestBody BloodGlucoseDto.AssignBedRequest request) {
        return ApiResponse.success(apiSpecService.assignBed(bedNo, request));
    }

    private BloodGlucoseDto.WarningActionRequest buildWarningActionRequest(String reason, Long operatorId) {
        if ((reason == null || reason.isBlank()) && operatorId == null) {
            return null;
        }
        BloodGlucoseDto.WarningActionRequest request = new BloodGlucoseDto.WarningActionRequest();
        request.setReason(reason);
        request.setOperatorId(operatorId);
        return request;
    }
}
