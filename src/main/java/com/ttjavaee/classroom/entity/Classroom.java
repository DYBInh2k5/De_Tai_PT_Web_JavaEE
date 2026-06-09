package com.ttjavaee.classroom.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

// ============================================================
// Entity: Classroom dai dien bang "Classes" trong SQL Server
// Moi doi tuong Classroom la 1 dong trong bang Classes
// ============================================================
@Entity                         // Danh dau day la 1 JPA Entity (map voi 1 bang CSDL)
@Table(name = "Classes")        // Chi ro ten bang la "Classes" (neu khong co, mac dinh la ten lop)
@Getter                         // Lombok: tu dong sinh getter cho tat ca field
@Setter                         // Lombok: tu dong sinh setter cho tat ca field
@NoArgsConstructor              // Lombok: sinh constructor khong tham so (JPA can)
@AllArgsConstructor             // Lombok: sinh constructor co tham so (tien loi)
public class Classroom {

    // ============================================================
    // Khoa chinh (Primary Key) - tu dong tang (IDENTITY)
    // ============================================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // SQL Server tu dong tang
    private Long id;

    // ============================================================
    // Ma lop: VD "CNTT01", "KTPM01"
    // NOT NULL + UNIQUE: khong duoc de trong, khong duoc trung
    // ============================================================
    @Column(nullable = false, unique = true)
    private String classCode;

    // ============================================================
    // Ten lop: VD "Cong nghe thong tin 01"
    // ============================================================
    @Column(nullable = false)
    private String className;

    // ============================================================
    // QUAN HE ONE-TO-MANY: Mot lop co NHIEU sinh vien
    //
    // mappedBy = "classroom": cho biet ben Student (entity con)
    //   co field ten "classroom" la chu quan he (ben chua khoa ngoai)
    //
    // cascade = {PERSIST, MERGE}:
    //   - PERSIST: khi luu Classroom, neu co Student trong danh sach
    //     thi cung tu dong luon Student
    //   - KHONG dung REMOVE vi trong CSDL da cai ON DELETE SET NULL
    //
    // fetch = FetchType.LAZY:
    //   - Khi load Classroom, danh sach Student CHUA duoc load ngay
    //   - Chi load khi thuc su goi classroom.getStudents()
    //   - Tranh load qua nhieu du lieu gay cham
    // ============================================================
    @OneToMany(mappedBy = "classroom", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Student> students;

}
