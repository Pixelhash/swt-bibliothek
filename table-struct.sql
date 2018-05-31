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
  `plz` INT(5) UNSIGNED NOT NULL,
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
