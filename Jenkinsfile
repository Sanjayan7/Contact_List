pipeline {
    agent any

    environment {
        // Adjust these if your Jenkins environment uses different names for tools
        MAVEN_HOME = tool 'Maven'
        JAVA_HOME = tool 'JDK17'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Sanjayan7/Contact_List.git'
            }
        }

        stage('Build') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn clean compile"
            }
        }

        stage('Test') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn test"
            }
        }

        stage('Package') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn package"
            }
        }
    }

    post {
        success {
            echo 'Build Successful!'
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
        failure {
            echo 'Build Failed!'
        }
        always {
            // Record test results if they exist
            // junit 'target/surefire-reports/*.xml'
        }
    }
}
