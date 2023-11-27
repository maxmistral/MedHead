# On utilise une image de base officielle Java
FROM openjdk:17-jdk-alpine

# Informations sur l'auteur (optionnel)
LABEL maintainer="echodream-studio@gmail.com"

# Ob indique le dossier de travail dans le conteneur Docker
WORKDIR /app

# On copie le jar dans le conteneur Docker
COPY ./Backend/build/libs/medhead-0.0.1-SNAPSHOT.jar app.jar

# On expose le port sur lequel votre application s'exécute
EXPOSE 8080

# Commande à exécuter lors de l'exécution de l'image Docker
ENTRYPOINT ["java","-jar","/app/app.jar"]
