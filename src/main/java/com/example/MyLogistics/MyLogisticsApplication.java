package com.example.MyLogistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MyLogisticsApplication {
	public static void main(String[] args) {
		SpringApplication.run(MyLogisticsApplication.class, args);
	}
}

