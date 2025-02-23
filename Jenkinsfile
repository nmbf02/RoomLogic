pipeline {
    agent any

    environment {
        GIT_CREDENTIALS_ID = 'github-credentials'
        NEXUS_CREDENTIALS_ID = 'nexus-credentials'
        SSH_CREDENTIALS_ID = 'jenkins-ssh-key'
        REGISTRY = 'localhost:8082'  
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
                    sh "docker push ${REGISTRY}/${IMAGE_NAME}:latest"
                }
            }
        }

        stage('Deploy to Server') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: env.SSH_CREDENTIALS_ID, keyFileVariable: 'SSH_KEY')]) {
                    sh '''ssh -i "$SSH_KEY" ${SSH_USER}@${SERVER_IP} 'docker pull ${REGISTRY}/${IMAGE_NAME}:latest && docker-compose up -d' '''
                }
            }
        }
    }
}