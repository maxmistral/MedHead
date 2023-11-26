# Repository MedHead - Système d'Intervention d'Urgence
![image](https://github.com/maxmistral/MedHead/assets/15127550/144ea495-6b81-4189-8c18-e7076d1c6795)

## Contexte du Projet

MedHead est un regroupement de grandes institutions médicales œuvrant au sein du système de santé britannique. Les organisations membres du consortium utilisent actuellement une grande variété de technologies et d'appareils. Ils souhaitent une nouvelle plateforme pour unifier leurs pratiques et réduire les risques liés au traitement des recommandations de lits d'hôpitaux dans des situations d'intervention d'urgence.

Ce projet vise à développer une preuve de concept (PoC) pour une solution d'intervention d'urgence basée sur Java, afin de convaincre le comité d'architecture de MedHead de l'efficacité de la solution proposée.

## 📂 Structure du Répertoire

Ce repository est organisé en plusieurs parties :

### Backend
- **Répertoire Backend** : Contient le backend du projet, développé en Java Spring.

### Frontend
- **Répertoire Front** : Contient le frontend principal du projet, développé en HTML/CSS/JS et communiquant avec le backend.

### CSharp Version
- **Répertoire CSharp Version** : Contient une vieille version alternative du backend du projet, développée en C# ASP.NET, ainsi qu'un début de frontend associé, développé en Blazor.

### CI/CD
- **Répertoire .github/workflows** : Contient le workflow Gradle utilisé pour l'intégration continue de l'API.

## Exigences de la PoC (/PoC)

Les exigences clés pour la PoC sont détaillées dans un document séparé, disponible dans le répertoire d'architecture.

---

## 🚀 Configuration pour le Développement Local

### Prérequis

Il est nécessaire de s'assurer que les dépendances suivantes soient installées :

- [Java JDK 17](https://adoptium.net/fr/temurin/archive/?version=17)
- [Node.js](https://nodejs.org/en/download) et npm (conçu et testé uniquement avec Node.js 18.16.0)

### 🌐 Configuration du Backend

1. **Clonez le Repository** : Clonez le repository MedHead sur votre machine.
2. **Accédez au Répertoire Backend** : Ouvrez un terminal et naviguez dans le répertoire `Backend`.
4. **Construisez et Exécutez** : Lancez l'application avec `./gradlew bootRun`. Alternativement, vous pouvez build l'application (`./gradlew build`) et lancer le fichier medhead-0.0.1-SNAPSHOT.jar dans le répertoire `build/libs` avec la commande `java -jar medhead-0.0.1-SNAPSHOT.jar`.

### 🎨 Configuration du Frontend

1. **Accédez au Répertoire Front** : Ouvrez un autre terminal et naviguez vers `Front`.
2. **Installez les Dépendances** : Exécutez `npm install`.
3. **Démarrez le Serveur Frontend** : Utilisez `npm run serve -- --port 8081`. Attention, il est important de lancer le front sur le port 8081. En effet, le cross-origin a été autorisé uniquement sur ce port dans le Backend.

### 🖥️ Tester l'Application

- Ouvrez `http://localhost:8081` dans un navigateur pour accéder à l'interface utilisateur.
- Il est possible de consulter la documentation de l'API en accédant à l'URL de la documentation Swagger (`http://localhost:8081/swagger-ui/index.html`).

### Autres

- Des images Docker pour le front et le back sont accessible à la racine de ce repos sous les nom respectifs de `Dockerfile.backend` et `Dockerfile.frontent`

Application développée par M.B. dans le cadre du projet 11 MedHead d'OpenClassrooms.
---
