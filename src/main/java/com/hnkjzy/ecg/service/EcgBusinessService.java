package com.hnkjzy.ecg.service;

/**
 * 文件说明：业务服务文件。
 * 主要职责：负责业务规则编排、状态流转与事务控制。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import com.hnkjzy.ecg.common.PageResult;
import com.hnkjzy.ecg.mapper.AnalysisMapper;
import com.hnkjzy.ecg.mapper.CoreIndexMapper;
import com.hnkjzy.ecg.mapper.DictMapper;
import com.hnkjzy.ecg.mapper.DoctorMapper;
import com.hnkjzy.ecg.mapper.EquipmentMapper;
import com.hnkjzy.ecg.mapper.PatientMapper;
import com.hnkjzy.ecg.mapper.ReportMapper;
import com.hnkjzy.ecg.mapper.WarningMapper;
import com.hnkjzy.ecg.mapper.WardMapper;
import com.hnkjzy.ecg.model.BedOverviewItem;
import com.hnkjzy.ecg.model.CoreIndexInfo;
import com.hnkjzy.ecg.model.DictItem;
import com.hnkjzy.ecg.model.DoctorInfo;
import com.hnkjzy.ecg.model.EquipmentInfo;
import com.hnkjzy.ecg.model.MeasureStatInfo;
import com.hnkjzy.ecg.model.PatientInfo;
import com.hnkjzy.ecg.model.PatientManageView;
import com.hnkjzy.ecg.model.ReportInfo;
import com.hnkjzy.ecg.model.ReportReviewRequest;
import com.hnkjzy.ecg.model.ReportStatInfo;
import com.hnkjzy.ecg.model.SupplierSummary;
import com.hnkjzy.ecg.model.TrendPoint;
import com.hnkjzy.ecg.model.WarningHandleRequest;
import com.hnkjzy.ecg.model.WarningInfo;
import com.hnkjzy.ecg.model.WarningLevelItem;
import com.hnkjzy.ecg.model.WarningStatInfo;
import com.hnkjzy.ecg.model.WardInfo;
import com.hnkjzy.ecg.model.WorkbenchOverview;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/**
 * 类说明：EcgBusinessService。
 * 业务定位：负责业务规则编排、状态流转与事务控制。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class EcgBusinessService {

    private final PatientMapper patientMapper;
    private final WarningMapper warningMapper;
    private final CoreIndexMapper coreIndexMapper;
    private final ReportMapper reportMapper;
    private final WardMapper wardMapper;
    private final DoctorMapper doctorMapper;
    private final EquipmentMapper equipmentMapper;
    private final DictMapper dictMapper;
    private final AnalysisMapper analysisMapper;

    public EcgBusinessService(PatientMapper patientMapper,
                              WarningMapper warningMapper,
                              CoreIndexMapper coreIndexMapper,
                              ReportMapper reportMapper,
                              WardMapper wardMapper,
                              DoctorMapper doctorMapper,
                              EquipmentMapper equipmentMapper,
                              DictMapper dictMapper,
                              AnalysisMapper analysisMapper) {
        this.patientMapper = patientMapper;
        this.warningMapper = warningMapper;
        this.coreIndexMapper = coreIndexMapper;
        this.reportMapper = reportMapper;
        this.wardMapper = wardMapper;
        this.doctorMapper = doctorMapper;
        this.equipmentMapper = equipmentMapper;
        this.dictMapper = dictMapper;
        this.analysisMapper = analysisMapper;
    }

    /**
     * 方法说明：查询业务方法，方法名为 getWorkbenchOverview。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public WorkbenchOverview getWorkbenchOverview() {
        return analysisMapper.selectWorkbenchOverview();
    }

    /**
     * 方法说明：查询业务方法，方法名为 getWarningTrend。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public List<TrendPoint> getWarningTrend(Integer days) {
        int safeDays = days == null || days < 1 ? 7 : Math.min(days, 90);
        return analysisMapper.warningTrend(safeDays);
    }

    /**
     * 方法说明：查询业务方法，方法名为 listMeasureStats。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public List<MeasureStatInfo> listMeasureStats(String statCycle) {
        return analysisMapper.listMeasureStats(statCycle);
    }

    /**
     * 方法说明：查询业务方法，方法名为 listWarningStats。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public List<WarningStatInfo> listWarningStats(String statCycle) {
        return analysisMapper.listWarningStats(statCycle);
    }

    /**
     * 方法说明：查询业务方法，方法名为 listReportStats。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public List<ReportStatInfo> listReportStats(String statCycle) {
        return analysisMapper.listReportStats(statCycle);
    }

    /**
     * 方法说明：查询业务方法，方法名为 pageHospitalPatients。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public PageResult<PatientInfo> pageHospitalPatients(String name, Integer wardId, Integer page, Integer size) {
        int safePage = normalizePage(page);
        int safeSize = normalizeSize(size);
        int offset = (safePage - 1) * safeSize;
        long total = patientMapper.count(name, wardId);
        List<PatientInfo> records = patientMapper.selectPage(name, wardId, offset, safeSize);
        return new PageResult<>(total, safePage, safeSize, records);
    }

    /**
     * 方法说明：查询业务方法，方法名为 getPatient。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public PatientInfo getPatient(Integer id) {
        return patientMapper.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：新增业务方法，方法名为 createPatient。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public PatientInfo createPatient(PatientInfo patientInfo) {
        patientMapper.insert(patientInfo);
        return patientMapper.findById(patientInfo.getPatientId());
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：修改业务方法，方法名为 updatePatient。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean updatePatient(Integer id, PatientInfo patientInfo) {
        patientInfo.setPatientId(id);
        return patientMapper.update(patientInfo) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：删除业务方法，方法名为 deletePatient。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean deletePatient(Integer id) {
        return patientMapper.deleteById(id) > 0;
    }

    /**
     * 方法说明：查询业务方法，方法名为 pageWarnings。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public PageResult<WarningInfo> pageWarnings(String status,
                                                String level,
                                                String type,
                                                Integer patientId,
                                                Integer page,
                                                Integer size) {
        int safePage = normalizePage(page);
        int safeSize = normalizeSize(size);
        int offset = (safePage - 1) * safeSize;
        long total = warningMapper.count(status, level, type, patientId);
        List<WarningInfo> records = warningMapper.selectPage(status, level, type, patientId, offset, safeSize);
        return new PageResult<>(total, safePage, safeSize, records);
    }

    /**
     * 方法说明：查询业务方法，方法名为 getWarning。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public WarningInfo getWarning(Integer id) {
        return warningMapper.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：新增业务方法，方法名为 createWarning。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public WarningInfo createWarning(WarningInfo warningInfo) {
        if (warningInfo.getWarningTime() == null) {
            warningInfo.setWarningTime(LocalDateTime.now());
        }
        if (isBlank(warningInfo.getWarningStatus())) {
            warningInfo.setWarningStatus("未处理");
        }
        warningMapper.insert(warningInfo);
        return warningMapper.findById(warningInfo.getWarningId());
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：修改业务方法，方法名为 updateWarning。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean updateWarning(Integer id, WarningInfo warningInfo) {
        warningInfo.setWarningId(id);
        return warningMapper.update(warningInfo) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：处理业务方法，方法名为 handleWarning。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean handleWarning(Integer id, WarningHandleRequest request) {
        String status = isBlank(request.getWarningStatus()) ? "已处理" : request.getWarningStatus();
        return warningMapper.handle(id, request.getHandleDoctorId(), request.getHandleResult(), status) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：删除业务方法，方法名为 deleteWarning。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean deleteWarning(Integer id) {
        return warningMapper.deleteById(id) > 0;
    }

    /**
     * 方法说明：查询业务方法，方法名为 listPendingPatients。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public List<PatientManageView> listPendingPatients() {
        return analysisMapper.listPendingPatients();
    }

    /**
     * 方法说明：查询业务方法，方法名为 listManagedPatients。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public List<PatientManageView> listManagedPatients() {
        return analysisMapper.listManagedPatients();
    }

    /**
     * 方法说明：查询业务方法，方法名为 listBedOverview。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public List<BedOverviewItem> listBedOverview() {
        return analysisMapper.listBedOverview();
    }

    /**
     * 方法说明：查询业务方法，方法名为 pageCoreIndexes。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public PageResult<CoreIndexInfo> pageCoreIndexes(Integer measureId,
                                                     String rhythmType,
                                                     Integer page,
                                                     Integer size) {
        int safePage = normalizePage(page);
        int safeSize = normalizeSize(size);
        int offset = (safePage - 1) * safeSize;
        long total = coreIndexMapper.count(measureId, rhythmType);
        List<CoreIndexInfo> records = coreIndexMapper.selectPage(measureId, rhythmType, offset, safeSize);
        return new PageResult<>(total, safePage, safeSize, records);
    }

    /**
     * 方法说明：查询业务方法，方法名为 getCoreIndex。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public CoreIndexInfo getCoreIndex(Integer id) {
        return coreIndexMapper.findById(id);
    }

    /**
     * 方法说明：查询业务方法，方法名为 listHighRiskCoreIndexes。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public List<CoreIndexInfo> listHighRiskCoreIndexes() {
        return coreIndexMapper.selectHighRisk();
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：新增业务方法，方法名为 createCoreIndex。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public CoreIndexInfo createCoreIndex(CoreIndexInfo coreIndexInfo) {
        coreIndexMapper.insert(coreIndexInfo);
        return coreIndexMapper.findById(coreIndexInfo.getIndexId());
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：修改业务方法，方法名为 updateCoreIndex。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean updateCoreIndex(Integer id, CoreIndexInfo coreIndexInfo) {
        coreIndexInfo.setIndexId(id);
        return coreIndexMapper.update(coreIndexInfo) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：删除业务方法，方法名为 deleteCoreIndex。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean deleteCoreIndex(Integer id) {
        return coreIndexMapper.deleteById(id) > 0;
    }

    /**
     * 方法说明：查询业务方法，方法名为 pageReports。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public PageResult<ReportInfo> pageReports(String status, Integer patientId, Integer page, Integer size) {
        int safePage = normalizePage(page);
        int safeSize = normalizeSize(size);
        int offset = (safePage - 1) * safeSize;
        long total = reportMapper.count(status, patientId);
        List<ReportInfo> records = reportMapper.selectPage(status, patientId, offset, safeSize);
        return new PageResult<>(total, safePage, safeSize, records);
    }

    /**
     * 方法说明：查询业务方法，方法名为 getReport。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public ReportInfo getReport(Integer id) {
        return reportMapper.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：新增业务方法，方法名为 createReport。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public ReportInfo createReport(ReportInfo reportInfo) {
        if (reportInfo.getCreateTime() == null) {
            reportInfo.setCreateTime(LocalDateTime.now());
        }
        if (isBlank(reportInfo.getReportStatus())) {
            reportInfo.setReportStatus("草稿");
        }
        if (isBlank(reportInfo.getReportNo())) {
            reportInfo.setReportNo(generateReportNo());
        }
        reportMapper.insert(reportInfo);
        return reportMapper.findById(reportInfo.getReportId());
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：修改业务方法，方法名为 updateReport。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean updateReport(Integer id, ReportInfo reportInfo) {
        reportInfo.setReportId(id);
        return reportMapper.update(reportInfo) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：处理业务方法，方法名为 reviewReport。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean reviewReport(Integer id, ReportReviewRequest request) {
        String status = isBlank(request.getReportStatus()) ? "已审核" : request.getReportStatus();
        return reportMapper.review(id, request.getReviewDoctorId(), status) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：删除业务方法，方法名为 deleteReport。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean deleteReport(Integer id) {
        return reportMapper.deleteById(id) > 0;
    }

    /**
     * 方法说明：查询业务方法，方法名为 listSupplierSummary。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public List<SupplierSummary> listSupplierSummary() {
        return analysisMapper.listSupplierSummary();
    }

    /**
     * 方法说明：查询业务方法，方法名为 listWards。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public List<WardInfo> listWards() {
        return wardMapper.findAll();
    }

    /**
     * 方法说明：查询业务方法，方法名为 getWard。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public WardInfo getWard(Integer id) {
        return wardMapper.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：新增业务方法，方法名为 createWard。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public WardInfo createWard(WardInfo wardInfo) {
        wardMapper.insert(wardInfo);
        return wardMapper.findById(wardInfo.getWardId());
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：修改业务方法，方法名为 updateWard。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean updateWard(Integer id, WardInfo wardInfo) {
        wardInfo.setWardId(id);
        return wardMapper.update(wardInfo) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：删除业务方法，方法名为 deleteWard。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean deleteWard(Integer id) {
        return wardMapper.deleteById(id) > 0;
    }

    /**
     * 方法说明：查询业务方法，方法名为 listDoctors。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public List<DoctorInfo> listDoctors(Integer wardId, Integer isActive) {
        return doctorMapper.findAll(wardId, isActive);
    }

    /**
     * 方法说明：查询业务方法，方法名为 getDoctor。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public DoctorInfo getDoctor(Integer id) {
        return doctorMapper.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：新增业务方法，方法名为 createDoctor。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public DoctorInfo createDoctor(DoctorInfo doctorInfo) {
        if (doctorInfo.getIsActive() == null) {
            doctorInfo.setIsActive(1);
        }
        doctorMapper.insert(doctorInfo);
        return doctorMapper.findById(doctorInfo.getDoctorId());
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：修改业务方法，方法名为 updateDoctor。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean updateDoctor(Integer id, DoctorInfo doctorInfo) {
        doctorInfo.setDoctorId(id);
        return doctorMapper.update(doctorInfo) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：删除业务方法，方法名为 deleteDoctor。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean deleteDoctor(Integer id) {
        return doctorMapper.deleteById(id) > 0;
    }

    /**
     * 方法说明：查询业务方法，方法名为 listEquipments。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public List<EquipmentInfo> listEquipments(String manufacturer, Integer wardId, String equipmentType) {
        return equipmentMapper.findAll(manufacturer, wardId, equipmentType);
    }

    /**
     * 方法说明：查询业务方法，方法名为 getEquipment。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public EquipmentInfo getEquipment(Integer id) {
        return equipmentMapper.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：新增业务方法，方法名为 createEquipment。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public EquipmentInfo createEquipment(EquipmentInfo equipmentInfo) {
        if (isBlank(equipmentInfo.getEquipmentStatus())) {
            equipmentInfo.setEquipmentStatus("正常");
        }
        equipmentMapper.insert(equipmentInfo);
        return equipmentMapper.findById(equipmentInfo.getEquipmentId());
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：修改业务方法，方法名为 updateEquipment。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean updateEquipment(Integer id, EquipmentInfo equipmentInfo) {
        equipmentInfo.setEquipmentId(id);
        return equipmentMapper.update(equipmentInfo) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：删除业务方法，方法名为 deleteEquipment。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean deleteEquipment(Integer id) {
        return equipmentMapper.deleteById(id) > 0;
    }

    /**
     * 方法说明：查询业务方法，方法名为 listWarningLevels。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public List<WarningLevelItem> listWarningLevels() {
        return dictMapper.listWarningLevels();
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：新增业务方法，方法名为 createWarningLevel。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public WarningLevelItem createWarningLevel(WarningLevelItem item) {
        dictMapper.insertWarningLevel(item);
        return item;
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：修改业务方法，方法名为 updateWarningLevel。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean updateWarningLevel(Integer id, WarningLevelItem item) {
        item.setDictId(id);
        return dictMapper.updateWarningLevel(item) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：删除业务方法，方法名为 deleteWarningLevel。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean deleteWarningLevel(Integer id) {
        return dictMapper.deleteWarningLevel(id) > 0;
    }

    /**
     * 方法说明：查询业务方法，方法名为 listWarningTypes。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public List<DictItem> listWarningTypes() {
        return dictMapper.listWarningTypes();
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：新增业务方法，方法名为 createWarningType。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public DictItem createWarningType(DictItem item) {
        dictMapper.insertWarningType(item);
        return item;
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：修改业务方法，方法名为 updateWarningType。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean updateWarningType(Integer id, DictItem item) {
        item.setDictId(id);
        return dictMapper.updateWarningType(item) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：删除业务方法，方法名为 deleteWarningType。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean deleteWarningType(Integer id) {
        return dictMapper.deleteWarningType(id) > 0;
    }

    /**
     * 方法说明：查询业务方法，方法名为 listEquipmentTypes。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public List<DictItem> listEquipmentTypes() {
        return dictMapper.listEquipmentTypes();
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：新增业务方法，方法名为 createEquipmentType。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public DictItem createEquipmentType(DictItem item) {
        dictMapper.insertEquipmentType(item);
        return item;
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：修改业务方法，方法名为 updateEquipmentType。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean updateEquipmentType(Integer id, DictItem item) {
        item.setDictId(id);
        return dictMapper.updateEquipmentType(item) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * 方法说明：删除业务方法，方法名为 deleteEquipmentType。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public boolean deleteEquipmentType(Integer id) {
        return dictMapper.deleteEquipmentType(id) > 0;
    }

    private int normalizePage(Integer page) {
        return page == null || page < 1 ? 1 : page;
    }

    private int normalizeSize(Integer size) {
        if (size == null || size < 1) {
            return 10;
        }
        return Math.min(size, 200);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String generateReportNo() {
        return "R" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
    }
}
