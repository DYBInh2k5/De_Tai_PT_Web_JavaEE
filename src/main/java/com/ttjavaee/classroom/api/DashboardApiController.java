package com.ttjavaee.classroom.api;

import com.ttjavaee.classroom.api.dto.ApiResponse;
import com.ttjavaee.classroom.dto.DashboardStatsDto;
import com.ttjavaee.classroom.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@Tag(name = "Dashboard", description = "Thống kê tổng quan")
public class DashboardApiController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> stats() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getDashboardStats()));
    }
}
