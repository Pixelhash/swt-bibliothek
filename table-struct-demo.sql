-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema bibliothekssoftware
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema bibliothekssoftware
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bibliothekssoftware` DEFAULT CHARACTER SET utf8 ;
USE `bibliothekssoftware` ;

-- -----------------------------------------------------
-- Table `bibliothekssoftware`.`kategorie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliothekssoftware`.`kategorie` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bibliothekssoftware`.`autor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliothekssoftware`.`autor` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bibliothekssoftware`.`verlag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliothekssoftware`.`verlag` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `ort` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bibliothekssoftware`.`adresse`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliothekssoftware`.`adresse` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `strasse` VARCHAR(100) NOT NULL,
  `hausnummer` VARCHAR(8) NOT NULL,
  `ort` VARCHAR(100) NOT NULL,
  `plz` CHAR(5) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bibliothekssoftware`.`buch`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliothekssoftware`.`buch` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `titel` VARCHAR(150) NOT NULL,
  `isbn` VARCHAR(13) NULL,
  `erscheinungsjahr` SMALLINT(4) NULL,
  `standort` VARCHAR(45) NOT NULL,
  `kategorie_id` INT UNSIGNED NOT NULL,
  `verlag_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_buch_kategorie_idx` (`kategorie_id` ASC),
  INDEX `fk_buch_verlag_idx` (`verlag_id` ASC),
  CONSTRAINT `fk_buch_kategorie`
    FOREIGN KEY (`kategorie_id`)
    REFERENCES `bibliothekssoftware`.`kategorie` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_buch_verlag`
    FOREIGN KEY (`verlag_id`)
    REFERENCES `bibliothekssoftware`.`verlag` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bibliothekssoftware`.`benutzer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliothekssoftware`.`benutzer` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `vorname` VARCHAR(30) NOT NULL,
  `nachname` VARCHAR(30) NOT NULL,
  `rolle` ENUM('KUNDE', 'MITARBEITER') NOT NULL,
  `telefonnummer` VARCHAR(20) NULL,
  `email` VARCHAR(100) NOT NULL,
  `geburtsdatum` DATE NOT NULL,
  `passwort` CHAR(60) NOT NULL,
  `activation_token` CHAR(64) NULL,
  `adresse_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_benutzer_adresse1_idx` (`adresse_id` ASC),
  CONSTRAINT `fk_benutzer_adresse`
    FOREIGN KEY (`adresse_id`)
    REFERENCES `bibliothekssoftware`.`adresse` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bibliothekssoftware`.`buchexemplar`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliothekssoftware`.`buchexemplar` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ausleihdatum` DATETIME NULL,
  `rueckgabedatum` DATETIME NULL,
  `buch_id` INT UNSIGNED NOT NULL,
  `benutzer_id` INT UNSIGNED NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_ausleihe_buch1_idx` (`buch_id` ASC),
  INDEX `fk_ausleihe_benutzer1_idx` (`benutzer_id` ASC),
  CONSTRAINT `fk_ausleihe_buch`
    FOREIGN KEY (`buch_id`)
    REFERENCES `bibliothekssoftware`.`buch` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ausleihe_benutzer`
    FOREIGN KEY (`benutzer_id`)
    REFERENCES `bibliothekssoftware`.`benutzer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bibliothekssoftware`.`buch_hat_autor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliothekssoftware`.`buch_hat_autor` (
  `autor_id` INT UNSIGNED NOT NULL,
  `buch_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`autor_id`, `buch_id`),
  INDEX `fk_autor_has_buch_buch1_idx` (`buch_id` ASC),
  INDEX `fk_autor_has_buch_autor1_idx` (`autor_id` ASC),
  CONSTRAINT `fk_autor_has_buch_autor`
    FOREIGN KEY (`autor_id`)
    REFERENCES `bibliothekssoftware`.`autor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_autor_has_buch_buch`
    FOREIGN KEY (`buch_id`)
    REFERENCES `bibliothekssoftware`.`buch` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `bibliothekssoftware`.`kategorie`
-- -----------------------------------------------------
START TRANSACTION;
USE `bibliothekssoftware`;
INSERT INTO `bibliothekssoftware`.`kategorie` (`id`, `name`) VALUES (1, 'Jura');
INSERT INTO `bibliothekssoftware`.`kategorie` (`id`, `name`) VALUES (2, 'Mathematik');
INSERT INTO `bibliothekssoftware`.`kategorie` (`id`, `name`) VALUES (3, 'Informatik');
INSERT INTO `bibliothekssoftware`.`kategorie` (`id`, `name`) VALUES (4, 'Geowissenschaften');
INSERT INTO `bibliothekssoftware`.`kategorie` (`id`, `name`) VALUES (5, 'Betriebswirtschaftslehre');
INSERT INTO `bibliothekssoftware`.`kategorie` (`id`, `name`) VALUES (6, 'Volkswirtschaftslehre');
INSERT INTO `bibliothekssoftware`.`kategorie` (`id`, `name`) VALUES (7, 'Sprache');
INSERT INTO `bibliothekssoftware`.`kategorie` (`id`, `name`) VALUES (8, 'Politik');
INSERT INTO `bibliothekssoftware`.`kategorie` (`id`, `name`) VALUES (9, 'Religion');
INSERT INTO `bibliothekssoftware`.`kategorie` (`id`, `name`) VALUES (10, 'Geschichte');
INSERT INTO `bibliothekssoftware`.`kategorie` (`id`, `name`) VALUES (11, 'Roman');
INSERT INTO `bibliothekssoftware`.`kategorie` (`id`, `name`) VALUES (12, 'Biografie');
INSERT INTO `bibliothekssoftware`.`kategorie` (`id`, `name`) VALUES (13, 'Kochbuch');
INSERT INTO `bibliothekssoftware`.`kategorie` (`id`, `name`) VALUES (14, 'Kunst');
INSERT INTO `bibliothekssoftware`.`kategorie` (`id`, `name`) VALUES (15, 'Sport');
INSERT INTO `bibliothekssoftware`.`kategorie` (`id`, `name`) VALUES (16, 'Krimi');
INSERT INTO `bibliothekssoftware`.`kategorie` (`id`, `name`) VALUES (17, 'Erotik');

COMMIT;


-- -----------------------------------------------------
-- Data for table `bibliothekssoftware`.`autor`
-- -----------------------------------------------------
START TRANSACTION;
USE `bibliothekssoftware`;
INSERT INTO `bibliothekssoftware`.`autor` (`id`, `name`) VALUES (1, 'Jiang Liping');
INSERT INTO `bibliothekssoftware`.`autor` (`id`, `name`) VALUES (2, 'Otto Fischer');
INSERT INTO `bibliothekssoftware`.`autor` (`id`, `name`) VALUES (3, 'Jochen Schwarze');
INSERT INTO `bibliothekssoftware`.`autor` (`id`, `name`) VALUES (4, 'Marc Opresnik');
INSERT INTO `bibliothekssoftware`.`autor` (`id`, `name`) VALUES (5, 'Tilo Arens');
INSERT INTO `bibliothekssoftware`.`autor` (`id`, `name`) VALUES (6, 'Frank Hettich');
INSERT INTO `bibliothekssoftware`.`autor` (`id`, `name`) VALUES (7, 'Christian Karpfinge');

COMMIT;


-- -----------------------------------------------------
-- Data for table `bibliothekssoftware`.`verlag`
-- -----------------------------------------------------
START TRANSACTION;
USE `bibliothekssoftware`;
INSERT INTO `bibliothekssoftware`.`verlag` (`id`, `name`, `ort`) VALUES (1, 'Klett', 'Stuttgart');
INSERT INTO `bibliothekssoftware`.`verlag` (`id`, `name`, `ort`) VALUES (2, 'Springer', 'Berlin');
INSERT INTO `bibliothekssoftware`.`verlag` (`id`, `name`, `ort`) VALUES (3, 'Rowohlt', 'Leipzig');
INSERT INTO `bibliothekssoftware`.`verlag` (`id`, `name`, `ort`) VALUES (4, 'Carlsen', 'Hamburg');
INSERT INTO `bibliothekssoftware`.`verlag` (`id`, `name`, `ort`) VALUES (5, 'Eher', 'München');
INSERT INTO `bibliothekssoftware`.`verlag` (`id`, `name`, `ort`) VALUES (6, 'S. Fischer', 'Frankfurt');
INSERT INTO `bibliothekssoftware`.`verlag` (`id`, `name`, `ort`) VALUES (7, 'Gabler', 'Wiesbaden');

COMMIT;


-- -----------------------------------------------------
-- Data for table `bibliothekssoftware`.`adresse`
-- -----------------------------------------------------
START TRANSACTION;
USE `bibliothekssoftware`;
INSERT INTO `bibliothekssoftware`.`adresse` (`id`, `strasse`, `hausnummer`, `ort`, `plz`) VALUES (1, 'Schulweg', '10', 'Vinzier', '23843');
INSERT INTO `bibliothekssoftware`.`adresse` (`id`, `strasse`, `hausnummer`, `ort`, `plz`) VALUES (2, 'Oldesloer Strasse', '126', 'Bad Oldesloe', '23843');
INSERT INTO `bibliothekssoftware`.`adresse` (`id`, `strasse`, `hausnummer`, `ort`, `plz`) VALUES (3, 'Ringstrasse', '76', 'Lübeck', '23558');

COMMIT;


-- -----------------------------------------------------
-- Data for table `bibliothekssoftware`.`buch`
-- -----------------------------------------------------
START TRANSACTION;
USE `bibliothekssoftware`;
INSERT INTO `bibliothekssoftware`.`buch` (`id`, `titel`, `isbn`, `erscheinungsjahr`, `standort`, `kategorie_id`, `verlag_id`) VALUES (1, 'Ni Xing', '000111222333', 2017, 'R45', 7, 1);
INSERT INTO `bibliothekssoftware`.`buch` (`id`, `titel`, `isbn`, `erscheinungsjahr`, `standort`, `kategorie_id`, `verlag_id`) VALUES (2, 'Mathematik', '9783642449185', 2017, 'R13', 2, 2);
INSERT INTO `bibliothekssoftware`.`buch` (`id`, `titel`, `isbn`, `erscheinungsjahr`, `standort`, `kategorie_id`, `verlag_id`) VALUES (3, 'Arbeitsbuch Mathematik', '9783642549472', 2015, 'R12', 2, 2);
INSERT INTO `bibliothekssoftware`.`buch` (`id`, `titel`, `isbn`, `erscheinungsjahr`, `standort`, `kategorie_id`, `verlag_id`) VALUES (4, 'Grundlagen der Allgemeinen Betriebswirtschaftslehre', '9783834915627', 2011, 'R7', 5, 7);

COMMIT;


-- -----------------------------------------------------
-- Data for table `bibliothekssoftware`.`benutzer`
-- -----------------------------------------------------
START TRANSACTION;
USE `bibliothekssoftware`;
INSERT INTO `bibliothekssoftware`.`benutzer` (`id`, `vorname`, `nachname`, `rolle`, `telefonnummer`, `email`, `geburtsdatum`, `passwort`, `adresse_id`) VALUES (1, 'Peter', 'Meyer', 'MITARBEITER', '0123456789', 'peter.meyer@gmail.com', '1979-01-10', '$2a$10$VeGuPer4REsqJABd7DwYyeP6oB1Ft7LpzkBHbTDy.CSNxwctriKuy', 2);
INSERT INTO `bibliothekssoftware`.`benutzer` (`id`, `vorname`, `nachname`, `rolle`, `telefonnummer`, `email`, `geburtsdatum`, `passwort`, `adresse_id`) VALUES (2, 'Anna', 'Kubinzki', 'KUNDE', '0987654321', 'anna.kubinzki@web.de', '1987-07-24', '$2a$10$bJQMIcO4ldf5xwuNK/Rtx.2z1bsXHH5V0UNOtDvNm0i.iigVSRh16', 1);
INSERT INTO `bibliothekssoftware`.`benutzer` (`id`, `vorname`, `nachname`, `rolle`, `telefonnummer`, `email`, `geburtsdatum`, `passwort`, `adresse_id`) VALUES (3, 'Klaus-Dieter', 'Wottramm', 'KUNDE', '01928173645', 'klaus-dieter.wottramm@gmx.de', '1958-04-16', '$2a$10$MHq0aXFeXFXsu7wYlRavTOarqkZ4T/7DlXbk4S1RC.m17MTER2hue', 3);

COMMIT;


-- -----------------------------------------------------
-- Data for table `bibliothekssoftware`.`buchexemplar`
-- -----------------------------------------------------
START TRANSACTION;
USE `bibliothekssoftware`;
INSERT INTO `bibliothekssoftware`.`buchexemplar` (`id`, `ausleihdatum`, `rueckgabedatum`, `buch_id`, `benutzer_id`) VALUES (1, NULL, NULL, 2, NULL);
INSERT INTO `bibliothekssoftware`.`buchexemplar` (`id`, `ausleihdatum`, `rueckgabedatum`, `buch_id`, `benutzer_id`) VALUES (2, NULL, NULL, 3, NULL);
INSERT INTO `bibliothekssoftware`.`buchexemplar` (`id`, `ausleihdatum`, `rueckgabedatum`, `buch_id`, `benutzer_id`) VALUES (3, NULL, NULL, 4, NULL);
INSERT INTO `bibliothekssoftware`.`buchexemplar` (`id`, `ausleihdatum`, `rueckgabedatum`, `buch_id`, `benutzer_id`) VALUES (4, NULL, NULL, 4, NULL);
INSERT INTO `bibliothekssoftware`.`buchexemplar` (`id`, `ausleihdatum`, `rueckgabedatum`, `buch_id`, `benutzer_id`) VALUES (5, NULL, NULL, 4, NULL);
INSERT INTO `bibliothekssoftware`.`buchexemplar` (`id`, `ausleihdatum`, `rueckgabedatum`, `buch_id`, `benutzer_id`) VALUES (6, NULL, NULL, 2, NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `bibliothekssoftware`.`buch_hat_autor`
-- -----------------------------------------------------
START TRANSACTION;
USE `bibliothekssoftware`;
INSERT INTO `bibliothekssoftware`.`buch_hat_autor` (`autor_id`, `buch_id`) VALUES (4, 4);
INSERT INTO `bibliothekssoftware`.`buch_hat_autor` (`autor_id`, `buch_id`) VALUES (5, 2);
INSERT INTO `bibliothekssoftware`.`buch_hat_autor` (`autor_id`, `buch_id`) VALUES (6, 3);
INSERT INTO `bibliothekssoftware`.`buch_hat_autor` (`autor_id`, `buch_id`) VALUES (7, 2);
INSERT INTO `bibliothekssoftware`.`buch_hat_autor` (`autor_id`, `buch_id`) VALUES (1, 1);

COMMIT;

