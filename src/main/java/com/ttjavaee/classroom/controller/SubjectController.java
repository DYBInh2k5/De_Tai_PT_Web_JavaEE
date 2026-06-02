package com.ttjavaee.classroom.controller;

import com.ttjavaee.classroom.entity.Subject;
import com.ttjavaee.classroom.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public String listSubjects(Model model) {
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "subject-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        Subject subject = new Subject();
        subject.setCredits(3);
        model.addAttribute("subject", subject);
        return "subject-form";
    }

    @PostMapping("/save")
    public String saveSubject(@Valid @ModelAttribute("subject") Subject subject,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "subject-form";
        }
        boolean isNew = subject.getId() == null;
        subjectService.saveSubject(subject);
        redirectAttributes.addFlashAttribute("successMessage",
                isNew ? "Đã thêm môn học thành công." : "Đã cập nhật môn học thành công.");
        return "redirect:/subjects";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Subject subject = subjectService.getSubjectById(id);
        if (subject == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy môn học.");
            return "redirect:/subjects";
        }
        model.addAttribute("subject", subject);
        return "subject-form";
    }

    @PostMapping("/delete/{id}")
    public String deleteSubject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (subjectService.getSubjectById(id) == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy môn học.");
            return "redirect:/subjects";
        }
        subjectService.deleteSubject(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa môn học thành công.");
        return "redirect:/subjects";
    }
}
