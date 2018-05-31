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

## Installation (Entwicklung)

1. MySQL/MariaDB Server muss vorhanden und die Datenbankstruktur angelegt/vorhanden sein.

2. Die Installation einiger Plugins in IntelliJ IDEA ist empfohlen:

    - [Pebble](https://plugins.jetbrains.com/plugin/9407-pebble) (Template Engine Support)
    - [CheckStyle-IDEA](https://plugins.jetbrains.com/plugin/1065-checkstyle-idea) Code Analyse
    
3. Das Projekt in IntelliJ IDEA öffnen

4. Die fehlenden Model Klassen mithilfe von Maven generieren lassen:

    - `mvn compile` ausführen oder im Maven Tab in IntelliJ ausführen lassen

5. Die generierten Klassen befinden sich im Ordner `target/generated-sources` und sollten von IntelliJ automatisch erkannt werden

6. Konfigurationsdatei kopieren und anpassen:

    - `cp conf/application-example.conf conf/application.conf`

7. Applikation im Entwicklungsmodus starten, dies startet die App bei jeder Veränderung im Code automatisch neu:

    - `mvn jooby:run`    

## Installation (Produktion)

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

    - Release .zip Datei herunterladen (`swt-library-x.x.x.stork.zip`)
    - Jene .zip Datei entpacken
    - Konfigurationsdatei kopieren: `cp conf/application-example.conf conf/application.conf`
    - Die kopierte `application.conf` anpassen
    - Applikation starten mit `bin/swt-library{.bat} --run`

### Mit Docker/Docker Compose

**Noch nicht wieder funktionsfähig!**

1. Sicherstellen, dass [Docker](https://www.docker.com/) und [Docker Compose](https://docs.docker.com/compose/) installiert sind:

    - `docker -v`, `docker-compose -v`

1. Repository klonen:

    - `cd {repository}`
    - Folgende Instruktionen gehen davon aus, dass man sich im Verzeichnis des geklonten Repos befindet!

1. Benötigte Verzeichnisse erstellen:

    - `mkdir -p data/bibliothek/`

1. Konfigurationsdatei für Docker kopieren:

    - `cp conf/application-docker.yaml data/bibliothek/application.conf`

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

#### Java:

- [Maven](https://maven.apache.org/)
- [jooby](https://jooby.org/)
- [Pebble Template Engine](http://www.mitchellbosecke.com/pebble/home)
- [requery](https://github.com/requery/requery)
- [jBCrypt](https://www.mindrot.org/projects/jBCrypt/)

#### SQL:

- [MySQL](https://www.mysql.com/de/) / [MariaDB](https://mariadb.org/)

#### JavaScript:

- [jQuery](https://jquery.com/)
- [SweetAlert](https://sweetalert.js.org/)

#### CSS:

- [Bulma](https://bulma.io/)

## Testkonzept

- Unit Testing: [HtmlUnit](http://htmlunit.sourceforge.net/)
- Integration Testing: Auch [HtmlUnit](http://htmlunit.sourceforge.net/)
- Geplante Testabdeckung: Mindestens 85%
