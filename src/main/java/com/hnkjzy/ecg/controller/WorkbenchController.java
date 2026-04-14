package com.hnkjzy.ecg.controller;

/**
 * 文件说明：控制器文件。
 * 主要职责：负责接收前端菜单请求、参数解析并返回统一 REST 响应。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import com.hnkjzy.ecg.common.ApiResponse;
import com.hnkjzy.ecg.model.TrendPoint;
import com.hnkjzy.ecg.model.WorkbenchOverview;
import com.hnkjzy.ecg.service.EcgBusinessService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workbench")
/**
 * 类说明：WorkbenchController。
 * 业务定位：负责接收前端菜单请求、参数解析并返回统一 REST 响应。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class WorkbenchController {

    private final EcgBusinessService ecgBusinessService;

    public WorkbenchController(EcgBusinessService ecgBusinessService) {
        this.ecgBusinessService = ecgBusinessService;
    }

    @GetMapping("/overview")
    /**
     * 接口说明：查询接口，方法名为 overview。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<WorkbenchOverview> overview() {
        return ApiResponse.success(ecgBusinessService.getWorkbenchOverview());
    }

    @GetMapping("/warning-trend")
    /**
     * 接口说明：业务处理接口，方法名为 warningTrend。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<List<TrendPoint>> warningTrend(@RequestParam(required = false) Integer days) {
        return ApiResponse.success(ecgBusinessService.getWarningTrend(days));
    }
}
