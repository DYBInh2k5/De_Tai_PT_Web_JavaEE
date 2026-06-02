package com.ttjavaee.classroom.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectRequest {

    @NotBlank
    @Size(max = 50)
    private String subjectCode;

    @NotBlank
    @Size(max = 255)
    private String subjectName;

    @NotNull
    @Min(1)
    @Max(10)
    private Integer credits;
}
