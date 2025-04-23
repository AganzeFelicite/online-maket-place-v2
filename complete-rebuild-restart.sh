# Stop and remove containers
docker-compose down

# Remove all images related to the project
docker-compose rm -f

# Remove cached images
docker system prune -a

# Rebuild without using cache
docker-compose build --no-cache

# Start containers
docker-compose up
