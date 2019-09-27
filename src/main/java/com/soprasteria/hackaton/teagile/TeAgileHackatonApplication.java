package com.soprasteria.hackaton.teagile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableDiscoveryClient
@SpringBootApplication
public class TeAgileHackatonApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeAgileHackatonApplication.class, args);
	}

}
