package com.ttjavaee.classroom.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubjectResponse {

    private Long id;
    private String subjectCode;
    private String subjectName;
    private Integer credits;
}
