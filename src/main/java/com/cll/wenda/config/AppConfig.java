package com.cll.wenda.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * @author chenliangliang
 * @date: 2017/10/30
 */

@Configuration
public class AppConfig {


    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        /*设置文件大小限制 ,超了，页面会抛出异常信息，这时候就需要进行异常信息的处理了;*/
        factory.setMaxFileSize(30*1024L * 1024L);
        /// 设置总上传数据总大小

        factory.setMaxRequestSize("256KB");

        //Sets the directory location where files will be stored.

        /*factory.setLocation("路径地址");*/
        return factory.createMultipartConfig();

    }


    /* 配置Http使其自动重定向到Https */
    /*@Bean
    public EmbeddedServletContainerFactory servletContainerFactory(){


        TomcatEmbeddedServletContainerFactory factory=new TomcatEmbeddedServletContainerFactory(){

            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint=new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection=new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };

        factory.addAdditionalTomcatConnectors(createConnector());
        return factory;
    }


    private Connector createConnector(){
        Connector connector=new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setSecure(false);
        connector.setPort(8080);
        connector.setRedirectPort(8443);
        return connector;
    }*/

}
