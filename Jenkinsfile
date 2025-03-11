pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "yacineniasse/school-app:latest"
        GIT_REPO_URL = "https://github.com/minaniasse3/school"
        REGISTRY_CREDENTIALS = "docker-credentials"
        SONARQUBE_URL = "http://sonarqube:9000"
    }

    stages {
        // Étape 1 : Récupération du code source
        stage('Checkout') {
            steps {
                echo "🔄 Récupération du code source depuis ${GIT_REPO_URL}"
                git branch: 'main', url: "${GIT_REPO_URL}"
            }
        }

        // Étape 2 : Construction du projet avec Maven
        stage('Build') {
            steps {
                echo '⚙ Construction du projet avec Maven'
                sh 'chmod +x ./mvnw'
                sh './mvnw clean install'
            }
        }

        // Étape 3 : Exécution des tests unitaires
        stage('Unit Tests') {
            steps {
                echo "🧪 Exécution des tests unitaires"
                sh 'chmod +x ./mvnw'
                sh './mvnw test'
            }
        }

        // Étape 4 : Vérification de la qualité du code avec SonarQube
        stage('Quality Check with SonarQube') {
            steps {
                echo "✅ Vérification de la qualité du code avec SonarQube"
                script {
                    sh 'chmod +x ./mvnw'
                    sh "./mvnw sonar:sonar -Dsonar.host.url=${SONARQUBE_URL}"
                }
            }
        }

        // Étape 5 : Connexion au registre Docker
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

        // Étape 6 : Construction et push de l'image Docker
        stage('Docker Build & Push') {
            steps {
                echo "🐳 Construction et push de l'image Docker"
                script {
                    sh "docker build -t ${DOCKER_IMAGE} ."
                    sh "docker push ${DOCKER_IMAGE}"
                }
            }
        }

        // Étape 7 : Déploiement sur l'environnement Dev
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

        // Étape 8 : Déploiement sur l'environnement Staging
        stage('Deploy to Staging') {
            steps {
                echo "🚀 Déploiement sur l'environnement Staging"
                script {
                    sh 'kubectl apply -f kubernetes/deployment-staging.yaml'
                }
            }
        }

        // Étape 9 : Déploiement sur l'environnement de Production (avec confirmation manuelle)
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

        // Étape 10 : Surveillance des métriques avec Prometheus
        stage('Monitor Metrics with Prometheus') {
            steps {
                echo "📊 Configuration de la surveillance avec Prometheus"
                script {
                    sh 'kubectl apply -f kubernetes/prometheus-deployment.yaml'
                }
            }
        }

        // Étape 11 : Collecte de logs avec ELK
        stage('Monitor Logs with ELK') {
            steps {
                echo "📄 Configuration de la collecte de logs avec ELK"
                script {
                    sh 'kubectl apply -f kubernetes/elk-deployment.yaml'
                }
            }
        }
    }

    // Actions post-build
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
