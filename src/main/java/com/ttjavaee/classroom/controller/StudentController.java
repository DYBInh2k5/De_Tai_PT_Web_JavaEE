package com.ttjavaee.classroom.controller;

import com.ttjavaee.classroom.entity.Classroom;
import com.ttjavaee.classroom.entity.Student;
import com.ttjavaee.classroom.service.ClassroomService;
import com.ttjavaee.classroom.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassroomService classroomService;

    @GetMapping
    public String listStudents(
            @RequestParam(value = "classId", required = false) Long classId,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("studentCode").ascending());
        model.addAttribute("studentPage", studentService.searchStudents(classId, keyword, pageable));
        model.addAttribute("classes", classroomService.getAllClassrooms());
        model.addAttribute("selectedClassId", classId);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        model.addAttribute("pageSize", size);
        return "student-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("classes", classroomService.getAllClassrooms());
        return "student-form";
    }

    @PostMapping("/save")
    public String saveStudent(
            @Valid @ModelAttribute("student") Student student,
            BindingResult bindingResult,
            @RequestParam(value = "classroomId", required = false) Long classroomId,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (classroomId != null) {
            Classroom classroom = classroomService.getClassroomById(classroomId);
            if (classroom == null) {
                bindingResult.reject("classroom.notFound", "Lớp học được chọn không tồn tại.");
            } else {
                student.setClassroom(classroom);
            }
        } else {
            student.setClassroom(null);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("classes", classroomService.getAllClassrooms());
            return "student-form";
        }

        boolean isNew = student.getId() == null;
        studentService.saveStudent(student);
        redirectAttributes.addFlashAttribute("successMessage",
                isNew ? "Đã thêm sinh viên thành công." : "Đã cập nhật sinh viên thành công.");
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sinh viên.");
            return "redirect:/students";
        }
        model.addAttribute("student", student);
        model.addAttribute("classes", classroomService.getAllClassrooms());
        return "student-form";
    }

    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (studentService.getStudentById(id) == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sinh viên.");
            return "redirect:/students";
        }
        studentService.deleteStudent(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa sinh viên thành công.");
        return "redirect:/students";
    }
}
