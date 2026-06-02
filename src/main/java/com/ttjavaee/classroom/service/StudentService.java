package com.ttjavaee.classroom.service;

import com.ttjavaee.classroom.entity.Student;
import com.ttjavaee.classroom.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Page<Student> searchStudents(Long classroomId, String keyword, Pageable pageable) {
        String normalizedKeyword = StringUtils.hasText(keyword) ? keyword.trim() : null;
        return studentRepository.searchStudents(classroomId, normalizedKeyword, pageable);
    }

    public List<Student> getStudentsByClassroom(Long classroomId) {
        return studentRepository.findByClassroomId(classroomId);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
