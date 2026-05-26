package com.ttjavaee.classroom.controller;

import com.ttjavaee.classroom.entity.Classroom;
import com.ttjavaee.classroom.entity.Student;
import com.ttjavaee.classroom.service.ClassroomService;
import com.ttjavaee.classroom.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassroomService classroomService;

    // Hiển thị danh sách sinh viên, có thể lọc theo lớp
    @GetMapping
    public String listStudents(
            @RequestParam(value = "classId", required = false) Long classId,
            Model model) {
        if (classId != null) {
            model.addAttribute("students", studentService.getStudentsByClassroom(classId));
            model.addAttribute("selectedClassId", classId);
        } else {
            model.addAttribute("students", studentService.getAllStudents());
        }
        model.addAttribute("classes", classroomService.getAllClassrooms());
        return "student-list";
    }

    // Hiển thị form thêm sinh viên
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("classes", classroomService.getAllClassrooms());
        return "student-form";
    }

    // Lưu sinh viên (thêm mới hoặc cập nhật)
    // classroomId được gửi riêng từ form select, có thể null nếu chọn "Không có lớp"
    @PostMapping("/save")
    public String saveStudent(
            @ModelAttribute("student") Student student,
            @RequestParam(value = "classroomId", required = false) Long classroomId) {
        // Nếu classroomId null (chọn "Không có lớp") thì set classroom = null
        if (classroomId != null) {
            Classroom classroom = classroomService.getClassroomById(classroomId);
            student.setClassroom(classroom);
        } else {
            student.setClassroom(null);
        }
        studentService.saveStudent(student);
        return "redirect:/students";
    }

    // Hiển thị form sửa sinh viên
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        model.addAttribute("classes", classroomService.getAllClassrooms());
        return "student-form";
    }

    // Xóa sinh viên
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }
}
