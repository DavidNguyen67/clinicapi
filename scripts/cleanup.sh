#!/bin/bash

# Script dọn dẹp Docker images và containers cũ
# Chạy trên VPS server sau khi deploy thành công

set -e

APP_DIR="${APP_DIR:-/opt/clinic-api/app}"
KEEP_IMAGES=3  # Giữ lại 3 images gần nhất
KEEP_BACKUPS=2 # Giữ lại 2 backups gần nhất

echo "===================================="
echo "Starting cleanup process..."
echo "===================================="

# Remove stopped containers (ngoại trừ container đang chạy)
echo "Removing stopped containers..."
STOPPED_CONTAINERS=$(docker ps -a -f status=exited -f name=clinic-api -q)
if [ -n "$STOPPED_CONTAINERS" ]; then
    echo "Found stopped containers:"
    docker ps -a -f status=exited -f name=clinic-api
    docker rm $STOPPED_CONTAINERS
    echo "✓ Removed stopped containers"
else
    echo "No stopped containers to remove"
fi

# Remove dangling images (images không có tag)
echo ""
echo "Removing dangling images..."
DANGLING_IMAGES=$(docker images -f "dangling=true" -q)
if [ -n "$DANGLING_IMAGES" ]; then
    docker rmi $DANGLING_IMAGES || true
    echo "✓ Removed dangling images"
else
    echo "No dangling images to remove"
fi

# Cleanup old images (giữ lại N images gần nhất)
echo ""
echo "Cleaning up old images (keeping $KEEP_IMAGES latest versions)..."
OLD_IMAGES=$(docker images --format "{{.Repository}}:{{.Tag}}" | grep "clinic-api-backend" | grep -v "latest" | grep -v "backup" | tail -n +$((KEEP_IMAGES + 1)))
if [ -n "$OLD_IMAGES" ]; then
    echo "Found old images to remove:"
    echo "$OLD_IMAGES"
    echo "$OLD_IMAGES" | xargs -r docker rmi || true
    echo "✓ Removed old images"
else
    echo "No old images to remove"
fi

# Cleanup old backup images (giữ lại N backups gần nhất)
echo ""
echo "Cleaning up old backup images (keeping $KEEP_BACKUPS latest backups)..."
OLD_BACKUPS=$(docker images --format "{{.Repository}}:{{.Tag}}" | grep "backup-" | tail -n +$((KEEP_BACKUPS + 1)))
if [ -n "$OLD_BACKUPS" ]; then
    echo "Found old backups to remove:"
    echo "$OLD_BACKUPS"
    echo "$OLD_BACKUPS" | xargs -r docker rmi || true
    echo "✓ Removed old backup images"
else
    echo "No old backups to remove"
fi

# Cleanup unused volumes
echo ""
echo "Removing unused volumes..."
UNUSED_VOLUMES=$(docker volume ls -qf dangling=true)
if [ -n "$UNUSED_VOLUMES" ]; then
    docker volume rm $UNUSED_VOLUMES || true
    echo "✓ Removed unused volumes"
else
    echo "No unused volumes to remove"
fi

# Cleanup unused networks
echo ""
echo "Removing unused networks..."
docker network prune -f || true

# Show disk usage
echo ""
echo "Docker disk usage after cleanup:"
docker system df

# Show remaining images
echo ""
echo "Remaining images:"
docker images | grep -E "REPOSITORY|clinic-api" || echo "No clinic-api images found"

echo ""
echo "===================================="
echo "Cleanup completed!"
echo "===================================="
