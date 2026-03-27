package com.geez14.sysmon.controller;

import com.geez14.sysmon.model.ApiResponse;
import com.geez14.sysmon.model.ResourceInfo;
import com.geez14.sysmon.model.SystemInfo;
import com.geez14.sysmon.service.SystemMonitorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API Controller for System Monitoring
 * Base path: /api/system
 */
@RestController
@RequestMapping("/system")
public class SystemController {

    private final SystemMonitorService systemMonitorService;

    public SystemController(SystemMonitorService systemMonitorService) {
        this.systemMonitorService = systemMonitorService;
    }

    /**
     * Get comprehensive system information
     * GET /api/system/info
     */
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<SystemInfo>> getSystemInfo() {
        try {
            SystemInfo systemInfo = systemMonitorService.getSystemInfo();
            return ResponseEntity.ok(
                    ApiResponse.success("System information retrieved successfully", systemInfo)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("SYSTEM_ERROR", e.getMessage()));
        }
    }

    /**
     * Get detailed resource utilization
     * GET /api/system/resources
     */
    @GetMapping("/resources")
    public ResponseEntity<ApiResponse<ResourceInfo>> getResources() {
        try {
            ResourceInfo resourceInfo = systemMonitorService.getResourceInfo();
            return ResponseEntity.ok(
                    ApiResponse.success("Resource information retrieved successfully", resourceInfo)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("RESOURCE_ERROR", e.getMessage()));
        }
    }

    /**
     * Get CPU load
     * GET /api/system/cpu-load
     */
    @GetMapping("/cpu-load")
    public ResponseEntity<ApiResponse<Double>> getCpuLoad() {
        try {
            double cpuLoad = systemMonitorService.getCpuLoadAverage();
            return ResponseEntity.ok(
                    ApiResponse.success("CPU load retrieved successfully", cpuLoad)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("CPU_ERROR", e.getMessage()));
        }
    }

    /**
     * Health check endpoint
     * GET /api/system/health
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(
                ApiResponse.success("System Monitor API is running", "OK")
        );
    }
}
