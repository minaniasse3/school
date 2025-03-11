pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "yacineniasse/school-app:latest"
        GIT_REPO_URL = "https://github.com/minaniasse3/school"
        REGISTRY_CREDENTIALS = "docker-credentials"
        SONARQUBE_URL = "http://sonarqube:9000"
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
                echo '⚙ Construction du projet avec Maven'
                sh 'chmod +x ./mvnw'
                sh './mvnw clean install'
            }
        }

        stage('Unit Tests') {
            steps {
                echo "🧪 Exécution des tests unitaires"
                sh 'chmod +x ./mvnw'
                sh './mvnw test'
            }
        }

        stage('Quality Check with SonarQube') {
            steps {
                echo "✅ Vérification de la qualité du code avec SonarQube"
                script {
                    sh 'chmod +x ./mvnw'
                    sh "./mvnw sonar:sonar -Dsonar.host.url=${SONARQUBE_URL}"
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

        stage('Docker Build & Push') {
            steps {
                echo "🐳 Construction et push de l'image Docker"
                script {
                    sh "docker build -t ${DOCKER_IMAGE} ."
                    sh "docker push ${DOCKER_IMAGE}"
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
            input {
                message "Voulez-vous déployer en production?"
                ok "Oui, déployer"
            }
            steps {
                echo "🚀 Déploiement sur l'environnement de Production"
                script {
                    sh 'kubectl apply -f kubernetes/deployment-prod.yaml'
                }
            }
        }

        stage('Monitor Metrics with Prometheus') {
            steps {
                echo "📊 Configuration de la surveillance avec Prometheus"
                script {
                    sh 'kubectl apply -f kubernetes/prometheus-deployment.yaml'
                }
            }
        }

        stage('Monitor Logs with ELK') {
            steps {
                echo "📄 Configuration de la collecte de logs avec ELK"
                script {
                    sh 'kubectl apply -f kubernetes/elk-deployment.yaml'
                }
            }
        }
    }

    post {
        always {
            echo "🧹 Nettoyage des ressources Docker et de l'espace de travail"
            script {
                try {
                    sh 'docker system prune -f'
                } catch (Exception e) {
                    echo "⚠ Docker non disponible pour le nettoyage: ${e.message}"
                }
                cleanWs()
            }
        }
        success {
            echo "✅ Pipeline exécuté avec succès"
        }
        failure {
            echo "❌ Échec du pipeline"
        }
    }
}
