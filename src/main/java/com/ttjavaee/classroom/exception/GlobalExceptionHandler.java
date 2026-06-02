package com.ttjavaee.classroom.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(annotations = Controller.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrity(DataIntegrityViolationException ex,
                                      HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) {
        String message = "Dữ liệu trùng lặp (mã lớp hoặc mã sinh viên đã tồn tại).";
        if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("email")) {
            message = "Email đã được sử dụng bởi sinh viên khác.";
        }
        redirectAttributes.addFlashAttribute("errorMessage", message);
        return "redirect:" + resolveRedirectPath(request);
    }

    private String resolveRedirectPath(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer != null) {
            if (referer.contains("/students")) {
                return "/students";
            }
            if (referer.contains("/classes")) {
                return "/classes";
            }
        }
        return "/classes";
    }
}
