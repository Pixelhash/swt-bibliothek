package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.BookCopy;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.quartz.Scheduled;
import java.util.List;

public class ReminderController {

  private EntityStore<Persistable, BookCopy> bookCopyEntityStore;

  @Inject
  public ReminderController(
                            EntityStore<Persistable, BookCopy> bookCopyEntityStore) {

    this.bookCopyEntityStore = bookCopyEntityStore;

  }

  // Initialize the cronjob to fire at 12am every day
  @Scheduled("5s")
  public void doWork() {
    remind();
  }

  public List<BookCopy> remind() {
    String name;
    String mail;
    String title;
    String author;
    String message;
    String subject;

    List<BookCopy> borrowedBooks = bookCopyEntityStore.select(BookCopy.class)
      .where(BookCopy.BORROWER_ID.notNull())
      .get()
      .toList();

    for (BookCopy b : borrowedBooks){
     // if(b.getDaysLeft() == 3){
        name = b.getBorrower().getFullName();
        mail = b.getBorrower().getEmail();
        title = b.getBook().getTitle();
        author = b.getBook().getAuthors().get(0).toString().trim();
        StringBuilder fAuthor = new StringBuilder().append(author, 10, author.length()-1);

        message = "Sehr geeehrte(r) " + name + ",\nDies ist eine automatische Erinnerung für Sie, da sie das Buch \"" + title + "\" von " + fAuthor + " in drei Tagen zurückgeben müssen.\n\nMit freundlichen Grüßen\nIhr Bibliotheksteam";
        subject = "Erinnerung an fällige Buchrückgabe";
        System.out.println(message);

    //  }
    }


    return borrowedBooks;
  }
}
