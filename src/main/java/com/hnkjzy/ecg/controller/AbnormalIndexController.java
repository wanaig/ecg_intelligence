package com.hnkjzy.ecg.controller;

/**
 * 文件说明：控制器文件。
 * 主要职责：负责接收前端菜单请求、参数解析并返回统一 REST 响应。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import com.hnkjzy.ecg.common.ApiResponse;
import com.hnkjzy.ecg.common.PageResult;
import com.hnkjzy.ecg.model.CoreIndexInfo;
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
@RequestMapping("/api/abnormal-indexes")
/**
 * 类说明：AbnormalIndexController。
 * 业务定位：负责接收前端菜单请求、参数解析并返回统一 REST 响应。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class AbnormalIndexController {

    private final EcgBusinessService ecgBusinessService;

    public AbnormalIndexController(EcgBusinessService ecgBusinessService) {
        this.ecgBusinessService = ecgBusinessService;
    }

    @GetMapping
    /**
     * 接口说明：查询接口，方法名为 page。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<PageResult<CoreIndexInfo>> page(@RequestParam(required = false) Integer measureId,
                                                        @RequestParam(required = false) String rhythmType,
                                                        @RequestParam(required = false) Integer page,
                                                        @RequestParam(required = false) Integer size) {
        return ApiResponse.success(ecgBusinessService.pageCoreIndexes(measureId, rhythmType, page, size));
    }

    @GetMapping("/{id}")
    /**
     * 接口说明：查询接口，方法名为 detail。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<CoreIndexInfo> detail(@PathVariable Integer id) {
        CoreIndexInfo coreIndexInfo = ecgBusinessService.getCoreIndex(id);
        return coreIndexInfo == null ? ApiResponse.fail("异常指标不存在") : ApiResponse.success(coreIndexInfo);
    }

    @GetMapping("/high-risk")
    /**
     * 接口说明：业务处理接口，方法名为 highRisk。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<List<CoreIndexInfo>> highRisk() {
        return ApiResponse.success(ecgBusinessService.listHighRiskCoreIndexes());
    }

    @PostMapping
    /**
     * 接口说明：新增接口，方法名为 create。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<CoreIndexInfo> create(@RequestBody CoreIndexInfo coreIndexInfo) {
        return ApiResponse.success("创建成功", ecgBusinessService.createCoreIndex(coreIndexInfo));
    }

    @PutMapping("/{id}")
    /**
     * 接口说明：修改接口，方法名为 update。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> update(@PathVariable Integer id, @RequestBody CoreIndexInfo coreIndexInfo) {
        return ApiResponse.success(ecgBusinessService.updateCoreIndex(id, coreIndexInfo));
    }

    @DeleteMapping("/{id}")
    /**
     * 接口说明：删除接口，方法名为 delete。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> delete(@PathVariable Integer id) {
        return ApiResponse.success(ecgBusinessService.deleteCoreIndex(id));
    }
}
