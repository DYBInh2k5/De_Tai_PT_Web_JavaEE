package com.ttjavaee.classroom.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClassroomResponse {

    private Long id;
    private String classCode;
    private String className;
    private Long teacherId;
    private String teacherName;
    private long studentCount;
}
