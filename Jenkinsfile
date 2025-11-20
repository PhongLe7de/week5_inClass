pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        SONARQUBE_SERVER = 'SonarQubeServer'
        SONAR_TOKEN = 'sqa_72be9133b5fb18a5367107a4c1519a808d8e9112'
        DOCKERHUB_CREDENTIALS_ID = 'docker_hub'
        DOCKER_IMAGE = 'phongle7de/sep2_inclass_assignment'
        DOCKER_TAG = 'latest'
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
                withCredentials([usernamePassword(credentialsId: 'docker_hub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        # create temporary docker config folder
                        mkdir -p /tmp/docker-config

                        # write auth to config.json (base64 encoded username:password)
                        echo "{\\"auths\\":{\\"https://index.docker.io/v1/\\":{\\"auth\\":\\"$(echo -n $DOCKER_USER:$DOCKER_PASS | base64)\\"}}}" > /tmp/docker-config/config.json

                        # set DOCKER_CONFIG to the temp folder and build the image
                        DOCKER_CONFIG=/tmp/docker-config /usr/local/bin/docker build -t $DOCKER_IMAGE:$DOCKER_TAG .
                    '''
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', env.DOCKERHUB_CREDENTIALS_ID) {
                        docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push()
                    }
                }
            }
        }
    }
}
