package com.ttjavaee.classroom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClassroomSummaryDto {

    private Long id;
    private String classCode;
    private String className;
    private long studentCount;
    private String teacherName;
}
