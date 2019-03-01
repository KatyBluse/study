package com.study.studymarket.common.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class Swagger2 {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("测试文档1")
                .select()  // 选择那些路径和api会生成document
                .apis(RequestHandlerSelectors.basePackage("com.study.studymarket.controller")) //对所有api进行监控
                .paths(PathSelectors.any()) // 对所有路径进行监控
                //不显示错误的接口地址
//                .paths(Predicates.not(PathSelectors.regex("/error.*")))//错误路径不监控
//                .paths(PathSelectors.regex("/.*"))// 对根下所有路径进行监控
                .build().apiInfo(apiTemplate());
    }


    @Bean
    public Docket createRestApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("测试文档2")
                .select()  // 选择那些路径和api会生成document
                .apis(RequestHandlerSelectors.basePackage("com.study.studymarket.controller")) //对所有api进行监控
                .paths(PathSelectors.any()) // 对所有路径进行监控
                //不显示错误的接口地址
//                .paths(Predicates.not(PathSelectors.regex("/error.*")))//错误路径不监控
//                .paths(PathSelectors.regex("/.*"))// 对根下所有路径进行监控
                .build().apiInfo(apiTemplate1());
    }

    //设置预定文档的包含的参数
    private ApiInfo apiTemplate1 () {
        return new ApiInfoBuilder()
                .title("这是测试swapper的title2") //标题
                .contact(new Contact("彭思源", "", "")) //作者
                .license("Apache License, Version 2.0") //许可版本名称
                .licenseUrl("http://www.apache.org/licenses/") //许可地址
//                .termsOfServiceUrl("http://www.google.com.hk") //（不可见）条款地址
                .version("1.0")//版本号
                .description("这是测试swapper的描述") //描述
                .build();
    }

    //设置预定文档的包含的参数
    private ApiInfo apiTemplate () {
        return new ApiInfoBuilder()
                .title("这是测试swapper的title1")
                .version("2.0")//版本号
                .description("这是测试swapper的描述")
                .build();
    }
}
