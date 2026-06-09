package com.ttjavaee.classroom.service;

import com.ttjavaee.classroom.entity.Student;
import com.ttjavaee.classroom.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

// ============================================================
// StudentService: NGHIEP VU cho sinh vien
// ============================================================
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // Lay danh sach TAT CA sinh vien (khong loc)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Lay danh sach sinh vien thuoc ve mot lop cu the
    // Dua vao classroomId -> tim class_id trong bang Students
    public List<Student> getStudentsByClassroom(Long classroomId) {
        return studentRepository.findByClassroomId(classroomId);
    }

    // Tim 1 sinh vien theo ID
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    // Luu sinh vien (THEM moi hoac CAP NHAT)
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // Xoa sinh vien theo ID
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
