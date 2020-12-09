package com.offcn.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Auther: lhq
 * @Date: 2020/12/3 09:25
 * @Description:
 */
@SpringCloudApplication  //@SpringBootApplication+@EnableDiscoveryClient+@EnableCircuitBreaker
@MapperScan("com.offcn.order.mapper")
@EnableFeignClients
public class ScwOrderStart {

    public static void main(String[] args) {
        SpringApplication.run(ScwOrderStart.class);
    }
}
