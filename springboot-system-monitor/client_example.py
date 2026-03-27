#!/usr/bin/env python3
"""
System Monitor API Python Client

A simple Python client for interacting with the Spring Boot System Monitor API.
Install requests: pip install requests
"""

import requests
import json
import sys
from typing import Dict, Any, Optional

class SystemMonitorClient:
    """Client for System Monitor REST API"""
    
    def __init__(self, base_url: str = "http://localhost:8000/api"):
        """
        Initialize the client
        
        Args:
            base_url: Base URL of the API
        """
        self.base_url = base_url
        self.session = requests.Session()
        self.session.headers.update({"Content-Type": "application/json"})
    
    def _make_request(self, method: str, endpoint: str, **kwargs) -> Optional[Dict[str, Any]]:
        """
        Make HTTP request to API
        
        Args:
            method: HTTP method (GET, POST, etc.)
            endpoint: API endpoint path
            **kwargs: Additional arguments to pass to requests
            
        Returns:
            Response JSON or None if error
        """
        url = f"{self.base_url}{endpoint}"
        try:
            if method == "GET":
                response = self.session.get(url, **kwargs)
            elif method == "POST":
                response = self.session.post(url, **kwargs)
            else:
                print(f"Unsupported method: {method}")
                return None
            
            response.raise_for_status()
            return response.json()
        except requests.exceptions.ConnectionError:
            print(f"ERROR: Could not connect to {self.base_url}")
            print("Make sure the API server is running: mvn spring-boot:run")
            return None
        except requests.exceptions.HTTPError as e:
            print(f"HTTP Error: {e}")
            return None
        except json.JSONDecodeError:
            print("ERROR: API response is not valid JSON")
            return None
    
    # System Endpoints
    def get_system_info(self) -> Optional[Dict]:
        """Get system information"""
        return self._make_request("GET", "/system/info")
    
    def get_resources(self) -> Optional[Dict]:
        """Get resource utilization"""
        return self._make_request("GET", "/system/resources")
    
    def get_cpu_load(self) -> Optional[Dict]:
        """Get CPU load percentage"""
        return self._make_request("GET", "/system/cpu-load")
    
    def health_check(self) -> Optional[Dict]:
        """Check API health"""
        return self._make_request("GET", "/system/health")
    
    # Network Endpoints
    def get_network_info(self) -> Optional[Dict]:
        """Get network information"""
        return self._make_request("GET", "/network/info")
    
    def get_network_interfaces(self) -> Optional[Dict]:
        """Get network interfaces"""
        return self._make_request("GET", "/network/interfaces")
    
    def get_network_devices(self) -> Optional[Dict]:
        """Get connected devices"""
        return self._make_request("GET", "/network/devices")
    
    # Process Endpoints
    def get_open_ports(self) -> Optional[Dict]:
        """Get open ports"""
        return self._make_request("GET", "/process/ports")
    
    def kill_process(self, pid: int) -> Optional[Dict]:
        """Kill a process by PID"""
        return self._make_request("POST", f"/process/kill/{pid}")
    
    def pretty_print(self, data: Dict[str, Any]) -> None:
        """Pretty print JSON response"""
        print(json.dumps(data, indent=2))


def main():
    """Main function with example usage"""
    
    # Create client
    client = SystemMonitorClient()
    
    print("=" * 60)
    print("System Monitor API - Python Client")
    print("=" * 60)
    print()
    
    # 1. Health Check
    print("1. Health Check")
    print("-" * 60)
    response = client.health_check()
    if response:
        client.pretty_print(response)
    print()
    
    # 2. System Info
    print("2. System Information")
    print("-" * 60)
    response = client.get_system_info()
    if response:
        if response.get('success'):
            data = response.get('data', {})
            print(f"Computer Name: {data.get('computerName')}")
            print(f"OS: {data.get('osName')} {data.get('osVersion')}")
            print(f"Processors: {data.get('processorCount')}")
            print(f"Total Memory: {data.get('totalMemoryBytes') / (1024**3):.2f} GB")
            print(f"Memory Usage: {data.get('memoryUsagePercent'):.2f}%")
            print(f"Uptime: {data.get('uptime')} seconds")
    print()
    
    # 3. Resources
    print("3. Resource Utilization")
    print("-" * 60)
    response = client.get_resources()
    if response:
        if response.get('success'):
            data = response.get('data', {})
            if data.get('memory'):
                mem = data['memory']
                print(f"Memory Usage: {mem.get('usedMemoryMB')} MB / {mem.get('totalMemoryMB')} MB ({mem.get('usagePercent'):.2f}%)")
            if data.get('cpu'):
                cpu = data['cpu']
                print(f"CPU Usage: {cpu.get('cpuUsagePercent'):.2f}%")
                print(f"CPU Cores: {cpu.get('processorCount')}")
            if data.get('disk'):
                disk = data['disk']
                print(f"Disk Usage: {disk.get('usedSpaceGB')} GB / {disk.get('totalSpaceGB')} GB ({disk.get('usagePercent'):.2f}%)")
    print()
    
    # 4. Network Info
    print("4. Network Information")
    print("-" * 60)
    response = client.get_network_info()
    if response:
        if response.get('success'):
            data = response.get('data', {})
            print(f"Host Name: {data.get('hostName')}")
            print(f"DNS Servers: {', '.join(data.get('dnsServers', []))}")
            print(f"Network Interfaces: {len(data.get('interfaces', []))}")
            for iface in data.get('interfaces', [])[:3]:  # Show first 3
                print(f"  - {iface.get('name')}: {', '.join(iface.get('ipAddresses', []))}")
    print()
    
    # 5. Open Ports
    print("5. Open Ports")
    print("-" * 60)
    response = client.get_open_ports()
    if response:
        if response.get('success'):
            ports = response.get('data', [])
            print(f"Total Open Ports: {len(ports)}")
            for port in ports[:5]:  # Show first 5
                print(f"  - Port {port.get('port')}/{port.get('protocol')}: {port.get('state')} (PID: {port.get('pid')})")
    print()
    
    # 6. CPU Load
    print("6. CPU Load")
    print("-" * 60)
    response = client.get_cpu_load()
    if response:
        if response.get('success'):
            cpu_load = response.get('data')
            print(f"Current CPU Load: {cpu_load:.2f}%")
    print()
    
    print("=" * 60)
    print("Examples of other operations:")
    print("=" * 60)
    print()
    print("Kill a process:")
    print("  result = client.kill_process(1234)")
    print()
    print("Get specific network interface data:")
    print("  network = client.get_network_info()")
    print("  interfaces = network['data']['interfaces']")
    print()


if __name__ == "__main__":
    main()
