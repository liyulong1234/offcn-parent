package com.offcn.webui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Auther: lhq
 * @Date: 2020/12/7 09:32
 * @Description:
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)   //去除数据源的自动配置
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class ScwWebuiStart {
    public static void main(String[] args) {
        SpringApplication.run(ScwWebuiStart.class);
    }
}
