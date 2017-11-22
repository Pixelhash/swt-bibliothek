# Bibliothekssoftware

#### Systemvoraussetzungen

- Java 8
- MariaDB 10.1

#### Installation

1. MySQL/MariaDB installieren
   - Fedora: `dnf install mysql-community-server`
   - Ubuntu: `sudo apt install mariadb-server-10.1`
2. MySQL/MariaDB Server starten: `systemctl start mariadb`
3. MySQL/MariaDB vorbereiten:
   3.1. User erstellen: `create user 'user'@'localhost' identified by 'password';`
   3.2. Datenbank erstellen: `create database bibliothek`
   3.3. Dem User Rechte geben: `grant all privileges on bibliothek.* to 'user';`

#### Verwendete Tools und Libraries

- [Spark Web Framework](http://sparkjava.com/), Java
- [Velocity Template Engine](http://velocity.apache.org/engine/1.7/), Java
- [OrmLite](http://ormlite.com/), Java
- [jBCrypt](https://www.mindrot.org/projects/jBCrypt/), Java
- [Maven](https://maven.apache.org/), Java
- [MySQL](https://www.mysql.com/de/), SQL
- [Bulma](https://bulma.io/), CSS
- [jQuery](https://jquery.com/), JavaScript
