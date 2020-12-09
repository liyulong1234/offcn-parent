package com.offcn.webui.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: lhq
 * @Date: 2020/12/7 09:46
 * @Description:
 */
@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level getFeignLogger(){
        return Logger.Level.FULL;
    }
}
