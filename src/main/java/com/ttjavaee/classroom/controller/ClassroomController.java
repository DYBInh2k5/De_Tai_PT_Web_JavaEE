package com.ttjavaee.classroom.controller;

import com.ttjavaee.classroom.entity.Classroom;
import com.ttjavaee.classroom.service.ClassroomService;
import com.ttjavaee.classroom.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/classes")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private StudentService studentService;

    @GetMapping
    public String listClasses(Model model) {
        model.addAttribute("classes", classroomService.getAllClassrooms());
        return "class-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("classroom", new Classroom());
        return "class-form";
    }

    @PostMapping("/save")
    public String saveClass(@ModelAttribute("classroom") Classroom classroom) {
        classroomService.saveClassroom(classroom);
        return "redirect:/classes";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Classroom classroom = classroomService.getClassroomById(id);
        model.addAttribute("classroom", classroom);
        return "class-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteClass(@PathVariable Long id) {
        classroomService.deleteClassroom(id);
        return "redirect:/classes";
    }

    @GetMapping("/{id}/students")
    public String viewStudents(@PathVariable Long id, Model model) {
        Classroom classroom = classroomService.getClassroomById(id);
        model.addAttribute("classroom", classroom);
        model.addAttribute("students", studentService.getStudentsByClassroom(id));
        return "class-students";
    }
}
