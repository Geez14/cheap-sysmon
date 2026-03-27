# System Monitor API - Quick Start Guide

## One-Liner Build & Run

```bash
cd /mnt/c/Users/2444709/Coding/Claude/Portfolio/springboot-system-monitor && mvn clean install && mvn spring-boot:run
```

## In WSL Terminal

```bash
# Navigate to project
cd /mnt/c/Users/2444709/Coding/Claude/Portfolio/springboot-system-monitor

# First time: Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The server will start at: **http://localhost:8080/api**

## Quick Test Commands

Once running, test the API in another terminal:

```bash
# System info
curl http://localhost:8080/api/system/info | jq .

# Resources
curl http://localhost:8080/api/system/resources | jq .

# Network info
curl http://localhost:8080/api/network/info | jq .

# Open ports
curl http://localhost:8080/api/process/ports | jq .

# Health check
curl http://localhost:8080/api/system/health | jq .
```

## Available Endpoints Summary

| Method | Endpoint                      | Description                  |
|--------|-------------------------------|------------------------------|
| GET    | /api/system/info              | Server info & load           |
| GET    | /api/system/resources         | Detailed resource metrics    |
| GET    | /api/system/cpu-load          | CPU load percentage          |
| GET    | /api/system/health            | API health status            |
| GET    | /api/network/info             | Network interfaces info      |
| GET    | /api/network/interfaces       | Network interfaces list      |
| GET    | /api/network/devices          | Connected devices            |
| GET    | /api/process/ports            | All open ports               |
| GET    | /api/process/open-ports       | All open ports (alternative) |
| POST   | /api/process/kill/{pid}       | Kill process by PID          |

## Architecture

- **Controllers**: Handle HTTP requests and return responses
- **Services**: Implement business logic for system monitoring
- **Models**: Data transfer objects for requests/responses
- **OSHI**: External library for system information gathering

## Windows File Access

Since you're using WSL, the Windows files at:
```
c:\Users\2444709\Coding\Claude\Portfolio\springboot-system-monitor
```

Are accessible in WSL at:
```
/mnt/c/Users/2444709/Coding/Claude/Portfolio/springboot-system-monitor
```

## Troubleshooting

**Build fails**: Ensure Java 17 is installed
```bash
java -version
```

**Port 8080 in use**: Kill existing process or change port in application.properties

**netstat errors**: Install net-tools
```bash
sudo apt-get install net-tools  # Ubuntu/Debian
```

See README.md for complete documentation!
