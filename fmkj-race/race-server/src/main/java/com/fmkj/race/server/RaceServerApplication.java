package com.fmkj.race.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@SpringCloudApplication
@ComponentScan(basePackages = {"com.fmkj.race.server","com.fmkj.race.dao.domain"})     //多模块注入
@MapperScan("com.fmkj.race.dao.*")
@EnableSwagger2
public class RaceServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RaceServerApplication.class, args);
	}
}
