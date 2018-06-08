package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.BookCopy;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.jooby.quartz.Scheduled;
import java.util.List;

public class ReminderController {

  private EntityStore<Persistable, BookCopy> bookCopyEntityStore;
  private SimpleEmail email;

  @Inject
  public ReminderController(SimpleEmail email,
                            EntityStore<Persistable, BookCopy> bookCopyEntityStore) {

    this.bookCopyEntityStore = bookCopyEntityStore;
    this.email = email;

  }

  // Initialize the cronjob to fire at 12am every day
  @Scheduled("1m")
  public void doWork() throws EmailException {
    remind();
  }

  public List<BookCopy> remind() throws EmailException {
    String name;
    String mailAdress;
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
        mailAdress = b.getBorrower().getEmail();
        title = b.getBook().getTitle();
        author = b.getBook().getAuthors().get(0).toString().trim();
        StringBuilder fAuthor = new StringBuilder().append(author, 10, author.length()-1);

        message = "Sehr geeehrte(r) " + name + ",\n\nDies ist eine automatische Erinnerung für Sie, da sie das Buch \"" + title + "\" von " + fAuthor + " in \ndrei Tagen zurückgeben müssen.\n\nMit freundlichen Grüßen\nIhr Bibliotheksteam";
        subject = "[Bibliothek]Erinnerung an fällige Buchrückgabe";
        System.out.println("Mail gesendet.");

      {
        email
          .setSubject(subject)
          .setMsg(message)
          .addTo(mailAdress)
          .send();
      }

    //  }
    }


    return borrowedBooks;
  }
}
