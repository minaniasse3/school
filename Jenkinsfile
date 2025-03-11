pipeline {
    agent any

    environment {
        DOCKER_REGISTRY = "docker.io"
        DOCKER_IMAGE_NAME = "school-app"
        DOCKER_IMAGE = "${DOCKER_REGISTRY}/yacineniasse/${DOCKER_IMAGE_NAME}:latest"
        GIT_REPO_URL = "https://github.com/minaniasse03/School"
        REGISTRY_CREDENTIALS = "docker-credentials"
        SONARQUBE = "sonarqube-server"
    }

    stages {
        stage('Checkout') {
            steps {
                echo "🔄 Récupération du code source depuis ${GIT_REPO_URL}"
                git branch: 'main', url: "${GIT_REPO_URL}"
            }
        }

        stage('Build') {
            steps {
                echo "⚙️ Construction du projet avec Maven"
                sh './mvnw clean install'
            }
        }

        stage('Unit Tests') {
            steps {
                echo "🧪 Exécution des tests unitaires"
                sh './mvnw test'
            }
        }

        stage('Quality Check with SonarQube') {
            steps {
                echo "✅ Vérification de la qualité du code avec SonarQube"
                script {
                    sh "mvn sonar:sonar -Dsonar.host.url=${SONARQUBE}"
                }
            }
        }

        stage('Docker Login') {
            steps {
                echo "🔐 Connexion au registre Docker"
                script {
                    withCredentials([usernamePassword(credentialsId: REGISTRY_CREDENTIALS, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                    }
                }
            }
        }

        stage('Deploy to Dev') {
            steps {
                echo "🚀 Déploiement sur l'environnement Dev"
                script {
                    sh "docker pull ${DOCKER_IMAGE}"
                    sh "docker-compose up -d"
                    sh "docker ps | grep school-app || echo '❌ Erreur : L'application ne tourne pas'"
                }
            }
        }

        stage('Deploy to Staging') {
            steps {
                echo "🚀 Déploiement sur l'environnement Staging"
                script {
                    sh 'kubectl apply -f kubernetes/deployment-staging.yaml'
                }
            }
        }

        stage('Deploy to Prod') {
            steps {
                echo "🚀 Déploiement sur l'environnement de Production"
                script {
                    sh 'kubectl apply -f kubernetes/deployment-prod.yaml'
                }
            }
        }

        stage('Monitor Metrics with Prometheus') {
            steps {
                echo "📊 Déploiement de Prometheus pour la surveillance"
                script {
                    sh 'kubectl apply -f kubernetes/prometheus-deployment.yaml'
                }
            }
        }

        stage('Monitor Logs with ELK') {
            steps {
                echo "📄 Déploiement de l'ELK Stack pour les logs"
                script {
                    sh 'kubectl apply -f kubernetes/elk-deployment.yaml'
                }
            }
        }
    }

    post {
        always {
            echo "🧹 Nettoyage des ressources Docker et de l'espace de travail"
            sh 'docker system prune -f'
            cleanWs()
        }
    }
}
