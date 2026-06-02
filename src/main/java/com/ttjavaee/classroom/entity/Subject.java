package com.ttjavaee.classroom.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "Subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Mã môn không được để trống")
    @Size(max = 50)
    @Column(nullable = false, unique = true)
    private String subjectCode;

    @NotBlank(message = "Tên môn không được để trống")
    @Size(max = 255)
    @Column(nullable = false)
    private String subjectName;

    @NotNull(message = "Số tín chỉ không được để trống")
    @Min(value = 1, message = "Số tín chỉ tối thiểu là 1")
    @Max(value = 10, message = "Số tín chỉ tối đa là 10")
    @Column(nullable = false)
    private Integer credits;
}
