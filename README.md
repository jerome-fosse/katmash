# Katmash
Katmash est une implementation du kata catmash écrite en kotlin. Il s'agit d'une application permettant d'élire le plus beau chat.

## Prérequis
Il est nécessaire d'avoir un jdk 8 ou plus installé. Katmash a été testé avec succes avec un jdk 8 et un jdk 12.
<br>Docker est obligatoire pour pouvoir executer certains tests. Docker Compose est recommandé pour faire tourner localement la base de données.

## Stack technique
* langage : kotlin 1.3
* Frameworks : Spring Boot 2.2.0.M3 avec R2DBC
* Base de données : PostgreSQL 11.5

## Build du projet
A la racine, faire :
```$xslt
mvn clean install
```

## Lancement du projet
Un fichier docker-compose est présent afin de lancer la base de données. Pour créer le container, faites :
```$xslt
docker-compose up -d
```
puis pour démarrer le container, faites :
```$xslt
docker-compose start
```
Enfin, pour démarrer l'application, depuis la racine du projet, faites :
```$xslt
java -jar target/katmash-1.0.0-SNAPSHOT.jar
```
Avec votre navigateur préféré, allez à l'adresse http://localhost:8080/
