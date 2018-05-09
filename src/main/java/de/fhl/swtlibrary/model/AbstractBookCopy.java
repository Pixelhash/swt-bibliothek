package de.fhl.swtlibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.fhl.swtlibrary.util.TimeUtil;
import io.requery.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Table(name = "buchexemplar")
@Entity(cacheable = false)
public abstract class AbstractBookCopy {

  @Key
  @Generated
  int id;

  @Column(name = "ausleihdatum")
  Date borrowedOn;

  @Column(name = "rueckgabedatum")
  Date returnOn;

  @Column(name = "buch_id", nullable = false)
  @ForeignKey
  @ManyToOne
  AbstractBook book;

  @Column(name = "benutzer_id")
  @ForeignKey
  @OneToOne
  @JsonIgnore
  AbstractUser borrower;

  public String getBorrowedOnFormatted() {
    return new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).format(borrowedOn);
  }

  public String getReturnOnFormatted() {
    return new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).format(returnOn);
  }

  public long getDaysLeft() {
    return TimeUtil.getDaysBetween(new Date(), returnOn);
  }

}
