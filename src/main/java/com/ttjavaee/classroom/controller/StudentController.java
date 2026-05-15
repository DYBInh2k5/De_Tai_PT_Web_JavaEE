package com.ttjavaee.classroom.controller;

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

    @GetMapping
    public String listStudents(@RequestParam(value = "classId", required = false) Long classId, Model model) {
        if (classId != null) {
            model.addAttribute("students", studentService.getStudentsByClassroom(classId));
            model.addAttribute("selectedClassId", classId);
        } else {
            model.addAttribute("students", studentService.getAllStudents());
        }
        model.addAttribute("classes", classroomService.getAllClassrooms());
        return "student-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("classes", classroomService.getAllClassrooms());
        return "student-form";
    }

    @PostMapping("/save")
    public String saveStudent(@ModelAttribute("student") Student student) {
        studentService.saveStudent(student);
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        model.addAttribute("classes", classroomService.getAllClassrooms());
        return "student-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }
}
