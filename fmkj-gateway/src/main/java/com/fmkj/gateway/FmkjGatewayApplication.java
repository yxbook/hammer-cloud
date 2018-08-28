package com.fmkj.gateway;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
@EnableAutoConfiguration(exclude ={DataSourceAutoConfiguration.class,DruidDataSourceAutoConfigure.class})
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class FmkjGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FmkjGatewayApplication.class, args);
	}

}
