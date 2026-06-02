package com.ttjavaee.classroom.service;

import com.ttjavaee.classroom.entity.Grade;
import com.ttjavaee.classroom.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    public Page<Grade> getAllGrades(Pageable pageable) {
        return gradeRepository.findAll(pageable);
    }

    public Page<Grade> getGradesByStudent(Long studentId, Pageable pageable) {
        if (studentId == null) {
            return gradeRepository.findAll(pageable);
        }
        return gradeRepository.findByStudentId(studentId, pageable);
    }

    public Grade getGradeById(Long id) {
        return gradeRepository.findById(id).orElse(null);
    }

    public Grade saveGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }

    public Double getAverageScore() {
        return gradeRepository.findAverageScore();
    }
}
