package com.hnkjzy.ecg.service.impl;

import com.hnkjzy.ecg.common.PageResult;
import com.hnkjzy.ecg.dto.BloodGlucoseDto;
import com.hnkjzy.ecg.dto.QualityDto;
import com.hnkjzy.ecg.dto.SupplierDto;
import com.hnkjzy.ecg.dto.SystemDto;
import com.hnkjzy.ecg.exception.BusinessException;
import com.hnkjzy.ecg.mapper.AnalysisMapper;
import com.hnkjzy.ecg.mapper.ApiSpecMapper;
import com.hnkjzy.ecg.mapper.DictMapper;
import com.hnkjzy.ecg.mapper.DoctorMapper;
import com.hnkjzy.ecg.mapper.EquipmentMapper;
import com.hnkjzy.ecg.mapper.WardMapper;
import com.hnkjzy.ecg.model.DictItem;
import com.hnkjzy.ecg.model.DoctorInfo;
import com.hnkjzy.ecg.model.EquipmentInfo;
import com.hnkjzy.ecg.model.TrendPoint;
import com.hnkjzy.ecg.model.WarningQueryRow;
import com.hnkjzy.ecg.model.WardInfo;
import com.hnkjzy.ecg.model.WaveformMetricRow;
import com.hnkjzy.ecg.model.WorkbenchOverview;
import com.hnkjzy.ecg.service.ApiSpecService;
import com.hnkjzy.ecg.vo.AnalysisVo;
import com.hnkjzy.ecg.vo.BloodGlucoseVo;
import com.hnkjzy.ecg.vo.CommonVo;
import com.hnkjzy.ecg.vo.QualityVo;
import com.hnkjzy.ecg.vo.SupplierVo;
import com.hnkjzy.ecg.vo.SystemVo;
import com.hnkjzy.ecg.vo.WorkbenchVo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;

@Service
public class ApiSpecServiceImpl implements ApiSpecService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final WardMapper wardMapper;
    private final DictMapper dictMapper;
    private final DoctorMapper doctorMapper;
    private final EquipmentMapper equipmentMapper;
    private final AnalysisMapper analysisMapper;
    private final ApiSpecMapper apiSpecMapper;

    public ApiSpecServiceImpl(WardMapper wardMapper,
                              DictMapper dictMapper,
                              DoctorMapper doctorMapper,
                              EquipmentMapper equipmentMapper,
                              AnalysisMapper analysisMapper,
                              ApiSpecMapper apiSpecMapper) {
        this.wardMapper = wardMapper;
        this.dictMapper = dictMapper;
        this.doctorMapper = doctorMapper;
        this.equipmentMapper = equipmentMapper;
        this.analysisMapper = analysisMapper;
        this.apiSpecMapper = apiSpecMapper;
    }

    @Override
    public List<BloodGlucoseVo.DepartmentItem> listDepartments() {
        List<WardInfo> wards = wardMapper.findAll();
        if (wards == null || wards.isEmpty()) {
            return Collections.emptyList();
        }
        return wards.stream()
                .map(ward -> new BloodGlucoseVo.DepartmentItem(
                        toLong(ward.getWardId()),
                        ward.getWardName(),
                        ward.getWardPhone()
                ))
                .toList();
    }

    @Override
    public List<BloodGlucoseVo.IndexDictItem> listIndexes() {
        List<DictItem> warningTypes = dictMapper.listWarningTypes();
        if (warningTypes == null || warningTypes.isEmpty()) {
            return Collections.emptyList();
        }
        return warningTypes.stream()
                .map(item -> {
                    String unit = "score";
                    double refMin = 0D;
                    double refMax = 100D;
                    if ("T03".equals(item.getTypeCode()) || "T04".equals(item.getTypeCode()) || "T19".equals(item.getTypeCode())) {
                        unit = "bpm";
                        refMin = 60D;
                        refMax = 100D;
                    } else if ("T09".equals(item.getTypeCode())) {
                        unit = "ms";
                        refMin = 360D;
                        refMax = 440D;
                    }
                    return new BloodGlucoseVo.IndexDictItem(
                            item.getTypeCode(),
                            item.getTypeName(),
                            unit,
                            refMin,
                            refMax
                    );
                })
                .toList();
    }

    @Override
    public PageResult<BloodGlucoseVo.WarningItem> pageWarnings(Integer pageNum,
                                                               Integer pageSize,
                                                               String status,
                                                               Long wardId,
                                                               String patientKeyword,
                                                               String indexName,
                                                               String condition,
                                                               String startDate,
                                                               String endDate,
                                                               String excludeWard) {
        int safePage = normalizePage(pageNum);
        int safeSize = normalizeSize(pageSize);
        int offset = offset(safePage, safeSize);

        List<WarningQueryRow> rows = apiSpecMapper.listWarningRows(
                status,
                wardId,
                patientKeyword,
                indexName,
                startDate,
                endDate,
                excludeWard,
                offset,
                safeSize
        );
        long total = apiSpecMapper.countWarningRows(status, wardId, patientKeyword, indexName, startDate, endDate, excludeWard);

        List<BloodGlucoseVo.WarningItem> list = rows == null
                ? Collections.emptyList()
                : rows.stream().map(this::toWarningItem).toList();
        return new PageResult<>(total, safePage, safeSize, list);
    }

    @Override
    public CommonVo.OperationResult includeWarning(Long warningId, BloodGlucoseDto.WarningActionRequest request) {
        int affected = apiSpecMapper.updateWarningAction(warningId, request.getOperatorId(), request.getReason(), "1");
        if (affected == 0) {
            throw new BusinessException(404, "预警不存在");
        }
        return new CommonVo.OperationResult(String.valueOf(warningId), "include", "预警已纳入(1)");
    }

    @Override
    public CommonVo.OperationResult excludeWarning(Long warningId, BloodGlucoseDto.WarningActionRequest request) {
        int affected = apiSpecMapper.updateWarningAction(warningId, request.getOperatorId(), request.getReason(), "0");
        if (affected == 0) {
            throw new BusinessException(404, "预警不存在");
        }
        return new CommonVo.OperationResult(String.valueOf(warningId), "exclude", "预警已不纳入(0)");
    }

    @Override
    public BloodGlucoseVo.ExportResult exportWarnings(Integer pageNum,
                                                      Integer pageSize,
                                                      String status,
                                                      Long wardId,
                                                      String patientKeyword,
                                                      String indexName,
                                                      String condition,
                                                      String startDate,
                                                      String endDate,
                                                      String excludeWard) {
        long total = apiSpecMapper.countWarningRows(status, wardId, patientKeyword, indexName, startDate, endDate, excludeWard);
        String fileName = "warnings-export-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".xlsx";
        return new BloodGlucoseVo.ExportResult("/api/files/" + fileName + "?total=" + total);
    }

    @Override
    public PageResult<BloodGlucoseVo.PatientMetricItem> pagePatientMetrics(String scope,
                                                                           String name,
                                                                           String status,
                                                                           String startDate,
                                                                           String endDate,
                                                                           Integer pageNum,
                                                                           Integer pageSize) {
        int safePage = normalizePage(pageNum);
        int safeSize = normalizeSize(pageSize);
        int offset = offset(safePage, safeSize);

        List<BloodGlucoseVo.PatientMetricItem> list = apiSpecMapper.listPatientMetrics(
                scope,
                name,
                status,
                startDate,
                endDate,
                offset,
                safeSize
        );
        long total = apiSpecMapper.countPatientMetrics(scope, name, status, startDate, endDate);
        return new PageResult<>(total, safePage, safeSize, safeList(list));
    }

    @Override
    public CommonVo.OperationResult includePatient(String patientId, BloodGlucoseDto.PatientManageActionRequest request) {
        String reason = request == null ? null : request.getReason();
        Long operatorId = request == null ? null : request.getOperatorId();
        Integer latestManagedFlag = apiSpecMapper.selectLatestPatientManagedFlag(patientId);
        if (latestManagedFlag != null && latestManagedFlag == 1) {
            return new CommonVo.OperationResult(patientId, "include", "患者已在管(1)");
        }
        int affected = apiSpecMapper.updateLatestPatientManageStatus(patientId, "1", operatorId, reason);
        if (affected == 0) {
            throw new BusinessException(404, "患者不存在或缺少可更新的预警记录");
        }
        return new CommonVo.OperationResult(patientId, "include", "患者已纳入在管(1)");
    }

    @Override
    public CommonVo.OperationResult excludePatient(String patientId, BloodGlucoseDto.PatientManageActionRequest request) {
        String reason = request == null ? null : request.getReason();
        Long operatorId = request == null ? null : request.getOperatorId();
        int affected = apiSpecMapper.updateLatestPatientManageStatus(patientId, "0", operatorId, reason);
        if (affected == 0) {
            throw new BusinessException(404, "患者不存在或缺少可更新的预警记录");
        }
        return new CommonVo.OperationResult(patientId, "exclude", "患者已标记为不纳入(0)");
    }

    @Override
    public CommonVo.OperationResult deleteUnmanagedPatient(String patientId) {
        int affected = apiSpecMapper.deleteLatestUnmanagedWarning(patientId);
        if (affected == 0) {
            throw new BusinessException(404, "未找到可删除的待管记录");
        }
        return new CommonVo.OperationResult(patientId, "delete-unmanaged", "待管患者不纳入记录已删除");
    }

    @Override
    public CommonVo.OperationResult includeManagedPatientToDischarged(String patientId) {
        Integer latestManagedFlag = apiSpecMapper.selectLatestPatientManagedFlag(patientId);
        if (latestManagedFlag == null) {
            throw new BusinessException(404, "患者不存在或缺少可更新的预警记录");
        }
        if (latestManagedFlag != 1) {
            throw new BusinessException(409, "患者当前不在管，无法执行出组");
        }
        int affected = apiSpecMapper.clearPatientBed(patientId);
        if (affected == 0) {
            throw new BusinessException(404, "患者不存在");
        }
        return new CommonVo.OperationResult(patientId, "managed-include-discharge", "患者已从在管转入出组");
    }

    @Override
    public BloodGlucoseVo.WaveformData getWaveform(String patientId, Long measureId, Integer durationSec) {
        WaveformMetricRow metric = apiSpecMapper.selectWaveformMetric(patientId, measureId);
        if (metric == null) {
            throw new BusinessException(404, "未找到对应测量记录");
        }

        int safeDuration = durationSec == null ? 10 : Math.max(5, Math.min(durationSec, 120));
        int renderSampleRate = 50;
        int totalPoints = safeDuration * renderSampleRate;

        double heartRate = metric.getHeartRate() == null ? 72D : metric.getHeartRate();
        double baseFreq = heartRate / 60D;

        List<Double> points = IntStream.range(0, totalPoints)
                .mapToObj(i -> {
                    double t = i / (double) renderSampleRate;
                    double primary = Math.sin(2 * Math.PI * baseFreq * t) * 1.05;
                    double harmonic = Math.sin(2 * Math.PI * baseFreq * 2 * t) * 0.22;
                    return primary + harmonic;
                })
                .toList();

        return new BloodGlucoseVo.WaveformData(
                nonBlank(metric.getPatientId(), patientId),
                metric.getMeasureId(),
                metric.getSampleRate() == null ? 250 : metric.getSampleRate(),
                points,
                new BloodGlucoseVo.WaveformMetrics(
                        heartRate,
                        metric.getStSegment() == null ? 0D : metric.getStSegment(),
                        metric.getQt() == null ? 0D : metric.getQt(),
                        metric.getQrs() == null ? 0D : metric.getQrs()
                )
        );
    }

    @Override
    public PageResult<BloodGlucoseVo.PatientListItem> pagePatients(String ward,
                                                                   String patient,
                                                                   String patientTag,
                                                                   String groupStatus,
                                                                   String hospitalStatus,
                                                                   String startDate,
                                                                   String endDate,
                                                                   Integer pageNum,
                                                                   Integer pageSize) {
        int safePage = normalizePage(pageNum);
        int safeSize = normalizeSize(pageSize);
        int offset = offset(safePage, safeSize);

        List<BloodGlucoseVo.PatientListItem> list = apiSpecMapper.listPatients(
                ward,
                patient,
                patientTag,
                groupStatus,
                hospitalStatus,
                startDate,
                endDate,
                offset,
                safeSize
        );
        long total = apiSpecMapper.countPatients(ward, patient, patientTag, groupStatus, hospitalStatus, startDate, endDate);
        return new PageResult<>(total, safePage, safeSize, safeList(list));
    }

    @Override
    public CommonVo.OperationResult leaveBed(String patientId, BloodGlucoseDto.LeaveBedRequest request) {
        int affected = apiSpecMapper.clearPatientBed(patientId);
        if (affected == 0) {
            throw new BusinessException(404, "患者不存在");
        }
        return new CommonVo.OperationResult(patientId, "leave-bed", "患者离床操作已记录");
    }

    @Override
    public List<BloodGlucoseVo.RoomItem> listRooms(Long wardId) {
        return safeList(apiSpecMapper.listRooms(wardId));
    }

    @Override
    public List<BloodGlucoseVo.RoomPatientItem> listRoomPatients(Long roomId) {
        return safeList(apiSpecMapper.listRoomPatients(roomId));
    }

    @Override
    public CommonVo.OperationResult assignBed(String bedNo, BloodGlucoseDto.AssignBedRequest request) {
        int affected = apiSpecMapper.assignBed(request.getPatientId(), bedNo);
        if (affected == 0) {
            throw new BusinessException(404, "患者不存在");
        }
        return new CommonVo.OperationResult(bedNo, "assign-bed", "床位分配成功");
    }

    @Override
    public WorkbenchVo.OverviewData getWorkbenchOverview(String dateType, String startDate, String endDate) {
        WorkbenchOverview overview = analysisMapper.selectWorkbenchOverview();
        if (overview == null) {
            return new WorkbenchVo.OverviewData(Collections.emptyList());
        }

        long totalWarnings = nullableLong(overview.getTotalWarnings());
        long handledWarnings = Math.max(0, totalWarnings - nullableLong(overview.getUnhandledWarnings()));
        double handleRate = totalWarnings == 0 ? 100D : (handledWarnings * 100.0 / totalWarnings);

        List<WorkbenchVo.StatItem> stats = List.of(
                new WorkbenchVo.StatItem("监测患者数", nullableLong(overview.getTotalPatients()), "人"),
                new WorkbenchVo.StatItem("今日预警", totalWarnings, "条"),
                new WorkbenchVo.StatItem("处理率", round1(handleRate), "%")
        );
        return new WorkbenchVo.OverviewData(stats);
    }

    @Override
    public List<WorkbenchVo.TrendPointItem> getWarningTrendChart() {
        List<TrendPoint> points = analysisMapper.warningTrend(7);
        if (points == null || points.isEmpty()) {
            return Collections.emptyList();
        }
        return points.stream()
                .map(item -> new WorkbenchVo.TrendPointItem(item.getLabel(), item.getValue() == null ? 0 : item.getValue().intValue()))
                .toList();
    }

    @Override
    public List<WorkbenchVo.WardWarningRankItem> getWardWarningRankChart() {
        return safeList(apiSpecMapper.listWardWarningRank());
    }

    @Override
    public PageResult<WorkbenchVo.WorkbenchPatientItem> pageWorkbenchPatients(String ward,
                                                                              String tab,
                                                                              Integer pageNum,
                                                                              Integer pageSize) {
        int safePage = normalizePage(pageNum);
        int safeSize = normalizeSize(pageSize);
        int offset = offset(safePage, safeSize);

        List<WorkbenchVo.WorkbenchPatientItem> list = apiSpecMapper.listWorkbenchPatients(ward, tab, offset, safeSize);
        long total = apiSpecMapper.countWorkbenchPatients(ward, tab);
        return new PageResult<>(total, safePage, safeSize, safeList(list));
    }

    @Override
    public List<WorkbenchVo.PatientRecordItem> listWorkbenchPatientRecords(String inpatientNo) {
        return safeList(apiSpecMapper.listPatientRecords(inpatientNo));
    }

    @Override
    public AnalysisVo.DashboardData getAnalysisDashboard() {
        return new AnalysisVo.DashboardData(
                safeList(apiSpecMapper.listGenderDist()),
                safeList(apiSpecMapper.listAgeDist()),
                safeList(apiSpecMapper.listWarningWardTop()),
                safeList(apiSpecMapper.listManagedOverview()),
                safeList(apiSpecMapper.listActiveWards()),
                safeList(apiSpecMapper.listGlucoseDistManaged()),
                safeList(apiSpecMapper.listGlucoseDistHospital()),
                safeList(apiSpecMapper.listAbnormalWardTop())
        );
    }

    @Override
    public List<AnalysisVo.PatientWarningListGroup> listPatientWarningLists() {
        List<AnalysisVo.PatientWarningListGroup> groups = new ArrayList<>();
        groups.add(new AnalysisVo.PatientWarningListGroup(
                "危急预警",
                "critical",
                safeList(apiSpecMapper.listCriticalWarnings())
        ));
        groups.add(new AnalysisVo.PatientWarningListGroup(
                "异常预警",
                "abnormal",
                safeList(apiSpecMapper.listAbnormalWarnings())
        ));
        return groups;
    }

    @Override
    public PageResult<QualityVo.DataQualityItem> pageDataQuality(String patientId,
                                                                 String qualityLevel,
                                                                 Integer pageNum,
                                                                 Integer pageSize) {
        int safePage = normalizePage(pageNum);
        int safeSize = normalizeSize(pageSize);
        int offset = offset(safePage, safeSize);

        List<QualityVo.DataQualityItem> list = apiSpecMapper.listDataQuality(patientId, qualityLevel, offset, safeSize);
        long total = apiSpecMapper.countDataQuality(patientId, qualityLevel);
        return new PageResult<>(total, safePage, safeSize, safeList(list));
    }

    @Override
    public QualityVo.DataQualityItem getDataQualityDetail(Long id) {
        QualityVo.DataQualityItem detail = apiSpecMapper.selectDataQualityDetail(id);
        if (detail == null) {
            throw new BusinessException(404, "数据质控记录不存在");
        }
        return detail;
    }

    @Override
    public PageResult<QualityVo.DeviceQualityItem> pageDeviceQuality(String mac,
                                                                     String deviceStatus,
                                                                     Integer pageNum,
                                                                     Integer pageSize) {
        int safePage = normalizePage(pageNum);
        int safeSize = normalizeSize(pageSize);
        int offset = offset(safePage, safeSize);

        List<QualityVo.DeviceQualityItem> list = apiSpecMapper.listDeviceQuality(mac, deviceStatus, offset, safeSize);
        long total = apiSpecMapper.countDeviceQuality(mac, deviceStatus);
        return new PageResult<>(total, safePage, safeSize, safeList(list));
    }

    @Override
    public List<QualityVo.DeviceRecordItem> listDeviceQualityRecords(Long id) {
        return safeList(apiSpecMapper.listDeviceRecords(id));
    }

    @Override
    public PageResult<QualityVo.ReportQualityItem> pageReportQuality(String doctor,
                                                                     String resultType,
                                                                     Integer pageNum,
                                                                     Integer pageSize) {
        int safePage = normalizePage(pageNum);
        int safeSize = normalizeSize(pageSize);
        int offset = offset(safePage, safeSize);

        List<QualityVo.ReportQualityItem> list = apiSpecMapper.listReportQuality(doctor, resultType, offset, safeSize);
        long total = apiSpecMapper.countReportQuality(doctor, resultType);
        return new PageResult<>(total, safePage, safeSize, safeList(list));
    }

    @Override
    public QualityVo.ReportQualityItem getReportQualityDetail(String reportId) {
        QualityVo.ReportQualityItem detail = apiSpecMapper.selectReportQualityByNo(reportId);
        if (detail == null) {
            Long numericId = parseLong(reportId);
            if (numericId != null) {
                detail = apiSpecMapper.selectReportQualityById(numericId);
            }
        }
        if (detail == null) {
            throw new BusinessException(404, "报告不存在");
        }
        return detail;
    }

    @Override
    public QualityVo.AppealResult appealReport(String reportId, QualityDto.ReportAppealRequest request) {
        Long dbReportId = apiSpecMapper.selectReportIdByNo(reportId);
        if (dbReportId == null) {
            dbReportId = parseLong(reportId);
        }
        if (dbReportId == null) {
            throw new BusinessException(404, "报告不存在");
        }

        int affected = apiSpecMapper.updateReportAppeal(dbReportId, request.getAppealReason());
        if (affected == 0) {
            throw new BusinessException(404, "报告不存在");
        }
        return new QualityVo.AppealResult(reportId, "appealed", "申诉已提交");
    }

    @Override
    public PageResult<SupplierVo.VendorItem> pageVendors(String vendorName, Integer pageNum, Integer pageSize) {
        int safePage = normalizePage(pageNum);
        int safeSize = normalizeSize(pageSize);
        int offset = offset(safePage, safeSize);

        List<SupplierVo.VendorItem> list = apiSpecMapper.listVendors(vendorName, offset, safeSize);
        long total = apiSpecMapper.countVendors(vendorName);
        return new PageResult<>(total, safePage, safeSize, safeList(list));
    }

    @Override
    public SupplierVo.VendorItem createVendor(SupplierDto.VendorUpsertRequest request) {
        EquipmentInfo equipment = new EquipmentInfo();
        equipment.setEquipmentCode(nonBlank(request.getCode(), "V" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))));
        equipment.setEquipmentName(nonBlank(request.getName(), "未命名厂商") + "设备");
        equipment.setEquipmentType(nonBlank(request.getType(), "通用设备"));
        equipment.setManufacturer(request.getName());
        equipment.setEquipmentVersion("v1.0");
        equipment.setWardId(1);
        equipment.setEquipmentStatus("启用".equals(request.getStatus()) || !hasText(request.getStatus()) ? "正常" : "维护");
        equipment.setInstallTime(LocalDateTime.now());
        equipment.setMaintainTime(LocalDateTime.now());
        equipment.setNextMaintainTime(LocalDateTime.now().plusMonths(6));
        equipmentMapper.insert(equipment);

        return new SupplierVo.VendorItem(
                toLong(equipment.getEquipmentId()),
                equipment.getEquipmentCode(),
                request.getName(),
                request.getType(),
                request.getContact(),
                request.getPhone(),
                nonBlank(request.getStatus(), "启用"),
                nowDateTime()
        );
    }

    @Override
    public SupplierVo.VendorItem updateVendor(Long id, SupplierDto.VendorUpsertRequest request) {
        String oldName = apiSpecMapper.selectVendorNameByEquipmentId(id);
        if (!hasText(oldName)) {
            throw new BusinessException(404, "厂商不存在");
        }
        if (hasText(request.getName()) && !request.getName().equals(oldName)) {
            apiSpecMapper.renameVendor(oldName, request.getName());
        }

        return new SupplierVo.VendorItem(
                id,
                nonBlank(request.getCode(), "V" + id),
                nonBlank(request.getName(), oldName),
                nonBlank(request.getType(), "设备厂商"),
                request.getContact(),
                request.getPhone(),
                nonBlank(request.getStatus(), "启用"),
                nowDateTime()
        );
    }

    @Override
    public CommonVo.OperationResult deleteVendor(Long id) {
        String vendorName = apiSpecMapper.selectVendorNameByEquipmentId(id);
        if (!hasText(vendorName)) {
            throw new BusinessException(404, "厂商不存在");
        }
        int affected = apiSpecMapper.deleteVendorByName(vendorName);
        return new CommonVo.OperationResult(String.valueOf(id), "delete", "厂商已删除，影响设备记录: " + affected);
    }

    @Override
    public List<SupplierVo.VendorQualificationItem> listVendorQualifications() {
        return safeList(apiSpecMapper.listVendorQualifications());
    }

    @Override
    public List<SupplierVo.VendorDeviceLedgerItem> listVendorDeviceLedger() {
        return safeList(apiSpecMapper.listVendorDeviceLedger());
    }

    @Override
    public List<SupplierVo.DeviceBasicLedgerItem> listDeviceBasicLedger() {
        return safeList(apiSpecMapper.listDeviceBasicLedger());
    }

    @Override
    public List<SupplierVo.DeviceBindingItem> listDeviceBinding() {
        return safeList(apiSpecMapper.listDeviceBinding());
    }

    @Override
    public List<SupplierVo.DeviceMaintenanceItem> listDeviceMaintenance() {
        return safeList(apiSpecMapper.listDeviceMaintenance());
    }

    @Override
    public List<SupplierVo.DeviceStatusItem> listDeviceStatus() {
        return safeList(apiSpecMapper.listDeviceStatus());
    }

    @Override
    public List<SupplierVo.ConsumableInfoItem> listConsumableInfo() {
        return safeList(apiSpecMapper.listConsumableInfo());
    }

    @Override
    public List<SupplierVo.ConsumableInventoryItem> listConsumableInventory() {
        return safeList(apiSpecMapper.listConsumableInventory());
    }

    @Override
    public List<SupplierVo.ConsumableTraceabilityItem> listConsumableTraceability() {
        return safeList(apiSpecMapper.listConsumableTraceability());
    }

    @Override
    public List<SupplierVo.ProcurementOrderItem> listProcurementOrder() {
        return safeList(apiSpecMapper.listProcurementOrder());
    }

    @Override
    public List<SupplierVo.ProcurementAcceptanceItem> listProcurementAcceptance() {
        return safeList(apiSpecMapper.listProcurementAcceptance());
    }

    @Override
    public List<SupplierVo.ProcurementStatisticsItem> listProcurementStatistics() {
        return safeList(apiSpecMapper.listProcurementStatistics());
    }

    @Override
    public PageResult<SystemVo.UserItem> pageUsers(String userName,
                                                   String phone,
                                                   String status,
                                                   Integer pageNum,
                                                   Integer pageSize) {
        int safePage = normalizePage(pageNum);
        int safeSize = normalizeSize(pageSize);
        int offset = offset(safePage, safeSize);

        List<SystemVo.UserItem> list = apiSpecMapper.listUsers(userName, phone, status, offset, safeSize);
        long total = apiSpecMapper.countUsers(userName, phone, status);
        return new PageResult<>(total, safePage, safeSize, safeList(list));
    }

    @Override
    public SystemVo.UserItem createUser(SystemDto.UserUpsertRequest request) {
        DoctorInfo doctor = new DoctorInfo();
        doctor.setDoctorName(request.getRealName());
        doctor.setDoctorTitle(request.getRole());
        doctor.setWardId(resolveWardId(request.getDept()));
        doctor.setUserName(request.getUserName());
        doctor.setPassword("123456");
        doctor.setIsActive("禁用".equals(request.getStatus()) ? 0 : 1);
        doctorMapper.insert(doctor);

        return buildUserItem(doctor, request.getDept(), request.getPhone());
    }

    @Override
    public SystemVo.UserItem updateUser(Long id, SystemDto.UserUpsertRequest request) {
        DoctorInfo doctor = doctorMapper.findById(id.intValue());
        if (doctor == null) {
            throw new BusinessException(404, "用户不存在");
        }

        if (hasText(request.getRealName())) {
            doctor.setDoctorName(request.getRealName());
        }
        if (hasText(request.getRole())) {
            doctor.setDoctorTitle(request.getRole());
        }
        if (hasText(request.getUserName())) {
            doctor.setUserName(request.getUserName());
        }
        Integer wardId = resolveWardId(request.getDept());
        if (wardId != null) {
            doctor.setWardId(wardId);
        }
        if (hasText(request.getStatus())) {
            doctor.setIsActive("禁用".equals(request.getStatus()) ? 0 : 1);
        }

        doctorMapper.update(doctor);
        return buildUserItem(doctor, request.getDept(), request.getPhone());
    }

    @Override
    public CommonVo.OperationResult updateUserStatus(Long id, String status) {
        int active = "禁用".equals(status) ? 0 : 1;
        int affected = apiSpecMapper.updateDoctorStatus(id, active);
        if (affected == 0) {
            throw new BusinessException(404, "用户不存在");
        }
        return new CommonVo.OperationResult(String.valueOf(id), "patch-status", "用户状态已更新");
    }

    @Override
    public CommonVo.OperationResult deleteUser(Long id) {
        int affected = doctorMapper.deleteById(id.intValue());
        if (affected == 0) {
            throw new BusinessException(404, "用户不存在");
        }
        return new CommonVo.OperationResult(String.valueOf(id), "delete", "用户已删除");
    }

    @Override
    public PageResult<SystemVo.DepartmentItem> pageDepartments(String deptName,
                                                               String status,
                                                               Integer pageNum,
                                                               Integer pageSize) {
        int safePage = normalizePage(pageNum);
        int safeSize = normalizeSize(pageSize);
        int offset = offset(safePage, safeSize);

        List<SystemVo.DepartmentItem> list = apiSpecMapper.listDepartments(deptName, status, offset, safeSize);
        long total = apiSpecMapper.countDepartments(deptName, status);
        return new PageResult<>(total, safePage, safeSize, safeList(list));
    }

    @Override
    public SystemVo.DepartmentItem createDepartment(SystemDto.DepartmentUpsertRequest request) {
        WardInfo ward = new WardInfo();
        ward.setWardName(request.getDeptName());
        ward.setWardPhone(request.getPhone());
        ward.setWardDesc(request.getCode());
        wardMapper.insert(ward);

        return new SystemVo.DepartmentItem(
                toLong(ward.getWardId()),
                request.getDeptName(),
                nonBlank(request.getCode(), "D" + ward.getWardId()),
                request.getHead(),
                request.getPhone(),
                nonBlank(request.getStatus(), "启用"),
                nowDateTime(),
                request.getOrder() == null ? ward.getWardId() : request.getOrder()
        );
    }

    @Override
    public SystemVo.DepartmentItem updateDepartment(Long id, SystemDto.DepartmentUpsertRequest request) {
        WardInfo ward = wardMapper.findById(id.intValue());
        if (ward == null) {
            throw new BusinessException(404, "科室不存在");
        }

        ward.setWardName(nonBlank(request.getDeptName(), ward.getWardName()));
        ward.setWardPhone(nonBlank(request.getPhone(), ward.getWardPhone()));
        ward.setWardDesc(nonBlank(request.getCode(), ward.getWardDesc()));
        wardMapper.update(ward);

        return new SystemVo.DepartmentItem(
                id,
                ward.getWardName(),
                nonBlank(request.getCode(), "D" + id),
                request.getHead(),
                ward.getWardPhone(),
                nonBlank(request.getStatus(), "启用"),
                nowDateTime(),
                request.getOrder() == null ? id.intValue() : request.getOrder()
        );
    }

    @Override
    public CommonVo.OperationResult deleteDepartment(Long id) {
        int affected = wardMapper.deleteById(id.intValue());
        if (affected == 0) {
            throw new BusinessException(404, "科室不存在");
        }
        return new CommonVo.OperationResult(String.valueOf(id), "delete", "科室已删除");
    }

    @Override
    public PageResult<SystemVo.RoleItem> pageRoles(String roleName,
                                                   String status,
                                                   Integer pageNum,
                                                   Integer pageSize) {
        int safePage = normalizePage(pageNum);
        int safeSize = normalizeSize(pageSize);
        int offset = offset(safePage, safeSize);

        List<SystemVo.RoleItem> list = apiSpecMapper.listRoles(roleName, status, offset, safeSize);
        long total = apiSpecMapper.countRoles(roleName, status);
        return new PageResult<>(total, safePage, safeSize, safeList(list));
    }

    @Override
    public SystemVo.RoleItem createRole(SystemDto.RoleUpsertRequest request) {
        DictItem role = new DictItem();
        role.setTypeName(request.getRoleName());
        role.setTypeCode(nonBlank(request.getRoleKey(), "ROLE_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))));
        role.setSort(request.getOrder() == null ? 1 : request.getOrder());
        role.setTypeDesc(request.getRemark());
        dictMapper.insertWarningType(role);

        return new SystemVo.RoleItem(
                toLong(role.getDictId()),
                role.getTypeName(),
                role.getTypeCode(),
                role.getSort(),
                nonBlank(request.getStatus(), "启用"),
                nowDateTime(),
                role.getTypeDesc()
        );
    }

    @Override
    public SystemVo.RoleItem updateRole(Long id, SystemDto.RoleUpsertRequest request) {
        DictItem role = new DictItem();
        role.setDictId(id.intValue());
        role.setTypeName(request.getRoleName());
        role.setTypeCode(request.getRoleKey());
        role.setSort(request.getOrder() == null ? 1 : request.getOrder());
        role.setTypeDesc(request.getRemark());

        int affected = dictMapper.updateWarningType(role);
        if (affected == 0) {
            throw new BusinessException(404, "角色不存在");
        }

        return new SystemVo.RoleItem(
                id,
                request.getRoleName(),
                request.getRoleKey(),
                role.getSort(),
                nonBlank(request.getStatus(), "启用"),
                nowDateTime(),
                request.getRemark()
        );
    }

    @Override
    public CommonVo.OperationResult deleteRole(Long id) {
        int affected = dictMapper.deleteWarningType(id.intValue());
        if (affected == 0) {
            throw new BusinessException(404, "角色不存在");
        }
        return new CommonVo.OperationResult(String.valueOf(id), "delete", "角色已删除");
    }

    @Override
    public List<SystemVo.MenuTreeItem> getMenuTree() {
        List<SystemVo.MenuTreeItem> children = safeList(apiSpecMapper.listMenuItems());
        SystemVo.MenuTreeItem root = new SystemVo.MenuTreeItem(1000L, "系统菜单", "/", children);
        return List.of(root);
    }

    @Override
    public CommonVo.OperationResult updateRoleMenus(Long id, List<Long> menuIds) {
        String menuIdsCsv = menuIds == null
                ? ""
                : menuIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        int affected = apiSpecMapper.updateRoleMenuDesc(id, menuIdsCsv);
        if (affected == 0) {
            throw new BusinessException(404, "角色不存在");
        }
        int count = menuIds == null ? 0 : menuIds.size();
        return new CommonVo.OperationResult(String.valueOf(id), "update-menus", "角色菜单更新成功，数量: " + count);
    }

    private BloodGlucoseVo.WarningItem toWarningItem(WarningQueryRow row) {
        String patientInfo = nonBlank(row.getPatientName(), "-") + " / "
                + nonBlank(row.getPatientGender(), "-") + " / "
                + (row.getPatientAge() == null ? "-" : row.getPatientAge()) + "岁";

        BloodGlucoseVo.WardInfo wardInfo = new BloodGlucoseVo.WardInfo(
                row.getWardId(),
                row.getWardName(),
                row.getWardPhone()
        );
        BloodGlucoseVo.IndexInfo indexInfo = new BloodGlucoseVo.IndexInfo(row.getIndexCode(), row.getIndexName());

        return new BloodGlucoseVo.WarningItem(
                row.getWarningId(),
                formatDateTime(row.getWarningTime()),
                patientInfo,
                row.getAdmissionNo(),
                row.getBedNo(),
                formatDate(row.getAdmissionDate()),
                row.getDetailsText(),
                row.getWarningLevel(),
                row.getWarningStatus(),
                wardInfo,
                indexInfo
        );
    }

    private SystemVo.UserItem buildUserItem(DoctorInfo doctor, String deptFallback, String phoneFallback) {
        return new SystemVo.UserItem(
                toLong(doctor.getDoctorId()),
                doctor.getUserName(),
                doctor.getDoctorName(),
                resolveWardName(doctor.getWardId(), deptFallback),
                doctor.getDoctorTitle(),
                nonBlank(phoneFallback, resolveWardPhone(doctor.getWardId())),
                doctor.getIsActive() != null && doctor.getIsActive() == 0 ? "禁用" : "启用",
                nowDateTime()
        );
    }

    private Integer resolveWardId(String dept) {
        if (!hasText(dept)) {
            return null;
        }
        Integer parsed = parseInteger(dept);
        if (parsed != null) {
            return parsed;
        }
        return apiSpecMapper.selectWardIdByName(dept);
    }

    private String resolveWardName(Integer wardId, String fallback) {
        if (wardId == null) {
            return fallback;
        }
        WardInfo ward = wardMapper.findById(wardId);
        return ward == null ? fallback : ward.getWardName();
    }

    private String resolveWardPhone(Integer wardId) {
        if (wardId == null) {
            return "";
        }
        WardInfo ward = wardMapper.findById(wardId);
        return ward == null ? "" : nonBlank(ward.getWardPhone(), "");
    }

    private int normalizePage(Integer pageNum) {
        return pageNum == null || pageNum < 1 ? 1 : pageNum;
    }

    private int normalizeSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            return 20;
        }
        return Math.min(pageSize, 200);
    }

    private int offset(int pageNum, int pageSize) {
        return (pageNum - 1) * pageSize;
    }

    private String nowDateTime() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }

    private String formatDateTime(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        return time.format(DATE_TIME_FORMATTER);
    }

    private String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DATE_FORMATTER);
    }

    private long nullableLong(Long value) {
        return value == null ? 0L : value;
    }

    private double round1(double value) {
        return Math.round(value * 10D) / 10D;
    }

    private Long parseLong(String value) {
        if (!hasText(value)) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Integer parseInteger(String value) {
        if (!hasText(value)) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String nonBlank(String value, String defaultValue) {
        return hasText(value) ? value : defaultValue;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private Long toLong(Integer value) {
        if (value == null) {
            return null;
        }
        return value.longValue();
    }

    private <T> List<T> safeList(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }
}
