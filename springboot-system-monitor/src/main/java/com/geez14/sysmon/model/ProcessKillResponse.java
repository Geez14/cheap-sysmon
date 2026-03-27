package com.geez14.sysmon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

/**
 * Response for process kill operation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessKillResponse {
    private int pid;
    private String processName;
    private boolean killed;
    private String message;
    private String requestedBy;
    private LocalDateTime requestedAt;
    private String error;
}
