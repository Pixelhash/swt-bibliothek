package de.fhl.swtlibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.requery.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Table(name = "buchexemplar")
@Entity(cacheable = false)
public abstract class AbstractBookCopy {

  @Key
  @Generated
  int id;

  @Column(name = "ausleihdatum")
  LocalDateTime borrowedOn;

  @Column(name = "rueckgabedatum")
  LocalDateTime returnOn;

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
    return borrowedOn.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
  }

  public String getReturnOnFormatted() {
    return returnOn.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
  }

  public long getDaysLeft() {
    return ChronoUnit.DAYS.between(LocalDateTime.now(), returnOn);
  }

}
