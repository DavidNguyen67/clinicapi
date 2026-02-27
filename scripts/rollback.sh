#!/bin/bash

# Script rollback về phiên bản trước khi deployment failed
# Chạy trên VPS server khi deployment thất bại

set -e

DOCKER_USERNAME="${DOCKER_USERNAME}"
IMAGE_TAG="${IMAGE_TAG:-latest}"
APP_NAME="${APP_NAME:-clinic-api-backend}"
APP_DIR="${APP_DIR:-/opt/clinic-api/app}"

CONTAINER_NAME="clinic-api-app"
NETWORK_NAME="clinic-network"

echo "===================================="
echo "Starting rollback process..."
echo "===================================="

# Find the most recent backup image
echo "Finding backup images..."
BACKUP_IMAGE=$(docker images --format "{{.Repository}}:{{.Tag}}" | grep "${DOCKER_USERNAME}/${APP_NAME}:backup-" | head -n 1)

if [ -z "$BACKUP_IMAGE" ]; then
    echo "✗ No backup image found!"
    echo "Available images:"
    docker images | grep "$APP_NAME" || echo "No images found"
    echo ""
    echo "Attempting to rollback to 'latest' tag..."
    ROLLBACK_IMAGE="${DOCKER_USERNAME}/${APP_NAME}:latest"
else
    echo "✓ Found backup image: $BACKUP_IMAGE"
    ROLLBACK_IMAGE="$BACKUP_IMAGE"
fi

# Stop and remove current (failed) container
if docker ps -a | grep -q "$CONTAINER_NAME"; then
    echo "Stopping current container..."
    docker stop "$CONTAINER_NAME" || true
    docker rm "$CONTAINER_NAME" || true
    echo "✓ Removed failed container"
fi

# Start container with backup/latest image
echo ""
echo "Starting container with image: $ROLLBACK_IMAGE"
cd "$APP_DIR"

docker run -d \
    --name "$CONTAINER_NAME" \
    --network "$NETWORK_NAME" \
    -p 8080:8080 \
    -e SPRING_PROFILES_ACTIVE=prod \
    -e JWT_SECRET="${JWT_SECRET:-clinic-default-secret}" \
    --restart unless-stopped \
    --health-cmd="wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1" \
    --health-interval=30s \
    --health-timeout=3s \
    --health-start-period=40s \
    --health-retries=3 \
    "$ROLLBACK_IMAGE"

# Wait for container to be healthy
echo ""
echo "Waiting for application to be healthy..."
TIMEOUT=120
ELAPSED=0
INTERVAL=5

while [ $ELAPSED -lt $TIMEOUT ]; do
    if docker ps | grep -q "$CONTAINER_NAME"; then
        HEALTH_STATUS=$(docker inspect --format='{{.State.Health.Status}}' "$CONTAINER_NAME" 2>/dev/null || echo "starting")
        echo "Health status: $HEALTH_STATUS (${ELAPSED}s elapsed)"
        
        if [ "$HEALTH_STATUS" = "healthy" ]; then
            echo "✓ Application is healthy after rollback!"
            break
        fi
    else
        echo "✗ Container is not running after rollback!"
        docker logs --tail 50 "$CONTAINER_NAME"
        exit 1
    fi
    
    sleep $INTERVAL
    ELAPSED=$((ELAPSED + INTERVAL))
done

if [ $ELAPSED -ge $TIMEOUT ]; then
    echo "✗ Rollback failed! Application did not become healthy in ${TIMEOUT}s"
    echo "Container logs:"
    docker logs --tail 100 "$CONTAINER_NAME"
    exit 1
fi

# Remove failed image
echo ""
echo "Cleaning up failed deployment image..."
FAILED_IMAGE="${DOCKER_USERNAME}/${APP_NAME}:${IMAGE_TAG}"
if docker images | grep -q "$FAILED_IMAGE"; then
    docker rmi "$FAILED_IMAGE" || echo "Could not remove failed image (might be in use)"
fi

# Show container info
echo ""
echo "===================================="
echo "Rollback successful!"
echo "===================================="
echo "Container Name: $CONTAINER_NAME"
echo "Rollback Image: $ROLLBACK_IMAGE"
echo "Network: $NETWORK_NAME"
echo "Port: 8080"
echo ""
echo "Container Status:"
docker ps | grep "$CONTAINER_NAME"

echo ""
echo "Recent logs:"
docker logs --tail 20 "$CONTAINER_NAME"

echo ""
echo "⚠ Please investigate the deployment failure before attempting to deploy again."
