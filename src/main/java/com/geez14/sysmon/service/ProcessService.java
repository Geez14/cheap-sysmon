package com.geez14.sysmon.service;

import com.geez14.sysmon.model.PortInfo;
import com.geez14.sysmon.model.RunningProcessInfo;
import org.springframework.stereotype.Service;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for managing processes and ports
 */
@Service
public class ProcessService {

    /**
     * Get running processes
     */
    public List<RunningProcessInfo> getRunningProcesses(int limit, String sortBy) {
        int effectiveLimit = limit <= 0 ? 100 : Math.min(limit, 2000);
        Comparator<OSProcess> sorting = mapSorting(sortBy);

        OperatingSystem os = new oshi.SystemInfo().getOperatingSystem();
        List<OSProcess> processes = os.getProcesses(process -> true, sorting, effectiveLimit);
        List<RunningProcessInfo> result = new ArrayList<>();

        for (OSProcess process : processes) {
            result.add(RunningProcessInfo.builder()
                    .pid(process.getProcessID())
                    .parentPid(process.getParentProcessID())
                    .name(process.getName())
                    .state(process.getState() != null ? process.getState().name() : "UNKNOWN")
                    .user(process.getUser())
                    .commandLine(process.getCommandLine())
                    .executablePath(process.getPath())
                    .threadCount(process.getThreadCount())
                    .uptimeSeconds(process.getUpTime() / 1000)
                    .startTimeEpochMs(process.getStartTime())
                    .cpuLoadPercent(process.getProcessCpuLoadCumulative() * 100.0)
                    .residentMemoryBytes(process.getResidentSetSize())
                    .virtualMemoryBytes(process.getVirtualSize())
                    .build());
        }

        return result;
    }

    private Comparator<OSProcess> mapSorting(String sortBy) {
        if (sortBy == null) {
            return Comparator.comparingDouble(OSProcess::getProcessCpuLoadCumulative).reversed();
        }

        return switch (sortBy.toLowerCase()) {
            case "memory", "mem", "ram" -> Comparator.comparingLong(OSProcess::getResidentSetSize).reversed();
            case "oldest", "uptime" -> Comparator.comparingLong(OSProcess::getUpTime).reversed();
            case "newest", "recent" -> Comparator.comparingLong(OSProcess::getStartTime).reversed();
            case "pid" -> Comparator.comparingInt(OSProcess::getProcessID);
            default -> Comparator.comparingDouble(OSProcess::getProcessCpuLoadCumulative).reversed();
        };
    }

    /**
     * Kill a process by PID
     */
    public boolean killProcessByPid(int pid) {
        try {
            String osName = System.getProperty("os.name").toLowerCase();

            if (osName.contains("win")) {
                // Windows
                ProcessBuilder pb = new ProcessBuilder("taskkill", "/PID", String.valueOf(pid), "/F");
                Process process = pb.start();
                return process.waitFor() == 0;
            } else {
                // Linux/Unix/Mac
                ProcessBuilder pb = new ProcessBuilder("kill", "-9", String.valueOf(pid));
                Process process = pb.start();
                return process.waitFor() == 0;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get list of open ports
     */
    public List<PortInfo> getOpenPorts() {
        List<PortInfo> ports = new ArrayList<>();
        String osName = System.getProperty("os.name").toLowerCase();

        try {
            if (osName.contains("win")) {
                ports = getWindowsOpenPorts();
            } else {
                ports = getUnixOpenPorts();
            }
        } catch (Exception e) {
            System.err.println("Error getting open ports: " + e.getMessage());
        }

        return ports;
    }

    /**
     * Get open ports on Windows
     */
    private List<PortInfo> getWindowsOpenPorts() throws Exception {
        List<PortInfo> ports = new ArrayList<>();
        ProcessBuilder pb = new ProcessBuilder("netstat", "-ano");
        Process process = pb.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            String headerSkipped = "";
            while ((line = br.readLine()) != null) {
                // Skip header lines
                if (line.contains("Proto") || line.isEmpty()) {
                    continue;
                }

                // Parse netstat output: Proto  Local Address          Foreign Address        State           PID
                Pattern pattern = Pattern.compile("TCP\\s+(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)\\s+.*\\s+(LISTENING|ESTABLISHED)\\s+(\\d+)");
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    int pid = Integer.parseInt(matcher.group(4));
                    PortInfo port = PortInfo.builder()
                            .ipAddress(matcher.group(1))
                            .port(Integer.parseInt(matcher.group(2)))
                            .state(matcher.group(3))
                        .pid(pid)
                            .protocol("TCP")
                        .localAddress(matcher.group(1) + ":" + matcher.group(2))
                        .rawLocalAddress(matcher.group(1) + ":" + matcher.group(2))
                        .processName(getProcessNameByPid(pid))
                        .processCommand(getProcessNameByPid(pid))
                            .build();
                    ports.add(port);
                }
            }
        }

        return ports;
    }

    /**
     * Get open ports on Unix/Linux/Mac
     */
    private List<PortInfo> getUnixOpenPorts() throws Exception {
        List<PortInfo> ports = new ArrayList<>();
        ProcessBuilder pb = new ProcessBuilder("netstat", "-tlnp");
        Process process = pb.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("tcp") || line.startsWith("udp")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length >= 4) {
                        try {
                            String localAddr = parts[3];
                            String[] addressParts = localAddr.split(":");
                            int port = Integer.parseInt(addressParts[addressParts.length - 1]);
                            String ip = addressParts.length > 1 ? addressParts[0] : "*";

                            int pid = -1;
                            String processInfo = "";
                            if (parts.length > 6) {
                                String[] pidInfo = parts[6].split("/");
                                if (pidInfo.length > 0) {
                                    try {
                                        pid = Integer.parseInt(pidInfo[0]);
                                    } catch (NumberFormatException e) {
                                        // Skip
                                    }
                                }
                            }

                            PortInfo portInfo = PortInfo.builder()
                                    .ipAddress(ip)
                                    .port(port)
                                    .state("LISTENING")
                                    .pid(pid)
                                    .protocol(parts[0].toUpperCase())
                                    .localAddress(localAddr)
                                    .rawLocalAddress(localAddr)
                                    .processName(pid > 0 ? getProcessNameByPid(pid) : "Unknown")
                                    .processCommand(pid > 0 ? getProcessNameByPid(pid) : "Unknown")
                                    .build();
                            ports.add(portInfo);
                        } catch (Exception e) {
                            // Skip malformed lines
                        }
                    }
                }
            }
        }

        return ports;
    }

    /**
     * Get process description by PID
     */
    public String getProcessNameByPid(int pid) {
        try {
            String osName = System.getProperty("os.name").toLowerCase();

            if (osName.contains("win")) {
                //Windows - use tasklist
                ProcessBuilder pb = new ProcessBuilder("tasklist", "/FI", "PID eq " + pid);
                Process process = pb.start();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (!line.isEmpty() && !line.contains("PID")) {
                            return line.split("\\s+")[0];
                        }
                    }
                }
            } else {
                // Linux - use ps
                ProcessBuilder pb = new ProcessBuilder("ps", "-p", String.valueOf(pid), "-o", "comm=");
                Process process = pb.start();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line = br.readLine();
                    if (line != null && !line.isEmpty()) {
                        return line.trim();
                    }
                }
            }
        } catch (Exception e) {
            return "Unknown";
        }

        return "Unknown";
    }
}
