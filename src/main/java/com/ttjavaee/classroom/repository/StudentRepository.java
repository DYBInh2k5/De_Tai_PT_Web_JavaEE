package com.ttjavaee.classroom.repository;

import com.ttjavaee.classroom.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Lọc sinh viên theo lớp — Spring Data JPA tự sinh SQL: WHERE class_id = ?
    List<Student> findByClassroomId(Long classroomId);
}
