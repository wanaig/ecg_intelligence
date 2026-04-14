package com.hnkjzy.ecg.service;

import com.hnkjzy.ecg.common.PageResult;
import com.hnkjzy.ecg.dto.BloodGlucoseDto;
import com.hnkjzy.ecg.dto.QualityDto;
import com.hnkjzy.ecg.dto.SupplierDto;
import com.hnkjzy.ecg.dto.SystemDto;
import com.hnkjzy.ecg.vo.AnalysisVo;
import com.hnkjzy.ecg.vo.BloodGlucoseVo;
import com.hnkjzy.ecg.vo.CommonVo;
import com.hnkjzy.ecg.vo.QualityVo;
import com.hnkjzy.ecg.vo.SupplierVo;
import com.hnkjzy.ecg.vo.SystemVo;
import com.hnkjzy.ecg.vo.WorkbenchVo;
import java.util.List;

public interface ApiSpecService {

    List<BloodGlucoseVo.DepartmentItem> listDepartments();

    List<BloodGlucoseVo.IndexDictItem> listIndexes();

    PageResult<BloodGlucoseVo.WarningItem> pageWarnings(Integer pageNum,
                                                        Integer pageSize,
                                                        String status,
                                                        Long wardId,
                                                        String patientKeyword,
                                                        String indexName,
                                                        String condition,
                                                        String startDate,
                                                        String endDate,
                                                        String excludeWard);

    CommonVo.OperationResult includeWarning(Long warningId, BloodGlucoseDto.WarningActionRequest request);

    CommonVo.OperationResult excludeWarning(Long warningId, BloodGlucoseDto.WarningActionRequest request);

    BloodGlucoseVo.ExportResult exportWarnings(Integer pageNum,
                                               Integer pageSize,
                                               String status,
                                               Long wardId,
                                               String patientKeyword,
                                               String indexName,
                                               String condition,
                                               String startDate,
                                               String endDate,
                                               String excludeWard);

    PageResult<BloodGlucoseVo.PatientMetricItem> pagePatientMetrics(String scope,
                                                                    String name,
                                                                    String status,
                                                                    String startDate,
                                                                    String endDate,
                                                                    Integer pageNum,
                                                                    Integer pageSize);

    BloodGlucoseVo.WaveformData getWaveform(String patientId, Long measureId, Integer durationSec);

    PageResult<BloodGlucoseVo.PatientListItem> pagePatients(String ward,
                                                            String patient,
                                                            String patientTag,
                                                            String groupStatus,
                                                            String hospitalStatus,
                                                            String startDate,
                                                            String endDate,
                                                            Integer pageNum,
                                                            Integer pageSize);

    CommonVo.OperationResult leaveBed(String patientId, BloodGlucoseDto.LeaveBedRequest request);

    List<BloodGlucoseVo.RoomItem> listRooms(Long wardId);

    List<BloodGlucoseVo.RoomPatientItem> listRoomPatients(Long roomId);

    CommonVo.OperationResult assignBed(String bedNo, BloodGlucoseDto.AssignBedRequest request);

    WorkbenchVo.OverviewData getWorkbenchOverview(String dateType, String startDate, String endDate);

    List<WorkbenchVo.TrendPointItem> getWarningTrendChart();

    List<WorkbenchVo.WardWarningRankItem> getWardWarningRankChart();

    PageResult<WorkbenchVo.WorkbenchPatientItem> pageWorkbenchPatients(String ward,
                                                                       String tab,
                                                                       Integer pageNum,
                                                                       Integer pageSize);

    List<WorkbenchVo.PatientRecordItem> listWorkbenchPatientRecords(String inpatientNo);

    AnalysisVo.DashboardData getAnalysisDashboard();

    List<AnalysisVo.PatientWarningListGroup> listPatientWarningLists();

    PageResult<QualityVo.DataQualityItem> pageDataQuality(String patientId,
                                                          String qualityLevel,
                                                          Integer pageNum,
                                                          Integer pageSize);

    QualityVo.DataQualityItem getDataQualityDetail(Long id);

    PageResult<QualityVo.DeviceQualityItem> pageDeviceQuality(String mac,
                                                              String deviceStatus,
                                                              Integer pageNum,
                                                              Integer pageSize);

    List<QualityVo.DeviceRecordItem> listDeviceQualityRecords(Long id);

    PageResult<QualityVo.ReportQualityItem> pageReportQuality(String doctor,
                                                              String resultType,
                                                              Integer pageNum,
                                                              Integer pageSize);

    QualityVo.ReportQualityItem getReportQualityDetail(String reportId);

    QualityVo.AppealResult appealReport(String reportId, QualityDto.ReportAppealRequest request);

    PageResult<SupplierVo.VendorItem> pageVendors(String vendorName, Integer pageNum, Integer pageSize);

    SupplierVo.VendorItem createVendor(SupplierDto.VendorUpsertRequest request);

    SupplierVo.VendorItem updateVendor(Long id, SupplierDto.VendorUpsertRequest request);

    CommonVo.OperationResult deleteVendor(Long id);

    List<SupplierVo.VendorQualificationItem> listVendorQualifications();

    List<SupplierVo.VendorDeviceLedgerItem> listVendorDeviceLedger();

    List<SupplierVo.DeviceBasicLedgerItem> listDeviceBasicLedger();

    List<SupplierVo.DeviceBindingItem> listDeviceBinding();

    List<SupplierVo.DeviceMaintenanceItem> listDeviceMaintenance();

    List<SupplierVo.DeviceStatusItem> listDeviceStatus();

    List<SupplierVo.ConsumableInfoItem> listConsumableInfo();

    List<SupplierVo.ConsumableInventoryItem> listConsumableInventory();

    List<SupplierVo.ConsumableTraceabilityItem> listConsumableTraceability();

    List<SupplierVo.ProcurementOrderItem> listProcurementOrder();

    List<SupplierVo.ProcurementAcceptanceItem> listProcurementAcceptance();

    List<SupplierVo.ProcurementStatisticsItem> listProcurementStatistics();

    PageResult<SystemVo.UserItem> pageUsers(String userName,
                                            String phone,
                                            String status,
                                            Integer pageNum,
                                            Integer pageSize);

    SystemVo.UserItem createUser(SystemDto.UserUpsertRequest request);

    SystemVo.UserItem updateUser(Long id, SystemDto.UserUpsertRequest request);

    CommonVo.OperationResult updateUserStatus(Long id, String status);

    CommonVo.OperationResult deleteUser(Long id);

    PageResult<SystemVo.DepartmentItem> pageDepartments(String deptName,
                                                        String status,
                                                        Integer pageNum,
                                                        Integer pageSize);

    SystemVo.DepartmentItem createDepartment(SystemDto.DepartmentUpsertRequest request);

    SystemVo.DepartmentItem updateDepartment(Long id, SystemDto.DepartmentUpsertRequest request);

    CommonVo.OperationResult deleteDepartment(Long id);

    PageResult<SystemVo.RoleItem> pageRoles(String roleName,
                                            String status,
                                            Integer pageNum,
                                            Integer pageSize);

    SystemVo.RoleItem createRole(SystemDto.RoleUpsertRequest request);

    SystemVo.RoleItem updateRole(Long id, SystemDto.RoleUpsertRequest request);

    CommonVo.OperationResult deleteRole(Long id);

    List<SystemVo.MenuTreeItem> getMenuTree();

    CommonVo.OperationResult updateRoleMenus(Long id, List<Long> menuIds);
}
