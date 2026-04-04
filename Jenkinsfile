pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS = credentials('docker-hub-credentials')
        VPS_HOST         = credentials('vps-host')
        VPS_USER         = credentials('vps-nova-nestjs')
        
        GIT_COMMIT_SHORT = sh(
            script: "git rev-parse --short HEAD",
            returnStdout: true
        ).trim()
        
        IMAGE_TAG = "${GIT_COMMIT_SHORT}"
        APP_DIR = "/opt/nova-app/app"
    }

    stages {
        stage('Docker Login') {
            steps {
                sh 'echo "$DOCKER_CREDENTIALS_PSW" | docker login -u "$DOCKER_CREDENTIALS_USR" --password-stdin'
            }
        }

        stage('Checkout Code') {
            steps {
                echo 'Checking out code...'
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building image with tag: ${IMAGE_TAG}"
                sh """
                    docker build \
                      -t "${DOCKER_CREDENTIALS_USR}/nova-app-backend:${IMAGE_TAG}" \
                      -t "${DOCKER_CREDENTIALS_USR}/nova-app-backend:latest" \
                      .

                    docker push "${DOCKER_CREDENTIALS_USR}/nova-app-backend:${IMAGE_TAG}"
                    docker push "${DOCKER_CREDENTIALS_USR}/nova-app-backend:latest"
                """
            }
        }

        stage('Deploy to VPS') {
            steps {
                echo 'Deploying to VPS...'
                script {
                    sshagent(credentials: ['vps-nova-nestjs-ssh-key']) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ${VPS_USER}@${VPS_HOST} \
                                "mkdir -p /tmp/nova-app/backend"
                            
                            scp -o StrictHostKeyChecking=no \
                                scripts/deploy.sh \
                                ${VPS_USER}@${VPS_HOST}:/tmp/nova-app/backend/deploy.sh
                        """
                    }
                    
                    sshagent(credentials: ['vps-nova-nestjs-ssh-key']) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ${VPS_USER}@${VPS_HOST} \
                                "DOCKER_USERNAME=${DOCKER_CREDENTIALS_USR} \
                                 DOCKER_PASSWORD=${DOCKER_CREDENTIALS_PSW} \
                                 IMAGE_TAG=${IMAGE_TAG} \
                                 APP_DIR=${APP_DIR} \
                                 bash /tmp/nova-app/backend/deploy.sh"
                        """
                    }
                }
            }
        }

        stage('Health Check') {
            steps {
                echo 'Checking application health...'
                script {
                    sshagent(credentials: ['vps-nova-nestjs-ssh-key']) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ${VPS_USER}@${VPS_HOST} \
                                "mkdir -p /tmp/nova-app/backend"
                            
                            scp -o StrictHostKeyChecking=no \
                                scripts/health-check.sh \
                                ${VPS_USER}@${VPS_HOST}:/tmp/nova-app/backend/health-check.sh
                        """
                    }
                    
                    sshagent(credentials: ['vps-nova-nestjs-ssh-key']) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ${VPS_USER}@${VPS_HOST} \
                                "APP_DIR=${APP_DIR} bash /tmp/nova-app/backend/health-check.sh"
                        """
                    }
                }
            }
        }

        stage('Cleanup Old Images') {
            steps {
                echo 'Cleaning up old Docker images...'
                script {
                    sshagent(credentials: ['vps-nova-nestjs-ssh-key']) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ${VPS_USER}@${VPS_HOST} \
                                "mkdir -p /tmp/nova-app/backend"
                            
                            scp -o StrictHostKeyChecking=no \
                                scripts/cleanup.sh \
                                ${VPS_USER}@${VPS_HOST}:/tmp/nova-app/backend/cleanup.sh
                        """
                    }
                    
                    sshagent(credentials: ['vps-nova-nestjs-ssh-key']) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ${VPS_USER}@${VPS_HOST} \
                                "APP_DIR=${APP_DIR} bash /tmp/nova-app/backend/cleanup.sh"
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo "Build & Deploy successful!"
            echo "Image: ${DOCKER_CREDENTIALS_USR}/nova-app-backend:${IMAGE_TAG}"
            echo "Deployed to: ${VPS_HOST}"
        }

        failure {
            echo "Build or deploy failed. Rolling back..."
            
            script {
                sshagent(credentials: ['vps-nova-nestjs-ssh-key']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${VPS_USER}@${VPS_HOST} \
                            "mkdir -p /tmp/nova-app/backend"
                        
                        scp -o StrictHostKeyChecking=no \
                            scripts/rollback.sh \
                            ${VPS_USER}@${VPS_HOST}:/tmp/nova-app/backend/rollback.sh
                    """
                }
                
                sshagent(credentials: ['vps-nova-nestjs-ssh-key']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${VPS_USER}@${VPS_HOST} \
                            "DOCKER_USERNAME=${DOCKER_CREDENTIALS_USR} \
                             IMAGE_TAG=${IMAGE_TAG} \
                             APP_DIR=${APP_DIR} \
                             bash /tmp/nova-app/backend/rollback.sh"
                    """
                }
            }
        }

        always {
            echo "Pipeline completed."
        }
    }
}