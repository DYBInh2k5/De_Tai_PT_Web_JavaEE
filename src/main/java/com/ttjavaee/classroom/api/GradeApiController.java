package com.ttjavaee.classroom.api;

import com.ttjavaee.classroom.api.dto.*;
import com.ttjavaee.classroom.api.mapper.ApiMapper;
import com.ttjavaee.classroom.entity.Grade;
import com.ttjavaee.classroom.entity.Student;
import com.ttjavaee.classroom.entity.Subject;
import com.ttjavaee.classroom.exception.ResourceNotFoundException;
import com.ttjavaee.classroom.service.GradeService;
import com.ttjavaee.classroom.service.StudentService;
import com.ttjavaee.classroom.service.SubjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/grades")
@Tag(name = "Điểm", description = "CRUD điểm sinh viên")
public class GradeApiController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<GradeResponse>>> list(
            @RequestParam(required = false) Long studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        var result = gradeService.getGradesByStudent(studentId, pageable)
                .map(ApiMapper::toGradeResponse);
        return ResponseEntity.ok(ApiResponse.ok(PageResponse.from(result)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GradeResponse>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(ApiMapper.toGradeResponse(requireGrade(id))));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GradeResponse>> create(@Valid @RequestBody GradeRequest request) {
        Grade grade = ApiMapper.apply(request, new Grade(),
                requireStudent(request.getStudentId()),
                requireSubject(request.getSubjectId()));
        grade = gradeService.saveGrade(grade);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Đã tạo điểm", ApiMapper.toGradeResponse(grade)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GradeResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody GradeRequest request) {
        Grade grade = requireGrade(id);
        ApiMapper.apply(request, grade,
                requireStudent(request.getStudentId()),
                requireSubject(request.getSubjectId()));
        grade = gradeService.saveGrade(grade);
        return ResponseEntity.ok(ApiResponse.ok("Đã cập nhật điểm", ApiMapper.toGradeResponse(grade)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        requireGrade(id);
        gradeService.deleteGrade(id);
        return ResponseEntity.ok(ApiResponse.ok("Đã xóa điểm", null));
    }

    private Grade requireGrade(Long id) {
        Grade grade = gradeService.getGradeById(id);
        if (grade == null) {
            throw new ResourceNotFoundException("Không tìm thấy điểm id=" + id);
        }
        return grade;
    }

    private Student requireStudent(Long id) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            throw new ResourceNotFoundException("Không tìm thấy sinh viên id=" + id);
        }
        return student;
    }

    private Subject requireSubject(Long id) {
        Subject subject = subjectService.getSubjectById(id);
        if (subject == null) {
            throw new ResourceNotFoundException("Không tìm thấy môn học id=" + id);
        }
        return subject;
    }
}
