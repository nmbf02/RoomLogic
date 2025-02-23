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
                    sh "docker build -t ${REGISTRY}/${IMAGE_NAME}:latest -f ActixWebAPI/Dockerfile ActixWebAPI"
                }
            }
        }

        stage('Push to Nexus') {
            steps {
                script {
                    sh "docker login ${REGISTRY} -u admin -p Nmbf021101"
                    sh "docker tag ${IMAGE_NAME}:latest ${REGISTRY}/${IMAGE_NAME}:latest"
                    sh "docker push ${REGISTRY}/${IMAGE_NAME}:latest"
                }
            }
        }

        stage('Deploy to Server') {
            steps {
                sshagent(['SSH_CREDENTIALS_ID']) {
                    sh "ssh -i ~/.ssh/id_rsa root@45.55.43.15 'docker pull ${REGISTRY}/${IMAGE_NAME}:latest && docker-compose up -d'"
                }
            }
        }
    }
}