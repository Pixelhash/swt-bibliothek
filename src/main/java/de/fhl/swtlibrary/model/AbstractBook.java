package de.fhl.swtlibrary.model;

import io.requery.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "buch")
@Entity(cacheable = false)
public abstract class AbstractBook {

  @Key
  @Generated
  int id;

  @Column(name = "titel", length = 150, nullable = false)
  String title;

  @Column(length = 13)
  String isbn;

  @Column(name = "erscheinungsjahr")
  short releaseYear;

  @Column(name = "standort", length = 45, nullable = false)
  String location;

  @Column(name = "kategorie_id", nullable = false)
  @ForeignKey
  @OneToOne
  Category category;

  @Column(name = "verlag_id", nullable = false)
  @ForeignKey
  @OneToOne
  Publisher publisher;

  /* Classes given in annotations must be available at compile time! */
  @JunctionTable(type = AbstractBookAuthor.class)
  //@JunctionTable(name = "buch_hat_autor")
  @ManyToMany
  List<Author> authors;

  @OneToMany
  List<BookCopy> copies;

  public String authorsToString() {
    return authors.stream()
      .map(Author::getName)
      .collect(Collectors.joining(", "));
  }

  public List<BookCopy> getAvailableCopies() {
    return copies.stream()
      .filter(c -> c.borrower == null && c.reservation== null)
      .collect(Collectors.toList());
  }
}
