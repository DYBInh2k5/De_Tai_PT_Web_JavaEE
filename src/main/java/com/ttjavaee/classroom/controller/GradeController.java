package com.ttjavaee.classroom.controller;

import com.ttjavaee.classroom.entity.Grade;
import com.ttjavaee.classroom.entity.Student;
import com.ttjavaee.classroom.entity.Subject;
import com.ttjavaee.classroom.service.GradeService;
import com.ttjavaee.classroom.service.StudentService;
import com.ttjavaee.classroom.service.SubjectService;
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
@RequestMapping("/grades")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public String listGrades(
            @RequestParam(value = "studentId", required = false) Long studentId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        model.addAttribute("gradePage", gradeService.getGradesByStudent(studentId, pageable));
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("selectedStudentId", studentId);
        model.addAttribute("pageSize", size);
        model.addAttribute("averageScore", gradeService.getAverageScore());
        return "grade-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("grade", new Grade());
        populateFormOptions(model);
        return "grade-form";
    }

    @PostMapping("/save")
    public String saveGrade(
            @Valid @ModelAttribute("grade") Grade grade,
            BindingResult bindingResult,
            @RequestParam("studentId") Long studentId,
            @RequestParam("subjectId") Long subjectId,
            Model model,
            RedirectAttributes redirectAttributes) {
        Student student = studentService.getStudentById(studentId);
        Subject subject = subjectService.getSubjectById(subjectId);
        if (student == null) {
            bindingResult.reject("student.notFound", "Sinh viên không tồn tại.");
        }
        if (subject == null) {
            bindingResult.reject("subject.notFound", "Môn học không tồn tại.");
        }
        if (bindingResult.hasErrors()) {
            populateFormOptions(model);
            return "grade-form";
        }
        grade.setStudent(student);
        grade.setSubject(subject);
        boolean isNew = grade.getId() == null;
        gradeService.saveGrade(grade);
        redirectAttributes.addFlashAttribute("successMessage",
                isNew ? "Đã thêm điểm thành công." : "Đã cập nhật điểm thành công.");
        return "redirect:/grades";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Grade grade = gradeService.getGradeById(id);
        if (grade == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy bản ghi điểm.");
            return "redirect:/grades";
        }
        model.addAttribute("grade", grade);
        populateFormOptions(model);
        return "grade-form";
    }

    @PostMapping("/delete/{id}")
    public String deleteGrade(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (gradeService.getGradeById(id) == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy bản ghi điểm.");
            return "redirect:/grades";
        }
        gradeService.deleteGrade(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa điểm thành công.");
        return "redirect:/grades";
    }

    private void populateFormOptions(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("subjects", subjectService.getAllSubjects());
    }
}
