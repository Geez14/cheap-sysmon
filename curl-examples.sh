#!/bin/bash

# Spring Boot System Monitor API - Useful cURL Commands

API_BASE="http://localhost:8080/api"

echo "System Monitor API - cURL Examples"
echo "===================================="
echo ""

# System Endpoints
echo "SYSTEM COMMANDS:"
echo "================"

echo "1. Get System Information:"
echo "   curl $API_BASE/system/info | jq ."
echo ""

echo "2. Get Resource Utilization:"
echo "   curl $API_BASE/system/resources | jq ."
echo ""

echo "3. Get CPU Load:"
echo "   curl $API_BASE/system/cpu-load | jq ."
echo ""

echo "4. Health Check:"
echo "   curl $API_BASE/system/health | jq ."
echo ""

# Network Endpoints
echo "NETWORK COMMANDS:"
echo "================"

echo "5. Get Network Information:"
echo "   curl $API_BASE/network/info | jq ."
echo ""

echo "6. Get Network Interfaces:"
echo "   curl $API_BASE/network/interfaces | jq ."
echo ""

echo "7. Get Network Devices:"
echo "   curl $API_BASE/network/devices | jq ."
echo ""

# Process Endpoints
echo "PROCESS COMMANDS:"
echo "================"

echo "8. Get Open Ports:"
echo "   curl $API_BASE/process/ports | jq ."
echo ""

echo "9. Kill Process by PID (e.g., PID 1234):"
echo "   curl -X POST $API_BASE/process/kill/1234 | jq ."
echo ""

echo "USEFUL VARIATIONS:"
echo "=================="

echo "10. Pretty-print JSON (requires jq):"
echo "    curl $API_BASE/system/info | jq ."
echo ""

echo "11. Save response to file:"
echo "    curl $API_BASE/system/info > system_info.json"
echo ""

echo "12. Get only specific data from response:"
echo "    curl $API_BASE/system/info | jq '.data.osName'"
echo ""

echo "13. Extract all IP addresses from network info:"
echo "    curl $API_BASE/network/info | jq '.data.interfaces[].ipAddresses[]'"
echo ""

echo "14. Get only hostname:"
echo "    curl $API_BASE/network/info | jq '.data.hostName'"
echo ""

echo "15. Filter open ports by protocol:"
echo "    curl $API_BASE/process/ports | jq '.data[] | select(.protocol==\"TCP\")'"
echo ""

echo "TESTING WITHOUT jq:"
echo "==================="

echo "16. Simple GET request:"
echo "    curl $API_BASE/system/health"
echo ""

echo "17. Show response headers:"
echo "    curl -i $API_BASE/system/health"
echo ""

echo "18. Verbose output:"
echo "    curl -v $API_BASE/system/health"
echo ""

echo "PIPING EXAMPLES:"
echo "==============="

echo "19. Count number of open ports:"
echo "    curl -s $API_BASE/process/ports | jq '.data | length'"
echo ""

echo "20. Get listening ports only:"
echo "    curl -s $API_BASE/process/ports | jq '.data[] | select(.state==\"LISTENING\")'"
echo ""
