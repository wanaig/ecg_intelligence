# ECG Intelligence 后端 API 接口文档

## 1. 文档说明

- 文档版本：v1.0
- 项目技术栈：Spring Boot + MyBatis + MySQL
- 接口风格：RESTful
- 基础路径：`/api`
- 字符集：UTF-8

## 2. 统一响应结构

### 2.1 通用响应

```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```

### 2.2 字段含义

- code：业务状态码，`0` 表示成功，非 `0` 表示失败
- message：提示信息
- data：业务数据，可能是对象、数组、布尔值或分页对象

### 2.3 分页响应 data 结构

```json
{
  "total": 100,
  "page": 1,
  "size": 10,
  "records": []
}
```

## 3. 二级菜单与接口映射

- 工作台：`/api/workbench/**`
- 数据分析：`/api/data-analysis/**`
- 预警列表：`/api/warnings/**`
- 待管患者：`/api/pending-patients`
- 在管患者：`/api/managed-patients`
- 床位一览表：`/api/bed-board`
- 全院心电图患者：`/api/hospital-patients/**`
- 异常指标管理：`/api/abnormal-indexes/**`
- 质控管理：`/api/quality-controls/reports/**`
- 供应商管理：`/api/suppliers/**`
- 系统管理：`/api/system/**`

---

## 4. 工作台

### 4.1 获取工作台概览

- 方法：GET
- 路径：`/api/workbench/overview`
- 参数：无
- 返回：`WorkbenchOverview`

### 4.2 获取预警趋势

- 方法：GET
- 路径：`/api/workbench/warning-trend`
- 查询参数：
  - days：可选，查询天数，默认 7，最大 90
- 返回：`TrendPoint[]`

---

## 5. 数据分析

### 5.1 心电测量统计

- 方法：GET
- 路径：`/api/data-analysis/measure-stats`
- 查询参数：
  - statCycle：可选，统计周期（如：日统计、周统计）
- 返回：`MeasureStatInfo[]`

### 5.2 异常预警统计

- 方法：GET
- 路径：`/api/data-analysis/warning-stats`
- 查询参数：
  - statCycle：可选
- 返回：`WarningStatInfo[]`

### 5.3 报告统计

- 方法：GET
- 路径：`/api/data-analysis/report-stats`
- 查询参数：
  - statCycle：可选
- 返回：`ReportStatInfo[]`

---

## 6. 预警列表

### 6.1 分页查询预警

- 方法：GET
- 路径：`/api/warnings`
- 查询参数：
  - status：可选，预警状态
  - level：可选，预警级别
  - type：可选，预警类型
  - patientId：可选，患者ID
  - page：可选，页码
  - size：可选，每页条数
- 返回：`PageResult<WarningInfo>`

### 6.2 预警详情

- 方法：GET
- 路径：`/api/warnings/{id}`
- 路径参数：
  - id：预警ID

### 6.3 新增预警

- 方法：POST
- 路径：`/api/warnings`
- 请求体：`WarningInfo`

### 6.4 更新预警

- 方法：PUT
- 路径：`/api/warnings/{id}`
- 请求体：`WarningInfo`

### 6.5 处理预警

- 方法：PATCH
- 路径：`/api/warnings/{id}/handle`
- 请求体：`WarningHandleRequest`

```json
{
  "handleDoctorId": 1,
  "handleResult": "已电话通知病区医生并完成处置",
  "warningStatus": "已处理"
}
```

### 6.6 删除预警

- 方法：DELETE
- 路径：`/api/warnings/{id}`

---

## 7. 待管患者

### 7.1 列表查询

- 方法：GET
- 路径：`/api/pending-patients`
- 返回：`PatientManageView[]`

---

## 8. 在管患者

### 8.1 列表查询

- 方法：GET
- 路径：`/api/managed-patients`
- 返回：`PatientManageView[]`

---

## 9. 床位一览表

### 9.1 列表查询

- 方法：GET
- 路径：`/api/bed-board`
- 返回：`BedOverviewItem[]`

---

## 10. 全院心电图患者

### 10.1 分页查询患者

- 方法：GET
- 路径：`/api/hospital-patients`
- 查询参数：
  - name：可选，患者姓名模糊查询
  - wardId：可选，病区ID
  - page：可选，页码
  - size：可选，每页条数
- 返回：`PageResult<PatientInfo>`

### 10.2 患者详情

- 方法：GET
- 路径：`/api/hospital-patients/{id}`

### 10.3 新增患者

- 方法：POST
- 路径：`/api/hospital-patients`
- 请求体：`PatientInfo`

### 10.4 更新患者

- 方法：PUT
- 路径：`/api/hospital-patients/{id}`
- 请求体：`PatientInfo`

### 10.5 删除患者

- 方法：DELETE
- 路径：`/api/hospital-patients/{id}`

---

## 11. 异常指标管理

### 11.1 分页查询异常指标

- 方法：GET
- 路径：`/api/abnormal-indexes`
- 查询参数：
  - measureId：可选，测量ID
  - rhythmType：可选，节律类型
  - page：可选
  - size：可选
- 返回：`PageResult<CoreIndexInfo>`

### 11.2 异常指标详情

- 方法：GET
- 路径：`/api/abnormal-indexes/{id}`

### 11.3 高风险异常指标

- 方法：GET
- 路径：`/api/abnormal-indexes/high-risk`
- 返回：`CoreIndexInfo[]`

### 11.4 新增异常指标

- 方法：POST
- 路径：`/api/abnormal-indexes`
- 请求体：`CoreIndexInfo`

### 11.5 更新异常指标

- 方法：PUT
- 路径：`/api/abnormal-indexes/{id}`
- 请求体：`CoreIndexInfo`

### 11.6 删除异常指标

- 方法：DELETE
- 路径：`/api/abnormal-indexes/{id}`

---

## 12. 质控管理

### 12.1 分页查询报告

- 方法：GET
- 路径：`/api/quality-controls/reports`
- 查询参数：
  - status：可选，报告状态（草稿/待审核/已审核）
  - patientId：可选
  - page：可选
  - size：可选
- 返回：`PageResult<ReportInfo>`

### 12.2 报告详情

- 方法：GET
- 路径：`/api/quality-controls/reports/{id}`

### 12.3 新增报告

- 方法：POST
- 路径：`/api/quality-controls/reports`
- 请求体：`ReportInfo`

### 12.4 更新报告

- 方法：PUT
- 路径：`/api/quality-controls/reports/{id}`
- 请求体：`ReportInfo`

### 12.5 审核报告

- 方法：PATCH
- 路径：`/api/quality-controls/reports/{id}/review`
- 请求体：`ReportReviewRequest`

```json
{
  "reviewDoctorId": 2,
  "reportStatus": "已审核"
}
```

### 12.6 删除报告

- 方法：DELETE
- 路径：`/api/quality-controls/reports/{id}`

---

## 13. 供应商管理

### 13.1 厂家汇总

- 方法：GET
- 路径：`/api/suppliers`
- 返回：`SupplierSummary[]`

### 13.2 设备列表

- 方法：GET
- 路径：`/api/suppliers/equipments`
- 查询参数：
  - manufacturer：可选，厂家名称模糊匹配
  - wardId：可选，病区ID
  - equipmentType：可选，设备类型

### 13.3 设备详情

- 方法：GET
- 路径：`/api/suppliers/equipments/{id}`

### 13.4 新增设备

- 方法：POST
- 路径：`/api/suppliers/equipments`
- 请求体：`EquipmentInfo`

### 13.5 更新设备

- 方法：PUT
- 路径：`/api/suppliers/equipments/{id}`
- 请求体：`EquipmentInfo`

### 13.6 删除设备

- 方法：DELETE
- 路径：`/api/suppliers/equipments/{id}`

---

## 14. 系统管理

### 14.1 病区管理

- GET `/api/system/wards`：病区列表
- GET `/api/system/wards/{id}`：病区详情
- POST `/api/system/wards`：新增病区
- PUT `/api/system/wards/{id}`：更新病区
- DELETE `/api/system/wards/{id}`：删除病区

### 14.2 医生管理

- GET `/api/system/doctors`：医生列表（支持 `wardId`、`isActive`）
- GET `/api/system/doctors/{id}`：医生详情
- POST `/api/system/doctors`：新增医生
- PUT `/api/system/doctors/{id}`：更新医生
- DELETE `/api/system/doctors/{id}`：删除医生

### 14.3 预警级别字典

- GET `/api/system/dict/warning-levels`
- POST `/api/system/dict/warning-levels`
- PUT `/api/system/dict/warning-levels/{id}`
- DELETE `/api/system/dict/warning-levels/{id}`

### 14.4 预警类型字典

- GET `/api/system/dict/warning-types`
- POST `/api/system/dict/warning-types`
- PUT `/api/system/dict/warning-types/{id}`
- DELETE `/api/system/dict/warning-types/{id}`

### 14.5 设备类型字典

- GET `/api/system/dict/equipment-types`
- POST `/api/system/dict/equipment-types`
- PUT `/api/system/dict/equipment-types/{id}`
- DELETE `/api/system/dict/equipment-types/{id}`

---

## 15. 典型请求示例

### 15.1 新增患者

POST `/api/hospital-patients`

```json
{
  "patientName": "张三",
  "patientGender": "男",
  "patientAge": 63,
  "inpatientNo": "Z2026001",
  "outpatientNo": "M2026001",
  "inpatientDate": "2026-04-14",
  "bedNo": "A101",
  "inpatientDiagnosis": "冠心病",
  "wardId": 1,
  "phone": "13800000000"
}
```

### 15.2 处理预警

PATCH `/api/warnings/100/handle`

```json
{
  "handleDoctorId": 6,
  "handleResult": "已处理并安排复查",
  "warningStatus": "已处理"
}
```

### 15.3 审核报告

PATCH `/api/quality-controls/reports/200/review`

```json
{
  "reviewDoctorId": 8,
  "reportStatus": "已审核"
}
```

---

## 16. 状态与字典建议

- 预警状态建议值：`未处理`、`处理中`、`已处理`、`已撤销`
- 报告状态建议值：`草稿`、`待审核`、`已审核`、`已归档`
- 设备状态建议值：`正常`、`维护中`、`故障`

---

## 17. 联调说明

- 所有列表接口均建议由前端统一封装分页参数与空值处理。
- 所有新增/更新接口建议先做前端必填校验，再调用接口。
- 若接口返回 `code != 0`，建议前端统一展示 `message`。
