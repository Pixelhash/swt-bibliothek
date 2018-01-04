# Bibliothekssoftware

- [Beschreibung](#beschreibung)
- [Demo](#demo)
- [Systemvoraussetzungen](#systemvoraussetzungen)
- [Installation](#installation)
- [Passwort für einen Nutzer erstellen](#passwort-für-nutzer-erstellen)
- [Verwendete Tools und Libraries](#verwendete-tools-und-libraries)

## Beschreibung

Diese Applikation wurde im Studiengang Informatik/Softwaretechnik
im Fach Softwaretchnik 1 im WS 2017/18 der FH-Lübeck entwickelt.

## Demo
- URL: https://swt-bibliothek.codehat.de/
- Nutzername: `swt`
- Passwort: `bibliothek`

## Systemvoraussetzungen

- Java 8
- MariaDB 10.1

## Installation

Die Installation kann entweder manuell oder mit Docker/Docker Compose durchgeführt werden.

### Manuell

1. MySQL/MariaDB installieren:
    - Fedora: `dnf install mysql-community-server`
    - Ubuntu: `sudo apt install mariadb-server-10.1`

2. MySQL/MariaDB Server starten: `systemctl start mariadb`

3. MySQL/MariaDB vorbereiten:
    - User erstellen: `create user 'user'@'localhost' identified by 'password';`
    - Datenbank erstellen: `create database bibliothek;`
    - Dem User Rechte geben: `grant all privileges on bibliothek.* to 'user'@'localhost';`

4. Tabellen erstellen (+ Demo-Daten)
    - Ohne Demo-Daten: `table-struct.sql`
    - Mit Demo-Daten: `table-struct-demo.sql`. Das Passwort für alle Benutzer lautet `Test1234`.

5. Applikation vorbereiten und starten:
    - Konfigurationsdatei kopieren: `cp application-example.yaml application.yaml`
    - Die kopierte `application.yaml` anpassen
    - Applikation starten mit `java -jar {datei}.jar`

### Mit Docker/Docker Compose

1. Sicherstellen, dass [Docker](https://www.docker.com/) und [Docker Compose](https://docs.docker.com/compose/) installiert sind:
    - `docker -v`, `docker-compose -v`

2. Repository klonen:
    - `cd {repository}`
    - Folgende Instruktionen gehen davon aus, dass man sich im Verzeichnis
des geklonten Repos befindet!

3. Benötigte Verzeichnisse erstellen:
    - `mkdir -p data/bibliothek/`

4. Konfigurationsdatei für Docker kopieren:
    - `cp application-docker.yaml data/bibliothek/application.yaml`

5. Einstellungen abgleichen:
    - Die Einstellungen von der Applikation und MariaDB müssen in der
    `data/bibliothek/application.yaml` und in der `docker-compose.yml` übereinstimmen!
    
6. MariaDB mit (Standard) oder ohne Demo-Daten:
    - Falls die Demo-Daten eingefügt werden sollen, kann
    dieser Schritt übersprungen werden!
    - In der `docker-compose.yml` folgendes finden und ändern:
    - Mit (Standard): `./table-struct-demo.sql`
    - Ohne: `./table-struct.sql`

7. Applikation und MariaDB starten:
    - `docker-compose up -d`
    - Dauert beim ersten Start länger, weil das Image der Applikation
    erstellt wird.
    
8. Prüfen, ob alles funktioniert:
    - `docker-compose logs` sollte ungefähr folgende Zeile beinhalten:
    - `bibliothek  | [Thread-1] INFO org.eclipse.jetty.server.Server - Started @684ms`
    - Browser öffnen unter `localhost:4567`
    
9. (Optional) Applikation und MariaDB stoppen:
    - Ins Verzeichnis des Repos wechseln
    - `docker-compose down` ausführen

## Passwort für Nutzer erstellen

- In das Verzeichnis der kompilierten JAR-Datei wechseln
- Ausführen mit folgendem Befehl: `java -jar {datei}.jar -p`
- Das Passwort eingeben (ist in der Konsole nicht sichtbar) und ENTER drücken
- Der generierte Hash zum Passwort wird angezeigt

## Verwendete Tools und Libraries

- [Spark Web Framework](http://sparkjava.com/), Java
- [Velocity Template Engine](http://velocity.apache.org/engine/1.7/), Java
- [OrmLite](http://ormlite.com/), Java
- [jBCrypt](https://www.mindrot.org/projects/jBCrypt/), Java
- [cfg4j](http://www.cfg4j.org/), Java
- [minimal-json](https://github.com/ralfstx/minimal-json), Java
- [jcabi-manifests](http://manifests.jcabi.com/), Java
- [Bugsnag](https://www.bugsnag.com/), Java
- [Maven](https://maven.apache.org/), Java
- [MySQL](https://www.mysql.com/de/), SQL
- [Bulma](https://bulma.io/), CSS
- [jQuery](https://jquery.com/), JavaScript
- [SweetAlert](https://sweetalert.js.org/), JavaScript
