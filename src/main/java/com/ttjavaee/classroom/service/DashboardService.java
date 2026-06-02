package com.ttjavaee.classroom.service;

import com.ttjavaee.classroom.dto.DashboardStatsDto;
import com.ttjavaee.classroom.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private GradeRepository gradeRepository;

    public DashboardStatsDto getDashboardStats() {
        return new DashboardStatsDto(
                classroomRepository.count(),
                studentRepository.count(),
                teacherRepository.count(),
                subjectRepository.count(),
                gradeRepository.count(),
                gradeRepository.findAverageScore(),
                classroomRepository.findTopByStudentCount(PageRequest.of(0, 5))
        );
    }
}
