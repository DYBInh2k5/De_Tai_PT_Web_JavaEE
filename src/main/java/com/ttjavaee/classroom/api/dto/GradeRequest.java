package com.ttjavaee.classroom.api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class GradeRequest {

    @NotNull
    private Long studentId;

    @NotNull
    private Long subjectId;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("10.0")
    private BigDecimal score;

    private LocalDate examDate;
}
