package com.geez14.sysmon.controller;

import com.geez14.sysmon.model.ApiResponse;
import com.geez14.sysmon.model.PortInfo;
import com.geez14.sysmon.model.ProcessKillResponse;
import com.geez14.sysmon.model.RunningProcessInfo;
import com.geez14.sysmon.service.ProcessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST API Controller for Process Management
 * Base path: /api/process
 */
@RestController
@RequestMapping("/process")
public class ProcessController {

    private final ProcessService processService;

    public ProcessController(ProcessService processService) {
        this.processService = processService;
    }

    /**
     * Get all open ports
     * GET /api/process/ports
     */
    @GetMapping("/ports")
    public ResponseEntity<ApiResponse<List<PortInfo>>> getOpenPorts() {
        try {
            List<PortInfo> ports = processService.getOpenPorts();
            return ResponseEntity.ok(
                    ApiResponse.success("Open ports retrieved successfully", ports)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("PORT_ERROR", e.getMessage()));
        }
    }

    /**
     * Get running processes
     * GET /api/process/running?limit=100&sortBy=cpu
     */
    @GetMapping("/running")
    public ResponseEntity<ApiResponse<List<RunningProcessInfo>>> getRunningProcesses(
            @RequestParam(defaultValue = "100") int limit,
            @RequestParam(defaultValue = "cpu") String sortBy) {
        try {
            List<RunningProcessInfo> processes = processService.getRunningProcesses(limit, sortBy);
            return ResponseEntity.ok(
                    ApiResponse.success("Running processes retrieved successfully", processes)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("RUNNING_PROCESS_ERROR", e.getMessage()));
        }
    }

    /**
     * Kill a process by PID
     * POST /api/process/kill/{pid}
     *
     * @param pid Process ID to kill
     * @return ProcessKillResponse indicating success or failure
     */
    @PostMapping("/kill/{pid}")
    public ResponseEntity<ApiResponse<ProcessKillResponse>> killProcess(@PathVariable int pid) {
        try {
            // Extra safety check - prevent killing critical system processes
            if (pid <= 0 || pid == 1 || pid == System.identityHashCode(Thread.currentThread())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("INVALID_PID", "Invalid PID or protection against critical processes"));
            }

            boolean killed = processService.killProcessByPid(pid);
            String processName = processService.getProcessNameByPid(pid);

            ProcessKillResponse response = ProcessKillResponse.builder()
                    .pid(pid)
                    .processName(processName)
                    .killed(killed)
                    .message(killed ? "Process terminated successfully" : "Failed to terminate process")
                    .requestedBy(System.getProperty("user.name"))
                    .requestedAt(LocalDateTime.now())
                    .error(killed ? null : "Process not found, already terminated, or permission denied")
                    .build();

            return ResponseEntity.ok(
                    ApiResponse.success("Process kill operation completed", response)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("PROCESS_ERROR", e.getMessage()));
        }
    }

    /**
     * Get open ports (alternative endpoint)
     * GET /api/process/open-ports
     */
    @GetMapping("/open-ports")
    public ResponseEntity<ApiResponse<List<PortInfo>>> getOpenPortsAlternative() {
        return getOpenPorts();
    }
}
