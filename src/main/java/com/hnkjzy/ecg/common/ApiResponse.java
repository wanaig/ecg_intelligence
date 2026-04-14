package com.hnkjzy.ecg.common;

/**
 * 文件说明：公共组件文件。
 * 主要职责：负责统一响应结构与分页结构等公共能力。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


public class ApiResponse<T> {

    /**
     * 字段说明：编号或编码信息，字段名为 code。
     */
    private int code;
    /**
     * 字段说明：年龄信息，字段名为 message。
     */
    private String message;
    /**
     * 字段说明：业务字段，字段名为 data。
     */
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 方法说明：业务处理业务方法，方法名为 success。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, "success", data);
    }

    /**
     * 方法说明：业务处理业务方法，方法名为 success。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(0, message, data);
    }

    /**
     * 方法说明：业务处理业务方法，方法名为 fail。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(1, message, null);
    }

    /**
     * 方法说明：查询业务方法，方法名为 getCode。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public int getCode() {
        return code;
    }

    /**
     * 方法说明：业务处理业务方法，方法名为 setCode。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 方法说明：查询业务方法，方法名为 getMessage。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public String getMessage() {
        return message;
    }

    /**
     * 方法说明：业务处理业务方法，方法名为 setMessage。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 方法说明：查询业务方法，方法名为 getData。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public T getData() {
        return data;
    }

    /**
     * 方法说明：业务处理业务方法，方法名为 setData。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public void setData(T data) {
        this.data = data;
    }
}
