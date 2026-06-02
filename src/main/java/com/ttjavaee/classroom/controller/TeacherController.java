package com.ttjavaee.classroom.controller;

import com.ttjavaee.classroom.entity.Teacher;
import com.ttjavaee.classroom.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public String listTeachers(Model model) {
        model.addAttribute("teachers", teacherService.getAllTeachers());
        return "teacher-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        return "teacher-form";
    }

    @PostMapping("/save")
    public String saveTeacher(@Valid @ModelAttribute("teacher") Teacher teacher,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "teacher-form";
        }
        boolean isNew = teacher.getId() == null;
        teacherService.saveTeacher(teacher);
        redirectAttributes.addFlashAttribute("successMessage",
                isNew ? "Đã thêm giảng viên thành công." : "Đã cập nhật giảng viên thành công.");
        return "redirect:/teachers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Teacher teacher = teacherService.getTeacherById(id);
        if (teacher == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy giảng viên.");
            return "redirect:/teachers";
        }
        model.addAttribute("teacher", teacher);
        return "teacher-form";
    }

    @PostMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (teacherService.getTeacherById(id) == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy giảng viên.");
            return "redirect:/teachers";
        }
        teacherService.deleteTeacher(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa giảng viên thành công.");
        return "redirect:/teachers";
    }
}
