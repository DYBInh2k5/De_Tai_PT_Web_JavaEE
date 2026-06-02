package com.ttjavaee.classroom.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherRequest {

    @NotBlank
    @Size(max = 50)
    private String teacherCode;

    @NotBlank
    @Size(max = 255)
    private String fullName;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;
}
