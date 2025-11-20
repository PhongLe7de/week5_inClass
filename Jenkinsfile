pipeline {
    agent any
    tools{
        maven 'maven3'
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
                git branch: 'master', url: 'https://github.com/ckapiainen/OTP2_week4.git'
            }
        }

        stage('Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn clean package -DskipTests'
                    } else {
                        bat 'mvn clean package -DskipTests'
                    }
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn test'
                    } else {
                        bat 'mvn test'
                    }
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    script {
                        if (isUnix()) {
                            sh """
                                sonar-scanner \
                                -Dsonar.projectKey=app:otp2-w4 \
                                -Dsonar.sources=src/main/java \
                                -Dsonar.tests=src/test/java \
                                -Dsonar.projectName='Week 4 - OTP2' \
                                -Dsonar.host.url=http://localhost:9000 \
                                -Dsonar.login=${env.SONAR_TOKEN} \
                                -Dsonar.java.binaries=target/classes \
                                -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                            """
                        } else {
                            bat """
                                sonar-scanner ^
                                -Dsonar.projectKey=app:otp2-w4 ^
                                -Dsonar.sources=src/main/java ^
                                -Dsonar.tests=src/test/java ^
                                -Dsonar.projectName="Week 4 - OTP2" ^
                                -Dsonar.host.url=http://localhost:9000 ^
                                -Dsonar.login=%SONAR_TOKEN% ^
                                -Dsonar.java.binaries=target/classes ^
                                -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                            """
                        }
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    if (isUnix()) {
                        sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                    } else {
                        bat "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                    }
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: env.DOCKERHUB_CREDENTIALS_ID, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        if (isUnix()) {
                            sh """
                                echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin
                                docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                                docker logout
                            """
                        } else {
                            bat """
                                echo %DOCKER_PASSWORD% | docker login -u %DOCKER_USERNAME% --password-stdin
                                docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                                docker logout
                            """
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            junit(testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true)
            jacoco(execPattern: '**/target/jacoco.exec', classPattern: '**/target/classes', sourcePattern: '**/src/main/java', inclusionPattern: '**/*.class', exclusionPattern: '')
        }
    }
}