package de.swt.bibliothek.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

@DatabaseTable
@Data
public class Adresse {

    @DatabaseField(generatedId = true, canBeNull = false)
    int id;

    @DatabaseField(canBeNull = false)
    String strasse;

    @DatabaseField(canBeNull = false)
    String hausnummer;

    @DatabaseField(canBeNull = false)
    String ort;

    @DatabaseField(canBeNull = false)
    String plz;

    public Adresse() {
        // Required by ORMLite
    }

    public String getStrasseUndHausnummer() {
        return this.getStrasse() + " " + this.getHausnummer();
    }

    public String getOrtUndPlz() {
        return this.getPlz() + " " + this.getOrt();
    }
}
