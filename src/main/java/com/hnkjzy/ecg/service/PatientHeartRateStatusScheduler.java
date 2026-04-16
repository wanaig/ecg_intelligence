package com.hnkjzy.ecg.service;

/**
 * 文件说明：定时任务文件。
 * 主要职责：负责执行周期性心率状态刷新任务。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */

import com.hnkjzy.ecg.mapper.PatientMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
/**
 * 类说明：PatientHeartRateStatusScheduler。
 * 业务定位：负责执行周期性心率状态刷新任务。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class PatientHeartRateStatusScheduler {

    private final PatientMapper patientMapper;

    public PatientHeartRateStatusScheduler(PatientMapper patientMapper) {
        this.patientMapper = patientMapper;
    }

    @Scheduled(cron = "*/10 * * * * *")
    /**
     * 方法说明：处理业务方法，方法名为 randomizePatientHeartRate。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public void randomizePatientHeartRate() {
        patientMapper.randomizeHeartRate();
    }

    @Scheduled(cron = "0 * * * * *")
    /**
     * 方法说明：处理业务方法，方法名为 refreshPatientStatusByHeartRate。
     * 处理流程：执行参数校验、业务逻辑处理并返回稳定结果。
     * 注意事项：保持事务边界清晰，避免出现脏数据与状态不一致。
     */
    public void refreshPatientStatusByHeartRate() {
        patientMapper.refreshHeartRateStatus();
    }
}
