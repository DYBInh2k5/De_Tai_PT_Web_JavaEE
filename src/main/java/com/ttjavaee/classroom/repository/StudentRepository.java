package com.ttjavaee.classroom.repository;

import com.ttjavaee.classroom.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByClassroomId(Long classroomId);
}
