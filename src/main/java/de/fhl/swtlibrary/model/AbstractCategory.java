package de.fhl.swtlibrary.model;

import io.requery.*;

@Table(name = "kategorie")
@Entity
public abstract class AbstractCategory {

  @Key
  @Generated
  int id;

  @Column(length = 100, nullable = false)
  String name;

}
