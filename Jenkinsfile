pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                echo 'Checkout'
            }
        }

        stage('Build') {
            steps {
                echo 'Clean Build'
                sh 'mvn clean compile -P dev'
            }
        }

        stage('Test') {
            steps {
                echo 'Testing'
                sh 'mvn test'
            }
        }


        stage('Package') {
            steps {
                echo 'Packaging'
                sh 'mvn clean package -P dev'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Build Docker Image'
                sh 'docker build --no-cache -t leon4uk/botmasterzzz-individual:1.0.0 .'
            }
        }

        stage('Push Docker image') {
            steps {
                echo 'Push Docker image'
                withCredentials([string(credentialsId: 'dockerHubPwd', variable: 'dockerHubPwd')]) {
                    sh "docker login -u leon4uk -p ${dockerHubPwd}"
                }
                sh 'docker push leon4uk/botmasterzzz-individual:1.0.0'
                sh 'docker rmi leon4uk/botmasterzzz-individual:1.0.0'
            }
        }

        stage('Deploy') {
            steps {
                echo '## Deploy locally ##'
                withCredentials([string(credentialsId: 'dockerHubPwd', variable: 'dockerHubPwd')]) {
                    sh "docker login -u leon4uk -p ${dockerHubPwd}"
                }
                sh "docker container ls -a -f name=botmasterzzz-individual -q | xargs --no-run-if-empty docker container stop"
                sh 'docker container ls -a -f name=botmasterzzz-individual -q | xargs -r docker container rm'
                sh 'docker run -v /etc/localtime:/etc/localtime --name botmasterzzz-individual -d --net=botmasterzzznetwork -p 127.0.0.1:7000:7000 --restart always leon4uk/botmasterzzz-individual:1.0.0'
            }
        }

    }
}