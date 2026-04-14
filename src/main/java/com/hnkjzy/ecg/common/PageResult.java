package com.hnkjzy.ecg.common;

/**
 * 文件说明：公共组件文件。
 * 主要职责：负责统一响应结构与分页结构等公共能力。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

/**
 * 类说明：PageResult。
 * 业务定位：负责统一响应结构与分页结构等公共能力。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class PageResult<T> {

    /**
     * 字段说明：统计数量，字段名为 total。
     */
    private long total;
    /**
        * 字段说明：页码，字段名为 pageNum。
     */
        private int pageNum;
    /**
        * 字段说明：页大小，字段名为 pageSize。
     */
        private int pageSize;
    /**
        * 字段说明：分页列表，字段名为 list。
     */
        private List<T> list;

    public PageResult() {
    }

    public PageResult(long total, int pageNum, int pageSize, List<T> list) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.list = list;
    }

    /**
     * 方法说明：查询业务方法，方法名为 getTotal。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public long getTotal() {
        return total;
    }

    /**
     * 方法说明：业务处理业务方法，方法名为 setTotal。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * 方法说明：查询业务方法，方法名为 getPage。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public int getPageNum() {
        return pageNum;
    }

    /**
     * 方法说明：业务处理业务方法，方法名为 setPage。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * 方法说明：查询业务方法，方法名为 getSize。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 方法说明：业务处理业务方法，方法名为 setSize。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 方法说明：查询业务方法，方法名为 getRecords。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public List<T> getList() {
        return list;
    }

    /**
     * 方法说明：业务处理业务方法，方法名为 setRecords。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public void setList(List<T> list) {
        this.list = list;
    }

    @JsonIgnore
    public int getPage() {
        return pageNum;
    }

    public void setPage(int page) {
        this.pageNum = page;
    }

    @JsonIgnore
    public int getSize() {
        return pageSize;
    }

    public void setSize(int size) {
        this.pageSize = size;
    }

    @JsonIgnore
    public List<T> getRecords() {
        return list;
    }

    public void setRecords(List<T> records) {
        this.list = records;
    }
}
