package com.geez14.sysmon.controller;

import com.geez14.sysmon.model.ApiResponse;
import com.geez14.sysmon.model.NetworkInfo;
import com.geez14.sysmon.service.NetworkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API Controller for Network Information
 * Base path: /api/network
 */
@RestController
@RequestMapping("/network")
public class NetworkController {

    private final NetworkService networkService;

    public NetworkController(NetworkService networkService) {
        this.networkService = networkService;
    }

    /**
     * Get all network interfaces and information
     * GET /api/network/info
     */
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<NetworkInfo>> getNetworkInfo() {
        try {
            NetworkInfo networkInfo = networkService.getNetworkInfo();
            return ResponseEntity.ok(
                    ApiResponse.success("Network information retrieved successfully", networkInfo)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("NETWORK_ERROR", e.getMessage()));
        }
    }

    /**
     * Get network interfaces only
     * GET /api/network/interfaces
     */
    @GetMapping("/interfaces")
    public ResponseEntity<ApiResponse<NetworkInfo>> getNetworkInterfaces() {
        try {
            NetworkInfo networkInfo = networkService.getNetworkInfo();
            return ResponseEntity.ok(
                    ApiResponse.success("Network interfaces retrieved successfully", networkInfo)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("NETWORK_ERROR", e.getMessage()));
        }
    }

    /**
     * Get network devices (alias for network info)
     * GET /api/network/devices
     */
    @GetMapping("/devices")
    public ResponseEntity<ApiResponse<NetworkInfo>> getNetworkDevices() {
        try {
            NetworkInfo networkInfo = networkService.getNetworkInfo();
            return ResponseEntity.ok(
                    ApiResponse.success("Network devices retrieved successfully", networkInfo)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("NETWORK_ERROR", e.getMessage()));
        }
    }
}
