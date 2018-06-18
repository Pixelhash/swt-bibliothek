package de.fhl.swtlibrary.model;

import io.requery.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Table(name = "reservierung")
@Entity(cacheable = false)
public abstract class AbstractReservation {

  @Key
  @Generated
  int id;

  @Column(name = "ablaufdatum")
  LocalDateTime reservedUntil;

  @Column(name = "benutzer_id", nullable = false)
  @ForeignKey
  @ManyToOne
  User user;

  @Column(name = "buchexemplar_id", nullable = false)
  @ForeignKey
  @OneToOne
  BookCopy bookCopy;

  public String getReservedUntilFormatted() {
    return reservedUntil.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
  }

  public long getDaysLeft() {
    return ChronoUnit.DAYS.between(LocalDateTime.now(), reservedUntil);
  }

}
