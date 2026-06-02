package com.ttjavaee.classroom.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentResponse {

    private Long id;
    private String studentCode;
    private String fullName;
    private String email;
    private Long classroomId;
    private String classroomName;
}
