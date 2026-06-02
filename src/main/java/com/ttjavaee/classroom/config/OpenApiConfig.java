package com.ttjavaee.classroom.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI classroomOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Classroom Management API")
                        .description("REST API — Quản lý Lớp học & Sinh viên (Đề tài 07 JPA)")
                        .version("v1")
                        .contact(new Contact().name("Nhóm PT Web Java EE")));
    }
}
