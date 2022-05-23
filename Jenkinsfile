pipeline {
    agent any
    tools {
        maven "maven"
    }
    stages {
        stage ("Cloning the repo"){
            steps {
                git 'https://github.com/Marija999/document-management-software'
            }
        }
        stage ("Maven Build"){
            steps {
                sh 'mvn -f /var/lib/jenkins/workspace/logicaldoc/build/poms/pom.xml clean install'
            }
        }
        stage ("Test - executed"){
            steps {
                echo 'skip!'
            }
        }
        stage ('Build Docker image'){
            steps{
                script {
                    sh 'docker build  milicm/logical_doc .'
                }
            }
        }
        stage ('Push Docker image'){
            steps {
                script {
                    withCredentials([string(credentialsId: 'dockerhubpw', variable: 'dockerhubpwd')]) {
                        sh 'docker push -u milicm -p ${dockerhubpwd}'
                        sh 'docker push milicm/logical_doc:tagname'
                    }
                }
            }
        }
        
    }
}

