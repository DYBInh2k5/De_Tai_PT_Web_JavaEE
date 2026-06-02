package com.ttjavaee.classroom.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "Classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Mã lớp không được để trống")
    @Size(max = 50, message = "Mã lớp tối đa 50 ký tự")
    @Column(nullable = false, unique = true)
    private String classCode;

    @NotBlank(message = "Tên lớp không được để trống")
    @Size(max = 255, message = "Tên lớp tối đa 255 ký tự")
    @Column(nullable = false)
    private String className;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    // cascade không dùng REMOVE vì DB dùng ON DELETE SET NULL
    // Dùng PERSIST + MERGE để save/update lan truyền, nhưng xóa lớp không xóa sinh viên
    @OneToMany(mappedBy = "classroom", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Student> students;
}
