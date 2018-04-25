package de.swt.bibliothek.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

@DatabaseTable(tableName = "buch_hat_autor")
@Data
public class BuchAutor {

    @DatabaseField(columnName = "autor_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    Autor autor;

    @DatabaseField(columnName = "buch_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    Buch buch;

    public BuchAutor() {
        // Required by ORMLite
    }
}
