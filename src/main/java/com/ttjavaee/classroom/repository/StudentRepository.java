package com.ttjavaee.classroom.repository;

import com.ttjavaee.classroom.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByClassroomId(Long classroomId);

    @EntityGraph(attributePaths = "classroom")
    Page<Student> findByClassroomId(Long classroomId, Pageable pageable);

    @EntityGraph(attributePaths = "classroom")
    @Query("""
            SELECT s FROM Student s
            LEFT JOIN s.classroom c
            WHERE (:classId IS NULL OR c.id = :classId)
              AND (:keyword IS NULL OR :keyword = ''
                   OR LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(s.studentCode) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Student> searchStudents(
            @Param("classId") Long classId,
            @Param("keyword") String keyword,
            Pageable pageable);
}
