package com.geez14.sysmon.service;

import com.geez14.sysmon.model.NetworkInfo;
import oshi.hardware.NetworkIF;
import oshi.software.os.NetworkParams;
import org.springframework.stereotype.Service;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for network information retrieval
 */
@Service
public class NetworkService {

    /**
     * Get all network interfaces and their information
     */
    public NetworkInfo getNetworkInfo() {
        List<NetworkInfo.NetworkInterface> interfaces = new ArrayList<>();
        Map<String, NetworkIF> statsByName = new HashMap<>();

        List<NetworkIF> networkIFs = new oshi.SystemInfo().getHardware().getNetworkIFs();
        for (NetworkIF nif : networkIFs) {
            nif.updateAttributes();
            statsByName.put(nif.getName(), nif);
        }

        long totalBytesReceived = 0L;
        long totalBytesSent = 0L;
        long totalPacketsReceived = 0L;
        long totalPacketsSent = 0L;

        try {
            for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                NetworkIF nifStats = statsByName.get(ni.getName());
                NetworkInfo.NetworkInterface networkInterface = NetworkInfo.NetworkInterface.builder()
                        .name(ni.getName())
                        .displayName(ni.getDisplayName())
                        .macAddress(getMacAddress(ni))
                        .isUp(ni.isUp())
                .loopback(ni.isLoopback())
                .virtualInterface(ni.isVirtual())
                .pointToPoint(ni.isPointToPoint())
                .supportsMulticast(ni.supportsMulticast())
                .mtu(ni.getMTU())
                        .ipAddresses(getIpAddresses(ni))
                .ipv4Addresses(getIpv4Addresses(ni))
                .ipv6Addresses(getIpv6Addresses(ni))
                .speedBitsPerSecond(nifStats != null ? nifStats.getSpeed() : 0L)
                        .bytesReceived(nifStats != null ? nifStats.getBytesRecv() : 0L)
                        .bytesSent(nifStats != null ? nifStats.getBytesSent() : 0L)
                        .packetsReceived(nifStats != null ? nifStats.getPacketsRecv() : 0L)
                        .packetsSent(nifStats != null ? nifStats.getPacketsSent() : 0L)
                        .build();
                interfaces.add(networkInterface);

            totalBytesReceived += networkInterface.getBytesReceived();
            totalBytesSent += networkInterface.getBytesSent();
            totalPacketsReceived += networkInterface.getPacketsReceived();
            totalPacketsSent += networkInterface.getPacketsSent();
            }
        } catch (SocketException e) {
            throw new RuntimeException("Error retrieving network information", e);
        }

        String hostName = getHostName();
        List<String> dnsServers = getDnsServers();
        NetworkParams networkParams = new oshi.SystemInfo().getOperatingSystem().getNetworkParams();

        return NetworkInfo.builder()
                .interfaces(interfaces)
                .hostName(hostName)
                .dnsServers(dnsServers)
            .defaultGatewayIpv4(networkParams.getIpv4DefaultGateway())
            .defaultGatewayIpv6(networkParams.getIpv6DefaultGateway())
            .interfaceCount(interfaces.size())
            .totalBytesReceived(totalBytesReceived)
            .totalBytesSent(totalBytesSent)
            .totalPacketsReceived(totalPacketsReceived)
            .totalPacketsSent(totalPacketsSent)
                .build();
    }

    /**
     * Get MAC address for a network interface
     */
    private String getMacAddress(NetworkInterface ni) {
        try {
            byte[] mac = ni.getHardwareAddress();
            if (mac == null) {
                return "N/A";
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            return sb.toString();
        } catch (Exception e) {
            return "N/A";
        }
    }

    /**
     * Get IP addresses for a network interface
     */
    private List<String> getIpAddresses(NetworkInterface ni) {
        List<String> ipAddresses = new ArrayList<>();
        for (InetAddress inetAddress : Collections.list(ni.getInetAddresses())) {
            ipAddresses.add(inetAddress.getHostAddress());
        }
        return ipAddresses;
    }

    private List<String> getIpv4Addresses(NetworkInterface ni) {
        List<String> ipAddresses = new ArrayList<>();
        for (InetAddress inetAddress : Collections.list(ni.getInetAddresses())) {
            if (inetAddress.getHostAddress().contains(".")) {
                ipAddresses.add(inetAddress.getHostAddress());
            }
        }
        return ipAddresses;
    }

    private List<String> getIpv6Addresses(NetworkInterface ni) {
        List<String> ipAddresses = new ArrayList<>();
        for (InetAddress inetAddress : Collections.list(ni.getInetAddresses())) {
            if (inetAddress.getHostAddress().contains(":")) {
                ipAddresses.add(inetAddress.getHostAddress());
            }
        }
        return ipAddresses;
    }

    /**
     * Get host name
     */
    private String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "Unknown";
        }
    }

    /**
     * Get DNS servers (simplified - returns system configured DNS)
     */
    private List<String> getDnsServers() {
        List<String> dnsServers = new ArrayList<>();
        try {
            // Try to read from /etc/resolv.conf on Linux
            if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                try {
                    java.nio.file.Files.lines(java.nio.file.Paths.get("/etc/resolv.conf"))
                            .filter(line -> line.startsWith("nameserver"))
                            .map(line -> line.replace("nameserver", "").trim())
                            .forEach(dnsServers::add);
                } catch (Exception e) {
                    dnsServers.add("127.0.0.1"); // Default localhost
                }
            } else {
                dnsServers.add("8.8.8.8"); // Default public DNS
            }
        } catch (Exception e) {
            dnsServers.add("Unknown");
        }
        return dnsServers.isEmpty() ? List.of("Not available") : dnsServers;
    }
}
