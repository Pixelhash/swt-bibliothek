package de.fhl.swtlibrary.model;

import io.requery.*;

@Table(name = "autor")
@Entity(cacheable = false)
public abstract class AbstractAuthor {

  @Key
  @Generated
  int id;

  @Column(length = 100, nullable = false)
  String name;

}
