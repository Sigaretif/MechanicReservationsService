package com.zaioro.mechanicservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication//(exclude = {DataSourceAutoConfiguration.class })
public class MechanicServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MechanicServiceApplication.class, args);
	}

}
