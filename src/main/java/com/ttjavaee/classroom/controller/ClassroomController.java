package com.ttjavaee.classroom.controller;

import com.ttjavaee.classroom.entity.Classroom;
import com.ttjavaee.classroom.service.ClassroomService;
import com.ttjavaee.classroom.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// ============================================================
// Controller: TIEP NHAN VA XU LY CAC YEU CAU TU TRINH DUYET
//
// @Controller: danh dau day la 1 Web Controller
// @RequestMapping("/classes"): tat ca URL trong class nay
//   deu bat dau bang /classes
//
// Moi phuong thuc xu ly 1 loai request va tra ve ten template (HTML)
// ============================================================
@Controller
@RequestMapping("/classes")
public class ClassroomController {

    // Spring tu dong inject cac Service vao Controller
    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private StudentService studentService;

    // ============================================================
    // GET /classes
    // Chuc nang: HIEN THI DANH SACH LOP HOC
    // - Goi Service lay tat ca lop
    // - Day vao Model de Thymeleaf render ra HTML
    // - Tra ve template "class-list.html"
    // ============================================================
    @GetMapping
    public String listClasses(Model model) {
        // model.addAttribute("ten_bien", gia_tri):
        //   day du lieu tu Java sang HTML (Thymeleaf)
        //   Trong HTML: ${classes} se lay du lieu nay
        model.addAttribute("classes", classroomService.getAllClassrooms());
        return "class-list";  // ten file .html trong templates/
    }

    // ============================================================
    // GET /classes/add
    // Chuc nang: HIEN THI FORM THEM LOP MOI
    // - Tao doi tuong Classroom rong de binding du lieu tu form
    // - Tra ve template "class-form.html"
    // ============================================================
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("classroom", new Classroom());
        return "class-form";
    }

    // ============================================================
    // POST /classes/save
    // Chuc nang: LUU LOP HOC (TAO MOI hoac CAP NHAT)
    //
    // @ModelAttribute("classroom"):
    //   - Lay du lieu tu form HTML (name="classCode", name="className")
    //   - Tu dong map vao doi tuong Classroom
    //   - Neu co id -> cap nhat; khong co id -> them moi
    //
    // "redirect:/classes": sau khi luu xong, chuyen huong ve danh sach
    // ============================================================
    @PostMapping("/save")
    public String saveClass(@ModelAttribute("classroom") Classroom classroom) {
        classroomService.saveClassroom(classroom);
        return "redirect:/classes";
    }

    // ============================================================
    // GET /classes/edit/{id}
    // Chuc nang: HIEN THI FORM SUA LOP HOC
    //
    // @PathVariable Long id: lay {id} tu URL
    //   VD: /classes/edit/3 -> id = 3
    // - Tim lop theo id, day vao Model de hien thi du lieu cu
    // - Tra ve template "class-form.html" (form tu dong dien san)
    // ============================================================
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("classroom", classroomService.getClassroomById(id));
        return "class-form";
    }

    // ============================================================
    // POST /classes/delete/{id}
    // Chuc nang: XOA LOP HOC
    // - Xoa lop theo id
    // - DB tu dong SET NULL class_id cho sinh vien trong lop
    // - Chuyen huong ve danh sach
    // ============================================================
    @PostMapping("/delete/{id}")
    public String deleteClass(@PathVariable Long id) {
        classroomService.deleteClassroom(id);
        return "redirect:/classes";
    }

    // ============================================================
    // GET /classes/{id}/students
    // Chuc nang: XEM DANH SACH SINH VIEN CUA 1 LOP
    // - Tim lop theo id de lay thong tin lop
    // - Loc sinh vien theo lop (classroomId)
    // - Tra ve template "class-students.html"
    // ============================================================
    @GetMapping("/{id}/students")
    public String viewStudents(@PathVariable Long id, Model model) {
        model.addAttribute("classroom", classroomService.getClassroomById(id));
        model.addAttribute("students", studentService.getStudentsByClassroom(id));
        return "class-students";
    }
}
