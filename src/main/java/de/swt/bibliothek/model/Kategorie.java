package de.swt.bibliothek.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Kategorie {

    @DatabaseField(generatedId = true, canBeNull = false)
    int id;

    @DatabaseField(canBeNull = false)
    String name;

    public Kategorie() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
