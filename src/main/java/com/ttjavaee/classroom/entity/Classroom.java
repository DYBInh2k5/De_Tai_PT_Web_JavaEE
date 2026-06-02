package com.ttjavaee.classroom.entity;

import jakarta.persistence.*;
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

    @Column(nullable = false, unique = true)
    private String classCode;

    @Column(nullable = false)
    private String className;

    // Quan hệ One-to-Many: 1 lớp có nhiều sinh viên
    // cascade không dùng REMOVE vì DB dùng ON DELETE SET NULL
    @OneToMany(mappedBy = "classroom", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Student> students;
}
