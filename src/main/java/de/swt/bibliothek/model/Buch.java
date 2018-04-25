package de.swt.bibliothek.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@DatabaseTable
@Data
public class Buch {

    private static final SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");

    List<Autor> autoren;

    int exemplare;

    @DatabaseField(generatedId = true, canBeNull = false)
    int id;

    @DatabaseField(canBeNull = false)
    String titel;

    @DatabaseField
    String isbn;

    @DatabaseField
    Date erscheinungsjahr;

    @DatabaseField(canBeNull = false)
    String standort;

    @DatabaseField(columnName = "kategorie_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    Kategorie kategorie;

    @DatabaseField(columnName = "verlag_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    Verlag verlag;

    public Buch() {
        // Required by ORMLite
    }

    public String getYearFormatted() {
        return yearFormatter.format(this.getErscheinungsjahr());
    }

}
