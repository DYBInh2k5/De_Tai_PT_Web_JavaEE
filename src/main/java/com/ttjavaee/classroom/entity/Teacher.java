package com.ttjavaee.classroom.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Mã giảng viên không được để trống")
    @Size(max = 50)
    @Column(nullable = false, unique = true)
    private String teacherCode;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 255)
    @Column(nullable = false)
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 255)
    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<Classroom> classrooms;
}
