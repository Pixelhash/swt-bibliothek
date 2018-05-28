package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.BookCopy;
import de.fhl.swtlibrary.model.User;
import de.fhl.swtlibrary.util.AuthenticationChecker;
import de.fhl.swtlibrary.util.Paths;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;

import java.time.LocalDateTime;
import java.util.Optional;

@Path(Paths.BOOK_BORROW)
public class BorrowController {

  private Request req;
  private EntityStore<Persistable, User> userEntityStore;
  private EntityStore<Persistable, BookCopy> bookCopyEntityStore;

  @Inject
  public BorrowController(Request req,
                          EntityStore<Persistable, User> userEntityStore,
                          EntityStore<Persistable, BookCopy> bookCopyEntityStore) {

    this.req = req;
    this.userEntityStore = userEntityStore;
    this.bookCopyEntityStore = bookCopyEntityStore;
  }

  @GET
  public Result getBorrowBookCopy() {
    // Check if logged in
    if (!AuthenticationChecker.isLoggedIn(req)) {
      return Results.redirect(Paths.USER_LOGIN);
    }

    User user = AuthenticationChecker.getLoggedInUser(userEntityStore, req);

    if (user == null) {
      return Results.redirect(Paths.USER_LOGIN);
    }

    // Check if user is an employee
    if (!user.isEmployee()) {
      return Results.redirect(Paths.USER_DASHBOARD);
    }

    return Results.html("pages/borrow");
  }

  @POST
  public Result postBorrowBookCopy(final Optional<Integer> user_id,
                                   final Optional<Integer> bookcopy_id) {
    // Check if logged in
    if (!AuthenticationChecker.isLoggedIn(req)) {
      return Results.redirect(Paths.USER_LOGIN);
    }

    User user = AuthenticationChecker.getLoggedInUser(userEntityStore, req);

    if (user == null) {
      return Results.redirect(Paths.USER_LOGIN);
    }

    // Check if user is an employee
    if (!user.isEmployee()) {
      return Results.redirect(Paths.USER_DASHBOARD);
    }

    if (!user_id.isPresent() || !bookcopy_id.isPresent()) {
      req.flash("error", true)
        .flash("error_message", "ERROR_INVALID_BORROW_DATA");
      return Results.redirect(Paths.BOOK_BORROW);
    }

    int userId = user_id.orElse(-1);
    int bookCopyId = bookcopy_id.orElse(-1);

    if (userId == -1 || bookCopyId == -1) {
      req.flash("error", true)
        .flash("error_message", "ERROR_INVALID_BORROW_DATA");
      return Results.redirect(Paths.BOOK_BORROW);
    }

    User targetUser = userEntityStore.select(User.class)
      .where(User.ID.eq(userId))
      .get()
      .firstOrNull();

    BookCopy bookCopy = bookCopyEntityStore.select(BookCopy.class)
      .where(BookCopy.ID.eq(bookCopyId))
      .get()
      .firstOrNull();

    if (targetUser == null) {
      req.flash("error", true)
        .flash("error_message", "ERROR_USER_NOT_FOUND");
      return Results.redirect(Paths.BOOK_BORROW);
    } else if (bookCopy == null) {
      req.flash("error", true)
        .flash("error_message", "ERROR_BOOKCOPY_NOT_FOUND");
      return Results.redirect(Paths.BOOK_BORROW);
    }
    // Both objects exist

    // Check if book copy already borrowed
    if (bookCopy.getBorrower() != null) {
      req.flash("error", true)
        .flash("error_message", "ERROR_BOOKCOPY_BORROWED");
      return Results.redirect(Paths.BOOK_BORROW);
    }

    // Borrow book
    LocalDateTime now = LocalDateTime.now();

    bookCopy.setBorrower(targetUser);
    bookCopy.setBorrowedOn(now);
    bookCopy.setReturnOn(now.plusDays(30));

    bookCopyEntityStore.update(bookCopy);

    req.flash("success", true)
      .flash("success_message", "SUCCESS_BOOK_BORROWED");
    return Results.redirect(Paths.BOOK_BORROW);
  }
}
