# ECG Intelligence Analysis Platform 后端 API 设计文档

## 1. 文档目标

本文档基于以下来源生成：
- 前端静态页面字段与交互逻辑
- 现有请求封装规则
- 数据库初始化脚本：C:/Users/aaa/workspace/cosmic/ecg_intelligence/sql/init.sql

目标是给后端开发提供一份可直接落地的接口设计说明，覆盖：
- 血糖管理
- 工作台与数据分析
- 质量管理
- 供应链管理
- 系统管理

说明：本版本已按你的要求去掉接口中的 v1 前缀。

---

## 2. 通用约定

## 2.1 基础地址
- 网关前缀：/api
- 业务接口示例：/api/blood-glucose/warnings

## 2.2 鉴权
前端请求头会带：
- Authorization: token
- token: token

## 2.3 统一响应结构

```json
{
  "code": 200,
  "success": true,
  "message": "OK",
  "data": {}
}
```

## 2.4 分页结构

```json
{
  "code": 200,
  "success": true,
  "message": "OK",
  "data": {
    "list": [],
    "total": 0,
    "pageNum": 1,
    "pageSize": 20
  }
}
```

## 2.5 时间格式
- 日期：YYYY-MM-DD
- 日期时间：YYYY-MM-DD HH:mm:ss

## 2.6 错误码建议
- 200：成功
- 400：参数错误
- 401：未登录或登录过期
- 403：无权限
- 404：资源不存在
- 409：业务冲突
- 500：服务端异常

## 2.7 纳入状态约定
- 1：纳入（在管）
- 0：不纳入（待管）
- 兼容历史值：已纳入/未纳入（查询时统一映射为 1/0）

---

## 3. 数据库映射设计

## 3.1 可复用现有表

| 业务域 | 表名 | 关键字段 |
|---|---|---|
| 患者 | sys_ecg_patient_info | patient_id, patient_name, inpatient_no, bed_no, ward_id, inpatient_date |
| 病区 | sys_ecg_ward_info | ward_id, ward_name, ward_phone |
| 测量记录 | sys_ecg_measure_record | measure_id, patient_id, equipment_id, start_time, ward_id, measure_status |
| 核心指标 | sys_ecg_core_index | measure_id, avg_heart_rate, min_heart_rate, max_heart_rate |
| AI分析 | sys_ecg_ai_analysis | measure_id, ai_diagnosis, abnormal_index, confidence |
| 异常预警 | sys_ecg_abnormal_warning | warning_id, patient_id, measure_id, warning_time, warning_level, warning_status |
| 报告 | sys_ecg_report | report_id, report_no, patient_id, diagnosis_result, report_status |
| 统计 | sys_ecg_measure_stat/sys_ecg_warning_stat/sys_ecg_report_stat | 各类统计指标 |
| 字典 | sys_dict_warning_level/sys_dict_warning_type/sys_dict_equipment_type | 编码、名称、排序 |

## 3.2 建议新增表

| 表名 | 用途 | 关键字段 |
|---|---|---|
| sys_dict_ecg_index | 指标下拉字典 | index_code, index_name, unit, ref_min, ref_max, sort |
| sys_ecg_warning_manage_log | 预警纳入与排除日志 | id, warning_id, action, reason, operator_id, operate_time |
| sys_ecg_room_info | 病房房间信息 | room_id, ward_id, room_name, nurse_name, total_beds |
| sys_ecg_room_bed | 床位占用状态 | bed_id, room_id, bed_no, patient_id, occupied_flag |
| sys_quality_data_record | 数据质控记录 | id, patient_id, record_time, device_type, score, quality_level, issues, status |
| sys_quality_device_record | 设备质控记录 | id, device_code, model, dept, check_time, next_check_time, pass_rate, status |
| sys_quality_report_record | 报告质控记录 | id, report_no, doctor_name, reviewer_name, score, result, error_type, qc_time |
| sys_supplier_vendor | 厂商信息 | id, code, name, type, contact, phone, status |
| sys_user/sys_role/sys_dept/sys_role_menu | 系统管理 | 用户、角色、科室、权限关系 |

---

## 4. 血糖管理 API

## 4.0 二级标题下接口清单（按 src/views 页面）
- bloodGlucoseManagement/warningList/index.vue：GET /blood-glucose/bed-view/departments，GET /blood-glucose/dict/indexes，GET /blood-glucose/warnings，POST /blood-glucose/warnings/{warningId}/include，POST /blood-glucose/warnings/{warningId}/exclude，GET /blood-glucose/warnings/export
- bloodGlucoseManagement/measurement/index.vue：GET /blood-glucose/patients/measurement（或 GET /blood-glucose/patients/metrics?scope=measurement），GET /blood-glucose/patients/{patientId}/waveform
- bloodGlucoseManagement/managed/index.vue：GET /blood-glucose/patients/managed（或 GET /blood-glucose/patients/metrics?scope=managed），POST /blood-glucose/patients/{patientId}/managed/include，POST /blood-glucose/patients/{patientId}/exclude，GET /blood-glucose/patients/{patientId}/waveform
- bloodGlucoseManagement/unmanaged/index.vue：GET /blood-glucose/patients/unmanaged（或 GET /blood-glucose/patients/metrics?scope=unmanaged），POST /blood-glucose/patients/{patientId}/include，POST /blood-glucose/patients/{patientId}/unmanaged/delete，GET /blood-glucose/patients/{patientId}/waveform
- bloodGlucoseManagement/abnormal/index.vue：GET /blood-glucose/patients/abnormal（或 GET /blood-glucose/patients/metrics?scope=abnormal），GET /blood-glucose/patients/{patientId}/waveform
- bloodGlucoseManagement/discharged/index.vue：GET /blood-glucose/patients/discharged（或 GET /blood-glucose/patients/metrics?scope=discharged），POST /blood-glucose/patients/{patientId}/leave-bed
- bloodGlucoseManagement/allPatients/index.vue：GET /blood-glucose/patients
- bloodGlucoseManagement/bedList/index.vue：GET /blood-glucose/bed-view/departments，GET /blood-glucose/bed-view/rooms，GET /blood-glucose/bed-view/rooms/{roomId}/patients，POST /blood-glucose/bed-view/beds/{bedNo}/assign

## 4.1 病区列表
- 方法：GET
- 路径：/blood-glucose/bed-view/departments
- 用途：病区下拉（预警页、床位页、患者页）

响应 data:
- departmentId: number
- departmentName: string
- wardPhone: string

## 4.2 指标字典列表
- 方法：GET
- 路径：/blood-glucose/dict/indexes
- 用途：预警页指标下拉

响应 data:
- indexCode: string
- indexName: string
- unit: string
- refMin: number
- refMax: number

## 4.3 预警分页查询
- 方法：GET
- 路径：/blood-glucose/warnings

Query 参数：
- pageNum: number
- pageSize: number
- status: string，ALL/危急/异常
- wardId: number
- patientKeyword: string
- indexName: string
- condition: string，> 或 <
- startDate: string
- endDate: string
- excludeWard: string

响应 list 字段：
- warningId: number
- warningTime: string
- patientInfo: string
- admissionNo: string
- bedNo: string
- admissionDate: string
- detailsText: string
- warningLevel: string
- warningStatus: string
- wardInfo: { wardId, wardName, wardPhone }
- index: { indexCode, indexName }

示例：

```json
{
  "code": 200,
  "success": true,
  "message": "OK",
  "data": {
    "list": [
      {
        "warningId": 91231,
        "warningTime": "2026-04-14 09:30:00",
        "patientInfo": "李四 / 男 / 62岁",
        "admissionNo": "Z2025003",
        "bedNo": "201",
        "admissionDate": "2026-04-10",
        "detailsText": "心率过快，持续监测",
        "warningLevel": "中度",
        "warningStatus": "未处理",
        "wardInfo": {
          "wardId": 2,
          "wardName": "心内科二病区",
          "wardPhone": "010-10002"
        },
        "index": {
          "indexCode": "HR",
          "indexName": "心率"
        }
      }
    ],
    "total": 1,
    "pageNum": 1,
    "pageSize": 20
  }
}
```

## 4.4 预警纳入
- 方法：POST
- 路径：/blood-glucose/warnings/{warningId}/include
- Query（可选）：reason、operatorId

效果：
- warning_status 更新为 1（纳入）
- 该预警对应患者可在异常指标列表接口（/blood-glucose/patients/abnormal）中查询到

## 4.5 预警排除
- 方法：POST
- 路径：/blood-glucose/warnings/{warningId}/exclude
- Query（可选）：reason、operatorId

效果：
- 执行不纳入删除：直接删除该预警记录
- 删除成功后，预警从预警列表移除

## 4.6 预警导出
- 方法：GET
- 路径：/blood-glucose/warnings/export
- 参数：同预警查询
- 返回：文件流或下载地址

## 4.7 患者心电列表（统一）
- 方法：GET
- 路径：/blood-glucose/patients/metrics
- 覆盖页面：measurement、managed、unmanaged、discharged、abnormal

Query 参数：
- scope: string，measurement/managed/unmanaged/discharged/abnormal
- name: string
- status: string，stable/abnormal
- startDate: string
- endDate: string
- pageNum: number
- pageSize: number

响应 list 字段：
- id: number
- patientId: string
- name: string
- bedNo: string
- measureTime: string
- heartRate: number
- stSegment: number
- qt: number
- qrs: number
- status: string
- desc: string

## 4.7.1 在管患者列表（独立接口）
- 方法：GET
- 路径：/blood-glucose/patients/managed
- 说明：参数与 4.7 一致（不需要传 scope）
- 页面动作：点击“纳入”调用 POST /blood-glucose/patients/{patientId}/managed/include，患者进入出组患者列表（4.7.2c）

## 4.7.2 待管患者列表（独立接口）
- 方法：GET
- 路径：/blood-glucose/patients/unmanaged
- 说明：参数与 4.7 一致（不需要传 scope）
- 页面动作：点击“不纳入删除”调用 POST /blood-glucose/patients/{patientId}/unmanaged/delete

## 4.7.2a 心电图测量管理列表（独立接口）
- 方法：GET
- 路径：/blood-glucose/patients/measurement
- 说明：参数与 4.7 一致（不需要传 scope）

## 4.7.2b 异常指标管理列表（独立接口）
- 方法：GET
- 路径：/blood-glucose/patients/abnormal
- 说明：参数与 4.7 一致（不需要传 scope）

## 4.7.2c 出组患者列表（独立接口）
- 方法：GET
- 路径：/blood-glucose/patients/discharged
- 说明：参数与 4.7 一致（不需要传 scope）
- 进入方式：在管患者列表点击“纳入”（POST /blood-glucose/patients/{patientId}/managed/include）后进入

## 4.7.3 患者纳入（待管 -> 在管）
- 方法：POST
- 路径：/blood-glucose/patients/{patientId}/include

Body:
- reason: string
- operatorId: number

效果：
- 将患者最近一条预警的 warning_status 置为 1（纳入）并进入在管列表

## 4.7.4 患者不纳入（在管 -> 待管）
- 方法：POST
- 路径：/blood-glucose/patients/{patientId}/exclude

Body:
- reason: string
- operatorId: number

效果：
- 将患者最近一条预警的 warning_status 置为 0（不纳入）

## 4.7.5 待管患者不纳入删除
- 方法：POST
- 路径：/blood-glucose/patients/{patientId}/unmanaged/delete

效果：
- 删除患者最近一条待管记录（warning_status=0）
- 删除成功后，患者从待管患者列表中移除

## 4.7.6 在管点击纳入 -> 出组
- 方法：POST
- 路径：/blood-glucose/patients/{patientId}/managed/include

效果：
- 仅允许在管患者执行
- 执行后清空床位，患者进入出组患者列表（/blood-glucose/patients/discharged）

## 4.8 心电波形详情
- 方法：GET
- 路径：/blood-glucose/patients/{patientId}/waveform

Query 参数：
- measureId: number
- durationSec: number

响应 data:
- patientId: string
- measureId: number
- sampleRate: number
- points: number[]
- metrics: { heartRate, stSegment, qt, qrs }

## 4.9 全部患者分页
- 方法：GET
- 路径：/blood-glucose/patients

Query 参数：
- ward: string
- patient: string
- patientTag: string
- groupStatus: string，1/0（兼容“已纳入/未纳入”）
- hospitalStatus: string
- startDate: string
- endDate: string
- pageNum: number
- pageSize: number

响应 list 字段：
- ward
- bedNo
- patientInfo
- tag
- admissionNo
- admissionDate
- diagnosis
- groupStatus（1=纳入，0=不纳入）
- hospitalStatus

## 4.10 患者离床
- 方法：POST
- 路径：/blood-glucose/patients/{patientId}/leave-bed

Body:
- leaveTime: string
- reason: string

## 4.11 病房房间列表
- 方法：GET
- 路径：/blood-glucose/bed-view/rooms
- 参数：wardId

响应 list 字段：
- id
- name
- nurse
- totalBeds
- occupiedBeds

## 4.12 房间患者列表
- 方法：GET
- 路径：/blood-glucose/bed-view/rooms/{roomId}/patients

响应 list 字段：
- bed
- time
- name
- day
- condition
- status
- medication
- hospitalDays
- isEmpty

## 4.13 空床位分配患者
- 方法：POST
- 路径：/blood-glucose/bed-view/beds/{bedNo}/assign

Body:
- patientId
- roomId
- assignTime

## 4.14 血糖管理闭环流程
1. 预警生成：患者进入预警列表（/blood-glucose/warnings），默认 warning_status=0（不纳入）
2. 纳入在管：在预警列表或待管页执行 include（/warnings/{warningId}/include 或 /patients/{patientId}/include），warning_status 更新为 1
3. 在管跟踪：在管页查询 /blood-glucose/patients/managed，仅展示 warning_status=1 的患者
4. 移出在管：在在管页执行 exclude（/patients/{patientId}/exclude）后，warning_status 改为 0，患者回到待管页
4a. 待管删除：在待管页执行不纳入删除（/patients/{patientId}/unmanaged/delete）后，患者从待管页移除
5. 出组入列：在在管页点击纳入（/patients/{patientId}/managed/include）后，患者出组并进入出组页（/patients/discharged）；全院患者页离床（/patients/{patientId}/leave-bed）同样可进入出组页
6. 床位协同：床位一览表通过 /bed-view/rooms、/bed-view/rooms/{roomId}/patients、/bed-view/beds/{bedNo}/assign 完成床位占用与分配闭环

---

## 5. 工作台与数据分析 API

## 5.0 二级标题下接口清单（按 src/views 页面）
- workbench/index.vue：GET /workbench/overview，GET /workbench/charts/warning-trend，GET /workbench/charts/ward-warning-rank，GET /workbench/patients，GET /workbench/patients/{inpatientNo}/records
- dataAnalysis/index.vue：GET /analysis/dashboard，GET /analysis/patient-warning-lists

## 5.1 工作台概览
- 方法：GET
- 路径：/workbench/overview

Query 参数：
- dateType: today/week/month/year/custom
- startDate
- endDate

响应 data:
- stats: [{ label, value, unit }]

## 5.2 工作台图表
- 方法：GET
- 路径：/workbench/charts/warning-trend
- 用途：近7日预警趋势

- 方法：GET
- 路径：/workbench/charts/ward-warning-rank
- 用途：病区预警排行

## 5.3 工作台患者列表
- 方法：GET
- 路径：/workbench/patients

Query 参数：
- ward
- tab
- pageNum
- pageSize

响应 list 字段：
- info
- ward
- bedNo
- inpatientNo
- deviceNo
- attachTime
- changeTime
- abnTimes
- maxHr
- lowBgTimes

## 5.4 患者记录详情
- 方法：GET
- 路径：/workbench/patients/{inpatientNo}/records

## 5.5 数据分析大盘
- 方法：GET
- 路径：/analysis/dashboard

建议响应块：
- genderDist
- ageDist
- warningWardTop
- managedOverview
- activeWards
- glucoseDistManaged
- glucoseDistHospital
- abnormalWardTop

## 5.6 底部患者预警清单
- 方法：GET
- 路径：/analysis/patient-warning-lists

响应 data:
- label
- value
- data: [{ info, dept, bed, abnormal }]

---

## 6. 质量管理 API

## 6.0 二级标题下接口清单（按 src/views 页面）
- quality/list/index.vue：质量模块入口页，聚合使用 /quality/data、/quality/device、/quality/report 三类接口
- quality/data/index.vue：GET /quality/data，GET /quality/data/{id}
- quality/device/index.vue：GET /quality/device，GET /quality/device/{id}/records
- quality/report/index.vue：GET /quality/report，GET /quality/report/{reportId}，POST /quality/report/{reportId}/appeals

## 6.1 数据质控分页
- 方法：GET
- 路径：/quality/data

Query 参数：
- patientId
- qualityLevel
- pageNum
- pageSize

响应字段：
- patientId
- name
- recordTime
- type
- score
- level
- issues
- status

## 6.2 数据质控详情
- 方法：GET
- 路径：/quality/data/{id}

## 6.3 设备质控分页
- 方法：GET
- 路径：/quality/device

Query 参数：
- mac
- deviceStatus
- pageNum
- pageSize

响应字段：
- mac
- model
- dept
- checkTime
- nextCheckTime
- passRate
- status

## 6.4 设备质控记录详情
- 方法：GET
- 路径：/quality/device/{id}/records

## 6.5 报告质控分页
- 方法：GET
- 路径：/quality/report

Query 参数：
- doctor
- resultType
- pageNum
- pageSize

响应字段：
- reportId
- patient
- doctor
- reviewer
- score
- status
- errorType
- time

## 6.6 报告质控详情
- 方法：GET
- 路径：/quality/report/{reportId}

## 6.7 报告申诉
- 方法：POST
- 路径：/quality/report/{reportId}/appeals

Body:
- appealReason
- attachments

---

## 7. 供应链管理 API

## 7.0 二级标题下接口清单（按 src/views 页面）
- supplier/list/index.vue：供应链模块入口页，聚合展示 vendor/device/consumable/procurement 四类接口
- supplier/vendor/info/index.vue：GET/POST/PUT/DELETE /supplier/vendors
- supplier/vendor/qualifications/index.vue：GET /supplier/vendors/qualifications
- supplier/vendor/deviceLedger/index.vue：GET /supplier/vendors/device-ledger
- supplier/device/basicLedger/index.vue：GET /supplier/devices/basic-ledger
- supplier/device/binding/index.vue：GET /supplier/devices/binding
- supplier/device/maintenance/index.vue：GET /supplier/devices/maintenance
- supplier/device/status/index.vue：GET /supplier/devices/status
- supplier/consumable/info/index.vue：GET /supplier/consumables/info
- supplier/consumable/inventory/index.vue：GET /supplier/consumables/inventory
- supplier/consumable/traceability/index.vue：GET /supplier/consumables/traceability
- supplier/procurement/order/index.vue：GET /supplier/procurement/order
- supplier/procurement/acceptance/index.vue：GET /supplier/procurement/acceptance
- supplier/procurement/statistics/index.vue：GET /supplier/procurement/statistics

## 7.1 厂商管理

### 7.1.1 厂商分页
- 方法：GET
- 路径：/supplier/vendors
- 参数：vendorName, pageNum, pageSize

### 7.1.2 新增厂商
- 方法：POST
- 路径：/supplier/vendors

### 7.1.3 编辑厂商
- 方法：PUT
- 路径：/supplier/vendors/{id}

### 7.1.4 删除厂商
- 方法：DELETE
- 路径：/supplier/vendors/{id}

### 7.1.5 厂商资质列表
- 方法：GET
- 路径：/supplier/vendors/qualifications
- 字段：vendorName, certName, expireDate, status

### 7.1.6 厂商设备台账
- 方法：GET
- 路径：/supplier/vendors/device-ledger
- 字段：vendorName, deviceModel, deviceType, totalSupply

## 7.2 设备管理
- GET /supplier/devices/basic-ledger
- GET /supplier/devices/binding
- GET /supplier/devices/maintenance
- GET /supplier/devices/status

对应字段：
- basic-ledger: code, name, sn, dept, status
- binding: code, patient, bindTime, status
- maintenance: code, type, date, operator, result
- status: code, battery, signal, onlineStatus, lastActive

## 7.3 耗材管理
- GET /supplier/consumables/info
- GET /supplier/consumables/inventory
- GET /supplier/consumables/traceability

对应字段：
- info: code, name, spec, unit, brand
- inventory: name, stock, unit, warningLevel, status
- traceability: name, amount, dept, applicant, time

## 7.4 采购管理
- GET /supplier/procurement/order
- GET /supplier/procurement/acceptance
- GET /supplier/procurement/statistics

对应字段：
- order: orderNo, itemName, amount, vendor, status, date
- acceptance: orderNo, itemName, amount, receiver, date, status
- statistics: month, totalAmount, deviceCount, consumableCount

---

## 8. 系统管理 API

## 8.0 二级标题下接口清单（按 src/views 页面）
- system/users/index.vue：GET/POST/PUT/PATCH/DELETE /system/users
- system/departments/index.vue：GET/POST/PUT/DELETE /system/departments
- system/roles/index.vue：GET/POST/PUT/DELETE /system/roles，GET /system/menus/tree，PUT /system/roles/{id}/menus

## 8.1 用户管理
- GET /system/users
- POST /system/users
- PUT /system/users/{id}
- PATCH /system/users/{id}/status
- DELETE /system/users/{id}

用户查询参数：
- userName
- phone
- status
- pageNum
- pageSize

用户列表字段：
- id
- userName
- realName
- dept
- role
- phone
- status
- createTime

## 8.2 科室管理
- GET /system/departments
- POST /system/departments
- PUT /system/departments/{id}
- DELETE /system/departments/{id}

科室查询参数：
- deptName
- status
- pageNum
- pageSize

科室列表字段：
- id
- deptName
- code
- head
- phone
- status
- createTime
- order

## 8.3 角色管理
- GET /system/roles
- POST /system/roles
- PUT /system/roles/{id}
- DELETE /system/roles/{id}
- GET /system/menus/tree
- PUT /system/roles/{id}/menus

角色查询参数：
- roleName
- status
- pageNum
- pageSize

角色列表字段：
- id
- roleName
- roleKey
- order
- status
- createTime
- remark

---

## 9. 索引建议

高频查询建议建立索引：
- sys_ecg_abnormal_warning(warning_time, warning_level, warning_status)
- sys_ecg_abnormal_warning(patient_id, measure_id)
- sys_ecg_patient_info(inpatient_no, patient_name, ward_id)
- sys_ecg_measure_record(patient_id, ward_id, start_time, measure_status)
- sys_ecg_report(report_no, create_time, report_status)

供应链和系统管理列表建议统一加：
- code/name/status/create_time 组合索引

---

## 10. 联调优先级建议

第一阶段（优先打通前端当前已接入）：
- GET /blood-glucose/warnings
- GET /blood-glucose/bed-view/departments
- GET /blood-glucose/dict/indexes

第二阶段（血糖核心流程）：
- /blood-glucose/patients/metrics
- /blood-glucose/patients/managed
- /blood-glucose/patients/unmanaged
- /blood-glucose/patients/measurement
- /blood-glucose/patients/abnormal
- /blood-glucose/patients/discharged
- /blood-glucose/patients/{patientId}/include
- /blood-glucose/patients/{patientId}/exclude
- /blood-glucose/patients/{patientId}/managed/include
- /blood-glucose/patients/{patientId}/waveform
- /blood-glucose/patients
- /blood-glucose/bed-view/*

第三阶段（系统管理 CRUD）：
- /system/users
- /system/departments
- /system/roles

第四阶段（质量与供应链）：
- /quality/*
- /supplier/*

以上接口已覆盖当前前端静态页面中的查询字段、表格字段和主要操作按钮，可直接用于后端实现与联调。