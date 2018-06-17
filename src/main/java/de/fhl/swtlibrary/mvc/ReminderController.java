package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import de.fhl.swtlibrary.model.BookCopy;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.jooby.quartz.Scheduled;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReminderController {

  private EntityStore<Persistable, BookCopy> bookCopyEntityStore;
  private Config config;

  @Inject
  public ReminderController(Config config,
    EntityStore<Persistable, BookCopy> bookCopyEntityStore) {

    this.bookCopyEntityStore = bookCopyEntityStore;
    this.config = config;

  }

  // Initialize the cronjob to fire at 8am every day
  @Scheduled("0 0 8am * * ?")
  public void doWork() throws EmailException {
    remind();
  }

  public List<BookCopy> remind() throws EmailException {
    String name;
    String mailAdress;
    String message;
    String subject;
    String booksDue;
    String bookTitles;
    List<String> alreadyReminded = new LinkedList<>();

    List<BookCopy> borrowedBooks = bookCopyEntityStore.select(BookCopy.class)
      .where(BookCopy.BORROWER_ID.notNull())
      .get()
      .toList();

    Map booksByUser = new HashMap();

    for (BookCopy b : borrowedBooks) {
      booksByUser.put(b.getBorrower().getFullName(), booksByUser.get(b.getBorrower().getFullName()) + "," + b.getBook().getTitle());
    }

    for (BookCopy b : borrowedBooks) {
       if(b.getDaysLeft() == 3 && !alreadyReminded.contains(b.getBorrower().getFullName())){
         Email email = new SimpleEmail();
         email.setHostName(config.getString("mail.hostName"));
         email.setSmtpPort(config.getInt("mail.smtpPort"));
         email.setAuthentication(config.getString("mail.username"), config.getString("mail.password"));
         email.setFrom(config.getString("mail.from"));

         name = b.getBorrower().getFullName();
         mailAdress = b.getBorrower().getEmail();
         booksDue = (String) booksByUser.get(name);
         alreadyReminded.add(name);

         bookTitles = "- " + booksDue.replaceAll(",","\n- ");
         StringBuilder correctedBookTitles = new StringBuilder().append(bookTitles, 6, bookTitles.length());

         message = "Sehr geeehrte(r) " + name + ",\n\nDies ist eine automatische Erinnerung für Sie, da Sie folgende Bücher in drei Tagen abgeben müssen:\n" + correctedBookTitles + "\n\nMit freundlichen Grüßen\nIhr Bibliotheksteam";
         subject = "[Bibliothek]Erinnerung an fällige Buchrückgabe";
         System.out.println("Mail gesendet!");


         email
           .setSubject(subject)
           .setMsg(message)
           .addTo(mailAdress)
           .send();
        }
    }
    return borrowedBooks;
  }

}
