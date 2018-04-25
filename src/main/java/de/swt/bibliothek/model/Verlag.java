package de.swt.bibliothek.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

@DatabaseTable
@Data
public class Verlag {

    @DatabaseField(generatedId = true, canBeNull = false)
    int id;

    @DatabaseField(canBeNull = false)
    String name;

    @DatabaseField(canBeNull = false)
    String ort;

    public Verlag() {
        // Required by ORMLite
    }
}
