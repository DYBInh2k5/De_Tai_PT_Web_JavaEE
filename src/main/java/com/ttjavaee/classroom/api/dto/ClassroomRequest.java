package com.ttjavaee.classroom.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassroomRequest {

    @NotBlank
    @Size(max = 50)
    private String classCode;

    @NotBlank
    @Size(max = 255)
    private String className;

    private Long teacherId;
}
