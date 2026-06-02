package com.ttjavaee.classroom.repository;

import com.ttjavaee.classroom.entity.Grade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    @EntityGraph(attributePaths = {"student", "subject"})
    Page<Grade> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"student", "subject"})
    @Query("SELECT g FROM Grade g WHERE g.student.id = :studentId")
    Page<Grade> findByStudentId(Long studentId, Pageable pageable);

    @Query("SELECT AVG(g.score) FROM Grade g")
    Double findAverageScore();
}
