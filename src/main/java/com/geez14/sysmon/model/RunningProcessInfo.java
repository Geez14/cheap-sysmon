package com.geez14.sysmon.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Running process information model
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RunningProcessInfo {
    private int pid;
    private int parentPid;
    private String name;
    private String state;
    private String user;
    private String commandLine;
    private String executablePath;
    private long threadCount;
    private long uptimeSeconds;
    private long startTimeEpochMs;
    private double cpuLoadPercent;
    private long residentMemoryBytes;
    private long virtualMemoryBytes;
}
