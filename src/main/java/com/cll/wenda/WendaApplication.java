package com.cll.wenda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author chenliangliang
 * @date 2017/11/11
 */
@SpringBootApplication
@ServletComponentScan(basePackageClasses = com.cll.wenda.filter.AuthFilter.class)
@CrossOrigin(methods = {RequestMethod.POST,RequestMethod.DELETE,RequestMethod.GET},origins = "*")/*跨域访问*/
public class WendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(WendaApplication.class, args);
	}
}
