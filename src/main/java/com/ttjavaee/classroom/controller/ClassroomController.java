package com.ttjavaee.classroom.controller;

import com.ttjavaee.classroom.entity.Classroom;
import com.ttjavaee.classroom.entity.Teacher;
import com.ttjavaee.classroom.service.ClassroomService;
import com.ttjavaee.classroom.service.StudentService;
import com.ttjavaee.classroom.service.TeacherService;
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
@RequestMapping("/classes")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public String listClasses(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("classCode").ascending());
        model.addAttribute("classPage", classroomService.getClassroomSummaries(pageable));
        model.addAttribute("pageSize", size);
        return "class-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("classroom", new Classroom());
        addTeachers(model);
        return "class-form";
    }

    @PostMapping("/save")
    public String saveClass(@Valid @ModelAttribute("classroom") Classroom classroom,
                            BindingResult bindingResult,
                            @RequestParam(value = "teacherId", required = false) Long teacherId,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        assignTeacher(classroom, teacherId, bindingResult);
        if (bindingResult.hasErrors()) {
            addTeachers(model);
            return "class-form";
        }
        boolean isNew = classroom.getId() == null;
        classroomService.saveClassroom(classroom);
        redirectAttributes.addFlashAttribute("successMessage",
                isNew ? "Đã thêm lớp học thành công." : "Đã cập nhật lớp học thành công.");
        return "redirect:/classes";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Classroom classroom = classroomService.getClassroomById(id);
        if (classroom == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy lớp học.");
            return "redirect:/classes";
        }
        model.addAttribute("classroom", classroom);
        addTeachers(model);
        return "class-form";
    }

    @PostMapping("/delete/{id}")
    public String deleteClass(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (classroomService.getClassroomById(id) == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy lớp học.");
            return "redirect:/classes";
        }
        classroomService.deleteClassroom(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "Đã xóa lớp học. Sinh viên trong lớp được gỡ gán (class_id = NULL).");
        return "redirect:/classes";
    }

    @GetMapping("/{id}/students")
    public String viewStudents(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Classroom classroom = classroomService.getClassroomById(id);
        if (classroom == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy lớp học.");
            return "redirect:/classes";
        }
        model.addAttribute("classroom", classroom);
        model.addAttribute("students", studentService.getStudentsByClassroom(id));
        return "class-students";
    }

    private void addTeachers(Model model) {
        model.addAttribute("teachers", teacherService.getAllTeachers());
    }

    private void assignTeacher(Classroom classroom, Long teacherId, BindingResult bindingResult) {
        if (teacherId != null) {
            Teacher teacher = teacherService.getTeacherById(teacherId);
            if (teacher == null) {
                bindingResult.reject("teacher.notFound", "Giảng viên được chọn không tồn tại.");
            } else {
                classroom.setTeacher(teacher);
            }
        } else {
            classroom.setTeacher(null);
        }
    }
}
