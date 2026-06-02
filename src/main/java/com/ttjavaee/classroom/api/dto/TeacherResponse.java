package com.ttjavaee.classroom.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeacherResponse {

    private Long id;
    private String teacherCode;
    private String fullName;
    private String email;
}
