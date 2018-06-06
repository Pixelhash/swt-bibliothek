INSERT INTO `kategorie` (`id`, `name`) VALUES (1, 'Jura');
INSERT INTO `kategorie` (`id`, `name`) VALUES (2, 'Mathematik');
INSERT INTO `kategorie` (`id`, `name`) VALUES (3, 'Informatik');
INSERT INTO `kategorie` (`id`, `name`) VALUES (4, 'Geowissenschaften');
INSERT INTO `kategorie` (`id`, `name`) VALUES (5, 'Betriebswirtschaftslehre');
INSERT INTO `kategorie` (`id`, `name`) VALUES (6, 'Volkswirtschaftslehre');
INSERT INTO `kategorie` (`id`, `name`) VALUES (7, 'Sprache');
INSERT INTO `kategorie` (`id`, `name`) VALUES (8, 'Politik');
INSERT INTO `kategorie` (`id`, `name`) VALUES (9, 'Religion');
INSERT INTO `kategorie` (`id`, `name`) VALUES (10, 'Geschichte');
INSERT INTO `kategorie` (`id`, `name`) VALUES (11, 'Roman');
INSERT INTO `kategorie` (`id`, `name`) VALUES (12, 'Biografie');
INSERT INTO `kategorie` (`id`, `name`) VALUES (13, 'Kochbuch');
INSERT INTO `kategorie` (`id`, `name`) VALUES (14, 'Kunst');
INSERT INTO `kategorie` (`id`, `name`) VALUES (15, 'Sport');
INSERT INTO `kategorie` (`id`, `name`) VALUES (16, 'Krimi');
INSERT INTO `kategorie` (`id`, `name`) VALUES (17, 'Erotik');

INSERT INTO `autor` (`id`, `name`) VALUES (1, 'Jiang Liping');
INSERT INTO `autor` (`id`, `name`) VALUES (2, 'Otto Fischer');
INSERT INTO `autor` (`id`, `name`) VALUES (3, 'Jochen Schwarze');
INSERT INTO `autor` (`id`, `name`) VALUES (4, 'Marc Opresnik');
INSERT INTO `autor` (`id`, `name`) VALUES (5, 'Tilo Arens');
INSERT INTO `autor` (`id`, `name`) VALUES (6, 'Frank Hettich');
INSERT INTO `autor` (`id`, `name`) VALUES (7, 'Christian Karpfinge');

INSERT INTO `verlag` (`id`, `name`, `ort`) VALUES (1, 'Klett', 'Stuttgart');
INSERT INTO `verlag` (`id`, `name`, `ort`) VALUES (2, 'Springer', 'Berlin');
INSERT INTO `verlag` (`id`, `name`, `ort`) VALUES (3, 'Rowohlt', 'Leipzig');
INSERT INTO `verlag` (`id`, `name`, `ort`) VALUES (4, 'Carlsen', 'Hamburg');
INSERT INTO `verlag` (`id`, `name`, `ort`) VALUES (5, 'Eher', 'München');
INSERT INTO `verlag` (`id`, `name`, `ort`) VALUES (6, 'S. Fischer', 'Frankfurt');
INSERT INTO `verlag` (`id`, `name`, `ort`) VALUES (7, 'Gabler', 'Wiesbaden');

INSERT INTO `adresse` (`id`, `strasse`, `hausnummer`, `ort`, `plz`) VALUES (1, 'Schulweg', '10', 'Vinzier', '23843');
INSERT INTO `adresse` (`id`, `strasse`, `hausnummer`, `ort`, `plz`) VALUES (2, 'Oldesloer Strasse', '126', 'Bad Oldesloe', '23843');
INSERT INTO `adresse` (`id`, `strasse`, `hausnummer`, `ort`, `plz`) VALUES (3, 'Ringstrasse', '76', 'Lübeck', '23558');

INSERT INTO `buch` (`id`, `titel`, `isbn`, `erscheinungsjahr`, `standort`, `kategorie_id`, `verlag_id`) VALUES (1, 'Ni Xing', '000111222333', 2017, 'R45', 7, 1);
INSERT INTO `buch` (`id`, `titel`, `isbn`, `erscheinungsjahr`, `standort`, `kategorie_id`, `verlag_id`) VALUES (2, 'Mathematik', '9783642449185', 2017, 'R13', 2, 2);
INSERT INTO `buch` (`id`, `titel`, `isbn`, `erscheinungsjahr`, `standort`, `kategorie_id`, `verlag_id`) VALUES (3, 'Arbeitsbuch Mathematik', '9783642549472', 2015, 'R12', 2, 2);
INSERT INTO `buch` (`id`, `titel`, `isbn`, `erscheinungsjahr`, `standort`, `kategorie_id`, `verlag_id`) VALUES (4, 'Grundlagen der Allgemeinen Betriebswirtschaftslehre', '9783834915627', 2011, 'R7', 5, 7);

INSERT INTO `benutzer` (`id`, `vorname`, `nachname`, `rolle`, `telefonnummer`, `email`, `geburtsdatum`, `passwort`, `adresse_id`) VALUES (1, 'Peter', 'Meyer', 'MITARBEITER', '0123456789', 'peter.meyer@gmail.com', '1979-01-10', '$2a$10$VeGuPer4REsqJABd7DwYyeP6oB1Ft7LpzkBHbTDy.CSNxwctriKuy', 2);
INSERT INTO `benutzer` (`id`, `vorname`, `nachname`, `rolle`, `telefonnummer`, `email`, `geburtsdatum`, `passwort`, `adresse_id`) VALUES (2, 'Anna', 'Kubinzki', 'KUNDE', '0987654321', 'anna.kubinzki@web.de', '1987-07-24', '$2a$10$bJQMIcO4ldf5xwuNK/Rtx.2z1bsXHH5V0UNOtDvNm0i.iigVSRh16', 1);
INSERT INTO `benutzer` (`id`, `vorname`, `nachname`, `rolle`, `telefonnummer`, `email`, `geburtsdatum`, `passwort`, `adresse_id`) VALUES (3, 'Klaus-Dieter', 'Wottramm', 'KUNDE', '01928173645', 'klaus-dieter.wottramm@gmx.de', '1958-04-16', '$2a$10$MHq0aXFeXFXsu7wYlRavTOarqkZ4T/7DlXbk4S1RC.m17MTER2hue', 3);

INSERT INTO `buchexemplar` (`id`, `ausleihdatum`, `rueckgabedatum`, `buch_id`, `benutzer_id`) VALUES (1, NULL, NULL, 2, NULL);
INSERT INTO `buchexemplar` (`id`, `ausleihdatum`, `rueckgabedatum`, `buch_id`, `benutzer_id`) VALUES (2, NULL, NULL, 3, NULL);
INSERT INTO `buchexemplar` (`id`, `ausleihdatum`, `rueckgabedatum`, `buch_id`, `benutzer_id`) VALUES (3, NULL, NULL, 4, NULL);
INSERT INTO `buchexemplar` (`id`, `ausleihdatum`, `rueckgabedatum`, `buch_id`, `benutzer_id`) VALUES (4, NULL, NULL, 4, NULL);
INSERT INTO `buchexemplar` (`id`, `ausleihdatum`, `rueckgabedatum`, `buch_id`, `benutzer_id`) VALUES (5, NULL, NULL, 4, NULL);
INSERT INTO `buchexemplar` (`id`, `ausleihdatum`, `rueckgabedatum`, `buch_id`, `benutzer_id`) VALUES (6, NULL, NULL, 2, NULL);

INSERT INTO `buch_hat_autor` (`autor_id`, `buch_id`) VALUES (4, 4);
INSERT INTO `buch_hat_autor` (`autor_id`, `buch_id`) VALUES (5, 2);
INSERT INTO `buch_hat_autor` (`autor_id`, `buch_id`) VALUES (6, 3);
INSERT INTO `buch_hat_autor` (`autor_id`, `buch_id`) VALUES (7, 2);
INSERT INTO `buch_hat_autor` (`autor_id`, `buch_id`) VALUES (1, 1);
