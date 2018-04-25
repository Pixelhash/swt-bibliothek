# Bibliothekssoftware

- [Beschreibung](#beschreibung)
- [Demo](#demo)
- [Systemvoraussetzungen](#systemvoraussetzungen)
- [Installation](#installation)
- [Passwort für einen Nutzer erstellen](#passwort-für-nutzer-erstellen)
- [Verwendete Tools und Libraries](#verwendete-tools-und-libraries)
- [Testkonzept](#Testkonzept)
- [Zukünftige Features](#zukünftige-features)

## Beschreibung

Diese Applikation wurde im Studiengang Informatik/Softwaretechnik
im Fach Softwaretchnik 2 im SS 2018 der FH-Lübeck entwickelt.

## Demo (inaktiv)

- URL: <https://swt-bibliothek.codehat.de/>
- Nutzername: `swt`
- Passwort: `bibliothek`

## Systemvoraussetzungen

- Java 8
- MariaDB 10.1

## Installation

Die Installation kann entweder manuell oder mit Docker/Docker Compose durchgeführt werden.

### Manuell

1. MySQL/MariaDB installieren:

    - Fedora: `dnf install mariadb-server`
    - Ubuntu: `sudo apt install mariadb-server-10.1`

1. MySQL/MariaDB Server starten:

    - `systemctl start mariadb`

1. MySQL/MariaDB vorbereiten:

    - User erstellen:

        ```sql
        CREATE USER 'user'@'localhost' IDENTIFIED BY 'password';
        ```

    - Datenbank erstellen:

        ```sql
        CREATE DATABASE bibliothek;
        ```

    - Dem User Rechte geben:

        ```sql
        GRANT all PRIVILEGES ON bibliothek.* TO 'user'@'localhost';
        ```

1. Tabellen erstellen (+ Demo-Daten):

    - Ohne Demo-Daten: `table-struct.sql`
    - Mit Demo-Daten: `table-struct-demo.sql`. Das Passwort für alle Benutzer lautet `Test1234`.

1. Applikation vorbereiten und starten:

    - Konfigurationsdatei kopieren: `cp application-example.yaml application.yaml`
    - Die kopierte `application.yaml` anpassen
    - Applikation starten mit `java -jar target/dist/Bibliothek.jar`

### Mit Docker/Docker Compose

1. Sicherstellen, dass [Docker](https://www.docker.com/) und [Docker Compose](https://docs.docker.com/compose/) installiert sind:

    - `docker -v`, `docker-compose -v`

1. Repository klonen:

    - `cd {repository}`
    - Folgende Instruktionen gehen davon aus, dass man sich im Verzeichnis des geklonten Repos befindet!

1. Benötigte Verzeichnisse erstellen:

    - `mkdir -p data/bibliothek/`

1. Konfigurationsdatei für Docker kopieren:

    - `cp application-docker.yaml data/bibliothek/application.yaml`

1. Einstellungen abgleichen:

    - Die Einstellungen von der Applikation und MariaDB müssen in der `data/bibliothek/application.yaml` und in der `docker-compose.yml` übereinstimmen!

1. MariaDB mit (Standard) oder ohne Demo-Daten:

    - Falls die Demo-Daten eingefügt werden sollen, kann dieser Schritt übersprungen werden!
    - In der `docker-compose.yml` folgendes finden und ändern:
    - Mit (Standard): `./table-struct-demo.sql`
    - Ohne: `./table-struct.sql`

1. Applikation und MariaDB starten:

    - `docker-compose up -d`
    - Dauert beim ersten Start länger, weil das Image der Applikation erstellt wird.

1. Prüfen, ob alles funktioniert:

    - `docker-compose logs` sollte ungefähr folgende Zeile beinhalten:
    - `bibliothek  | [Thread-1] INFO org.eclipse.jetty.server.Server - Started @684ms`
    - Browser öffnen unter `localhost:4567`

1. (Optional) Applikation und MariaDB stoppen:

    - Ins Verzeichnis des Repos wechseln
    - `docker-compose down` ausführen

## Passwort für Nutzer erstellen

- In das Verzeichnis der kompilierten JAR-Datei wechseln
- Ausführen mit folgendem Befehl: `java -jar {datei}.jar -p`
- Das Passwort eingeben (ist in der Konsole nicht sichtbar) und ENTER drücken
- Der generierte Hash zum Passwort wird angezeigt

## Verwendete Tools und Libraries

####Java:

- [Maven](https://maven.apache.org/)
- [Spark Web Framework](http://sparkjava.com/)
- [Velocity Template Engine](http://velocity.apache.org/engine/1.7/)
- [OrmLite](http://ormlite.com/)
- [jBCrypt](https://www.mindrot.org/projects/jBCrypt/)
- [cfg4j](http://www.cfg4j.org/)
- [minimal-json](https://github.com/ralfstx/minimal-json)

####SQL:

- [MySQL](https://www.mysql.com/de/) / [MariaDB](https://mariadb.org/)

####JavaScript:

- [jQuery](https://jquery.com/)
- [SweetAlert](https://sweetalert.js.org/)

####CSS:

- [Bulma](https://bulma.io/)

## Testkonzept

- Unit Testing: jUnit 5
- Integration Testing: Selenium (bzw. eines der Derivate, z.B. [Katalon Studio](https://github.com/Pixelhash/swt-bibliothek.git))
- Geplante Testabdeckung: Mindestens 85%

## Zukünftige Features

- Anlegen von Kunden
- Anlegen von Büchern
- Filter für die Buchsuche
- Anlegen eines Bereichs zum Stöbern in Kategorien
- Automatische Erinnerung von Kunden bei nahendem Abgabedatum
- Funktion um das Passwort zurückzusetzen
- Möglichkeit für den Kunden seine Daten zu ändern
- Reservierung von Büchern ermöglichen
- Automatisierte Registrierung von Kunden
