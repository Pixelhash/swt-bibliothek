package de.swt.bibliothek.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.swt.bibliothek.util.ViewUtil;

import java.util.Date;

@DatabaseTable
public class BuchExemplar {

    @DatabaseField(generatedId = true, canBeNull = false)
    int id;

    @DatabaseField
    Date ausleihdatum;

    @DatabaseField
    Date rueckgabedatum;

    @DatabaseField(columnName = "buch_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    Buch buch;

    @DatabaseField(columnName = "benutzer_id", foreign = true, foreignAutoRefresh = true)
    Benutzer benutzer;

    public BuchExemplar() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getAusleihdatum() {
        return ausleihdatum;
    }

    public void setAusleihdatum(Date ausleihdatum) {
        this.ausleihdatum = ausleihdatum;
    }

    public Date getRueckgabedatum() {
        return rueckgabedatum;
    }

    public void setRueckgabedatum(Date rueckgabedatum) {
        this.rueckgabedatum = rueckgabedatum;
    }

    public Buch getBuch() {
        return buch;
    }

    public void setBuch(Buch buch) {
        this.buch = buch;
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(Benutzer benutzer) {
        this.benutzer = benutzer;
    }

    public String getFormattedRueckgabedatum() {
        return ViewUtil.dateFormatter.format(this.getRueckgabedatum());
    }
}
