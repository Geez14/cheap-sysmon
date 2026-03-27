# Spring Boot System Monitor REST API

A comprehensive REST API built with Spring Boot for monitoring system resources, managing processes, and retrieving network information.

## Features

- **System Monitoring**: Get server load, CPU usage, memory utilization, and uptime
- **Resource Information**: Detailed CPU, memory, and disk usage metrics
- **Network Interfaces**: Retrieve all network interface information including IP addresses and MAC addresses
- **Open Ports**: List all open ports and listening services
- **Process Management**: Kill processes by PID

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Java Version**: Java 17
- **Build Tool**: Maven
- **System Monitoring**: OSHI (Operating System & Hardware Information)
- **Serialization**: Jackson JSON

## Project Structure

```
springboot-system-monitor/
├── pom.xml                          # Maven configuration
├── README.md                        # This file
└── src/
    └── main/
        ├── java/com/portfolio/systemmonitor/
        │   ├── SystemMonitorApplication.java  # Main Spring Boot app
        │   ├── controller/
        │   │   ├── SystemController.java      # System endpoints
        │   │   ├── NetworkController.java     # Network endpoints
        │   │   └── ProcessController.java     # Process management
        │   ├── service/
        │   │   ├── SystemMonitorService.java  # System monitoring logic
        │   │   ├── NetworkService.java        # Network info retrieval
        │   │   └── ProcessService.java        # Process management logic
        │   └── model/
        │       ├── ApiResponse.java           # Generic response wrapper
        │       ├── SystemInfo.java
        │       ├── ResourceInfo.java
        │       ├── NetworkInfo.java
        │       ├── PortInfo.java
        │       └── ProcessKillResponse.java
        └── resources/
            └── application.properties       # Configuration
```

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Linux/Unix/Mac or Windows with WSL

## Installation & Setup

### 1. Clone/Navigate to project
```bash
cd /mnt/c/Users/2444709/Coding/Claude/Portfolio/springboot-system-monitor
```

### 2. Build the application
```bash
mvn clean install
```

### 3. Run the application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## REST API Endpoints

### Base URL: `http://localhost:8080/api`

### System Endpoints

#### 1. Get System Information
```
GET /system/info
```
Returns: Comprehensive system information including OS, CPU count, memory, uptime

**Response Example**:
```json
{
  "success": true,
  "message": "System information retrieved successfully",
  "data": {
    "computerName": "mycomputer",
    "osName": "Linux",
    "osVersion": "5.15.0",
    "osArch": "amd64",
    "processorCount": 8,
    "totalMemoryBytes": 16000000000,
    "usedMemoryBytes": 8000000000,
    "availableMemoryBytes": 8000000000,
    "memoryUsagePercent": 50.0,
    "systemCpuLoad": 0.25,
    "processCpuLoad": 0.15,
    "uptime": 86400
  },
  "timestamp": "2024-03-27T10:30:00"
}
```

#### 2. Get Resource Utilization
```
GET /system/resources
```
Returns: Detailed resources info (memory, CPU, disk)

**Response Example**:
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
      "uptime": 86400
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

#### 3. Get CPU Load
```
GET /system/cpu-load
```
Returns: Current CPU load percentage

#### 4. Health Check
```
GET /system/health
```
Returns: API health status

### Network Endpoints

#### 1. Get Network Information
```
GET /network/info
```
Returns: All network interfaces with IP addresses, MAC addresses, and traffic stats

**Response Example**:
```json
{
  "success": true,
  "message": "Network information retrieved successfully",
  "data": {
    "hostName": "mycomputer",
    "dnsServers": ["8.8.8.8", "8.8.4.4"],
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
      }
    ]
  },
  "timestamp": "2024-03-27T10:30:00"
}
```

#### 2. Get Network Interfaces
```
GET /network/interfaces
```
Returns: List of network interfaces with details

#### 3. Get Network Devices
```
GET /network/devices
```
Returns: Connected network devices and interface information

### Process Endpoints

#### 1. Get Open Ports
```
GET /process/ports
```
or
```
GET /process/open-ports
```
Returns: List of all open ports with associated processes

**Response Example**:
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
    }
  ],
  "timestamp": "2024-03-27T10:30:00"
}
```

#### 2. Kill Process by PID
```
POST /process/kill/{pid}
```
Terminates a process by its PID

**Example**:
```
POST /process/kill/1234
```

**Response Example**:
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

## Usage Examples

### Using cURL

```bash
# Get system info
curl http://localhost:8080/api/system/info

# Get resources
curl http://localhost:8080/api/system/resources

# Get network info
curl http://localhost:8080/api/network/info

# Get open ports
curl http://localhost:8080/api/process/ports

# Kill process with PID 1234
curl -X POST http://localhost:8080/api/process/kill/1234
```

### Using Python

```python
import requests

BASE_URL = "http://localhost:8080/api"

# Get system info
response = requests.get(f"{BASE_URL}/system/info")
print(response.json())

# Get network info
response = requests.get(f"{BASE_URL}/network/info")
print(response.json())

# Get open ports
response = requests.get(f"{BASE_URL}/process/ports")
print(response.json())

# Kill process
response = requests.post(f"{BASE_URL}/process/kill/1234")
print(response.json())
```

### Using JavaScript/Node.js

```javascript
const BASE_URL = 'http://localhost:8080/api';

// Get system info
fetch(`${BASE_URL}/system/info`)
  .then(res => res.json())
  .then(data => console.log(data));

// Get network info
fetch(`${BASE_URL}/network/info`)
  .then(res => res.json())
  .then(data => console.log(data));

// Kill process
fetch(`${BASE_URL}/process/kill/1234`, { method: 'POST' })
  .then(res => res.json())
  .then(data => console.log(data));
```

## Configuration

Edit `src/main/resources/application.properties` to customize:

```properties
spring.application.name=System Monitor API
server.port=8080                              # Change port if needed
server.servlet.context-path=/api              # Change base path
logging.level.root=INFO                       # Change log level
```

## Response Format

All responses follow a consistent format:

```json
{
  "success": true,                            // Operation success indicator
  "message": "Description of response",       // Human-readable message
  "data": {...},                              // Response payload
  "timestamp": "2024-03-27T10:30:00",         // ISO-8601 timestamp
  "error": null                               // Error details (if failed)
}
```

## Error Handling

In case of errors, the API returns:

```json
{
  "success": false,
  "error": "ERROR_TYPE",
  "message": "Description of what went wrong",
  "timestamp": "2024-03-27T10:30:00"
}
```

## Security Notes

⚠️ **Important**: This is a simple demonstration application without authentication. For production use:

1. Add Spring Security with authentication/authorization
2. Implement role-based access control
3. Use HTTPS instead of HTTP
4. Add rate limiting
5. Implement comprehensive logging and monitoring
6. Validate and sanitize all inputs
7. Add API key or OAuth2 authentication
8. Restrict process killing to authorized users only

## Performance Considerations

- System info queries may take a few seconds on heavily loaded systems
- Port enumeration can be slow on systems with many open ports
- Consider caching responses for better performance in production

## Troubleshooting

### Issue: `netstat` command not found

**Solution**: Install netstat/net-tools:
- Ubuntu/Debian: `sudo apt-get install net-tools`
- RedHat/CentOS: `sudo yum install net-tools`
- macOS: Available by default

### Issue: Permission denied when killing processes

**Solution**: You may need elevated privileges:
```bash
sudo mvn spring-boot:run
```

### Issue: Port 8080 already in use

**Solution**: Change the port in application.properties:
```properties
server.port=8090
```

## Building & Deployment

### Create executable JAR:
```bash
mvn clean package
java -jar target/springboot-system-monitor-1.0.0.jar
```

### Using Docker:
Create a Dockerfile (optional):
```dockerfile
FROM openjdk:17
COPY target/springboot-system-monitor-1.0.0.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## License

Open source - MIT License

## Author

Created as a portfolio project

## Support

For issues or questions, please refer to the code comments or Spring Boot documentation.
