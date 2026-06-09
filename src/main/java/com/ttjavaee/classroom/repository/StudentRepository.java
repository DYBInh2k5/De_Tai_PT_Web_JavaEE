package com.ttjavaee.classroom.repository;

import com.ttjavaee.classroom.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// ============================================================
// Repository: thao tac voi bang Students
// JpaRepository<Student, Long> cung cap san CRUD co ban
// ============================================================
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // ============================================================
    // Phuong thuc tu dinh nghia: loc sinh vien theo lop
    //
    // Spring Data JPA phan tich ten phuong thuc:
    //   findBy + ClassroomId -> tu dong sinh:
    //   SELECT * FROM Students WHERE class_id = ?
    //
    // Khong can viet @Query hay SQL thu cong!
    // ============================================================
    List<Student> findByClassroomId(Long classroomId);
}
