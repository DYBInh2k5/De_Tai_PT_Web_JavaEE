package com.ttjavaee.classroom.entity;

import jakarta.persistence.*;
import lombok.*;

// ============================================================
// Entity: Student dai dien bang "Students" trong SQL Server
// Moi doi tuong Student la 1 dong trong bang Students
// ============================================================
@Entity                         // Danh dau day la 1 JPA Entity
@Table(name = "Students")       // Map voi bang "Students" trong database
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    // ============================================================
    // Khoa chinh - tu dong tang
    // ============================================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ============================================================
    // Ma sinh vien: VD "SV001", "SV002"...
    // NOT NULL + UNIQUE
    // ============================================================
    @Column(nullable = false, unique = true)
    private String studentCode;

    // ============================================================
    // Ho va ten day du
    // ============================================================
    @Column(nullable = false)
    private String fullName;

    // ============================================================
    // Email lien he
    // ============================================================
    @Column(nullable = false)
    private String email;

    // ============================================================
    // QUAN HE MANY-TO-ONE: Nhieu sinh vien thuoc VE 1 lop
    //
    // @ManyToOne: nhieu Student tro ve 1 Classroom
    // fetch = LAZY: chi load thong tin lop khi can
    //
    // @JoinColumn(name = "class_id"):
    //   - Day la COT KHOA NGOAI trong bang Students
    //   - Tro den khoa chinh (id) cua bang Classes
    //   - Ben "chu" quan he: giu khoa ngoai
    //
    // Day la ben "OWNING SIDE" (chu quan he):
    //   - Student biet no thuoc lop nao
    //   - JPA dung field nay de cap nhat FK trong DB
    // ============================================================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private Classroom classroom;

}
