package com.ttjavaee.classroom.api;

import com.ttjavaee.classroom.api.dto.*;
import com.ttjavaee.classroom.api.mapper.ApiMapper;
import com.ttjavaee.classroom.entity.Teacher;
import com.ttjavaee.classroom.exception.ResourceNotFoundException;
import com.ttjavaee.classroom.service.TeacherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teachers")
@Tag(name = "Giảng viên", description = "CRUD giảng viên")
public class TeacherApiController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TeacherResponse>>> list() {
        List<TeacherResponse> list = teacherService.getAllTeachers().stream()
                .map(ApiMapper::toTeacherResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponse>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(ApiMapper.toTeacherResponse(requireTeacher(id))));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TeacherResponse>> create(@Valid @RequestBody TeacherRequest request) {
        Teacher teacher = ApiMapper.apply(request, new Teacher());
        teacher = teacherService.saveTeacher(teacher);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Đã tạo giảng viên", ApiMapper.toTeacherResponse(teacher)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody TeacherRequest request) {
        Teacher teacher = requireTeacher(id);
        ApiMapper.apply(request, teacher);
        teacher = teacherService.saveTeacher(teacher);
        return ResponseEntity.ok(ApiResponse.ok("Đã cập nhật giảng viên", ApiMapper.toTeacherResponse(teacher)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        requireTeacher(id);
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok(ApiResponse.ok("Đã xóa giảng viên", null));
    }

    private Teacher requireTeacher(Long id) {
        Teacher teacher = teacherService.getTeacherById(id);
        if (teacher == null) {
            throw new ResourceNotFoundException("Không tìm thấy giảng viên id=" + id);
        }
        return teacher;
    }
}
