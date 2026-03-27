package com.geez14.sysmon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Network port information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortInfo {
    private int port;
    private String protocol;
    private String state;
    private String localAddress;
    private String rawLocalAddress;
    private String processName;
    private String processCommand;
    private int pid;
    private String ipAddress;
}
