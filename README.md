# Bibliothekssoftware

#### Systemvoraussetzungen

- Java 8
- MariaDB 10.1

#### Installation

1. Mysql/Mariadb installieren
   * fedora: dnf install mysql-community-server
2. starten: systemctl start mariadb
3. Mysql vorbereiten: 
3.1. user erstellen:  create user 'user'@'localhost' identified by 'password';
3.2. database erstellen: create database bibliothek
3.3. user Rchte geben: grant all privileges on bibliothek.* to 'user';




#### Verwendete Tools und Libraries

- [Spark Web Framework](http://sparkjava.com/), Java
- [Velocity Template Engine](http://velocity.apache.org/engine/1.7/), Java
- [OrmLite](http://ormlite.com/), Java
- [jBCrypt](https://www.mindrot.org/projects/jBCrypt/), Java
- [Maven](https://maven.apache.org/), Java
- [MySQL](https://www.mysql.com/de/), SQL
- [Bulma](https://bulma.io/), CSS
- [jQuery](https://jquery.com/), JavaScript
