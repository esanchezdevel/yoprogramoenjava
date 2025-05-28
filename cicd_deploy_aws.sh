#!/bin/bash

db_user=$1      # User to use in database installation
db_password=$2  # Password to use in database installation
aws_ssh_ip=$3   # IP of the server where we want to deploy
aws_ssh_key=$4  # Full path to the AWS SSH .pem Key
https_key_password=$5   # Password used by the HTTPS certificate
paypal_client_id=$6     # Paypal client id
paypal_client_secret=$7 # Paypal client secret
mail_host=$8
mail_port=$9
mail_user=$10
mail_pass=$11

version=v1.0.3  # Version of Docker image to push in Docker HUB

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
ssh -i $aws_ssh_key ec2-user@$aws_ssh_ip "sed -i 's/{PAYPAL_CLIENT_ID}/$paypal_client_id/g' docker-compose.yaml"
ssh -i $aws_ssh_key ec2-user@$aws_ssh_ip "sed -i 's/{PAYPAL_CLIENT_SECRET}/$paypal_client_secret/g' docker-compose.yaml"
ssh -i $aws_ssh_key ec2-user@$aws_ssh_ip "sed -i 's/{MAIL_HOST}/$mail_host/g' docker-compose.yaml"
ssh -i $aws_ssh_key ec2-user@$aws_ssh_ip "sed -i 's/{MAIL_PORT}/$mail_port/g' docker-compose.yaml"
ssh -i $aws_ssh_key ec2-user@$aws_ssh_ip "sed -i 's/{MAIL_USER}/$mail_user/g' docker-compose.yaml"
safe_mail_pass=$(printf '%s' "$mail_pass" | sed 's/&/\\&/g')
ssh -i $aws_ssh_key ec2-user@$aws_ssh_ip "sed -i 's/{MAIL_PASS}/"$safe_mail_pass"/g' docker-compose.yaml"


echo
echo "6. Clean environment"
ssh -i $aws_ssh_key ec2-user@$aws_ssh_ip "docker compose down"
ssh -i $aws_ssh_key ec2-user@$aws_ssh_ip "docker image rm \$(docker images -q)"

echo
echo "7. Deploy web application"
ssh -i $aws_ssh_key ec2-user@$aws_ssh_ip "docker compose up -d --build"

echo
echo "Finish..."