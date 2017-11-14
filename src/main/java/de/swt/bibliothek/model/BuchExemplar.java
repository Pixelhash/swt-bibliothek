package de.swt.bibliothek.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class BuchExemplar {

    @DatabaseField(generatedId = true, canBeNull = false)
    int id;

    @DatabaseField(canBeNull = false)
    String standort;

    @DatabaseField
    Date ausleihdatum;

    @DatabaseField
    Date rueckgabedatum;

    @DatabaseField(columnName = "buch_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    Buch buch;



}
