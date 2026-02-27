#!/bin/bash

# Script kiểm tra health của Spring Boot application
# Chạy trên VPS server sau khi deploy

set -e

APP_DIR="${APP_DIR:-/opt/clinic-api/app}"
CONTAINER_NAME="clinic-api-app"
HEALTH_URL="http://localhost:8080/actuator/health"
API_TEST_URL="http://localhost:8080/api/home"
MAX_RETRIES=30
RETRY_INTERVAL=5

echo "===================================="
echo "Starting health check..."
echo "===================================="

# Check if container exists and is running
echo "Checking if container is running..."
if ! docker ps | grep -q "$CONTAINER_NAME"; then
    echo "✗ Container $CONTAINER_NAME is not running!"
    echo "Checking all containers:"
    docker ps -a | grep "$CONTAINER_NAME" || echo "Container not found"
    exit 1
fi

echo "✓ Container is running"

# Check Docker health status
echo ""
echo "Checking Docker health status..."
DOCKER_HEALTH=$(docker inspect --format='{{.State.Health.Status}}' "$CONTAINER_NAME" 2>/dev/null || echo "no-healthcheck")
echo "Docker health status: $DOCKER_HEALTH"

if [ "$DOCKER_HEALTH" = "unhealthy" ]; then
    echo "✗ Docker health check failed!"
    echo "Container logs:"
    docker logs --tail 50 "$CONTAINER_NAME"
    exit 1
fi

# Check Spring Boot Actuator health endpoint
echo ""
echo "Checking Spring Boot Actuator health endpoint..."
for i in $(seq 1 $MAX_RETRIES); do
    echo "Attempt $i/$MAX_RETRIES: Checking $HEALTH_URL"
    
    if HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$HEALTH_URL" 2>/dev/null); then
        if [ "$HTTP_STATUS" = "200" ]; then
            echo "✓ Health check passed! (HTTP $HTTP_STATUS)"
            
            # Get detailed health info
            HEALTH_RESPONSE=$(curl -s "$HEALTH_URL")
            echo "Health response: $HEALTH_RESPONSE"
            break
        else
            echo "✗ Health check failed with HTTP $HTTP_STATUS"
        fi
    else
        echo "✗ Failed to connect to health endpoint"
    fi
    
    if [ $i -eq $MAX_RETRIES ]; then
        echo "✗ Health check failed after $MAX_RETRIES attempts!"
        echo "Container logs:"
        docker logs --tail 100 "$CONTAINER_NAME"
        exit 1
    fi
    
    sleep $RETRY_INTERVAL
done

# Test API endpoint
echo ""
echo "Testing API endpoint..."
if API_RESPONSE=$(curl -s "$API_TEST_URL" 2>/dev/null); then
    echo "✓ API test endpoint responded"
    echo "API response: $API_RESPONSE"
else
    echo "⚠ Warning: API test endpoint did not respond (this might be expected if authentication is required)"
fi

# Check container stats
echo ""
echo "Container resource usage:"
docker stats --no-stream "$CONTAINER_NAME" | tail -n 1

# Show recent logs
echo ""
echo "Recent application logs:"
docker logs --tail 30 "$CONTAINER_NAME"

echo ""
echo "===================================="
echo "Health check completed successfully!"
echo "===================================="
echo "Application is healthy and ready to serve requests"
echo "Health URL: $HEALTH_URL"
echo "API URL: $API_TEST_URL"
