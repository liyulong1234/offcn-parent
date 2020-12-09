package com.offcn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Auther: lhq
 * @Date: 2020/11/30 09:50
 * @Description:
 */
@SpringBootApplication
@EnableEurekaServer
public class ScwRegisterStart {

    public static void main(String[] args) {
        SpringApplication.run(ScwRegisterStart.class);
    }
}
