# System Monitor API - API Documentation

## Comprehensive API Reference

### Table of Contents
1. [Base Information](#base-information)
2. [System Endpoints](#system-endpoints)
3. [Network Endpoints](#network-endpoints)
4. [Process Endpoints](#process-endpoints)
5. [Response Format](#response-format)
6. [Error Codes](#error-codes)
7. [Status Codes](#status-codes)

---

## Base Information

**Base URL**: `http://localhost:8080/api`

**Content-Type**: `application/json`

**Response Format**: All responses are JSON-wrapped with standard format

---

## System Endpoints

### 1. System Information
**Endpoint**: `GET /system/info`

**Description**: Returns comprehensive system information including OS details, CPU count, memory, and uptime.

**Request**:
```bash
curl -X GET http://localhost:8080/api/system/info
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "System information retrieved successfully",
  "data": {
    "computerName": "localhost",
    "osName": "Linux",
    "osVersion": "5.15.0-1234-generic",
    "osArch": "amd64",
    "processorCount": 8,
    "totalMemoryBytes": 16000000000,
    "availableMemoryBytes": 8000000000,
    "usedMemoryBytes": 8000000000,
    "memoryUsagePercent": 50.0,
    "systemCpuLoad": 0.25,
    "processCpuLoad": 0.15,
    "uptime": 864000
  },
  "timestamp": "2024-03-27T10:30:00"
}
```

**Fields**:
- `computerName`: System hostname
- `osName`: Operating system name
- `osVersion`: OS version string
- `osArch`: Processor architecture (amd64, arm64, etc.)
- `processorCount`: Number of logical processors
- `totalMemoryBytes`: Total RAM in bytes
- `availableMemoryBytes`: Available RAM in bytes
- `usedMemoryBytes`: Used RAM in bytes
- `memoryUsagePercent`: Memory usage percentage (0-100)
- `systemCpuLoad`: System CPU load (0-1.0)
- `processCpuLoad`: Process CPU load (0-1.0)
- `uptime`: System uptime in seconds

---

### 2. Resource Utilization
**Endpoint**: `GET /system/resources`

**Description**: Returns detailed resource utilization including memory, CPU, and disk information.

**Request**:
```bash
curl -X GET http://localhost:8080/api/system/resources
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Resource information retrieved successfully",
  "data": {
    "memory": {
      "totalMemoryMB": 15625,
      "usedMemoryMB": 7812,
      "availableMemoryMB": 7813,
      "usagePercent": 50.0
    },
    "cpu": {
      "processorCount": 8,
      "systemLoadAverage": 0.25,
      "cpuUsagePercent": 15.0,
      "uptime": 864000
    },
    "disk": {
      "totalSpaceGB": 500,
      "usableSpaceGB": 250,
      "usedSpaceGB": 250,
      "usagePercent": 50.0
    }
  },
  "timestamp": "2024-03-27T10:30:00"
}
```

---

### 3. CPU Load
**Endpoint**: `GET /system/cpu-load`

**Description**: Returns current CPU load as a percentage.

**Response** (200 OK):
```json
{
  "success": true,
  "message": "CPU load retrieved successfully",
  "data": 15.5,
  "timestamp": "2024-03-27T10:30:00"
}
```

---

### 4. Health Check
**Endpoint**: `GET /system/health`

**Description**: Simple health check endpoint to verify API is running.

**Response** (200 OK):
```json
{
  "success": true,
  "message": "System Monitor API is running",
  "data": "OK",
  "timestamp": "2024-03-27T10:30:00"
}
```

---

## Network Endpoints

### 1. Network Information
**Endpoint**: `GET /network/info`

**Description**: Returns all network interfaces with detailed information.

**Request**:
```bash
curl -X GET http://localhost:8080/api/network/info
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Network information retrieved successfully",
  "data": {
    "hostName": "mycomputer",
    "dnsServers": ["8.8.8.8", "1.1.1.1"],
    "interfaces": [
      {
        "name": "eth0",
        "displayName": "Ethernet",
        "ipAddresses": ["192.168.1.100", "fe80::1"],
        "macAddress": "00-1A-2B-3C-4D-5E",
        "isUp": true,
        "bytesReceived": 1000000000,
        "bytesSent": 500000000,
        "packetsReceived": 1000000,
        "packetsSent": 500000
      },
      {
        "name": "lo",
        "displayName": "Loopback",
        "ipAddresses": ["127.0.0.1"],
        "macAddress": "N/A",
        "isUp": true,
        "bytesReceived": 5000000,
        "bytesSent": 5000000,
        "packetsReceived": 50000,
        "packetsSent": 50000
      }
    ]
  },
  "timestamp": "2024-03-27T10:30:00"
}
```

**Interface Fields**:
- `name`: Interface name (eth0, en0, etc.)
- `displayName`: Human-readable display name
- `ipAddresses`: Array of IPv4 and IPv6 addresses
- `macAddress`: MAC address (hardware address)
- `isUp`: Whether interface is active
- `bytesReceived`: Total bytes received
- `bytesSent`: Total bytes sent
- `packetsReceived`: Total packets received
- `packetsSent`: Total packets sent

---

### 2. Network Interfaces
**Endpoint**: `GET /network/interfaces`

**Description**: Returns network interfaces (same as /network/info).

---

### 3. Network Devices
**Endpoint**: `GET /network/devices`

**Description**: Returns connected network devices (same as /network/info).

---

## Process Endpoints

### 1. Open Ports
**Endpoints**:
- `GET /process/ports`
- `GET /process/open-ports` (alternative)

**Description**: Returns all open ports with associated process information.

**Request**:
```bash
curl -X GET http://localhost:8080/api/process/ports
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Open ports retrieved successfully",
  "data": [
    {
      "port": 8080,
      "protocol": "TCP",
      "state": "LISTENING",
      "processName": "java",
      "pid": 1234,
      "ipAddress": "0.0.0.0"
    },
    {
      "port": 22,
      "protocol": "TCP",
      "state": "LISTENING",
      "processName": "sshd",
      "pid": 567,
      "ipAddress": "0.0.0.0"
    },
    {
      "port": 53,
      "protocol": "TCP",
      "state": "LISTENING",
      "processName": "named",
      "pid": 890,
      "ipAddress": "127.0.0.1"
    }
  ],
  "timestamp": "2024-03-27T10:30:00"
}
```

**Port Fields**:
- `port`: Port number (1-65535)
- `protocol`: Protocol (TCP, UDP)
- `state`: Connection state (LISTENING, ESTABLISHED, etc.)
- `processName`: Name of process holding the port
- `pid`: Process ID
- `ipAddress`: IP address (0.0.0.0 for all interfaces)

---

### 2. Kill Process
**Endpoint**: `POST /process/kill/{pid}`

**Description**: Terminates a process by its PID.

**Path Parameters**:
- `pid` (integer, required): Process ID to kill

**Request**:
```bash
curl -X POST http://localhost:8080/api/process/kill/1234
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Process kill operation completed",
  "data": {
    "pid": 1234,
    "killed": true,
    "message": "Process terminated successfully"
  },
  "timestamp": "2024-03-27T10:30:00"
}
```

**Response if failed** (200 OK):
```json
{
  "success": true,
  "message": "Process kill operation completed",
  "data": {
    "pid": 1234,
    "killed": false,
    "message": "Failed to terminate process",
    "error": "Permission denied"
  },
  "timestamp": "2024-03-27T10:30:00"
}
```

**Error Response** (400 Bad Request):
```json
{
  "success": false,
  "error": "INVALID_PID",
  "message": "Invalid PID or protection against critical processes",
  "timestamp": "2024-03-27T10:30:00"
}
```

**Kill Response Fields**:
- `pid`: The process ID that was targeted
- `killed`: Boolean indicating if kill was successful
- `message`: Human-readable result message
- `error`: Error details if operation failed

---

## Response Format

### Success Response
```json
{
  "success": true,
  "message": "Descriptive message",
  "data": {...},
  "timestamp": "2024-03-27T10:30:00"
}
```

### Error Response
```json
{
  "success": false,
  "error": "ERROR_CODE",
  "message": "Descriptive error message",
  "timestamp": "2024-03-27T10:30:00"
}
```

### Response Wrapping
All responses are wrapped with:
- `success`: Boolean indicating operation success
- `message`: Human-readable message
- `data`: Response payload (null on error)
- `timestamp`: ISO-8601 timestamp
- `error`: Error code (only on failure)

---

## Error Codes

| Error Code | HTTP Status | Description |
|-----------|-------------|------------|
| SYSTEM_ERROR | 500 | System information retrieval failed |
| RESOURCE_ERROR | 500 | Resource information retrieval failed |
| CPU_ERROR | 500 | CPU load retrieval failed |
| NETWORK_ERROR | 500 | Network information retrieval failed |
| PORT_ERROR | 500 | Port enumeration failed |
| PROCESS_ERROR | 500 | Process operation failed |
| INVALID_PID | 400 | Invalid or protected PID |

---

## Status Codes

| Status Code | Meaning |
|-----------|---------|
| 200 OK | Request successful |
| 400 Bad Request | Invalid request parameters |
| 500 Internal Server Error | Server-side error |

---

## Rate Limiting

No rate limiting is currently implemented. For production use, implement rate limiting via Spring Cloud Config or gateway.

---

## Pagination

Not applicable - endpoints return complete datasets.

---

## Caching

Responses are not cached. Each request performs real-time queries. Consider implementing caching for frequently accessed endpoints:

```properties
# Add to application.properties if implementing cache
spring.cache.type=caffeine
spring.cache.caffeine.spec=maximumSize=100,expireAfterWrite=5m
```

---

## Platform-Specific Notes

### Linux
- Full support for all features
- Requires `net-tools` for port operations: `sudo apt-get install net-tools`

### Windows
- Full support via native commands
- Requires appropriate command-line tools
- May need administrator privileges for process operations

### macOS
- Full support
- Built-in tools available

---

## Examples

### Python
```python
import requests
resp = requests.get('http://localhost:8080/api/system/info')
print(resp.json())
```

### JavaScript
```javascript
fetch('http://localhost:8080/api/network/info')
  .then(r => r.json())
  .then(data => console.log(data))
```

### cURL
```bash
curl http://localhost:8080/api/system/resources | jq .
```

### Bash
```bash
#!/bin/bash
API="http://localhost:8080/api"
curl -s $API/system/info | jq '.data.computerName'
```
