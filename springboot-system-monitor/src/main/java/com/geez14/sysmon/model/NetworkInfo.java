package com.geez14.sysmon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * Network information models
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NetworkInfo {
    private List<NetworkInterface> interfaces;
    private List<String> dnsServers;
    private String hostName;
    private String defaultGatewayIpv4;
    private String defaultGatewayIpv6;
    private int interfaceCount;
    private long totalBytesReceived;
    private long totalBytesSent;
    private long totalPacketsReceived;
    private long totalPacketsSent;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NetworkInterface {
        private String name;
        private String displayName;
        private List<String> ipAddresses;
        private List<String> ipv4Addresses;
        private List<String> ipv6Addresses;
        private String macAddress;
        private boolean isUp;
        private boolean loopback;
        private boolean virtualInterface;
        private boolean pointToPoint;
        private boolean supportsMulticast;
        private int mtu;
        private long speedBitsPerSecond;
        private long bytesReceived;
        private long bytesSent;
        private long packetsReceived;
        private long packetsSent;
    }
}
