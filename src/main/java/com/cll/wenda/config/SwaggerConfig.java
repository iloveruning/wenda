package com.cll.wenda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author chenliangliang
 * @date: 2017/11/24
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {


    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SPRING_WEB)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cll.wenda.controller"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("问答小程序API")
                .contact(new Contact("明理苑技术部","","chenliangliang68@163.com"))
                .version("1.0")
                .build();
    }
}
