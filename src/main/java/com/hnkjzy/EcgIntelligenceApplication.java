package com.hnkjzy;

/**
 * 文件说明：基础文件。
 * 主要职责：负责应用启动与基础支撑能力。
 * 维护约定：注释采用中文，便于临床业务沟通、二次开发与运维排查。
 */


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/**
 * 类说明：EcgIntelligenceApplication。
 * 业务定位：负责应用启动与基础支撑能力。
 * 说明补充：该类中的字段、方法和返回值均遵循统一命名与结构规范。
 */
public class EcgIntelligenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcgIntelligenceApplication.class, args);
    }

}
