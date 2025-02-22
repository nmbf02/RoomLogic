pipeline {
    agent any

    environment {
        GIT_CREDENTIALS_ID = 'github-credentials'
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
                    sh "docker build -t ${REGISTRY}/${IMAGE_NAME}:latest -f Dockerfile ."
                }
            }
        }

        stage('Push to Nexus') {
            when {
                expression { return false }  // 🔹 Desactivado hasta que configures Nexus correctamente
            }
            steps {
                script {
                    sh "docker login ${REGISTRY} -u admin -p admin123"
                    sh "docker push ${REGISTRY}/${IMAGE_NAME}:latest"
                }
            }
        }

        stage('Deploy to Server') {
            steps {
                sshagent([env.SSH_CREDENTIALS_ID]) {
                    sh """
                        ssh ${SSH_USER}@${SERVER_IP} '
                        docker pull ${REGISTRY}/${IMAGE_NAME}:latest &&
                        docker stop ${IMAGE_NAME} || true &&
                        docker rm ${IMAGE_NAME} || true &&
                        docker run -d -p 8082:8082 --name ${IMAGE_NAME} ${REGISTRY}/${IMAGE_NAME}:latest
                        '
                    """
                }
            }
        }
    }
}