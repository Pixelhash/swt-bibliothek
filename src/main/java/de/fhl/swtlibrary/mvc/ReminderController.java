package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.Book;
import de.fhl.swtlibrary.model.BookCopy;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.query.function.Now;
import org.jooby.Request;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;
import org.jooby.quartz.Scheduled;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    System.out.println(remind());
  }

  public List<BookCopy> remind() {

    LocalDateTime date = LocalDateTime.now();
    Timestamp df = new Timestamp(System.currentTimeMillis());
    List<BookCopy> borrowedBooks = bookCopyEntityStore.select(BookCopy.class)
      .where(BookCopy.BORROWER_ID.notNull())// && ((Timestamp)BookCopy.RETURN_ON - df) = 3)
      .get()
      .toList();

    List<BookCopy> borrowedBooksCopy = new ArrayList<>(borrowedBooks);

    for (BookCopy b : borrowedBooks){
      System.out.println(b.getBook().getTitle());
    }


    return borrowedBooksCopy;
  }




}
