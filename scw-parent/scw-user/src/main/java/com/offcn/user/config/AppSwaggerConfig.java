package com.offcn.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;


/**
 * @Auther: lhq
 * @Date: 2020/11/30 11:19
 * @Description: swagger配置类
 */
@Configuration
@EnableSwagger2 //开启swagger的配置
public class AppSwaggerConfig {

    //swagger的介绍方法
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("七易众筹项目接口文档")
                .description("用户模块接口描述")
                .contact("刘老师")
                .version("1.0")
                .termsOfServiceUrl("www.ujiuye.com").build();
    }


    @Bean
    public Docket  createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.offcn.user.controller"))
                .paths(PathSelectors.any())
                .build();

    }
}
