package de.swt.bibliothek.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "buch_hat_autor")
public class BuchAutor {

    @DatabaseField(columnName = "autor_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    Autor autor;

    @DatabaseField(columnName = "buch_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    Buch buch;

    public BuchAutor() {

    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Buch getBuch() {
        return buch;
    }

    public void setBuch(Buch buch) {
        this.buch = buch;
    }
}
