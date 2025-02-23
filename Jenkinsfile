pipeline {
    agent any

    environment {
        GIT_CREDENTIALS_ID = 'github-credentials'
        NEXUS_CREDENTIALS_ID = 'nexus-credentials'
        SSH_CREDENTIALS_ID = 'jenkins-ssh-key'
        REGISTRY = 'localhost:8082/repository/docker-repo'
        IMAGE_NAME = 'roomlogic-api'
        SERVER_IP = '45.55.43.15'   
        SSH_USER = 'root'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'develop', credentialsId: env.GIT_CREDENTIALS_ID, url: 'https://github.com/nmbf02/RoomLogic.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t roomlogic-api:latest -f ActixWebAPI/Dockerfile ActixWebAPI/"
                }
            }
        }

        stage('Push to Nexus') {
            steps {
                script {
                    sh "docker login localhost:8082/repository/docker-repo -u admin -p Nmbf021101"
                    sh "docker tag roomlogic-api:latest localhost:8082/repository/docker-repo/roomlogic-api:latest"
                    sh "docker push localhost:8082/repository/docker-repo/roomlogic-api:latest"
                }
            }
        }

        stage('Deploy to Server') {
            steps {
                sshagent(['jenkins-ssh-key']) {  // Reemplaza con el ID correcto en Jenkins
                    sh "ssh -o StrictHostKeyChecking=no root@45.55.43.15 'docker pull localhost:8082/roomlogic-api:latest && docker-compose up -d'"
                }
            }
        }
    }
}



