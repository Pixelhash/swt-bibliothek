package de.fhl.swtlibrary.model;

import io.requery.*;
import io.requery.converter.EnumStringConverter;

import java.util.Date;
import java.util.List;

@Table(name = "benutzer")
@Entity(cacheable = false)
public abstract class AbstractUser {

  @Key
  @Generated
  int id;

  @Column(name = "vorname", length = 30, nullable = false)
  String forename;

  @Column(name = "nachname", length = 30, nullable = false)
  String surname;

  @Column(name = "rolle", length = 20, nullable = false)
  UserRole role;

  @Column(name = "telefonnummer", length = 20)
  String phoneNumber;

  @Column(name = "geburtsdatum", nullable = false)
  Date dateOfBirth;

  @Column(length = 100, nullable = false)
  String email;

  @Column(name = "passwort", length = 60, nullable = false)
  String password;

  @Column(name = "adresse_id")
  @ForeignKey
  @OneToOne
  Address address;

  @OneToMany
  List<BookCopy> borrowedBooks;

  public String getFullName() {
    return forename + " " + surname;
  }

  public boolean isEmployee() {
    return role == UserRole.MITARBEITER;
  }

  public boolean isCustomer() {
    return role == UserRole.KUNDE;
  }

}
