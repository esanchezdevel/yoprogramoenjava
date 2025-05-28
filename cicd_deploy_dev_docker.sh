#!/bin/bash

db_user=$1      # User to use in database installation
db_password=$2  # Password to use in database installation
paypal_client_id=$3     # Paypal client id
paypal_client_secret=$4 # Paypal client secret
mail_host=$5
mail_port=$6
mail_user=$7
mail_pass=$8

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
sed -i "s/{PAYPAL_CLIENT_ID}/$paypal_client_id/g" dev-deployment/docker-compose.yaml
sed -i "s/{PAYPAL_CLIENT_SECRET}/$paypal_client_secret/g" dev-deployment/docker-compose.yaml
sed -i "s/{MAIL_HOST}/$mail_host/g" dev-deployment/docker-compose.yaml
sed -i "s/{MAIL_PORT}/$mail_port/g" dev-deployment/docker-compose.yaml
sed -i "s/{MAIL_USER}/$mail_user/g" dev-deployment/docker-compose.yaml
safe_mail_pass=$(printf '%s' "$mail_pass" | sed 's/&/\\&/g')
sed -i "s/{MAIL_PASS}/$safe_mail_pass/g" dev-deployment/docker-compose.yaml

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