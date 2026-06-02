package com.ttjavaee.classroom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardStatsDto {

    private long totalClasses;
    private long totalStudents;
    private long totalTeachers;
    private long totalSubjects;
    private long totalGrades;
    private Double averageScore;
    private List<ClassroomSummaryDto> topClassesByStudents;
}
