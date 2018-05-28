package de.fhl.swtlibrary.model;

import io.requery.Column;
import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Table;

@Table(name = "buch_hat_autor")
@Entity(cacheable = false)
public abstract class AbstractBookAuthor {

  @Column(name = "autor_id", nullable = false)
  @ForeignKey(references = AbstractAuthor.class)
  int authorId;

  @Column(name = "buch_id", nullable = false)
  @ForeignKey(references = AbstractBook.class)
  int bookId;

}
