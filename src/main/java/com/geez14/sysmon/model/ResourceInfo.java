package com.geez14.sysmon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Detailed resource utilization information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceInfo {
    private MemoryInfo memory;
    private CpuInfo cpu;
    private DiskInfo disk;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemoryInfo {
        private long totalMemoryBytes;
        private long usedMemoryBytes;
        private long availableMemoryBytes;
        private long totalMemoryMB;
        private long usedMemoryMB;
        private long availableMemoryMB;
        private double usagePercent;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CpuInfo {
        private int processorCount;
        private int physicalProcessorCount;
        private double systemLoadAverage1m;
        private double systemLoadAverage5m;
        private double systemLoadAverage15m;
        private double systemLoadAverage;
        private double cpuUsagePercent;
        private long contextSwitches;
        private long interrupts;
        private long uptime;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DiskInfo {
        private String rootPath;
        private long totalSpaceBytes;
        private long usableSpaceBytes;
        private long usedSpaceBytes;
        private long totalSpaceGB;
        private long usableSpaceGB;
        private long usedSpaceGB;
        private double usagePercent;
    }
}
