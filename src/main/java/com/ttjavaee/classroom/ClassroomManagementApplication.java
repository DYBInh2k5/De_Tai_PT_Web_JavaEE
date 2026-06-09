package com.ttjavaee.classroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// ============================================================
// Lop chinh khoi dong ung dung Spring Boot
// @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan
// Khi chay file nay, Spring Boot se:
//   1. Khoi dong embedded Tomcat (mac dinh cong 8080)
//   2. Quet toan bo package "com.ttjavaee.classroom" de tim Bean
//   3. Tu dong cau hinh JPA, datasource, transaction,...
// ============================================================
@SpringBootApplication
public class ClassroomManagementApplication {

	public static void main(String[] args) {
		// SpringApplication.run() la phuong thuc tinh giup boot server
		SpringApplication.run(ClassroomManagementApplication.class, args);
	}

}
