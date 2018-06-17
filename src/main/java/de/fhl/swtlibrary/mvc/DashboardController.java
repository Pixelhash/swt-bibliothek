package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.*;
import de.fhl.swtlibrary.util.AuthenticationManager;
import de.fhl.swtlibrary.util.Paths;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.GET;
import org.jooby.mvc.Local;
import org.jooby.mvc.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/user/dashboard")
public class DashboardController {

  private Request req;
  private EntityStore<Persistable, BookCopy> bookCopyEntityStore;
  private EntityStore<Persistable, Book> bookEntityStore;
  private EntityStore<Persistable, User> userEntityStore;
  private EntityStore<Persistable, Author> authorEntityStore;
  private EntityStore<Persistable, Category> categoryEntityStore;
  private EntityStore<Persistable, Publisher> publisherEntityStore;

  @Inject
  public DashboardController(Request req,
                             EntityStore<Persistable, BookCopy> bookCopyEntityStore,
                             EntityStore<Persistable, Book> bookEntityStore,
                             EntityStore<Persistable, User> userEntityStore,
                             EntityStore<Persistable, Author> authorEntityStore,
                             EntityStore<Persistable, Category> categoryEntityStore,
                             EntityStore<Persistable, Publisher> publisherEntityStore) {
    this.req = req;
    this.bookCopyEntityStore = bookCopyEntityStore;
    this.bookEntityStore = bookEntityStore;
    this.userEntityStore = userEntityStore;
    this.authorEntityStore = authorEntityStore;
    this.categoryEntityStore = categoryEntityStore;
    this.publisherEntityStore = publisherEntityStore;
  }

  @GET
  public Result getUserDashboard(@Local User user) {

    if (user.isCustomer()) {
      // Get the user's borrowed books
      List<BookCopy> borrowedBooks = bookCopyEntityStore.select(BookCopy.class)
        .where(BookCopy.BORROWER_ID.eq(user.getId()))
        .get()
        .toList();

      // Copy list to be able to sort it, because the above list is immutable
      List<BookCopy> borrowedBooksCopy = new ArrayList<>(borrowedBooks);

      borrowedBooksCopy.sort((bc1, bc2) -> {
        if (bc1.getBorrowedOn() == null || bc2.getBorrowedOn() == null) {
          return 0;
        }
        return bc2.getBorrowedOn().compareTo(bc1.getBorrowedOn());
      });

      // Get the user's reservations
      List<Reservation> reservations = user.getReservations();

      // Copy list to be able to sort it, because the above list is immutable
      List<Reservation> reservationsCopy = new ArrayList<>(reservations);

      reservationsCopy.sort((r1, r2) -> {
        return r2.getReservedUntil().compareTo(r1.getReservedUntil());
      });

      req.set("borrowedBooks", borrowedBooksCopy);
      req.set("reservations", reservationsCopy);
    } else if (user.isEmployee()) {
      int userAmount = userEntityStore.select(User.class)
        .get()
        .toList()
        .size();

      int bookAmount = bookEntityStore.select(Book.class)
        .get()
        .toList()
        .size();

      int bookCopyAmount = userEntityStore.select(BookCopy.class)
        .get()
        .toList()
        .size();

      int authorAmount = userEntityStore.select(Author.class)
        .get()
        .toList()
        .size();

      int categoryAmount = userEntityStore.select(Category.class)
        .get()
        .toList()
        .size();

      int publisherAmount = userEntityStore.select(Publisher.class)
        .get()
        .toList()
        .size();

      req.set("userAmount", userAmount);
      req.set("bookAmount", bookAmount);
      req.set("bookCopyAmount", bookCopyAmount);
      req.set("authorAmount", authorAmount);
      req.set("categoryAmount", categoryAmount);
      req.set("publisherAmount", publisherAmount);
    }

    return Results.html("pages/dashboard");
  }
}
