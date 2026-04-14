package com.hnkjzy.ecg.controller;

/**
 * 文件说明：控制器文件。
 * 主要职责：负责接收前端菜单请求、参数解析并返回统一 REST 响应。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import com.hnkjzy.ecg.common.ApiResponse;
import com.hnkjzy.ecg.model.DictItem;
import com.hnkjzy.ecg.model.DoctorInfo;
import com.hnkjzy.ecg.model.WarningLevelItem;
import com.hnkjzy.ecg.model.WardInfo;
import com.hnkjzy.ecg.service.EcgBusinessService;
import java.util.List;
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
@RequestMapping("/api/system")
/**
 * 类说明：SystemController。
 * 业务定位：负责接收前端菜单请求、参数解析并返回统一 REST 响应。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class SystemController {

    private final EcgBusinessService ecgBusinessService;

    public SystemController(EcgBusinessService ecgBusinessService) {
        this.ecgBusinessService = ecgBusinessService;
    }

    @GetMapping("/wards")
    /**
     * 接口说明：查询接口，方法名为 listWards。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<List<WardInfo>> listWards() {
        return ApiResponse.success(ecgBusinessService.listWards());
    }

    @GetMapping("/wards/{id}")
    /**
     * 接口说明：业务处理接口，方法名为 wardDetail。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<WardInfo> wardDetail(@PathVariable Integer id) {
        WardInfo wardInfo = ecgBusinessService.getWard(id);
        return wardInfo == null ? ApiResponse.fail("病区不存在") : ApiResponse.success(wardInfo);
    }

    @PostMapping("/wards")
    /**
     * 接口说明：新增接口，方法名为 createWard。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<WardInfo> createWard(@RequestBody WardInfo wardInfo) {
        return ApiResponse.success("创建成功", ecgBusinessService.createWard(wardInfo));
    }

    @PutMapping("/wards/{id}")
    /**
     * 接口说明：修改接口，方法名为 updateWard。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> updateWard(@PathVariable Integer id, @RequestBody WardInfo wardInfo) {
        return ApiResponse.success(ecgBusinessService.updateWard(id, wardInfo));
    }

    @DeleteMapping("/wards/{id}")
    /**
     * 接口说明：删除接口，方法名为 deleteWard。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> deleteWard(@PathVariable Integer id) {
        return ApiResponse.success(ecgBusinessService.deleteWard(id));
    }

    @GetMapping("/doctors")
    /**
     * 接口说明：查询接口，方法名为 listDoctors。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<List<DoctorInfo>> listDoctors(@RequestParam(required = false) Integer wardId,
                                                     @RequestParam(required = false) Integer isActive) {
        return ApiResponse.success(ecgBusinessService.listDoctors(wardId, isActive));
    }

    @GetMapping("/doctors/{id}")
    /**
     * 接口说明：业务处理接口，方法名为 doctorDetail。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<DoctorInfo> doctorDetail(@PathVariable Integer id) {
        DoctorInfo doctorInfo = ecgBusinessService.getDoctor(id);
        return doctorInfo == null ? ApiResponse.fail("医生不存在") : ApiResponse.success(doctorInfo);
    }

    @PostMapping("/doctors")
    /**
     * 接口说明：新增接口，方法名为 createDoctor。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<DoctorInfo> createDoctor(@RequestBody DoctorInfo doctorInfo) {
        return ApiResponse.success("创建成功", ecgBusinessService.createDoctor(doctorInfo));
    }

    @PutMapping("/doctors/{id}")
    /**
     * 接口说明：修改接口，方法名为 updateDoctor。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> updateDoctor(@PathVariable Integer id, @RequestBody DoctorInfo doctorInfo) {
        return ApiResponse.success(ecgBusinessService.updateDoctor(id, doctorInfo));
    }

    @DeleteMapping("/doctors/{id}")
    /**
     * 接口说明：删除接口，方法名为 deleteDoctor。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> deleteDoctor(@PathVariable Integer id) {
        return ApiResponse.success(ecgBusinessService.deleteDoctor(id));
    }

    @GetMapping("/dict/warning-levels")
    /**
     * 接口说明：查询接口，方法名为 listWarningLevels。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<List<WarningLevelItem>> listWarningLevels() {
        return ApiResponse.success(ecgBusinessService.listWarningLevels());
    }

    @PostMapping("/dict/warning-levels")
    /**
     * 接口说明：新增接口，方法名为 createWarningLevel。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<WarningLevelItem> createWarningLevel(@RequestBody WarningLevelItem item) {
        return ApiResponse.success("创建成功", ecgBusinessService.createWarningLevel(item));
    }

    @PutMapping("/dict/warning-levels/{id}")
    /**
     * 接口说明：修改接口，方法名为 updateWarningLevel。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> updateWarningLevel(@PathVariable Integer id, @RequestBody WarningLevelItem item) {
        return ApiResponse.success(ecgBusinessService.updateWarningLevel(id, item));
    }

    @DeleteMapping("/dict/warning-levels/{id}")
    /**
     * 接口说明：删除接口，方法名为 deleteWarningLevel。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> deleteWarningLevel(@PathVariable Integer id) {
        return ApiResponse.success(ecgBusinessService.deleteWarningLevel(id));
    }

    @GetMapping("/dict/warning-types")
    /**
     * 接口说明：查询接口，方法名为 listWarningTypes。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<List<DictItem>> listWarningTypes() {
        return ApiResponse.success(ecgBusinessService.listWarningTypes());
    }

    @PostMapping("/dict/warning-types")
    /**
     * 接口说明：新增接口，方法名为 createWarningType。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<DictItem> createWarningType(@RequestBody DictItem item) {
        return ApiResponse.success("创建成功", ecgBusinessService.createWarningType(item));
    }

    @PutMapping("/dict/warning-types/{id}")
    /**
     * 接口说明：修改接口，方法名为 updateWarningType。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> updateWarningType(@PathVariable Integer id, @RequestBody DictItem item) {
        return ApiResponse.success(ecgBusinessService.updateWarningType(id, item));
    }

    @DeleteMapping("/dict/warning-types/{id}")
    /**
     * 接口说明：删除接口，方法名为 deleteWarningType。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> deleteWarningType(@PathVariable Integer id) {
        return ApiResponse.success(ecgBusinessService.deleteWarningType(id));
    }

    @GetMapping("/dict/equipment-types")
    /**
     * 接口说明：查询接口，方法名为 listEquipmentTypes。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<List<DictItem>> listEquipmentTypes() {
        return ApiResponse.success(ecgBusinessService.listEquipmentTypes());
    }

    @PostMapping("/dict/equipment-types")
    /**
     * 接口说明：新增接口，方法名为 createEquipmentType。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<DictItem> createEquipmentType(@RequestBody DictItem item) {
        return ApiResponse.success("创建成功", ecgBusinessService.createEquipmentType(item));
    }

    @PutMapping("/dict/equipment-types/{id}")
    /**
     * 接口说明：修改接口，方法名为 updateEquipmentType。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> updateEquipmentType(@PathVariable Integer id, @RequestBody DictItem item) {
        return ApiResponse.success(ecgBusinessService.updateEquipmentType(id, item));
    }

    @DeleteMapping("/dict/equipment-types/{id}")
    /**
     * 接口说明：删除接口，方法名为 deleteEquipmentType。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> deleteEquipmentType(@PathVariable Integer id) {
        return ApiResponse.success(ecgBusinessService.deleteEquipmentType(id));
    }
}
