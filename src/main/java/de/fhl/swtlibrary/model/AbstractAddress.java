package de.fhl.swtlibrary.model;

import io.requery.*;

@Table(name = "adresse")
@Entity
public abstract class AbstractAddress {

  @Key
  @Generated
  int id;

  @Column(name = "strasse", length = 100, nullable = false)
  String street;

  @Column(name = "hausnummer", length = 8, nullable = false)
  String houseNumber;

  @Column(name = "ort", length = 100, nullable = false)
  String city;

  @Column(name = "plz", length = 5, nullable = false)
  int postcode;

  public String getFullAddress() {
    return street + " " + houseNumber;
  }

  public String getFullCity() {
    return postcode + " " + city;
  }
}
