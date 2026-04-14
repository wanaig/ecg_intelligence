package com.hnkjzy.ecg.controller;

import com.hnkjzy.ecg.common.ApiResponse;
import com.hnkjzy.ecg.common.PageResult;
import com.hnkjzy.ecg.dto.SystemDto;
import com.hnkjzy.ecg.service.ApiSpecService;
import com.hnkjzy.ecg.vo.CommonVo;
import com.hnkjzy.ecg.vo.SystemVo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
@Validated
public class SystemController {

    private final ApiSpecService apiSpecService;

    public SystemController(ApiSpecService apiSpecService) {
        this.apiSpecService = apiSpecService;
    }

    @GetMapping("/users")
    public ApiResponse<PageResult<SystemVo.UserItem>> users(@RequestParam(required = false) String userName,
                                                            @RequestParam(required = false) String phone,
                                                            @RequestParam(required = false) String status,
                                                            @RequestParam(required = false) @Min(value = 1, message = "pageNum必须大于等于1") Integer pageNum,
                                                            @RequestParam(required = false) @Min(value = 1, message = "pageSize必须大于等于1") Integer pageSize) {
        return ApiResponse.success(apiSpecService.pageUsers(userName, phone, status, pageNum, pageSize));
    }

    @PostMapping("/users")
    public ApiResponse<SystemVo.UserItem> createUser(@Valid @RequestBody SystemDto.UserUpsertRequest request) {
        return ApiResponse.success(apiSpecService.createUser(request));
    }

    @PutMapping("/users/{id}")
    public ApiResponse<SystemVo.UserItem> updateUser(@PathVariable @Positive(message = "id必须为正数") Long id,
                                                     @Valid @RequestBody SystemDto.UserUpsertRequest request) {
        return ApiResponse.success(apiSpecService.updateUser(id, request));
    }

    @PatchMapping("/users/{id}/status")
    public ApiResponse<CommonVo.OperationResult> updateUserStatus(
            @PathVariable @Positive(message = "id必须为正数") Long id,
            @Valid @RequestBody SystemDto.UserStatusPatchRequest request) {
        String status = request.getStatus();
        return ApiResponse.success(apiSpecService.updateUserStatus(id, status));
    }

    @DeleteMapping("/users/{id}")
    public ApiResponse<CommonVo.OperationResult> deleteUser(@PathVariable @Positive(message = "id必须为正数") Long id) {
        return ApiResponse.success(apiSpecService.deleteUser(id));
    }

    @GetMapping("/departments")
    public ApiResponse<PageResult<SystemVo.DepartmentItem>> departments(
            @RequestParam(required = false) String deptName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @Min(value = 1, message = "pageNum必须大于等于1") Integer pageNum,
            @RequestParam(required = false) @Min(value = 1, message = "pageSize必须大于等于1") Integer pageSize) {
        return ApiResponse.success(apiSpecService.pageDepartments(deptName, status, pageNum, pageSize));
    }

    @PostMapping("/departments")
    public ApiResponse<SystemVo.DepartmentItem> createDepartment(
            @Valid @RequestBody SystemDto.DepartmentUpsertRequest request) {
        return ApiResponse.success(apiSpecService.createDepartment(request));
    }

    @PutMapping("/departments/{id}")
    public ApiResponse<SystemVo.DepartmentItem> updateDepartment(
            @PathVariable @Positive(message = "id必须为正数") Long id,
            @Valid @RequestBody SystemDto.DepartmentUpsertRequest request) {
        return ApiResponse.success(apiSpecService.updateDepartment(id, request));
    }

    @DeleteMapping("/departments/{id}")
    public ApiResponse<CommonVo.OperationResult> deleteDepartment(@PathVariable @Positive(message = "id必须为正数") Long id) {
        return ApiResponse.success(apiSpecService.deleteDepartment(id));
    }

    @GetMapping("/roles")
    public ApiResponse<PageResult<SystemVo.RoleItem>> roles(@RequestParam(required = false) String roleName,
                                                            @RequestParam(required = false) String status,
                                                            @RequestParam(required = false) @Min(value = 1, message = "pageNum必须大于等于1") Integer pageNum,
                                                            @RequestParam(required = false) @Min(value = 1, message = "pageSize必须大于等于1") Integer pageSize) {
        return ApiResponse.success(apiSpecService.pageRoles(roleName, status, pageNum, pageSize));
    }

    @PostMapping("/roles")
    public ApiResponse<SystemVo.RoleItem> createRole(@Valid @RequestBody SystemDto.RoleUpsertRequest request) {
        return ApiResponse.success(apiSpecService.createRole(request));
    }

    @PutMapping("/roles/{id}")
    public ApiResponse<SystemVo.RoleItem> updateRole(@PathVariable @Positive(message = "id必须为正数") Long id,
                                                     @Valid @RequestBody SystemDto.RoleUpsertRequest request) {
        return ApiResponse.success(apiSpecService.updateRole(id, request));
    }

    @DeleteMapping("/roles/{id}")
    public ApiResponse<CommonVo.OperationResult> deleteRole(@PathVariable @Positive(message = "id必须为正数") Long id) {
        return ApiResponse.success(apiSpecService.deleteRole(id));
    }

    @GetMapping("/menus/tree")
    public ApiResponse<List<SystemVo.MenuTreeItem>> menuTree() {
        return ApiResponse.success(apiSpecService.getMenuTree());
    }

    @PutMapping("/roles/{id}/menus")
    public ApiResponse<CommonVo.OperationResult> updateRoleMenus(
            @PathVariable @Positive(message = "id必须为正数") Long id,
            @Valid @RequestBody SystemDto.RoleMenuUpdateRequest request) {
        List<Long> menuIds = request.getMenuIds();
        return ApiResponse.success(apiSpecService.updateRoleMenus(id, menuIds));
    }
}
