package com.ttjavaee.classroom.api.mapper;

import com.ttjavaee.classroom.api.dto.*;
import com.ttjavaee.classroom.dto.ClassroomSummaryDto;
import com.ttjavaee.classroom.dto.DashboardStatsDto;
import com.ttjavaee.classroom.entity.*;

public final class ApiMapper {

    private ApiMapper() {
    }

    public static ClassroomResponse toClassroomResponse(Classroom c) {
        long count = c.getStudents() != null ? c.getStudents().size() : 0;
        return new ClassroomResponse(
                c.getId(),
                c.getClassCode(),
                c.getClassName(),
                c.getTeacher() != null ? c.getTeacher().getId() : null,
                c.getTeacher() != null ? c.getTeacher().getFullName() : null,
                count
        );
    }

    public static ClassroomResponse toClassroomResponse(ClassroomSummaryDto dto) {
        return new ClassroomResponse(
                dto.getId(),
                dto.getClassCode(),
                dto.getClassName(),
                null,
                dto.getTeacherName(),
                dto.getStudentCount()
        );
    }

    public static StudentResponse toStudentResponse(Student s) {
        return new StudentResponse(
                s.getId(),
                s.getStudentCode(),
                s.getFullName(),
                s.getEmail(),
                s.getClassroom() != null ? s.getClassroom().getId() : null,
                s.getClassroom() != null ? s.getClassroom().getClassName() : null
        );
    }

    public static TeacherResponse toTeacherResponse(Teacher t) {
        return new TeacherResponse(t.getId(), t.getTeacherCode(), t.getFullName(), t.getEmail());
    }

    public static SubjectResponse toSubjectResponse(Subject s) {
        return new SubjectResponse(s.getId(), s.getSubjectCode(), s.getSubjectName(), s.getCredits());
    }

    public static GradeResponse toGradeResponse(Grade g) {
        return new GradeResponse(
                g.getId(),
                g.getStudent().getId(),
                g.getStudent().getStudentCode(),
                g.getStudent().getFullName(),
                g.getSubject().getId(),
                g.getSubject().getSubjectName(),
                g.getScore(),
                g.getExamDate()
        );
    }

    public static Classroom apply(ClassroomRequest req, Classroom entity, Teacher teacher) {
        entity.setClassCode(req.getClassCode());
        entity.setClassName(req.getClassName());
        entity.setTeacher(teacher);
        return entity;
    }

    public static Student apply(StudentRequest req, Student entity, Classroom classroom) {
        entity.setStudentCode(req.getStudentCode());
        entity.setFullName(req.getFullName());
        entity.setEmail(req.getEmail());
        entity.setClassroom(classroom);
        return entity;
    }

    public static Teacher apply(TeacherRequest req, Teacher entity) {
        entity.setTeacherCode(req.getTeacherCode());
        entity.setFullName(req.getFullName());
        entity.setEmail(req.getEmail());
        return entity;
    }

    public static Subject apply(SubjectRequest req, Subject entity) {
        entity.setSubjectCode(req.getSubjectCode());
        entity.setSubjectName(req.getSubjectName());
        entity.setCredits(req.getCredits());
        return entity;
    }

    public static Grade apply(GradeRequest req, Grade entity, Student student, Subject subject) {
        entity.setStudent(student);
        entity.setSubject(subject);
        entity.setScore(req.getScore());
        entity.setExamDate(req.getExamDate());
        return entity;
    }

    public static DashboardStatsDto copyStats(DashboardStatsDto stats) {
        return stats;
    }
}
