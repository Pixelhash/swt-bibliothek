# Bibliothekssoftware

#### Systemvoraussetzungen

- Java 8
- MariaDB 10.1

#### Installation

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

#### Passwort für Nutzer erstellen

- In das Verzeichnis der kompilierten JAR-Datei wechseln
- Ausführen mit folgendem Befehl: `java -jar {datei}.jar -p`
- Das Passwort eingeben (ist in der Konsole nicht sichtbar) und ENTER drücken
- Der generierte Hash zum Passwort wird angezeigt

#### Verwendete Tools und Libraries

- [Spark Web Framework](http://sparkjava.com/), Java
- [Velocity Template Engine](http://velocity.apache.org/engine/1.7/), Java
- [OrmLite](http://ormlite.com/), Java
- [jBCrypt](https://www.mindrot.org/projects/jBCrypt/), Java
- [Maven](https://maven.apache.org/), Java
- [MySQL](https://www.mysql.com/de/), SQL
- [Bulma](https://bulma.io/), CSS
- [jQuery](https://jquery.com/), JavaScript
