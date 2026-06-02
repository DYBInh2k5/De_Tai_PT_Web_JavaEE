package com.ttjavaee.classroom.repository;

import com.ttjavaee.classroom.dto.ClassroomSummaryDto;
import com.ttjavaee.classroom.entity.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    @Query("""
            SELECT new com.ttjavaee.classroom.dto.ClassroomSummaryDto(
                c.id, c.classCode, c.className, SIZE(c.students),
                COALESCE(t.fullName, 'Chưa gán'))
            FROM Classroom c
            LEFT JOIN c.teacher t
            """)
    Page<ClassroomSummaryDto> findAllSummaries(Pageable pageable);

    @Query("""
            SELECT new com.ttjavaee.classroom.dto.ClassroomSummaryDto(
                c.id, c.classCode, c.className, SIZE(c.students),
                COALESCE(t.fullName, 'Chưa gán'))
            FROM Classroom c
            LEFT JOIN c.teacher t
            ORDER BY SIZE(c.students) DESC
            """)
    List<ClassroomSummaryDto> findTopByStudentCount(Pageable pageable);
}
