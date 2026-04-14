package com.hnkjzy.ecg.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CommonVo {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperationResult {
        private String targetId;
        private String action;
        private String message;
    }
}
