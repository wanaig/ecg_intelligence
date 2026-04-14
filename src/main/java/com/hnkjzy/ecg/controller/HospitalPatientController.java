package com.hnkjzy.ecg.controller;

/**
 * 文件说明：控制器文件。
 * 主要职责：负责接收前端菜单请求、参数解析并返回统一 REST 响应。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import com.hnkjzy.ecg.common.ApiResponse;
import com.hnkjzy.ecg.common.PageResult;
import com.hnkjzy.ecg.model.PatientInfo;
import com.hnkjzy.ecg.service.EcgBusinessService;
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
@RequestMapping("/api/hospital-patients")
/**
 * 类说明：HospitalPatientController。
 * 业务定位：负责接收前端菜单请求、参数解析并返回统一 REST 响应。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class HospitalPatientController {

    private final EcgBusinessService ecgBusinessService;

    public HospitalPatientController(EcgBusinessService ecgBusinessService) {
        this.ecgBusinessService = ecgBusinessService;
    }

    @GetMapping
    /**
     * 接口说明：查询接口，方法名为 page。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<PageResult<PatientInfo>> page(@RequestParam(required = false) String name,
                                                     @RequestParam(required = false) Integer wardId,
                                                     @RequestParam(required = false) Integer page,
                                                     @RequestParam(required = false) Integer size) {
        return ApiResponse.success(ecgBusinessService.pageHospitalPatients(name, wardId, page, size));
    }

    @GetMapping("/{id}")
    /**
     * 接口说明：查询接口，方法名为 detail。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<PatientInfo> detail(@PathVariable Integer id) {
        PatientInfo patientInfo = ecgBusinessService.getPatient(id);
        return patientInfo == null ? ApiResponse.fail("患者不存在") : ApiResponse.success(patientInfo);
    }

    @PostMapping
    /**
     * 接口说明：新增接口，方法名为 create。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<PatientInfo> create(@RequestBody PatientInfo patientInfo) {
        return ApiResponse.success("创建成功", ecgBusinessService.createPatient(patientInfo));
    }

    @PutMapping("/{id}")
    /**
     * 接口说明：修改接口，方法名为 update。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> update(@PathVariable Integer id, @RequestBody PatientInfo patientInfo) {
        return ApiResponse.success(ecgBusinessService.updatePatient(id, patientInfo));
    }

    @DeleteMapping("/{id}")
    /**
     * 接口说明：删除接口，方法名为 delete。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> delete(@PathVariable Integer id) {
        return ApiResponse.success(ecgBusinessService.deletePatient(id));
    }
}
