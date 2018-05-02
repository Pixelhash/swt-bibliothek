package de.fhl.swtlibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.requery.*;

import java.util.Date;

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

}
