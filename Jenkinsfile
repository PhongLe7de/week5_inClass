pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        SONARQUBE_SERVER = 'SonarQubeServer'
        SONAR_TOKEN = 'sqa_72be9133b5fb18a5367107a4c1519a808d8e9112'
        DOCKERHUB_CREDENTIALS_ID = 'Docker_Hub'
        DOCKERHUB_REPO = 'phongle7de/sep2_inclass_assignment'
        DOCKER_IMAGE_TAG = 'latest'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/PhongLe7de/week5_inClass.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    sh """
                    ${tool 'SonarScanner'}/bin/sonar-scanner \
                    -Dsonar.projectKey=devops-demo \
                    -Dsonar.sources=src \
                    -Dsonar.projectName=DevOps-Demo \
                    -Dsonar.host.url=http://localhost:9000 \
                    -Dsonar.login=${env.SONAR_TOKEN} \
                    -Dsonar.java.binaries=target/classes
                    """
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "/usr/local/bin/docker build -t ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG} ."
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKERHUB_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                    /usr/local/bin/docker login -u $DOCKER_USER -p $DOCKER_PASS
                    /usr/local/bin/docker push ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}
                    """
                }
            }
        }

    }
}
