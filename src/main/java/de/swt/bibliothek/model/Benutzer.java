package de.swt.bibliothek.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.swt.bibliothek.Application;
import de.swt.bibliothek.util.ViewUtil;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@DatabaseTable
public class Benutzer {

    public enum Rolle {
        KUNDE,
        MITARBEITER;

        @Override
        public String toString() {
            String name = this.name().toLowerCase();
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
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

    public String getFormattedGeburtsdatum() {
        return ViewUtil.dateFormatter.format(this.geburtsdatum);
    }

    /**
     * Returns a list of books the user has lent.
     *
     * @return a list of lent books or null on error.
     */
    public List<BuchExemplar> getAusgelieheneBuecher() {
        try {
            List<BuchExemplar> buchExemplarList = Application.buchExemplarDao.getAusgelieheneBuecher(this);
            buchExemplarList.sort((o1, o2) -> {
                if (o1.getAusleihdatum() == null || o2.getAusleihdatum() == null) {
                    return 0;
                }
                return o2.getAusleihdatum().compareTo(o1.getAusleihdatum());
            });
            return buchExemplarList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
