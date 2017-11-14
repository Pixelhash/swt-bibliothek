package de.swt.bibliothek.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Buch {

    @DatabaseField(generatedId = true, canBeNull = false)
    int id;

    @DatabaseField(canBeNull = false)
    String titel;

    @DatabaseField
    String isbn;

    @DatabaseField
    Date erscheinungsjahr;

    @DatabaseField(columnName = "kategorie_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    Kategorie kategorie;

    @DatabaseField(columnName = "verlag_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    Verlag verlag;

    public Buch() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getErscheinungsjahr() {
        return erscheinungsjahr;
    }

    public void setErscheinungsjahr(Date erscheinungsjahr) {
        this.erscheinungsjahr = erscheinungsjahr;
    }

    public Kategorie getKategorie() {
        return kategorie;
    }

    public void setKategorie(Kategorie kategorie) {
        this.kategorie = kategorie;
    }

    public Verlag getVerlag() {
        return verlag;
    }

    public void setVerlag(Verlag verlag) {
        this.verlag = verlag;
    }
}
