#!/bin/bash


echo "Building Spring Boot application..."
./gradlew clean build

# First try to build using bootBuildImage
echo "Attempting to build Docker image using Spring Boot plugin..."
./gradlew bootBuildImage || {
  echo "bootBuildImage task failed. Falling back to manual Docker build..."


  
  # Build the Docker image manually
  echo "Building Docker image manually..."
  docker build -t online-market-place:latest .
}

# Stop and remove containers
echo "Stopping and removing containers..."
docker-compose down

# Remove existing containers related to the project
echo "Removing project containers..."
docker-compose rm -f



# Rebuild docker-compose services without cache
echo "Rebuilding services..."
docker-compose build --no-cache

# Start containers
echo "Starting containers..."
docker-compose up -d

echo "Containers started in detached mode. Use 'docker-compose logs -f' to view logs."