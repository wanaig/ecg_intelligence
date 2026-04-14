package com.hnkjzy.ecg.controller;

/**
 * 文件说明：控制器文件。
 * 主要职责：负责接收前端菜单请求、参数解析并返回统一 REST 响应。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import com.hnkjzy.ecg.common.ApiResponse;
import com.hnkjzy.ecg.model.EquipmentInfo;
import com.hnkjzy.ecg.model.SupplierSummary;
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
@RequestMapping("/api/suppliers")
/**
 * 类说明：SupplierController。
 * 业务定位：负责接收前端菜单请求、参数解析并返回统一 REST 响应。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class SupplierController {

    private final EcgBusinessService ecgBusinessService;

    public SupplierController(EcgBusinessService ecgBusinessService) {
        this.ecgBusinessService = ecgBusinessService;
    }

    @GetMapping
    /**
     * 接口说明：查询接口，方法名为 summary。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<List<SupplierSummary>> summary() {
        return ApiResponse.success(ecgBusinessService.listSupplierSummary());
    }

    @GetMapping("/equipments")
    /**
     * 接口说明：查询接口，方法名为 listEquipments。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<List<EquipmentInfo>> listEquipments(@RequestParam(required = false) String manufacturer,
                                                           @RequestParam(required = false) Integer wardId,
                                                           @RequestParam(required = false) String equipmentType) {
        return ApiResponse.success(ecgBusinessService.listEquipments(manufacturer, wardId, equipmentType));
    }

    @GetMapping("/equipments/{id}")
    /**
     * 接口说明：业务处理接口，方法名为 equipmentDetail。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<EquipmentInfo> equipmentDetail(@PathVariable Integer id) {
        EquipmentInfo equipmentInfo = ecgBusinessService.getEquipment(id);
        return equipmentInfo == null ? ApiResponse.fail("设备不存在") : ApiResponse.success(equipmentInfo);
    }

    @PostMapping("/equipments")
    /**
     * 接口说明：新增接口，方法名为 createEquipment。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<EquipmentInfo> createEquipment(@RequestBody EquipmentInfo equipmentInfo) {
        return ApiResponse.success("创建成功", ecgBusinessService.createEquipment(equipmentInfo));
    }

    @PutMapping("/equipments/{id}")
    /**
     * 接口说明：修改接口，方法名为 updateEquipment。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> updateEquipment(@PathVariable Integer id, @RequestBody EquipmentInfo equipmentInfo) {
        return ApiResponse.success(ecgBusinessService.updateEquipment(id, equipmentInfo));
    }

    @DeleteMapping("/equipments/{id}")
    /**
     * 接口说明：删除接口，方法名为 deleteEquipment。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> deleteEquipment(@PathVariable Integer id) {
        return ApiResponse.success(ecgBusinessService.deleteEquipment(id));
    }
}
