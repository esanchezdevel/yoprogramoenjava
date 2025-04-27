#!/bin/bash

db_user=$1      # User to use in database installation
db_password=$2  # Password to use in database installation
aws_ssh_ip=$3   # IP of the server where we want to deploy
aws_ssh_key=$4  # Full path to the AWS SSH .pem Key
https_key_password=$5   # Password used by the HTTPS certificate

version=v1.0.1  # Version of Docker image to push in Docker HUB

echo "Deploy to AWS"
echo "-------------"
echo 

echo "1. Compile project"
mvn clean package -Dspring.profiles.active=prod -DskipTests 

echo
echo "2. Build Docker image..."
docker build --no-cache -t esanchezdevel/programandoconjava:$version .

echo
echo "3. Push Docker image to Registry..."
docker push esanchezdevel/programandoconjava:$version

echo
echo "4. Upload Docker Compose file to AWS"
scp -i $aws_ssh_key docker-compose.yaml ec2-user@$aws_ssh_ip:/home/ec2-user

echo
echo "5. Replace placeholders in Docker Compose file"
ssh -i $aws_ssh_key ec2-user@$aws_ssh_ip "sed -i 's/{DB_USER}/$db_user/g' docker-compose.yaml"
ssh -i $aws_ssh_key ec2-user@$aws_ssh_ip "sed -i 's/{DB_PASS}/$db_password/g' docker-compose.yaml"
ssh -i $aws_ssh_key ec2-user@$aws_ssh_ip "sed -i 's/{BLOG_VERSION}/$version/g' docker-compose.yaml"
ssh -i $aws_ssh_key ec2-user@$aws_ssh_ip "sed -i 's/{HTTPS_KEYSTORE_PASSWORD}/$https_key_password/g' docker-compose.yaml"

echo
echo "6. Clean environment"
ssh -i $aws_ssh_key ec2-user@$aws_ssh_ip "docker compose down"
ssh -i $aws_ssh_key ec2-user@$aws_ssh_ip "docker image rm \$(docker images -q)"

echo
echo "7. Deploy web application"
ssh -i $aws_ssh_key ec2-user@$aws_ssh_ip "docker compose up -d --build"

echo
echo "Finish..."