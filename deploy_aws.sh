#!/bin/bash

db_password=$1
aws_ssh_ip=$2
aws_ssh_key=$3 # Full path to the AWS SSH .pem Key

version=v1.0.0

echo "Deploy to AWS"
echo "-------------"
echo 

echo "1. Compile project"
mvn clean package -Dspring.profiles.active=prod -DskipTests 

echo
echo "2. Build Docker image..."
docker build -t esanchezdevel/yoprogramoenjava:$version .

echo
echo "3. Push Docker image to Registry..."
docker push esanchezdevel/yoprogramoenjava:$version

echo
echo "4. Upload Docker Compose file to AWS"
scp -i $aws_ssh_key docker-compose.yaml ec2-user@$aws_ssh_ip:/home/ec2-user

echo
echo "5. Replace placeholders in Docker Compose file"
# TODO continue

echo "Finish..."