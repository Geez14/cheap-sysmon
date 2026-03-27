package com.geez14.sysmon.service;

import com.geez14.sysmon.model.ResourceInfo;
import com.geez14.sysmon.model.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.CentralProcessor;
import oshi.software.os.OperatingSystem;
import org.springframework.stereotype.Service;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.time.ZoneId;

/**
 * Service for system monitoring operations
 */
@Service
public class SystemMonitorService {

        private final oshi.SystemInfo oshiSystemInfo = new oshi.SystemInfo();
        private long[] previousSystemTicks = null;
        private long[][] previousProcessorTicks = null;

    /**
     * Get comprehensive system information
     */
    public SystemInfo getSystemInfo() {
        HardwareAbstractionLayer hardware = oshiSystemInfo.getHardware();
        OperatingSystem os = oshiSystemInfo.getOperatingSystem();
        GlobalMemory memory = hardware.getMemory();
                CentralProcessor processor = hardware.getProcessor();

                if (previousSystemTicks == null) {
                        previousSystemTicks = processor.getSystemCpuLoadTicks();
                }
                if (previousProcessorTicks == null) {
                        previousProcessorTicks = processor.getProcessorCpuLoadTicks();
                }

                double systemCpuLoad = processor.getSystemCpuLoadBetweenTicks(previousSystemTicks) * 100.0;
                double[] processorLoads = processor.getProcessorCpuLoadBetweenTicks(previousProcessorTicks);
                double averageProcessCpuLoad = 0.0;
                if (processorLoads.length > 0) {
                        double sum = 0.0;
                        for (double load : processorLoads) {
                                sum += load;
                        }
                        averageProcessCpuLoad = (sum / processorLoads.length) * 100.0;
                }

                previousSystemTicks = processor.getSystemCpuLoadTicks();
                previousProcessorTicks = processor.getProcessorCpuLoadTicks();

                double[] systemLoadAverages = processor.getSystemLoadAverage(3);

        return SystemInfo.builder()
                .computerName(os.getNetworkParams().getHostName())
                .osName(os.getFamily())
                .osVersion(os.getVersionInfo().toString())
                .osArch(System.getProperty("os.arch"))
                        .javaVersion(System.getProperty("java.version"))
                        .javaVendor(System.getProperty("java.vendor"))
                        .jvmName(System.getProperty("java.vm.name"))
                        .processId(ManagementFactory.getRuntimeMXBean().getPid())
                        .activeThreadCount(Thread.activeCount())
                        .userName(System.getProperty("user.name"))
                        .userHome(System.getProperty("user.home"))
                        .javaHome(System.getProperty("java.home"))
                        .timezone(ZoneId.systemDefault().toString())
                                .processorCount(processor.getLogicalProcessorCount())
                .totalMemoryBytes(memory.getTotal())
                .availableMemoryBytes(memory.getAvailable())
                .usedMemoryBytes(memory.getTotal() - memory.getAvailable())
                .memoryUsagePercent((double) (memory.getTotal() - memory.getAvailable()) / memory.getTotal() * 100.0)
                                .systemCpuLoad(systemCpuLoad)
                                .processCpuLoad(averageProcessCpuLoad)
                        .bootTimeEpochSeconds(os.getSystemBootTime())
                        .systemLoadAverages(systemLoadAverages)
                .uptime(os.getSystemUptime())
                .build();
    }

    /**
     * Get detailed resource utilization
     */
    public ResourceInfo getResourceInfo() {
        HardwareAbstractionLayer hardware = oshiSystemInfo.getHardware();
        OperatingSystem os = oshiSystemInfo.getOperatingSystem();
        GlobalMemory memory = hardware.getMemory();

        // Memory Info
        ResourceInfo.MemoryInfo memoryInfo = ResourceInfo.MemoryInfo.builder()
                .totalMemoryBytes(memory.getTotal())
                .usedMemoryBytes(memory.getTotal() - memory.getAvailable())
                .availableMemoryBytes(memory.getAvailable())
                .totalMemoryMB(memory.getTotal() / (1024 * 1024))
                .usedMemoryMB((memory.getTotal() - memory.getAvailable()) / (1024 * 1024))
                .availableMemoryMB(memory.getAvailable() / (1024 * 1024))
                .usagePercent((double) (memory.getTotal() - memory.getAvailable()) / memory.getTotal() * 100.0)
                .build();

        // CPU Info
                CentralProcessor processor = hardware.getProcessor();
                if (previousSystemTicks == null) {
                        previousSystemTicks = processor.getSystemCpuLoadTicks();
                }
                double loadAverage = 0.0;
                double[] loadAverages = processor.getSystemLoadAverage(3);
                if (loadAverages.length > 0 && loadAverages[0] >= 0) {
                        loadAverage = loadAverages[0];
                }

                double cpuUsagePercent = processor.getSystemCpuLoadBetweenTicks(previousSystemTicks) * 100.0;
                previousSystemTicks = processor.getSystemCpuLoadTicks();

        ResourceInfo.CpuInfo cpuInfo = ResourceInfo.CpuInfo.builder()
                .processorCount(processor.getLogicalProcessorCount())
                .physicalProcessorCount(processor.getPhysicalProcessorCount())
                                .systemLoadAverage(loadAverage)
                .systemLoadAverage1m(loadAverages.length > 0 ? loadAverages[0] : -1.0)
                .systemLoadAverage5m(loadAverages.length > 1 ? loadAverages[1] : -1.0)
                .systemLoadAverage15m(loadAverages.length > 2 ? loadAverages[2] : -1.0)
                                .cpuUsagePercent(cpuUsagePercent)
                .contextSwitches(processor.getContextSwitches())
                .interrupts(processor.getInterrupts())
                .uptime(os.getSystemUptime())
                .build();

        // Disk Info
        File root = new File("/");
        ResourceInfo.DiskInfo diskInfo = ResourceInfo.DiskInfo.builder()
                .rootPath(root.getAbsolutePath())
                .totalSpaceBytes(root.getTotalSpace())
                .usableSpaceBytes(root.getUsableSpace())
                .usedSpaceBytes(root.getTotalSpace() - root.getUsableSpace())
                .totalSpaceGB(root.getTotalSpace() / (1024L * 1024L * 1024L))
                .usableSpaceGB(root.getUsableSpace() / (1024L * 1024L * 1024L))
                .usedSpaceGB((root.getTotalSpace() - root.getUsableSpace()) / (1024L * 1024L * 1024L))
                .usagePercent((double) (root.getTotalSpace() - root.getUsableSpace()) / root.getTotalSpace() * 100.0)
                .build();

        return ResourceInfo.builder()
                .memory(memoryInfo)
                .cpu(cpuInfo)
                .disk(diskInfo)
                .build();
    }

    /**
     * Get CPU load average
     */
    public double getCpuLoadAverage() {
        HardwareAbstractionLayer hardware = oshiSystemInfo.getHardware();
                CentralProcessor processor = hardware.getProcessor();
                if (previousSystemTicks == null) {
                        previousSystemTicks = processor.getSystemCpuLoadTicks();
                }
                double cpuLoad = processor.getSystemCpuLoadBetweenTicks(previousSystemTicks) * 100.0;
                previousSystemTicks = processor.getSystemCpuLoadTicks();
                return cpuLoad;
    }
}
