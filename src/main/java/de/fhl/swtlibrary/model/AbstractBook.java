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
  Date releaseYear;

  @Column(name = "standort", length = 45, nullable = false)
  String location;

  @Column(name = "kategorie_id", nullable = false)
  @ForeignKey
  @OneToOne
  AbstractCategory category;

  @Column(name = "verlag_id", nullable = false)
  @ForeignKey
  @OneToOne
  AbstractPublisher publisher;

  @JunctionTable(type = AbstractBookAuthor.class)
  //@JunctionTable(name = "buch_hat_autor")
  @ManyToMany
  List<AbstractAuthor> authors;

  @OneToMany
  List<AbstractBookCopy> copies;

  public String authorsToString() {
    return authors.stream()
      .map(a -> ((Author) a).getName())
      .collect(Collectors.joining(", "));
  }

  public List<BookCopy> getAvailableCopies() {
    return copies.stream()
      .map(c -> (BookCopy) c)
      .filter(c -> c.borrower == null)
      .collect(Collectors.toList());
  }
}
