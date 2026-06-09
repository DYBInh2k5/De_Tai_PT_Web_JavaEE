package com.ttjavaee.classroom.controller;

import com.ttjavaee.classroom.entity.Classroom;
import com.ttjavaee.classroom.entity.Student;
import com.ttjavaee.classroom.service.ClassroomService;
import com.ttjavaee.classroom.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// ============================================================
// StudentController: TIEP NHAN VA XU LY CAC YEU CAU VE SINH VIEN
// Tat ca URL deu bat dau bang /students
// ============================================================
@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassroomService classroomService;

    // ============================================================
    // GET /students (hoac /students?classId=1)
    // Chuc nang: HIEN THI DANH SACH SINH VIEN (co the loc theo lop)
    //
    // @RequestParam(value = "classId", required = false):
    //   - Lay tham so ?classId=... tu URL
    //   - Neu khong co -> required=false -> classId = null
    //
    // Neu co classId:
    //   - Chi lay sinh vien thuoc lop do
    //   - Luu selectedClassId de HTML hien thi "dang chon"
    // Neu khong co classId:
    //   - Lay TAT CA sinh vien
    //
    // Luon gui danh sach lop xuong HTML de ve dropdown loc
    // ============================================================
    @GetMapping
    public String listStudents(
            @RequestParam(value = "classId", required = false) Long classId,
            Model model) {
        if (classId != null) {
            // Dang loc theo lop
            model.addAttribute("students", studentService.getStudentsByClassroom(classId));
            model.addAttribute("selectedClassId", classId);
        } else {
            // Khong loc - lay tat ca
            model.addAttribute("students", studentService.getAllStudents());
        }
        // Gui danh sach lop xuong HTML cho dropdown filter
        model.addAttribute("classes", classroomService.getAllClassrooms());
        return "student-list";
    }

    // ============================================================
    // GET /students/add
    // Chuc nang: HIEN THI FORM THEM SINH VIEN
    // - Tao Student rong de binding
    // - Gui danh sach lop de ve dropdown chon lop
    // ============================================================
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("classes", classroomService.getAllClassrooms());
        return "student-form";
    }

    // ============================================================
    // POST /students/save
    // Chuc nang: LUU SINH VIEN (THEM moi hoac CAP NHAT)
    //
    // @ModelAttribute("student"): lay du lieu tu form sinh vien
    // @RequestParam("classroomId"): lay rieng id lop tu dropdown
    //
    // VI SAO classroomId GUI RIENG?
    //   - Student entity chua truc tiep Classroom object
    //   - Tu form HTML, select chi gui duoc mot so (classroomId)
    //   - Controller nhan so do, goi Service de tim Classroom
    //   - Set classroom vao Student -> JPA tu dong luu FK
    //
    // Neu classroomId = null: sinh vien khong thuoc lop nao
    // ============================================================
    @PostMapping("/save")
    public String saveStudent(
            @ModelAttribute("student") Student student,
            @RequestParam(value = "classroomId", required = false) Long classroomId) {
        if (classroomId != null) {
            // Tim lop theo id, gan vao sinh vien
            Classroom classroom = classroomService.getClassroomById(classroomId);
            student.setClassroom(classroom);
        } else {
            // Khong chon lop -> set null (FK = NULL)
            student.setClassroom(null);
        }
        studentService.saveStudent(student);
        return "redirect:/students";
    }

    // ============================================================
    // GET /students/edit/{id}
    // Chuc nang: HIEN THI FORM SUA SINH VIEN
    // ============================================================
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        model.addAttribute("classes", classroomService.getAllClassrooms());
        return "student-form";
    }

    // ============================================================
    // POST /students/delete/{id}
    // Chuc nang: XOA SINH VIEN
    // ============================================================
    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }
}
