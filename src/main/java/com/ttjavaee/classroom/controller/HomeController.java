package com.ttjavaee.classroom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// ============================================================
// HomeController: Dieu huong trang chu
// Khi nguoi dung vao http://localhost:8080/ se tu dong
// chuyen huong (redirect) den /classes (danh sach lop hoc)
// ============================================================
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // "redirect:/classes": bao trinh duyet nhay den URL /classes
        return "redirect:/classes";
    }
}
