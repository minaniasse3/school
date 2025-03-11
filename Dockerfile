# Utilisation d'une image plus légère pour optimiser l'espace
FROM eclipse-temurin:21-jre-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR dans le conteneur
COPY ./target/School-0.0.1-SNAPSHOT.jar /app/School-0.0.1-SNAPSHOT.jar

# Donner les permissions d'exécution
RUN chmod +x /app/School-0.0.1-SNAPSHOT.jar

# Exposer le port sur lequel l'application fonctionne
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java", "-jar", "/app/School-0.0.1-SNAPSHOT.jar"]
