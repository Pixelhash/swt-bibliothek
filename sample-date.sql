use bibliothek;

truncate table autor;
truncate table buch;
truncate table kategorie;
truncate table verlag;

alter table autor add unique (name);
alter table buch add unique (titel);
alter table kategorie add unique (name);
alter table verlag add unique (name);




insert into verlag (name, ort) values 
("Klett","Stuttgart"),
("Springer","Berlin"),
("Rowohlt","Leipzig"),
("Carlsen","Hamburg"),
("Eher","Munchen"),
("S. Fischer", "Frankfurt"),
("Gabler","Wiesbaden");


insert into autor (name) values
("Jiang Liping"),
("Otto Fischer"),
("Jochen Schwarze"),
("Marc Opresnik"),
("Tilo Arens"),
("Frank Hettich"),
("Christian Karpfinge");


insert into kategorie (name) values
("Jura"),
("Mathematik"),
("Informatik"),
("Geowissenschaften"),
("Betriebswirtschaftslehre"),
("Volkswirtschaftslehre"),
("Sprachen"),
("Politik"),
("Religion"),
("Geschichte"),
("Romane"),
("Biografie"),
("Kochbuch"),
("Kunst"),
("Sport"),
("Krimi"),
("Erotik");

insert into buch (titel, isbn, erscheinungsjahr, kategorie_id, verlag_id) values
("Ni Xing", "000111222333", 2017, (select id from kategorie where name="Sprachen"), (select id from verlag where name="Klett")),
("Mathematik", "978-3642449185",  2017, (select id from kategorie where name="Mathematik"), (select id from verlag where name="Springer")),
("Arbeitsbuch Mathematik", "978-3642549472",  2015, (select id from kategorie where name="Mathematik"), (select id from verlag where name="Springer")),
("Grundlagen der Allgemeinen Betriebswirtschaftslehre", "978-3834915627",  2011, (select id from kategorie where name="Betriebswirtschaftslehre"), (select id from verlag where name="Gabler"));


insert into buch_hat_autor (autor_id, buch_id) values
((select id from autor where name="Marc Opresnik" limit 1), 
(select id from buch where titel="Grundlagen der Allgemeinen Betriebswirtschaftslehre" limit 1)),

((select id from autor where name="Tilo Arens" limit 1),
(select id from buch where titel="Mathematik" limit 1)),

((select id from autor where name="Frank Hettich" limit 1),
 (select id from buch where titel="Mathematik" limit 1)),

((select id from autor where name="Christian Karpfinger" limit 1),
 (select id from buch where titel="Mathematik" limit 1)),

((select id from autor where name="Jiang Liping" limit 1),
 (select id from buch where titel="Ni Xing" limit 1));





