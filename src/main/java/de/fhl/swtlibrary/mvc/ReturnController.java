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

@Path(Paths.BOOK_RETURN)
public class ReturnController {

  private Request req;
  private EntityStore<Persistable, User> userEntityStore;
  private EntityStore<Persistable, BookCopy> bookCopyEntityStore;

  @Inject
  public ReturnController(Request req,
                          EntityStore<Persistable, User> userEntityStore,
                          EntityStore<Persistable, BookCopy> bookCopyEntityStore) {

    this.req = req;
    this.userEntityStore = userEntityStore;
    this.bookCopyEntityStore = bookCopyEntityStore;
  }

  @GET
  public Result getReturnBookCopy() {
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

    return Results.html("pages/return");
  }

  @POST
  public Result postReturnBookCopy() {
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

    int userId = req.param("user_id").intValue(-1);
    int bookCopyId = req.param("bookcopy_id").intValue(-1);

    if (userId == -1 || bookCopyId == -1) {
      req.flash("error", true)
        .flash("error_message", "ERROR_INVALID_BORROW_DATA");
      return Results.redirect(Paths.BOOK_RETURN);
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
      return Results.redirect(Paths.BOOK_RETURN);
    } else if (bookCopy == null) {
      req.flash("error", true)
        .flash("error_message", "ERROR_BOOKCOPY_NOT_FOUND");
      return Results.redirect(Paths.BOOK_RETURN);
    }
    // Both objects exist

    // Check if book copy isn't borrowed
    if (bookCopy.getBorrower() == null) {
      req.flash("error", true)
        .flash("error_message", "ERROR_BOOKCOPY_NOT_BORROWED");
      return Results.redirect(Paths.BOOK_RETURN);
    }

    // Return book

    bookCopy.setBorrower(null);
    bookCopy.setBorrowedOn(null);
    bookCopy.setReturnOn(null);

    bookCopyEntityStore.update(bookCopy);

    req.flash("success", true)
      .flash("success_message", "SUCCESS_BOOK_RETURNED");
    return Results.redirect(Paths.BOOK_RETURN);
  }

}
