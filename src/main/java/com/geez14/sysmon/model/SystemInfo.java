package com.geez14.sysmon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * System information model
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemInfo {
    private String computerName;
    private String osName;
    private String osVersion;
    private String osArch;
    private String javaVersion;
    private String javaVendor;
    private String jvmName;
    private long processId;
    private int activeThreadCount;
    private String userName;
    private String userHome;
    private String javaHome;
    private String timezone;
    private int processorCount;
    private long totalMemoryBytes;
    private long availableMemoryBytes;
    private long usedMemoryBytes;
    private double memoryUsagePercent;
    private double systemCpuLoad;
    private double processCpuLoad;
    private long bootTimeEpochSeconds;
    private double[] systemLoadAverages;
    private long uptime;
}
