package de.swt.bibliothek.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.swt.bibliothek.util.TimeUtil;
import de.swt.bibliothek.util.ViewUtil;
import lombok.Data;

import java.util.Date;

@DatabaseTable
@Data
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
        // Required by ORMLite
    }

    public String getFormattedRueckgabedatum() {
        return ViewUtil.dateFormatter.format(this.getRueckgabedatum());
    }

    public String getFormattedAusleihdatum() {
        return ViewUtil.dateFormatter.format(this.getAusleihdatum());
    }

    public long getDaysLeft() {
        return TimeUtil.getDaysBetween(new Date(), this.getRueckgabedatum());
    }
}
