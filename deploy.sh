#!/bin/bash
NAMESPACE=yoprogramoenjava
VERSION=v0.0.1-SNAPSHOT
PG_PASSWORD=$1
ADMIN_PASSWORD=$2

echo "======================="
echo "Compiling project..."
mvn clean package -Dspring.profiles.active=dev -DskipTests

echo
echo "Building image..."
docker build -t esanchezdevel/yoprogramoenjava:$VERSION .

echo
echo "Pushing image to registry..."
docker push esanchezdevel/yoprogramoenjava:$VERSION

echo
echo "Cleaning K8s namespace..."
kubectl delete all,configmaps,secrets --all -n $NAMESPACE

echo
echo "Creating secrets in K8s..."
kubectl create secret generic postgres-password --from-literal=password=$PG_PASSWORD -n $NAMESPACE
kubectl create secret generic admin-password --from-literal=password=$ADMIN_PASSWORD -n $NAMESPACE

echo
echo "Deploying in K8s cluster..."
kubectl apply -f k8sfile.yaml -n $NAMESPACE

echo
echo "Done..."
echo "======================="
