package com.ttjavaee.classroom.api;

import com.ttjavaee.classroom.api.dto.*;
import com.ttjavaee.classroom.api.mapper.ApiMapper;
import com.ttjavaee.classroom.entity.Classroom;
import com.ttjavaee.classroom.entity.Student;
import com.ttjavaee.classroom.exception.ResourceNotFoundException;
import com.ttjavaee.classroom.service.ClassroomService;
import com.ttjavaee.classroom.service.StudentService;
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
@RequestMapping("/api/v1/students")
@Tag(name = "Sinh viên", description = "CRUD sinh viên")
public class StudentApiController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassroomService classroomService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<StudentResponse>>> list(
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("studentCode").ascending());
        var result = studentService.searchStudents(classId, keyword, pageable)
                .map(ApiMapper::toStudentResponse);
        return ResponseEntity.ok(ApiResponse.ok(PageResponse.from(result)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(ApiMapper.toStudentResponse(requireStudent(id))));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> create(@Valid @RequestBody StudentRequest request) {
        Student student = ApiMapper.apply(request, new Student(), resolveClassroom(request.getClassroomId()));
        student = studentService.saveStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Đã tạo sinh viên", ApiMapper.toStudentResponse(student)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {
        Student student = requireStudent(id);
        ApiMapper.apply(request, student, resolveClassroom(request.getClassroomId()));
        student = studentService.saveStudent(student);
        return ResponseEntity.ok(ApiResponse.ok("Đã cập nhật sinh viên", ApiMapper.toStudentResponse(student)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        requireStudent(id);
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.ok("Đã xóa sinh viên", null));
    }

    private Student requireStudent(Long id) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            throw new ResourceNotFoundException("Không tìm thấy sinh viên id=" + id);
        }
        return student;
    }

    private Classroom resolveClassroom(Long classroomId) {
        if (classroomId == null) {
            return null;
        }
        Classroom classroom = classroomService.getClassroomById(classroomId);
        if (classroom == null) {
            throw new ResourceNotFoundException("Không tìm thấy lớp học id=" + classroomId);
        }
        return classroom;
    }
}
