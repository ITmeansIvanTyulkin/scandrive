pipeline {
    parameters {
        string(name: 'project_version', defaultValue: '')
    }
    tools {
        maven 'maven_3.9.1'
    }
    agent any
    stages {
        stage('Show maven info') {
            steps {
                sh 'mvn -version'
            }
        }

        stage('Clean workspace') {
            steps {
                cleanWs()
                // We need to explicitly checkout from SCM here
                checkout scm
            }
        }
        stage('Get version') {
            steps {
                script {
                    def props = readMavenPom file: 'pom.xml'
                    env.project_version = sh script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true
                }
            }
        }
        stage('Change build name') {
            steps {
                script {
                    currentBuild.displayName = "${BRANCH}-${env.project_version}"
                }
            }
        }
//        stage('Build') {
//            steps {
//                sh 'mvn clean package -DskipTests'
//            }
//        }

        stage('Build Docker image') {
            steps {
                script {
                    def rand = UUID.randomUUID().toString().replaceAll("-", "").take(6)
                    def imageTag = "cr.selcloud.ru/core-registry/core-autotest:${env.project_version}-${rand}"
                    env.IMAGE_TAG = imageTag  // сохраняем, чтобы использовать в следующем шаге
                    sh """
                            docker build \
                            -t ${imageTag} .
                        """
                }
            }
        }

        stage('Push Docker image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'selectel-docker-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh 'echo $DOCKER_PASS | docker login cr.selcloud.ru -u $DOCKER_USER --password-stdin'
                    }
                    sh "docker push ${env.IMAGE_TAG}"
                }
            }
        }


        stage('Write image tag to artifact') {
            steps {
                script {

                    def fileName = "docker-image-tag-${env.IMAGE_TAG}.txt"
                    writeFile file: fileName, text: "${env.IMAGE_TAG}\n"
                    archiveArtifacts artifacts: fileName, fingerprint: true
                }
            }
        }
    }


}