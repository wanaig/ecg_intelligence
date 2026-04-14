package com.hnkjzy.ecg.controller;

/**
 * 文件说明：控制器文件。
 * 主要职责：负责接收前端菜单请求、参数解析并返回统一 REST 响应。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import com.hnkjzy.ecg.common.ApiResponse;
import com.hnkjzy.ecg.common.PageResult;
import com.hnkjzy.ecg.model.ReportInfo;
import com.hnkjzy.ecg.model.ReportReviewRequest;
import com.hnkjzy.ecg.service.EcgBusinessService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quality-controls")
/**
 * 类说明：QualityControlController。
 * 业务定位：负责接收前端菜单请求、参数解析并返回统一 REST 响应。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class QualityControlController {

    private final EcgBusinessService ecgBusinessService;

    public QualityControlController(EcgBusinessService ecgBusinessService) {
        this.ecgBusinessService = ecgBusinessService;
    }

    @GetMapping("/reports")
    /**
     * 接口说明：查询接口，方法名为 pageReports。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<PageResult<ReportInfo>> pageReports(@RequestParam(required = false) String status,
                                                            @RequestParam(required = false) Integer patientId,
                                                            @RequestParam(required = false) Integer page,
                                                            @RequestParam(required = false) Integer size) {
        return ApiResponse.success(ecgBusinessService.pageReports(status, patientId, page, size));
    }

    @GetMapping("/reports/{id}")
    /**
     * 接口说明：业务处理接口，方法名为 reportDetail。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<ReportInfo> reportDetail(@PathVariable Integer id) {
        ReportInfo reportInfo = ecgBusinessService.getReport(id);
        return reportInfo == null ? ApiResponse.fail("报告不存在") : ApiResponse.success(reportInfo);
    }

    @PostMapping("/reports")
    /**
     * 接口说明：新增接口，方法名为 createReport。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<ReportInfo> createReport(@RequestBody ReportInfo reportInfo) {
        return ApiResponse.success("创建成功", ecgBusinessService.createReport(reportInfo));
    }

    @PutMapping("/reports/{id}")
    /**
     * 接口说明：修改接口，方法名为 updateReport。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> updateReport(@PathVariable Integer id, @RequestBody ReportInfo reportInfo) {
        return ApiResponse.success(ecgBusinessService.updateReport(id, reportInfo));
    }

    @PatchMapping("/reports/{id}/review")
    /**
     * 接口说明：处理接口，方法名为 reviewReport。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> reviewReport(@PathVariable Integer id, @RequestBody ReportReviewRequest request) {
        return ApiResponse.success(ecgBusinessService.reviewReport(id, request));
    }

    @DeleteMapping("/reports/{id}")
    /**
     * 接口说明：删除接口，方法名为 deleteReport。
     * 处理流程：接收请求参数，调用服务层并统一封装 ApiResponse 返回。
     * 返回规范：code=0 表示成功，非0表示失败。
     */
    public ApiResponse<Boolean> deleteReport(@PathVariable Integer id) {
        return ApiResponse.success(ecgBusinessService.deleteReport(id));
    }
}
