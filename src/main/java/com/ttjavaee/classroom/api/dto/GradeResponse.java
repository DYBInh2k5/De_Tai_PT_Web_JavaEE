package com.ttjavaee.classroom.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class GradeResponse {

    private Long id;
    private Long studentId;
    private String studentCode;
    private String studentName;
    private Long subjectId;
    private String subjectName;
    private BigDecimal score;
    private LocalDate examDate;
}
