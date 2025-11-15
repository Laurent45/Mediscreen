#!/bin/bash

# Clean and build all jar files with Maven
mvn clean
mvn install

# Build each image for each jar
docker build . -f report/DockerFile -t mediscreen_report:latest
docker build . -f patient/DockerFile -t mediscreen_patient:latest
docker build . -f note/DockerFile -t mediscreen_note:latest
docker build . -f frontend/Dockerfile -t mediscreen_frontend:latest

# Run container
docker compose up -d
