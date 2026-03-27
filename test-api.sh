#!/bin/bash

# Spring Boot System Monitor API - Test Script
# This script tests all endpoints with curl

API_BASE="http://localhost:8080/api"
HEADERS="Content-Type: application/json"

echo "=========================================="
echo "System Monitor API Test Suite"
echo "=========================================="
echo ""

# Function to test endpoint
test_endpoint() {
    local method=$1
    local endpoint=$2
    local description=$3
    
    echo "Testing: $description"
    echo "Request: $method $API_BASE$endpoint"
    
    if [ "$method" = "GET" ]; then
        curl -s -X GET "$API_BASE$endpoint" -H "$HEADERS" | jq . || echo "Response (non-JSON)"
    elif [ "$method" = "POST" ]; then
        curl -s -X POST "$API_BASE$endpoint" -H "$HEADERS" | jq . || echo "Response (non-JSON)"
    fi
    
    echo ""
    echo "---"
    echo ""
    sleep 1
}

# Test System Endpoints
echo "SYSTEM ENDPOINTS"
echo "=================="
echo ""

test_endpoint "GET" "/system/health" "Health Check"
test_endpoint "GET" "/system/info" "System Information"
test_endpoint "GET" "/system/resources" "Resource Utilization"
test_endpoint "GET" "/system/cpu-load" "CPU Load"

# Test Network Endpoints
echo "NETWORK ENDPOINTS"
echo "================="
echo ""

test_endpoint "GET" "/network/info" "Network Information"
test_endpoint "GET" "/network/interfaces" "Network Interfaces"
test_endpoint "GET" "/network/devices" "Network Devices"

# Test Process Endpoints
echo "PROCESS ENDPOINTS"
echo "================="
echo ""

test_endpoint "GET" "/process/ports" "Open Ports"
test_endpoint "GET" "/process/open-ports" "Open Ports (Alternative)"

# Note about kill endpoint
echo "PROCESS MANAGEMENT"
echo "=================="
echo ""
echo "To kill a process, use:"
echo "  curl -X POST http://localhost:8080/api/process/kill/{PID}"
echo ""
echo "Example (kill process with PID 1234):"
echo "  curl -X POST http://localhost:8080/api/process/kill/1234"
echo ""

echo "=========================================="
echo "Test Suite Complete!"
echo "=========================================="
