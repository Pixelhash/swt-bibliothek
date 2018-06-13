package de.fhl.swtlibrary.model;



import io.requery.*;
import java.time.LocalDateTime;

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




}


