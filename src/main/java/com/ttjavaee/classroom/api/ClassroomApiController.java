package com.ttjavaee.classroom.api;

import com.ttjavaee.classroom.api.dto.*;
import com.ttjavaee.classroom.api.mapper.ApiMapper;
import com.ttjavaee.classroom.entity.Classroom;
import com.ttjavaee.classroom.entity.Teacher;
import com.ttjavaee.classroom.exception.ResourceNotFoundException;
import com.ttjavaee.classroom.service.ClassroomService;
import com.ttjavaee.classroom.service.TeacherService;
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
@RequestMapping("/api/v1/classes")
@Tag(name = "Lớp học", description = "CRUD lớp học")
public class ClassroomApiController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ClassroomResponse>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("classCode").ascending());
        var result = classroomService.getClassroomSummaries(pageable)
                .map(ApiMapper::toClassroomResponse);
        return ResponseEntity.ok(ApiResponse.ok(PageResponse.from(result)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClassroomResponse>> get(@PathVariable Long id) {
        Classroom classroom = requireClassroom(id);
        return ResponseEntity.ok(ApiResponse.ok(ApiMapper.toClassroomResponse(classroom)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClassroomResponse>> create(@Valid @RequestBody ClassroomRequest request) {
        Classroom classroom = ApiMapper.apply(request, new Classroom(), resolveTeacher(request.getTeacherId()));
        classroom = classroomService.saveClassroom(classroom);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Đã tạo lớp học", ApiMapper.toClassroomResponse(classroom)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClassroomResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ClassroomRequest request) {
        Classroom classroom = requireClassroom(id);
        ApiMapper.apply(request, classroom, resolveTeacher(request.getTeacherId()));
        classroom = classroomService.saveClassroom(classroom);
        return ResponseEntity.ok(ApiResponse.ok("Đã cập nhật lớp học", ApiMapper.toClassroomResponse(classroom)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        requireClassroom(id);
        classroomService.deleteClassroom(id);
        return ResponseEntity.ok(ApiResponse.ok("Đã xóa lớp học", null));
    }

    private Classroom requireClassroom(Long id) {
        Classroom classroom = classroomService.getClassroomById(id);
        if (classroom == null) {
            throw new ResourceNotFoundException("Không tìm thấy lớp học id=" + id);
        }
        return classroom;
    }

    private Teacher resolveTeacher(Long teacherId) {
        if (teacherId == null) {
            return null;
        }
        Teacher teacher = teacherService.getTeacherById(teacherId);
        if (teacher == null) {
            throw new ResourceNotFoundException("Không tìm thấy giảng viên id=" + teacherId);
        }
        return teacher;
    }
}
