# Documentation Backend - Hellenic Rides


## Base de données

Ce projet utilise une base de données PostgreSQL conteneurisée avec Docker.

### Prérequis

*   [Docker](https://www.docker.com/products/docker-desktop) et Docker Compose doivent être installés sur votre machine.

### Configuration

Les variables d'environnement de la base de données sont définies dans le fichier `.env` à la racine du projet :

```env
POSTGRES_USER=admin
POSTGRES_PASSWORD=password123
POSTGRES_DB=ynov_db
POSTGRES_PORT=5432
```

### Lancer la base de données

Pour démarrer le conteneur de la base de données en arrière-plan :

```bash
docker-compose up -d
```

### Arrêter la base de données

Pour arrêter et supprimer le conteneur :

```bash
docker-compose down
```

### Vérifier le statut

Pour voir si le conteneur tourne correctement :

```bash
docker ps
```
