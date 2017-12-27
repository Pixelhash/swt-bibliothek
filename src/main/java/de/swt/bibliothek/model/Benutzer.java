package de.swt.bibliothek.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Benutzer {

    public enum Rolle {
        KUNDE,
        MITARBEITER;
    }

    @DatabaseField(generatedId = true, canBeNull = false)
    int id;

    @DatabaseField(canBeNull = false)
    String vorname;

    @DatabaseField(canBeNull = false)
    String nachname;

    @DatabaseField(canBeNull = false)
    Rolle rolle;

    @DatabaseField
    String telefonnummer;

    @DatabaseField(canBeNull = false)
    String email;

    @DatabaseField(canBeNull = false)
    Date geburtsdatum;

    @DatabaseField(canBeNull = false)
    String passwort;

    @DatabaseField(columnName = "adresse_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    Adresse adresse;

    public Benutzer() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public Rolle getRolle() {
        return rolle;
    }

    public void setRolle(Rolle rolle) {
        this.rolle = rolle;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(Date geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public String getFullName() {
        return this.vorname + " " + this.nachname;
    }
}
