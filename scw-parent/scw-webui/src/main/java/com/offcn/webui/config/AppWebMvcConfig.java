package com.offcn.webui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Auther: lhq
 * @Date: 2020/12/7 09:43
 * @Description:
 */
@Configuration
public class AppWebMvcConfig implements WebMvcConfigurer {



   public  void addViewControllers(ViewControllerRegistry registry){
       //如果controller仅仅用于转发页面，那在当前方法中配置映射即可
       registry.addViewController("/login.html").setViewName("/login");
   }
}
