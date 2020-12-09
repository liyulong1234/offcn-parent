package com.offcn.project.config;

import com.offcn.utils.OSSTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: lhq
 * @Date: 2020/12/1 14:30
 * @Description:
 */
@Configuration
public class AppProjectConfig {

    @ConfigurationProperties(prefix = "oss")
    @Bean
    public OSSTemplate getOSSTemplate(){
        return new OSSTemplate();
    }
}
