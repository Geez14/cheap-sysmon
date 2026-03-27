#!/bin/bash

# Spring Boot System Monitor - Build and Run Script

set -e  # Exit on error

echo "=================================="
echo "Spring Boot System Monitor"
echo "Build & Run Script"
echo "=================================="
echo ""

PROJECT_DIR=$(dirname "$(readlink -f "$0")")
cd "$PROJECT_DIR"

echo "Project Directory: $PROJECT_DIR"
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed. Please install Java 17 or higher."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -1)
echo "✓ Java found: $JAVA_VERSION"
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed. Please install Maven."
    exit 1
fi

MVN_VERSION=$(mvn -version | head -1)
echo "✓ Maven found: $MVN_VERSION"
echo ""

echo "Building project..."
echo ""

# Clean and build
mvn clean install -DskipTests

echo ""
echo "=================================="
echo "Build Complete!"
echo "=================================="
echo ""
echo "Starting Spring Boot Application..."
echo ""
echo "Server will run at: http://localhost:8000"
echo "API Base URL: http://localhost:8000/api"
echo ""
echo "Press Ctrl+C to stop the server"
echo ""

# Run the application
mvn spring-boot:run
