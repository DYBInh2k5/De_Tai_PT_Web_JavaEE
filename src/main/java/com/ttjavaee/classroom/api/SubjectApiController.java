package com.ttjavaee.classroom.api;

import com.ttjavaee.classroom.api.dto.*;
import com.ttjavaee.classroom.api.mapper.ApiMapper;
import com.ttjavaee.classroom.entity.Subject;
import com.ttjavaee.classroom.exception.ResourceNotFoundException;
import com.ttjavaee.classroom.service.SubjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subjects")
@Tag(name = "Môn học", description = "CRUD môn học")
public class SubjectApiController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> list() {
        List<SubjectResponse> list = subjectService.getAllSubjects().stream()
                .map(ApiMapper::toSubjectResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(ApiMapper.toSubjectResponse(requireSubject(id))));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SubjectResponse>> create(@Valid @RequestBody SubjectRequest request) {
        Subject subject = ApiMapper.apply(request, new Subject());
        subject = subjectService.saveSubject(subject);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Đã tạo môn học", ApiMapper.toSubjectResponse(subject)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody SubjectRequest request) {
        Subject subject = requireSubject(id);
        ApiMapper.apply(request, subject);
        subject = subjectService.saveSubject(subject);
        return ResponseEntity.ok(ApiResponse.ok("Đã cập nhật môn học", ApiMapper.toSubjectResponse(subject)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        requireSubject(id);
        subjectService.deleteSubject(id);
        return ResponseEntity.ok(ApiResponse.ok("Đã xóa môn học", null));
    }

    private Subject requireSubject(Long id) {
        Subject subject = subjectService.getSubjectById(id);
        if (subject == null) {
            throw new ResourceNotFoundException("Không tìm thấy môn học id=" + id);
        }
        return subject;
    }
}
