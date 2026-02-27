#!/bin/bash

# Script deploy Spring Boot application lên VPS
# Chạy trên VPS server

set -e

# Variables từ Jenkins
DOCKER_USERNAME="${DOCKER_USERNAME}"
DOCKER_PASSWORD="${DOCKER_PASSWORD}"
IMAGE_TAG="${IMAGE_TAG:-latest}"
APP_NAME="${APP_NAME:-clinic-api-backend}"
APP_DIR="${APP_DIR:-/opt/clinic-api/app}"

CONTAINER_NAME="clinic-api-app"
NETWORK_NAME="clinic-network"
DB_CONTAINER_NAME="clinic-mysql"

echo "===================================="
echo "Starting deployment process..."
echo "===================================="

# Login to Docker Hub
echo "Logging in to Docker Hub..."
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin

# Create network if not exists
echo "Checking Docker network..."
if ! docker network ls | grep -q "$NETWORK_NAME"; then
    echo "Creating network: $NETWORK_NAME"
    docker network create "$NETWORK_NAME"
else
    echo "Network already exists: $NETWORK_NAME"
fi

# Backup current container if exists
if docker ps -a | grep -q "$CONTAINER_NAME"; then
    echo "Backing up current container..."
    BACKUP_TAG="backup-$(date +%Y%m%d-%H%M%S)"
    docker commit "$CONTAINER_NAME" "${DOCKER_USERNAME}/${APP_NAME}:${BACKUP_TAG}" || true
    echo "Backup created: ${BACKUP_TAG}"
    
    echo "Stopping and removing current container..."
    docker stop "$CONTAINER_NAME" || true
    docker rm "$CONTAINER_NAME" || true
fi

# Pull new image
echo "Pulling new image: ${DOCKER_USERNAME}/${APP_NAME}:${IMAGE_TAG}"
docker pull "${DOCKER_USERNAME}/${APP_NAME}:${IMAGE_TAG}"

# Create app directory if not exists
mkdir -p "$APP_DIR"
cd "$APP_DIR"

# Create .env file (nếu cần)
cat > .env << EOF
# Database Configuration (nếu dùng local MySQL container)
MYSQL_ROOT_PASSWORD=your_root_password
MYSQL_DATABASE=clinic_system
MYSQL_USER=clinic_user
MYSQL_PASSWORD=clinic_password

# JWT Secret (override từ application.yml)
JWT_SECRET=your-production-jwt-secret-key-change-this-in-production

# Application Port
SERVER_PORT=8080
EOF

# Deploy new container
echo "Starting new container..."
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
    "${DOCKER_USERNAME}/${APP_NAME}:${IMAGE_TAG}"

# Wait for container to be healthy
echo "Waiting for application to be healthy..."
TIMEOUT=120
ELAPSED=0
INTERVAL=5

while [ $ELAPSED -lt $TIMEOUT ]; do
    if docker ps | grep -q "$CONTAINER_NAME"; then
        HEALTH_STATUS=$(docker inspect --format='{{.State.Health.Status}}' "$CONTAINER_NAME" 2>/dev/null || echo "starting")
        echo "Health status: $HEALTH_STATUS (${ELAPSED}s elapsed)"
        
        if [ "$HEALTH_STATUS" = "healthy" ]; then
            echo "✓ Application is healthy!"
            break
        fi
    else
        echo "✗ Container is not running!"
        docker logs --tail 50 "$CONTAINER_NAME"
        exit 1
    fi
    
    sleep $INTERVAL
    ELAPSED=$((ELAPSED + INTERVAL))
done

if [ $ELAPSED -ge $TIMEOUT ]; then
    echo "✗ Health check timeout! Application failed to become healthy in ${TIMEOUT}s"
    echo "Container logs:"
    docker logs --tail 100 "$CONTAINER_NAME"
    exit 1
fi

# Show container info
echo ""
echo "===================================="
echo "Deployment successful!"
echo "===================================="
echo "Container Name: $CONTAINER_NAME"
echo "Image: ${DOCKER_USERNAME}/${APP_NAME}:${IMAGE_TAG}"
echo "Network: $NETWORK_NAME"
echo "Port: 8080"
echo "Health Endpoint: http://localhost:8080/actuator/health"
echo ""
echo "Container Status:"
docker ps | grep "$CONTAINER_NAME"

echo ""
echo "Recent logs:"
docker logs --tail 20 "$CONTAINER_NAME"
