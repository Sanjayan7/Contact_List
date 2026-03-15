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
                // Jenkins usually handles the checkout automatically if configured via "Pipeline from SCM"
                // But including it explicitly for clarity or manual trigger
                checkout scm
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
