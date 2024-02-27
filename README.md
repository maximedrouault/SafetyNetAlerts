# SafetyNet - Alerts

## Introduction
SafetyNetAlerts est une application Spring Boot conçue pour fournir des informations essentielles aux services d'urgence, permettant de gérer et d'accéder rapidement aux données des résidents, des casernes de pompiers, et des dossiers médicaux en cas d'urgence.

## Configuration Requise
- Java 17
- Maven pour la gestion des dépendances et l'exécution du projet
- Spring Boot version 3.2.2

## Installation et démarrage
**Clonez le dépôt et exécutez l'application avec Maven :**

`git clone https://github.com/maximedrouault/SafetyNetAlerts.git`

**Pour démarrer l'API :**

`mvn spring-boot:run`


## Fonctionnalités Principales
- **Gestion des casernes de pompiers (FireStations)** : Ajout, mise à jour, suppression, et récupération de la liste complète des casernes de pompiers.
- **Gestion des dossiers médicaux (MedicalRecords)** : Ajout, mise à jour, suppression, et récupération de la liste complète des dossiers médicaux.
- **Gestion des personnes (Persons)** : Ajout, mise à jour, suppression, et récupération de la liste complète des informations des résidents.

### Requêtes Spécifiques
- **ChildAlert** : Liste des enfants résidant à une adresse spécifiée, avec les autres membres du foyer.
- **FireAddressInfo** : Informations sur les résidents d'une adresse en cas d'incendie, incluant le numéro de la caserne de pompiers la desservant.
- **FireStationCoverage** : Informations sur les résidents couverts par une caserne de pompiers spécifique, avec comptage des adultes et des enfants.
- **PhoneAlert** : Numéros de téléphone des résidents couverts par une caserne de pompiers spécifique.
- **CommunityEmail** : Emails des résidents d'une ville spécifique.
- **PersonInfo** : Informations détaillées sur une personne spécifique, incluant les antécédents médicaux.
- **FloodStationCoverage** : Informations sur les résidents couverts par plusieurs casernes en cas d'inondation.

## Gestion des Données
- **Fichier de données `src/main/resources/data.json`** : Contient les données actuelles utilisées par l'application. Ce fichier est modifié par l'application pour mettre à jour les informations.
- **Données d'origine `src/main/resources/originalData.json`** : Ce fichier contient les données d'origine qui ne sont pas touchées et peuvent être restaurées dans `data.json` au besoin.

## Test de l'API avec Postman
Une collection Postman est disponible dans le répertoire `src/test/resources/postman_collection.json`. Cette collection peut être importée dans Postman pour tester facilement les différentes routes de l'API.

## API et Documentation
Vous pouvez accéder à la documentation OpenAPI / Swagger de l'API via http://localhost:8080/swagger-ui/index.html après le démarrage de l'application.

## Tests
Le projet SafetyNetAlerts inclut des tests unitaires et d'intégrations pour assurer la qualité et la fiabilité du code.

### Exécution des Tests Unitaires
Pour exécuter les tests unitaires, utilisez la commande suivante :
`mvn clean test`

### Exécution des Tests Unitaires et Intégrations
Pour exécuter les tests unitaires et intégrations, utilisez la commande suivante :
`mvn clean verify`

### Rapports de Tests
#### Rapport de couverture de code par les tests
À la suite de l'une ou l'autre des précédentes commande, vous pouvez consulter le rapport de couverture JaCoCo dans : `target/site/jacoco/index.html`

#### Rapport de résultat d'exécution des tests
À la suite de l'une ou l'autre des précédentes commande, vous pouvez aussi lancer la commande `mvn site`.
Cela vous donnera accès au rapport Maven Surefire dans `target/site/index.html`


