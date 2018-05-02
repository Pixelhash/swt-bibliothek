package de.fhl.swtlibrary.model;

import io.requery.*;

@Table(name = "verlag")
@Entity
public abstract class AbstractPublisher {

  @Key
  @Generated
  int id;

  @Column(length = 100, nullable = false)
  String name;

}
