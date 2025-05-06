#!/bin/bash

db_user=$1      # User to use in database installation
db_password=$2  # Password to use in database installation

version=v1.0.4-SNAPSHOT  # Version of Docker image to push in Docker HUB

echo "Deploy to AWS"
echo "-------------"
echo 

echo "1. Compile project"
mvn clean package -Dspring.profiles.active=dev -DskipTests 

echo
echo "2. Build Docker image..."
docker build --no-cache -t esanchezdevel/programandoconjava:$version .

echo
echo "3. Push Docker image to Registry..."
docker push esanchezdevel/programandoconjava:$version

echo
echo "4. Copy Docker Compose file to deployment folder"
rm -rf dev-deployment
mkdir dev-deployment
cp docker-compose-dev.yaml dev-deployment/docker-compose.yaml

echo
echo "5. Replace placeholders in Docker Compose file"
sed -i "s/{DB_USER}/$db_user/g" dev-deployment/docker-compose.yaml
sed -i "s/{DB_PASS}/$db_password/g" dev-deployment/docker-compose.yaml
sed -i "s/{BLOG_VERSION}/$version/g" dev-deployment/docker-compose.yaml

echo
echo "5. Clean environment"
cd dev-deployment
docker compose down
docker image rm $(docker images -q)

echo
echo "6. Deploy web application"
docker compose up -d --build

echo
echo "Finish..."